package cz.edu.x3m.processing;

import cz.edu.x3m.utils.Strings;

/**
 *
 *  @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class RunEvaluation implements IRunEvaluation {

    private static final int EXIT_TIMEOUT = 143;
    private static final int EXIT_OK = 0;
    private static final int EXIT_MEMORYOUT = 144;
    private final IRunResult runResult;



    public RunEvaluation (IRunResult runResult) {
        this.runResult = runResult == null ? getNullResult () : runResult;
    }



    @Override
    public int getExitValue () {
        return runResult.getExitValue ();
    }



    @Override
    public Throwable getThrowable () {
        return runResult.getThrowable ();
    }



    @Override
    public String getError () {
        String error = runResult.getError ();
        return error == null || error.isEmpty () ? null : error;
    }



    @Override
    public int getRunTime () {
        return runResult.getRunTime ();
    }



    @Override
    public int getMemoryAvg () {
        return runResult.getMemoryAvg ();
    }



    @Override
    public int getLineCount () {
        return runResult.getLineCount ();
    }



    @Override
    public boolean isInterrupted () {
        return getExitValue () >= EXIT_TIMEOUT;
    }



    @Override
    public boolean isSuccessful () {
        return getExitValue () == EXIT_OK;
    }



    @Override
    public boolean isTimeOut () {
        return getExitValue () == EXIT_TIMEOUT;
    }



    @Override
    public boolean isMemoryOut () {
        return getExitValue () == EXIT_MEMORYOUT;
    }



    private IRunResult getNullResult () {
        return new NullResult ();
    }



    @Override
    public String print () {
        return Strings.create (
                String.format ("%-12s: %s (%s)", "exit-value", getExitValue (), getExitMessage ()),
                String.format ("%-12s: %s", "line-count", getLineCount ()),
                String.format ("%-12s: %s", "memory-avg", getMemoryAvg ()),
                String.format ("%-12s: %s", "run-time", getRunTime ()),
                String.format ("%-12s: %s", "error", getError ()),
                String.format ("%-12s: %s", "throwable", getThrowable ()) //
                );
    }



    private String getExitMessage () {
        int exit = getExitValue ();
        switch (exit) {
            case EXIT_OK:
                return "EXIT_OK";
            case EXIT_TIMEOUT:
                return "EXIT_TIMEOUT";
            case EXIT_MEMORYOUT:
                return "EXIT_MEMORYOUT";
            case -1:
                return "NO_RESULT";
            default:
                return String.format ("code: %d", exit);
        }
    }
}

class NullResult implements IRunResult {

    @Override
    public int getExitValue () {
        return -1;
    }



    @Override
    public Throwable getThrowable () {
        return null;
    }



    @Override
    public String getError () {
        return null;
    }



    @Override
    public int getRunTime () {
        return -1;
    }



    @Override
    public int getMemoryAvg () {
        return -1;
    }



    @Override
    public int getLineCount () {
        return -1;
    }
}
