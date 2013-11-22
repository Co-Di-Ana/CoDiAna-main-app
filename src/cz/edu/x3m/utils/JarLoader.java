package cz.edu.x3m.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jan Hybs
 */
public class JarLoader {

    private ClassLoader mainClassLoader;
    private List<URL> urls = new ArrayList<> ();
    private boolean loaded;
    private URLClassLoader urlClassLoader;



    public JarLoader (ClassLoader mainClassLoader) {
        this.mainClassLoader = mainClassLoader;
    }



    public void add (String path) throws MalformedURLException {
        File dir = new File (path);

        if (!dir.exists () || !dir.isDirectory ())
            return;

        File[] jars = dir.listFiles (new JarFileNameFilter ());

        for (int i = 0; i < jars.length; i++) {
            urls.add (jars[i].toURI ().toURL ());
        }

    }



    public Object get (String classname) throws Exception {
        load ();
        return Class.forName (classname, true, urlClassLoader).newInstance ();
    }



    private void load () {
        if (loaded)
            return;

        URL[] urlArray = new URL[urls.size ()];
        urlArray = urls.toArray (urlArray);
        urlClassLoader = new URLClassLoader (urlArray, mainClassLoader);
        loaded = true;
    }

    private class JarFileNameFilter implements FilenameFilter {

        @Override
        public boolean accept (File dir, String name) {
            return name.endsWith (".jar");
        }
    }
}
