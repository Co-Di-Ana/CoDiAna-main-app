package cz.edu.x3m.utils;

import cz.edu.x3m.core.Globals;
import cz.edu.x3m.database.structure.AttemptItem;
import cz.edu.x3m.database.structure.QueueItem;
import java.io.File;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class PathResolver {

    private static final String SEPARATOR = File.separator;
    private static final String CURRENT = "curr";
    private static final String PREVIOUS = "prev";
    private static final String DATA = "data";
    private static final String TEMP = "temp";



    /**
     * Resolves folder where all tasks are stored.
     * @return folder where all tasks are stored ending with separator
     */
    public static String getDataDirectory () {
        return Globals.getConfig ().getDataDirectory ();
    }



    /**
     * Method returns path to TEMP directory
     * @return 
     */
    public static String getTempDirectory () {
        return createPath (TEMP);
    }



    /**
     * Method returns path to TEMP directory A
     * @return 
     */
    public static String getTempDirectoryA () {
        return createPath (TEMP, "A");
    }



    /**
     * Method returns path to TEMP directory B
     * @return 
     */
    public static String getTempDirectoryB () {
        return createPath (TEMP, "B");
    }



    /**
     * Resolves folder where user current solution source codes are stored
     * @param taskID
     * @param userID
     * @return folder where user current solution source codes are stored ending with separator
     */
    public static String getCurrentSourceDirectory (int taskID, int userID) {
        String task = String.format ("task-%04d", taskID);
        String user = String.format ("user-%04d", userID);
        return createPath (task, user, CURRENT);
    }



    /**
     * Reesolves zip file of given user, task a attempt number.
     * @param s solution
     * @return path to zip file
     */
    public static String getAttemptSolution (AttemptItem s) {
        return getPreviousSolution (s.getTaskID (), s.getUserID (), s.getOrdinal ());
    }



    /**
     * Reesolves zip file of given user, task a attempt number.
     * @param taskID
     * @param userID
     * @param attemptNo
     * @return path to zip file
     */
    public static String getPreviousSolution (int taskID, int userID, int attemptNo) {
        String task = String.format ("task-%04d", taskID);
        String user = String.format ("user-%04d", userID);
        String attempt = String.format ("attempt-%04d.zip", attemptNo);
        return createPath (task, user, PREVIOUS).concat (attempt);
    }



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

            case COMPILE_OUTPUT:
                return createPath (task, user, CURRENT).concat (taskName).concat (".compile.out");

            case COMPILE_ERROR:
                return createPath (task, user, CURRENT).concat (taskName).concat (".compile.err");

            case TASK_INPUT:
                return createPath (task, DATA).concat (taskName).concat (".in");

            case TASK_OUTPUT:
                return createPath (task, DATA).concat (taskName).concat (".out");

            case TASK_ERROR:
                return createPath (task, DATA).concat (taskName).concat (".err");

            case EXECUTION_OUTPUT:
                return createPath (task, user, CURRENT).concat (taskName).concat (".out");

            case EXECUTION_ERROR:
                return createPath (task, user, CURRENT).concat (taskName).concat (".err");
            default:
                return null;
        }
    }

    public static enum PathType {

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



    private static String createPath (String... paths) {
        StringBuilder sb = new StringBuilder ();
        sb.append (getDataDirectory ());
        sb.append (SEPARATOR);

        for (String piece : paths) {
            sb.append (piece);
            sb.append (SEPARATOR);
        }
        return sb.toString ();
    }
}
