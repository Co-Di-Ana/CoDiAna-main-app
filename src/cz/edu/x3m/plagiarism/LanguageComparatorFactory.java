package cz.edu.x3m.plagiarism;

import cz.edu.x3m.core.Globals;
import cz.edu.x3m.logging.Log;
import cz.edu.x3m.processing.execption.ExecutionException;
import cz.edu.x3m.utils.JarLoader;

/**
 *
 *  @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class LanguageComparatorFactory {

    private static boolean inited;
    private static JarLoader loader;



    public static ILanguageComparator getInstance (String name) throws ExecutionException {
        try {
            init ();
            String classname = Globals.getConfig ().getLanguagePlagsClassname (name);
            if (classname == null || classname.isEmpty ()) {
                Log.err ("Unregistered name '%s'", name);
                return null;
            }


            return (ILanguageComparator) loader.get (classname);
        } catch (Exception ex) {
            throw new ExecutionException (ex);
        }
    }



    private static void init () throws Exception {
        if (inited)
            return;

        loader = new JarLoader (LanguageComparatorFactory.class.getClassLoader ());
        if (Globals.DEBUG) {
            loader.add ("c:\\Users\\Jan\\Documents\\NetBeansProjects\\DP\\CoDiAna-plagiarism-java\\dist\\");
        } else {
            loader.add ("libs/lang/");
            loader.add ("lib/lang/");
        }

        inited = true;
    }
}
