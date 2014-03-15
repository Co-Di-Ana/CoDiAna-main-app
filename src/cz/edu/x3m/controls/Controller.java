package cz.edu.x3m.controls;

import cz.edu.x3m.core.Globals;
import static cz.edu.x3m.database.data.FinalGradeMode.TEST;
import cz.edu.x3m.database.data.PlagsCheckStateType;
import cz.edu.x3m.database.data.types.AttemptStateType;
import cz.edu.x3m.database.data.types.QueueItemType;
import cz.edu.x3m.database.exception.DatabaseException;
import cz.edu.x3m.database.exception.InvalidArgument;
import cz.edu.x3m.database.structure.AttemptItem;
import cz.edu.x3m.database.structure.IPlagObject;
import cz.edu.x3m.database.structure.QueueItem;
import cz.edu.x3m.database.structure.TaskItem;
import cz.edu.x3m.grading.ISolutionGrading;
import cz.edu.x3m.grading.SolutionGrading;
import cz.edu.x3m.grading.SolutionGradingResult;
import cz.edu.x3m.grading.exception.GradingException;
import cz.edu.x3m.logging.Log;
import cz.edu.x3m.plagiarism.Difference;
import cz.edu.x3m.plagiarism.ILanguageComparator;
import cz.edu.x3m.plagiarism.IPlagPair;
import cz.edu.x3m.plagiarism.IPlagResult;
import cz.edu.x3m.plagiarism.LanguageComparatorFactory;
import cz.edu.x3m.plagiarism.PlagPair;
import cz.edu.x3m.plagiarism.PlagResult;
import cz.edu.x3m.processing.IRunEvaluation;
import cz.edu.x3m.processing.LanguageSupportFactory;
import cz.edu.x3m.processing.RunEvaluation;
import cz.edu.x3m.processing.compilation.ICompilableLanguage;
import cz.edu.x3m.processing.compilation.ICompileResult;
import cz.edu.x3m.processing.compilation.impl.CompileSetting;
import cz.edu.x3m.processing.execption.ExecutionException;
import cz.edu.x3m.processing.execution.IExecutableLanguage;
import cz.edu.x3m.processing.execution.IExecutionResult;
import cz.edu.x3m.processing.execution.impl.ExecutionSetting;
import cz.edu.x3m.utils.ListUtil;
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
        List<QueueItem> items = Globals.getDatabase ().getQueueItems ();
        List<UpdateResult> results = new ArrayList<> ();

        // empty queue? some mistake/debug
        if (items == null || items.isEmpty ()) {
            Log.info ("empty queue");
            return results;
        }

        // go through all items
        Log.info ("%d items", items.size ());
        for (int i = 0, j = items.size (); i < j; i++) {
            Globals.getConfig ().clearCache ();
            results.add (update (items.get (i)));
            Log.info ("item update complete");
        }

        // log results
        Log.info ("results:");
        for (UpdateResult updateResult : results)
            Log.info ("update: %s", updateResult.toString ());

        return results;
    }



    private UpdateResult update (QueueItem queueItem) throws DatabaseException, ExecutionException, GradingException, Exception {

        IRunEvaluation compilationResult;
        IRunEvaluation executionResult;
        AttemptItem attemptItem;
        IExecutableLanguage languageExec;
        TaskItem taskItem;

        taskItem = queueItem.getTaskItem ();
        QueueItemType queueType = queueItem.getType ();
        Log.info ("updating item queueid:%d from userid:%s type %s", queueItem.getId (), queueItem.getUserID (), queueType);

        if (queueType == QueueItemType.TYPE_SOLUTION_CHECK
                || queueType == QueueItemType.TYPE_MEASURE_VALUES) {

            // load detail (corresponding attempt)
            Log.info ("loading attempt item");
            attemptItem = queueItem.getAttemptItem ();
            Log.info ("getting language support instance");
            languageExec = LanguageSupportFactory.getInstance (attemptItem.getLanguage ());

            // compile if compilable
            compilationResult = null;
            if (languageExec instanceof ICompilableLanguage) {
                Log.info ("compiling");
                ((ICompilableLanguage) languageExec).setCompileSettings (CompileSetting.create (queueItem));
                compilationResult = compile ((ICompilableLanguage) languageExec);

                // Programmers error, throws ExtensionException
                if (compilationResult == null)
                    throw new Exception ("Extension error, compilation result cannot be null!");

                // Interrupted compilation, abort processing
                if (compilationResult.isInterrupted ()) {
                    Log.err ("compile interrupted");
                    Log.err (compilationResult.print ());
                    Log.err (compilationResult.getThrowable ());

                    Log.info ("saving to DB");
                    Globals.getDatabase ().saveGradingResult (queueItem, AttemptStateType.COMPILATION_TIMEOUT, null);
                    Log.info ("deleting from queue");
                    Globals.getDatabase ().deleteQueueItem (queueItem);
                    return new UpdateResult (queueItem, AttemptStateType.COMPILATION_TIMEOUT);
                }

                // Compilation error, abort processing
                if (!compilationResult.isSuccessful ()) {
                    Log.err ("compile error");
                    Log.err (compilationResult.print ());
                    Log.err (compilationResult.getThrowable ());

                    Log.info ("saving to DB");
                    Globals.getDatabase ().saveGradingResult (queueItem, AttemptStateType.COMPILATION_ERROR, compilationResult.getError ());
                    Log.info ("deleting from queue");
                    Globals.getDatabase ().deleteQueueItem (queueItem);
                    return new UpdateResult (queueItem, AttemptStateType.COMPILATION_ERROR);
                }
            }

            // execute if compilation is success or there is no compilation needed
            executionResult = null;
            Log.info ("executing");
            languageExec.setExecutionSettings (ExecutionSetting.create (queueItem));
            executionResult = execute (languageExec);


            // Programmers error, throws ExtensionException
            if (executionResult == null)
                throw new Exception ("Extension error, compilation result cannot be null!");

            // Interrupted execution, abort processing
            if (executionResult.isInterrupted ()) {
                Log.err ("executing interrupted");
                Log.err (executionResult.print ());
                Log.err (executionResult.getThrowable ());

                Log.info ("saving to DB");
                Globals.getDatabase ().saveGradingResult (queueItem, AttemptStateType.EXECUTION_TIMEOUT, null);
                Log.info ("deleting from queue");
                Globals.getDatabase ().deleteQueueItem (queueItem);
                return new UpdateResult (queueItem, AttemptStateType.EXECUTION_TIMEOUT);
            }

            // Execution error, abort processing
            if (!executionResult.isSuccessful ()) {
                Log.err ("executing error %d", executionResult.getExitValue ());
                Log.err (executionResult.print ());
                Log.err (executionResult.getThrowable ());

                Log.info ("saving to DB");
                Globals.getDatabase ().saveGradingResult (queueItem, AttemptStateType.EXECUTION_ERROR, executionResult.getError ());
                Log.info ("deleting from queue");
                Globals.getDatabase ().deleteQueueItem (queueItem);
                return new UpdateResult (queueItem, AttemptStateType.EXECUTION_ERROR);
            } else {
                Log.info ("execution successfull");
                queueItem.getAttemptItem ().setExecutionResult (executionResult);

                // solution check will grade solution
                if (queueItem.getType () == QueueItemType.TYPE_SOLUTION_CHECK)
                    return checkSolution (queueItem, attemptItem);

                // when measuring values, thresholds are unkwnown and they need to recorded
                if (queueItem.getType () == QueueItemType.TYPE_MEASURE_VALUES)
                    return measureValues (queueItem, attemptItem, taskItem, executionResult);
            }
        } else if (queueItem.getType () == QueueItemType.TYPE_PLAGIARISM_CHECK) {
            // list holder and lists for items and for plags
            final IPlagObject plagList = queueItem.getPlagObject ();
            final List<IPlagPair> result = new ArrayList<> ();
            final List<AttemptItem> solutions = plagList.getItems ();
            final int size = solutions.size ();

            // solutions compare result
            Difference difference;
            AttemptItem itemA, itemB;
            // comparator
            ILanguageComparator comparator;
            // files holders
            String directoryA, directoryB, zipA, zipB;
            // if userid is zero -> check all solution 
            boolean isSimpleCheck = queueItem.getUserID () != 0;

            // move desired to 0 index and break for cycle after one check
            if (isSimpleCheck) {
                int searchIndex = ListUtil.findByAttemptID (solutions, queueItem.getAttemptID ());
                if (searchIndex == -1)
                    throw new InvalidArgument ("invalid attemptID argument");
                AttemptItem copy = solutions.get (0);
                solutions.set (0, solutions.get (searchIndex));
                solutions.set (searchIndex, copy);
            }

            // check all solutions
            for (int i = 0; i < size - 1; i++) {
                // grab attempt zip file lcoation
                itemA = solutions.get (i);
                zipA = PathResolver.getAttemptSolution (itemA);
                // grab soon to be location of unzipped solution and unzip solution
                directoryA = PathResolver.getTempDirectoryA ();
                if (Zipper.unzip (zipA, directoryA) == false)
                    continue;

                // create comparator
                comparator = LanguageComparatorFactory.getInstance (itemA.getLanguage ());
                // unsupported comparator skip
                if (comparator == null)
                    continue;

                // prepare first solution
                comparator.prepare (new File (directoryA));

                for (int j = i + 1; j < size; j++) {
                    itemB = solutions.get (j);

                    // if solutions are incomprable, skip it
                    if (!itemA.getLanguage ().equals (itemB.getLanguage ()))
                        continue;

                    // grab attempt zip file lcoation
                    zipB = PathResolver.getAttemptSolution (itemB);
                    // grab soon to be location of unzipped solution and unzip solution
                    directoryB = PathResolver.getTempDirectoryB ();
                    if (Zipper.unzip (zipB, directoryB) == false)
                        continue;

                    // compare files and grab result
                    difference = comparator.compare (new File (directoryB));
                    Log.info ("comparement  %1.2f %% %s %s", difference.getIdenticalLikelihood () * 100, itemA.getUserItem ().getFullname (), itemB.getUserItem ().getFullname ());

                    if (difference.getIdenticalLikelihood () >= 0.70)
                        result.add (new PlagPair (solutions.get (i), solutions.get (j), difference));
                }

                if (isSimpleCheck)
                    break;
            }

            IPlagResult plagiarismResult = new PlagResult (queueItem, result, isSimpleCheck);
            Globals.getDatabase ().savePlagCheckResult (queueItem, plagiarismResult);
            Globals.getDatabase ().deleteQueueItem (queueItem);

            return new UpdateResult (queueItem, result.isEmpty () ? PlagsCheckStateType.NO_PLAGS_FOUND : PlagsCheckStateType.PLAGS_FOUND);
        }
        throw new InvalidArgument ("Unsupported queue type");
    }



    private IRunEvaluation compile (ICompilableLanguage compilableLanguage) throws Exception {
        IRunEvaluation result;

        compilableLanguage.preCompilation ();
        result = new RunEvaluation (compilableLanguage.compile ());
        compilableLanguage.postCompilation ();

        return result;
    }



    private IRunEvaluation execute (IExecutableLanguage executableLanguage) throws Exception {
        IRunEvaluation result;

        executableLanguage.preExecution ();
        result = new RunEvaluation (executableLanguage.execute ());
        executableLanguage.postExecution ();

        return result;
    }



    private UpdateResult checkSolution (QueueItem queueItem, AttemptItem attemptItem) throws GradingException, DatabaseException, InvalidArgument {
        ISolutionGrading solutionGrading = new SolutionGrading ();

        // add monitors
        Log.info ("add monitors");
        solutionGrading.setQueueItem (queueItem);
        solutionGrading.addMonitors ();

        // grade task
        Log.info ("grading");
        SolutionGradingResult gradingResult = (SolutionGradingResult) solutionGrading.grade ();
        AttemptStateType attemptStateType = determinateAttemptState (queueItem.getTaskItem (), gradingResult);
        Log.info ("saving to DB");
        Globals.getDatabase ().saveGradingResult (queueItem, gradingResult, attemptStateType);
        Log.info ("deleting from queue");
        Globals.getDatabase ().deleteQueueItem (queueItem);
        return new UpdateResult (queueItem, attemptStateType);
    }



    private UpdateResult measureValues (QueueItem queueItem, AttemptItem attemptItem, TaskItem taskItem, IRunEvaluation executionResult) throws DatabaseException, GradingException {
        Log.info ("saving to DB (codiana)");
        Globals.getDatabase ().saveMeasurementResult (taskItem, executionResult);
        Log.info ("saving to DB (attempt)");
        Globals.getDatabase ().saveGradingResult (attemptItem, executionResult);
        Log.info ("deleting from queue");
        Globals.getDatabase ().deleteQueueItem (queueItem);
        return new UpdateResult (queueItem, AttemptStateType.MEASUREMENT_OK);
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
    private AttemptStateType determinateAttemptState (TaskItem taskItem, SolutionGradingResult gradingResult) throws InvalidArgument {
        Log.info ("getting final state");
        boolean outputWrong = gradingResult.getOutputGradeResult ().isWrong ();
        boolean timeWrong = gradingResult.getTimeGradeResult ().isWrong ();
        boolean memoryWrong = gradingResult.getMemoryGradeResult ().isWrong ();

        boolean outputCorrect = gradingResult.getOutputGradeResult ().isCorrect ();

        switch (taskItem.getFinalGradeMode ()) {
            case TEST:
            case MEASURE:

                if (outputWrong)
                    return AttemptStateType.OUTPUT_ERROR;

                if (timeWrong)
                    return AttemptStateType.TIME_ERROR;

                if (memoryWrong)
                    return AttemptStateType.MEMORY_ERROR;

                return AttemptStateType.MEASUREMENT_OK;

            case PRECISE:
                if (!outputCorrect)
                    return AttemptStateType.OUTPUT_ERROR;

                if (timeWrong)
                    return AttemptStateType.TIME_ERROR;

                if (memoryWrong)
                    return AttemptStateType.MEMORY_ERROR;

                return AttemptStateType.MEASUREMENT_OK;
        }

        throw new InvalidArgument ("task final grade incorrect value");
    }
}
