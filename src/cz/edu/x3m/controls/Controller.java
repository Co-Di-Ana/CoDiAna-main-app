package cz.edu.x3m.controls;

import cz.edu.x3m.core.Globals;
import cz.edu.x3m.database.data.AttemptItem;
import cz.edu.x3m.database.data.QueueItem;
import cz.edu.x3m.database.data.TaskItem;
import cz.edu.x3m.database.data.types.AttemptStateType;
import cz.edu.x3m.grading.ISolutionGrading;
import cz.edu.x3m.grading.SolutionGrading;
import cz.edu.x3m.grading.SolutionGradingResult;
import cz.edu.x3m.processing.LanguageFactory;
import cz.edu.x3m.processing.compilation.ICompilableLanguage;
import cz.edu.x3m.processing.compilation.ICompileResult;
import cz.edu.x3m.processing.compilation.impl.CompileSetting;
import cz.edu.x3m.processing.execption.ExecutionException;
import cz.edu.x3m.processing.execution.IExecutableLanguage;
import cz.edu.x3m.processing.execution.IExecutionResult;
import cz.edu.x3m.processing.execution.impl.ExecutionSetting;
import java.util.List;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class Controller implements IController {

    private static IController instance;



    public static IController getInstance () {
        return instance == null ? instance = new Controller () : instance;
    }
    private ICompileResult compilationResult;
    private IExecutionResult executionResult;
    private AttemptItem attemptItem;
    private QueueItem queueItem;
    private IExecutableLanguage languageExec;
    private TaskItem taskItem;



    @Override
    public synchronized UpdateResult update () throws Exception {
        List<QueueItem> items = Globals.getDatabase ().getItems ();

        // empty queue? some mistake/debug
        if (items == null || items.isEmpty ())
            return new UpdateResult (UpdateResultType.NO_ITEMS);

        // go through all items
        for (int i = 0, j = items.size (); i < j; i++) {
            queueItem = items.get (i);
            taskItem = queueItem.getTaskItem ();

            if (queueItem.getType () == QueueItem.QueueType.TYPE_SOLUTION_CHECK
                    || queueItem.getType () == QueueItem.QueueType.TYPE_MEASURE_VALUES) {

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
                        return null;
                    }

                    // Compilation error, abort processing
                    if (!compilationResult.isSuccessful ()) {
                        Globals.getDatabase ().saveGradingResult (queueItem, AttemptStateType.COMPILATION_ERROR, compilationResult.getDetails ());
                        return null;
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
                    return null;
                }

                // Execution error, abort processing
                if (!executionResult.isSuccessful ()) {
                    Globals.getDatabase ().saveGradingResult (queueItem, AttemptStateType.EXECUTION_ERROR, executionResult.getDetails ());
                    return null;
                } else {
                    queueItem.setExecutionResult (executionResult);


                    // solution check will grade solution
                    if (queueItem.getType () == QueueItem.QueueType.TYPE_SOLUTION_CHECK) {
                        ISolutionGrading solutionGrading = new SolutionGrading ();

                        // add monitors
                        solutionGrading.setQueueItem (queueItem);
                        solutionGrading.addMonitors ();

                        // grade task
                        Globals.getDatabase ().saveGradingResult (queueItem, (SolutionGradingResult) solutionGrading.grade ());
//                        Globals.getDatabase ().deleteQueueItem (queueItem);
                        System.out.format ("TYPE_SOLUTION_CHECK from %s %n", attemptItem.getFullname ());
                    }

                    // when measuring values, thresholds are unkwnown and they need to recorded
                    if (queueItem.getType () == QueueItem.QueueType.TYPE_MEASURE_VALUES) {
                        Globals.getDatabase ().saveMeasurementResult (taskItem, executionResult);
//                        Globals.getDatabase ().deleteQueueItem (queueItem);
//                        Globals.getDatabase ().deleteAttemptItem (queueItem);
                        // TODO save results to attempt or delete attempt or set flag that it is a measure type
                        System.out.format ("TYPE_MEASURE_VALUES from %s %n", attemptItem.getFullname ());
                    }
                }
            }

            if (queueItem.getType () == QueueItem.QueueType.TYPE_PLAGIARISM_CHECK) {
                // TODO do it too
            }
        }
        return null;
    }



    private ICompileResult compile (ICompilableLanguage compilableLanguage) {
        ICompileResult primaryResult;
        ICompileResult secondaryResult;

        secondaryResult = compilableLanguage.preCompilation ();
        if (secondaryResult != null && !secondaryResult.isSuccessful ())
            return secondaryResult;

        primaryResult = compilableLanguage.compile ();
        if (primaryResult != null && !primaryResult.isSuccessful ())
            return primaryResult;

        secondaryResult = compilableLanguage.postCompilation ();
        if (secondaryResult != null && !secondaryResult.isSuccessful ())
            return secondaryResult;

        return primaryResult;
    }



    private IExecutionResult execute (IExecutableLanguage executableLanguage) {
        IExecutionResult primaryResult;
        IExecutionResult secondaryResult;

        secondaryResult = executableLanguage.preExecution ();
        if (secondaryResult != null && !secondaryResult.isSuccessful ())
            return secondaryResult;

        primaryResult = executableLanguage.execute ();
        if (primaryResult != null && !primaryResult.isSuccessful ())
            return primaryResult;

        secondaryResult = executableLanguage.postExecution ();
        if (secondaryResult != null && !secondaryResult.isSuccessful ())
            return secondaryResult;

        return primaryResult;
    }
}
