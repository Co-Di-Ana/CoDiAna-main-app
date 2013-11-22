package cz.edu.x3m.processing.execution;

/**
 *
 *  @author Jan Hybs
 */
public interface IExecutableLanguage {

    void setExecutionSettings (IExecutionSetting settings);



    IExecutionResult preExecution ();



    IExecutionResult execute ();



    IExecutionResult postExecution ();
}
