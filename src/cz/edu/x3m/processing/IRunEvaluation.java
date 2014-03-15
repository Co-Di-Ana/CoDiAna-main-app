package cz.edu.x3m.processing;

/**
 *
 *  @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public interface IRunEvaluation extends IRunResult {

    /**
     * @return true if process was interrupted by time memory or some other means (resource rejection)
     * false otherwise
     */
    boolean isInterrupted ();



    /**
     * @return true if process exit value is zero
     * false otherwise
     */
    boolean isSuccessful ();



    /**
     * @return true if process was terminated due time violation
     * false otherwise
     */
    boolean isTimeOut ();



    /**
     * @return true if process was terminated due memory violation
     * false otherwise
     */
    boolean isMemoryOut ();



    /**
     * @return information about this result
     */
    String print ();
}
