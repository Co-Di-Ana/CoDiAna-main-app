package cz.edu.x3m.utils;

import cz.edu.x3m.core.Globals;
import cz.edu.x3m.database.data.QueueItem;
import java.io.File;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class PathResolver {

    private static final String SEPARATOR = File.separator;
    private static final String CURRENT = "curr";
    private static final String DATA = "data";



    /**
     * Method resolves path based on given type and valid queue item
     *
     * @param type Type of the path
     * @param item valid Queue type object
     * @return path to file/folder or null if type is unknown
     */
    public static String get (PathType type, QueueItem item) {
        String dataDir = Globals.getConfig ().getDataDirectory ();
        String task = String.format ("task-%04d", item.getTaskID ());
        String user = String.format ("user-%04d", item.getUserID ());
        String taskName = item.getTaskItem ().getMainFileName ();

        switch (type) {
            case DATA_DIRECTORY:
                return dataDir;

            case SOURCE_DIRECTORY:
                return dataDir
                        .concat (SEPARATOR).concat (task)
                        .concat (SEPARATOR).concat (user)
                        .concat (SEPARATOR).concat (CURRENT);

            case COMPILE_OUTPUT:
                return dataDir
                        .concat (SEPARATOR).concat (task)
                        .concat (SEPARATOR).concat (user)
                        .concat (SEPARATOR).concat (CURRENT)
                        .concat (SEPARATOR).concat (taskName).concat (".compile.out");

            case COMPILE_ERROR:
                return dataDir
                        .concat (SEPARATOR).concat (task)
                        .concat (SEPARATOR).concat (user)
                        .concat (SEPARATOR).concat (CURRENT)
                        .concat (SEPARATOR).concat (taskName).concat (".compile.err");

            case TASK_INPUT:
                return dataDir
                        .concat (SEPARATOR).concat (task)
                        .concat (SEPARATOR).concat (DATA)
                        .concat (SEPARATOR).concat (taskName).concat (".in");

            case TASK_OUTPUT:
                return dataDir
                        .concat (SEPARATOR).concat (task)
                        .concat (SEPARATOR).concat (DATA)
                        .concat (SEPARATOR).concat (taskName).concat (".out");

            case TASK_ERROR:
                return dataDir
                        .concat (SEPARATOR).concat (task)
                        .concat (SEPARATOR).concat (DATA)
                        .concat (SEPARATOR).concat (taskName).concat (".err");

            case EXECUTION_OUTPUT:
                return dataDir
                        .concat (SEPARATOR).concat (task)
                        .concat (SEPARATOR).concat (user)
                        .concat (SEPARATOR).concat (CURRENT)
                        .concat (SEPARATOR).concat (taskName).concat (".out");

            case EXECUTION_ERROR:
                return dataDir
                        .concat (SEPARATOR).concat (task)
                        .concat (SEPARATOR).concat (user)
                        .concat (SEPARATOR).concat (CURRENT)
                        .concat (SEPARATOR).concat (taskName).concat (".err");
            default:
                return null;
        }
    }

    public static enum PathType {

        /** folder where all tasks are stored */
        DATA_DIRECTORY,
        /** folder where user current solution source codes are stored */
        SOURCE_DIRECTORY,
        //---------------------------------------------------------------
        /** path to user compilation output file */
        COMPILE_OUTPUT,
        /** path to user compilation error file */
        COMPILE_ERROR,
        //---------------------------------------------------------------
        /** path to task input file */
        TASK_INPUT,
        /** path to task output file */
        TASK_OUTPUT,
        /** path to task error file */
        TASK_ERROR,
        //---------------------------------------------------------------
        /** path to user execution output file */
        EXECUTION_OUTPUT,
        /** path to user execution error file */
        EXECUTION_ERROR
    }
}
