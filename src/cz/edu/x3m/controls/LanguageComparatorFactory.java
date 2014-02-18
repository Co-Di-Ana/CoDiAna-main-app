package cz.edu.x3m.controls;

import cz.edu.x3m.core.Globals;
import cz.edu.x3m.plagiarism.ILanguageComparator;
import cz.edu.x3m.processing.execption.ExecutionException;
import cz.edu.x3m.utils.JarLoader;

/**
 *
 *  @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class LanguageComparatorFactory {

    private static boolean inited;
    private static JarLoader loader;



    public static ILanguageComparator getInstance (String extension) throws ExecutionException {
        try {
            init ();
            String classname = Globals.getConfig ().getLanguageClassName (extension);
            if (classname == null)
                throw new ExecutionException ("Unregistered extension '.%s'", extension);

            return (ILanguageComparator) loader.get (classname);
        } catch (Exception ex) {
            throw new ExecutionException (ex);
        }
    }



    private static void init () throws Exception {
        if (inited)
            return;

        loader = new JarLoader (LanguageComparatorFactory.class.getClassLoader ());
        loader.add ("/home/hans/NetBeansProjects/LanguageJava/dist/");
//        loader.add ("libs/lang/");
//        loader.add ("lib/lang/");

        inited = true;
    }
}
