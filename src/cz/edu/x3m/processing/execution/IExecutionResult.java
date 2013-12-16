package cz.edu.x3m.processing.execution;

/**
 *
 *  @author Jan Hybs
 */
public interface IExecutionResult {



    public boolean isSuccessful ();



    public int getRunTime ();



    public int getMemoryPeak ();

}
