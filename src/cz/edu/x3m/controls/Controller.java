package cz.edu.x3m.controls;

import cz.edu.x3m.core.Globals;
import cz.edu.x3m.database.data.AttemptItem;
import cz.edu.x3m.database.data.IPlagiarismSolutionList;
import cz.edu.x3m.database.data.ISolution;
import cz.edu.x3m.database.data.QueueItem;
import cz.edu.x3m.database.data.TaskItem;
import cz.edu.x3m.database.data.types.AttemptStateType;
import cz.edu.x3m.database.data.types.QueueItemType;
import cz.edu.x3m.database.exception.DatabaseException;
import cz.edu.x3m.grading.ISolutionGrading;
import cz.edu.x3m.grading.SolutionGrading;
import cz.edu.x3m.grading.SolutionGradingResult;
import cz.edu.x3m.grading.exception.GradingException;
import cz.edu.x3m.plagiarism.Difference;
import cz.edu.x3m.plagiarism.ILanguageComparator;
import cz.edu.x3m.plagiarism.IPlagiarismResult;
import cz.edu.x3m.plagiarism.IPlagiasrismPair;
import cz.edu.x3m.plagiarism.PlagiarismResult;
import cz.edu.x3m.plagiarism.PlagiasrismPair;
import cz.edu.x3m.processing.LanguageFactory;
import cz.edu.x3m.processing.compilation.ICompilableLanguage;
import cz.edu.x3m.processing.compilation.ICompileResult;
import cz.edu.x3m.processing.compilation.impl.CompileSetting;
import cz.edu.x3m.processing.execption.ExecutionException;
import cz.edu.x3m.processing.execution.IExecutableLanguage;
import cz.edu.x3m.processing.execution.IExecutionResult;
import cz.edu.x3m.processing.execution.impl.ExecutionSetting;
import cz.edu.x3m.utils.PathResolver;
import cz.edu.x3m.utils.Zipper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class Controller implements IController {

    private static IController instance;



    private Controller () {
    }



    public static IController getInstance () {
        return instance == null ? instance = new Controller () : instance;
    }



    @Override
    public synchronized List<UpdateResult> update () throws DatabaseException, ExecutionException, GradingException, Exception {
        List<QueueItem> items = Globals.getDatabase ().getItems ();

        // empty queue? some mistake/debug
        if (items == null || items.isEmpty ())
            return new ArrayList<> ();

        // go through all items
        for (int i = 0, j = items.size (); i < j; i++)
            update (items.get (i));
        return null;
    }



    private UpdateResult update (QueueItem queueItem) throws DatabaseException, ExecutionException, GradingException, Exception {

        ICompileResult compilationResult;
        IExecutionResult executionResult;
        AttemptItem attemptItem;
        IExecutableLanguage languageExec;
        TaskItem taskItem;


        taskItem = queueItem.getTaskItem ();
        QueueItemType queueType = queueItem.getType ();

        if (queueType == QueueItemType.TYPE_SOLUTION_CHECK
                || queueType == QueueItemType.TYPE_MEASURE_VALUES) {

            // load detail (corresponding attempt)
            queueItem.loadDetails ();
            attemptItem = (AttemptItem) queueItem.getDetailItem ();
            attemptItem.setTaskItem (taskItem);
            languageExec = LanguageFactory.getInstance (attemptItem.getLanguage ());

            // compile if compilable
            compilationResult = null;
            if (languageExec instanceof ICompilableLanguage) {
                ((ICompilableLanguage) languageExec).setCompileSettings (CompileSetting.create (queueItem));
                compilationResult = compile ((ICompilableLanguage) languageExec);

                // Programmers error, throws ExtensionException
                if (compilationResult == null)
                    throw new Exception ("Extension error, compilation result cannot be null!");

                // Interrupted compilation, abort processing
                if (compilationResult.isInterrupted ()) {
                    Globals.getDatabase ().saveGradingResult (queueItem, AttemptStateType.COMPILATION_TIMEOUT, null);
                    return new UpdateResult (queueItem, attemptItem, AttemptStateType.COMPILATION_TIMEOUT);
                }

                // Compilation error, abort processing
                if (!compilationResult.isSuccessful ()) {
                    Globals.getDatabase ().saveGradingResult (queueItem, AttemptStateType.COMPILATION_ERROR, compilationResult.getDetails ());
                    return new UpdateResult (queueItem, attemptItem, AttemptStateType.COMPILATION_ERROR);
                }
            }

            // execute if compilation is success or there is no compilation needed
            executionResult = null;
            languageExec.setExecutionSettings (ExecutionSetting.create (queueItem));
            executionResult = execute (languageExec);


            // Programmers error, throws ExtensionException
            if (executionResult == null)
                throw new Exception ("Extension error, compilation result cannot be null!");

            // Interrupted execution, abort processing
            if (executionResult.isInterrupted ()) {
                Globals.getDatabase ().saveGradingResult (queueItem, AttemptStateType.EXECUTION_TIMEOUT, null);
                return new UpdateResult (queueItem, attemptItem, AttemptStateType.EXECUTION_TIMEOUT);
            }

            // Execution error, abort processing
            if (!executionResult.isSuccessful ()) {
                Globals.getDatabase ().saveGradingResult (queueItem, AttemptStateType.EXECUTION_ERROR, executionResult.getDetails ());
                return new UpdateResult (queueItem, attemptItem, AttemptStateType.EXECUTION_ERROR);
            } else {
                queueItem.setExecutionResult (executionResult);

                // solution check will grade solution
                if (queueItem.getType () == QueueItemType.TYPE_SOLUTION_CHECK)
                    return checkSolution (queueItem, attemptItem);

                // when measuring values, thresholds are unkwnown and they need to recorded
                if (queueItem.getType () == QueueItemType.TYPE_MEASURE_VALUES)
                    return measureValues (queueItem, attemptItem, taskItem, executionResult);
            }
        }

        if (queueItem.getType () == QueueItemType.TYPE_PLAGIARISM_CHECK) {
            queueItem.loadDetails ();
            // list holder and lists for items and for plags
            final IPlagiarismSolutionList plagList = (IPlagiarismSolutionList) queueItem.getDetailItem ();
            final List<IPlagiasrismPair> result = new ArrayList<> ();
            final List<ISolution> solutions = plagList.getItems ();
            final int size = solutions.size ();

            // solutions compare result
            Difference difference;
            // comparator
            ILanguageComparator comparator;
            // files holders
            String directoryA, directoryB, zipA, zipB;

            for (int i = 0; i < size - 1; i++) {
                // grab attempt zip file lcoation
                zipA = PathResolver.getAttemptSolution (solutions.get (i));
                // grab soon to be location of unzipped solution and unzip solution
                directoryA = PathResolver.getTempDirectoryA ();
                if (Zipper.unzip (zipA, directoryA) == false)
                    continue;

                // create comparator and prepare first 
                comparator = LanguageComparatorFactory.getInstance ("java");
                comparator.prepare (new File (directoryA));

                for (int j = i + 1; j < size; j++) {
                    // grab attempt zip file lcoation
                    zipB = PathResolver.getAttemptSolution (solutions.get (j));
                    // grab soon to be location of unzipped solution and unzip solution
                    directoryB = PathResolver.getTempDirectoryB ();
                    if (Zipper.unzip (zipB, directoryB) == false)
                        continue;

                    // compare files and grab result
                    difference = comparator.compare (new File (directoryB));

                    if (difference.getIdenticalLikelihood () >= 0.75)
                        result.add (new PlagiasrismPair (solutions.get (i), solutions.get (j), difference));
                }
            }

            IPlagiarismResult plagiarismResult = new PlagiarismResult (taskItem, result);
            Globals.getDatabase ().savePlagCheckResult (queueItem, plagiarismResult);

            return null;
        }
        return null;
    }



    private ICompileResult compile (ICompilableLanguage compilableLanguage) throws Exception {
        ICompileResult result;

        compilableLanguage.preCompilation ();

        result = compilableLanguage.compile ();
        if (result != null && !result.isSuccessful ())
            return result;

        compilableLanguage.postCompilation ();

        return result;
    }



    private IExecutionResult execute (IExecutableLanguage executableLanguage) throws Exception {
        IExecutionResult result;

        executableLanguage.preExecution ();

        result = executableLanguage.execute ();
        if (result != null && !result.isSuccessful ())
            return result;

        executableLanguage.postExecution ();

        return result;
    }



    private UpdateResult checkSolution (QueueItem queueItem, AttemptItem attemptItem) throws GradingException, DatabaseException {
        ISolutionGrading solutionGrading = new SolutionGrading ();

        // add monitors
        solutionGrading.setQueueItem (queueItem);
        solutionGrading.addMonitors ();

        // grade task
        SolutionGradingResult gradingResult = (SolutionGradingResult) solutionGrading.grade ();
        AttemptStateType attemptStateType = determinateAttemptState (queueItem.getTaskItem (), gradingResult);
        Globals.getDatabase ().saveGradingResult (queueItem, gradingResult, attemptStateType);
        Globals.getDatabase ().deleteQueueItem (queueItem);
        return new UpdateResult (queueItem, attemptItem, attemptStateType);
    }



    private UpdateResult measureValues (QueueItem queueItem, AttemptItem attemptItem, TaskItem taskItem, IExecutionResult executionResult) throws DatabaseException, GradingException {
        Globals.getDatabase ().saveMeasurementResult (taskItem, executionResult);
        Globals.getDatabase ().saveGradingResult (attemptItem, executionResult);
        Globals.getDatabase ().deleteQueueItem (queueItem);
        return new UpdateResult (queueItem, attemptItem, AttemptStateType.MEASUREMENT_OK);
    }



    /**
     * Method returns final attempt state.
     * to determine final state, we just need to check several things:
     *   1) correct output, otherwise OUTPUT_ERROR
     *   2) correct exec time, otherwise TIME_ERROR
     *   3) correct memory level, otherwise MEMORY_ERROR
     * if all things are ok then MEASUREMENT_OK
     * @param taskItem
     * @param gradingResult
     * @return 
     */
    private AttemptStateType determinateAttemptState (TaskItem taskItem, SolutionGradingResult gradingResult) {
        boolean output = gradingResult.getOutputGradeResult ().isCorrect ();
        boolean time = gradingResult.getTimeGradeResult ().isCorrect ();
        boolean memory = gradingResult.getMemoryGradeResult ().isCorrect ();

        if (!output)
            return AttemptStateType.OUTPUT_ERROR;

        if (!time)
            return AttemptStateType.TIME_ERROR;

        if (!memory)
            return AttemptStateType.MEMORY_ERROR;

        return AttemptStateType.MEASUREMENT_OK;
    }
}
