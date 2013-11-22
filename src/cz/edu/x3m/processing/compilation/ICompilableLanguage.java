package cz.edu.x3m.processing.compilation;

import cz.edu.x3m.processing.execution.IExecutableLanguage;

/**
 *
 * @author Jan Hybs
 */
public interface ICompilableLanguage extends IExecutableLanguage {

    void setCompileSettings (ICompileSetting settings);



    ICompileResult preCompilation ();



    ICompileResult compile ();



    ICompileResult postCompilation ();
}
