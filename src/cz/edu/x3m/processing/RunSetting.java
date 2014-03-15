package cz.edu.x3m.processing;

import cz.edu.x3m.core.Globals;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *  @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class RunSetting implements IRunSetting {

    @Override
    public String getInputPath () {
        return null;
    }



    @Override
    public String getOutputPath () {
        return null;
    }



    @Override
    public String getErrorPath () {
        return null;
    }



    @Override
    public int getLimitTime () {
        return Globals.getConfig ().getLimitTime ();
    }



    @Override
    public int getLimitMemory () {
        return Globals.getConfig ().getLimitMemory ();
    }



    @Override
    public List<String> getCommandBase () {
        List<String> command = new ArrayList<> ();

        // add script location
        command.add (Globals.getConfig ().getRunScript ());

        // streams
        addArg (command, "--input", getInputPath ());
        addArg (command, "--output", getOutputPath ());
        addArg (command, "--error", getErrorPath ());

        // limits
        addArg (command, "--time", getLimitTime ());
        addArg (command, "--memory", getLimitMemory ());
        
        // prepare for command input
        command.add("--command");
        
        return command;
    }



    private void addArg (List<String> command, String arg, String val) {
        if (val == null || val.trim ().isEmpty ())
            return;
        command.add (arg);
        command.add (val);
    }



    private void addArg (List<String> command, String arg, int val) {
        if (val <= 0)
            return;
        command.add (arg);
        command.add (Integer.toString (val));
    }
}
