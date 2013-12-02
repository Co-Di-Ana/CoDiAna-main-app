package cz.edu.x3m.controls;

import cz.edu.x3m.core.Globals;
import cz.edu.x3m.database.data.AbstractDetailItem;
import cz.edu.x3m.database.data.AttemptItem;
import cz.edu.x3m.database.data.QueueItem;
import cz.edu.x3m.database.data.TaskItem;
import cz.edu.x3m.grading.GradingFactory;
import cz.edu.x3m.grading.GradingSetting;
import cz.edu.x3m.grading.GradingType;
import cz.edu.x3m.grading.IGrading;
import cz.edu.x3m.grading.SolutionGrading;
import cz.edu.x3m.grading.SolutionGradingResult;
import cz.edu.x3m.grading.memory.MemoryGradeSetting;
import cz.edu.x3m.grading.output.OutputGradeSetting;
import cz.edu.x3m.grading.time.TimeGradeSetting;
import cz.edu.x3m.processing.LanguageFactory;
import cz.edu.x3m.processing.compilation.ICompilableLanguage;
import cz.edu.x3m.processing.compilation.ICompileResult;
import cz.edu.x3m.processing.execption.ExecutionException;
import cz.edu.x3m.processing.execution.IExecutableLanguage;
import cz.edu.x3m.processing.execution.IExecutionResult;
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
                attemptItem.setTask (taskItem);
                languageExec = LanguageFactory.getInstance (attemptItem.getLanguage ());

                // compile if compilable
                compilationResult = null;
                if (languageExec instanceof ICompilableLanguage) {
                    ((ICompilableLanguage) languageExec).preCompilation ();
                    compilationResult = ((ICompilableLanguage) languageExec).compile ();
                    ((ICompilableLanguage) languageExec).postCompilation ();
                }

                // execute if compilation is success or there is no compilation needed
                executionResult = null;
                if (compilationResult == null || compilationResult.isSuccessful ()) {
                    ((IExecutableLanguage) languageExec).preExecution ();
                    executionResult = ((IExecutableLanguage) languageExec).execute ();
                    ((IExecutableLanguage) languageExec).postExecution ();
                }

                // no result from execution means some serious mistake (maybe implementation error)
                if (executionResult == null)
                    throw new ExecutionException ("execution fatal error");

                // if execution is successful, store them in DB and delete queue item from queue
                if (executionResult.isSuccessful ()) {
                    
                    // solution check will grade solution
                    if (queueItem.getType () == QueueItem.QueueType.TYPE_SOLUTION_CHECK) {
                        SolutionGrading solutionGrading = new SolutionGrading ();
                        IGrading grading;
                        GradingSetting setting;

                        // add time grading
                        grading = GradingFactory.getInstance (GradingType.TIME, GradingFactory.TYPE_TIME_THRESHOLD);
                        setting = new TimeGradeSetting (true, taskItem.getLimitTimeFalling (), taskItem.getLimitTimeNothing ());
                        grading.setSettings (setting);
                        solutionGrading.addGrading (grading);

                        // add memory grading
                        grading = GradingFactory.getInstance (GradingType.MEMORY, GradingFactory.TYPE_MEMORY_THRESHOLD);
                        setting = new MemoryGradeSetting (true, taskItem.getLimitMemoryFalling (), taskItem.getLimitMemoryNothing ());
                        grading.setSettings (setting);
                        solutionGrading.addGrading (grading);

                        // add output grading
                        grading = GradingFactory.getInstance (GradingType.OUTPUT, taskItem.getOutputMethod ().value ());
                        setting = new OutputGradeSetting (true, taskItem.getOutputFile (), attemptItem.getOutputFile ());
                        grading.setSettings (setting);
                        solutionGrading.addGrading (grading);

                        // grade task
                        Globals.getDatabase ().saveGradingResult (queueItem, (SolutionGradingResult) solutionGrading.grade ());
                        Globals.getDatabase ().deleteItem (queueItem);
                    }
                    
                    // when measuring values, thresholds are unkwnown and they need to recorded
                    if (queueItem.getType () == QueueItem.QueueType.TYPE_MEASURE_VALUES) {
                        Globals.getDatabase ().saveMeasurementResult (taskItem, executionResult);
                        Globals.getDatabase ().deleteItem (queueItem);
                    }
                }
            }

            if (queueItem.getType () == QueueItem.QueueType.TYPE_PLAGIARISM_CHECK) {
                // TODO do it
            }
        }
        return null;
    }
}
