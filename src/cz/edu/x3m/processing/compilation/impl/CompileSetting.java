package cz.edu.x3m.processing.compilation.impl;

import cz.edu.x3m.processing.RunSetting;
import cz.edu.x3m.core.Globals;
import cz.edu.x3m.database.structure.QueueItem;
import cz.edu.x3m.processing.compilation.ICompileSetting;
import cz.edu.x3m.utils.PathResolver;
import java.util.List;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class CompileSetting extends RunSetting implements ICompileSetting {

    private String mainFileName;
    private String sourceDirectoryPath;
    private String outputPath;
    private String errorPath;



    @Override
    public String getMainFileName () {
        return mainFileName;
    }



    @Override
    public String getSourceDirectoryPath () {
        return sourceDirectoryPath;
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
     * Method creates CompileSetting based on given queue item
     * @param item
     * @return CompileSetting object
     */
    public static CompileSetting create (QueueItem item) {
        CompileSetting setting = new CompileSetting ();
        setting.mainFileName = item.getTaskItem ().getMainFileName ();
        setting.sourceDirectoryPath = PathResolver.getCurrentSourceDirectory (item.getTaskID (), item.getUserID ());
        setting.errorPath = PathResolver.get (PathResolver.PathType.COMPILE_ERROR, item);
        setting.outputPath = PathResolver.get (PathResolver.PathType.COMPILE_OUTPUT, item);
        return setting;
    }
}
