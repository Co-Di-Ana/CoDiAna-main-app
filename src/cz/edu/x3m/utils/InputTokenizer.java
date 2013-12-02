package cz.edu.x3m.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class InputTokenizer extends FileReader {

    public InputTokenizer (File file) throws FileNotFoundException {
        super (file);
    }



    public String nextToken () throws IOException {
        StringBuilder b = new StringBuilder ();
        int c;

        while (true) {
            c = read ();

            // end of stream
            if (c == -1)
                return b.length () == 0 ? null : b.toString ();


            // skip whites
            if (Character.isWhitespace (c)) {
                if (b.length () == 0)
                    continue;
                else
                    return b.toString ();
            } else {
                b.append ((char) c);
            }
        }
    }
}
