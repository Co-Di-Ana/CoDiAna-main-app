package cz.edu.x3m.utils;

import net.lingala.zip4j.core.ZipFile;

/**
 *
 *  @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class Zipper {

    public static boolean unzip (String zipFile, String location) {
        try {
            ZipFile zf = new ZipFile (zipFile);
            zf.extractAll (location);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
