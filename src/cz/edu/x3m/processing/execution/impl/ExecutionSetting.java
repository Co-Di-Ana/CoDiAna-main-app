package cz.edu.x3m.processing.execution.impl;

import cz.edu.x3m.database.data.QueueItem;
import cz.edu.x3m.database.data.types.QueueItemType;
import cz.edu.x3m.processing.execution.IExecutionSetting;
import cz.edu.x3m.utils.PathResolver;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class ExecutionSetting implements IExecutionSetting {

    private String mainFileName;
    private String sourceDirectoryPath;
    private String errorPath;
    private String outputPath;
    private String inputPath;



    @Override
    public String getMainFileName () {
        return mainFileName;
    }



    @Override
    public String getSourceDirectoryPath () {
        return sourceDirectoryPath;
    }



    @Override
    public String getInputPath () {
        return inputPath;
    }



    @Override
    public String getOutputPath () {
        return outputPath;
    }



    @Override
    public String getErrorPath () {
        return errorPath;
    }



    /**
     * Method creates ExecutionSetting based on given queue item
     *
     * @param item
     * @return ExecutionSetting object
     */
    public static ExecutionSetting create (QueueItem item) {
        ExecutionSetting setting = new ExecutionSetting ();
        setting.mainFileName = item.getTaskItem ().getMainFileName ();
        setting.sourceDirectoryPath = PathResolver.getCurrentSourceDirectory (item.getTaskID (), item.getUserID ());
        setting.inputPath = PathResolver.get (PathResolver.PathType.TASK_INPUT, item);

        if (item.getType () == QueueItemType.TYPE_MEASURE_VALUES) {
            setting.outputPath = PathResolver.get (PathResolver.PathType.TASK_OUTPUT, item);
            setting.errorPath = PathResolver.get (PathResolver.PathType.TASK_ERROR, item);
        } else {
            setting.outputPath = PathResolver.get (PathResolver.PathType.EXECUTION_OUTPUT, item);
            setting.errorPath = PathResolver.get (PathResolver.PathType.EXECUTION_ERROR, item);
        }

        return setting;
    }
}
