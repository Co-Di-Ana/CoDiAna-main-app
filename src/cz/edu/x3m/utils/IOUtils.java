package cz.edu.x3m.utils;

import java.io.PrintWriter;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class IOUtils {

    public static void writeAll (String filePath, String content) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter (filePath);
            pw.write (content);
            pw.close ();
        } catch (Exception ex) {
            //# oh, well
        } finally {
            if (pw != null)
                pw.close ();
        }
    }
}
