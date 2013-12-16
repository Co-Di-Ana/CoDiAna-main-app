package cz.edu.x3m.grading.output.impl;

import cz.edu.x3m.grading.GradingResult;
import cz.edu.x3m.grading.output.OutputGradeResult;
import cz.edu.x3m.grading.output.OutputGradeSetting;
import cz.edu.x3m.utils.IOUtils;
import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class VagueOutputGradingTest {

    public VagueOutputGradingTest () {
    }



    @BeforeClass
    public static void setUpClass () {
    }



    @AfterClass
    public static void tearDownClass () {
    }



    @Before
    public void setUp () {
    }



    @After
    public void tearDown () {
    }



    /**
     * Test of compare method, of class VagueOutputGrading.
     */
    @Test
    public void testCompareCorrect () throws Exception {
        System.out.println ("tokenizer and vague comparator test 1");
        File originalFile = File.createTempFile ("orig", "file");
        File comparedFile = File.createTempFile ("orig", "file");

        IOUtils.writeAll (originalFile.getAbsolutePath (), "1 2 3.3 4 5");
        IOUtils.writeAll (comparedFile.getAbsolutePath (), "1 2 3.2 4 5");


        VagueOutputGrading instance = new VagueOutputGrading ();
        OutputGradeResult result = (OutputGradeResult) instance.compare (originalFile, comparedFile);

        assertEquals (4, result.getCorrectLines ());
    }



    /**
     * Test of compare method, of class VagueOutputGrading.
     */
    @Test
    public void testCompareCorrect2 () throws Exception {
        System.out.println ("tokenizer and vague comparator test 2");
        File originalFile = File.createTempFile ("orig", "file");
        File comparedFile = File.createTempFile ("orig", "file");

        IOUtils.writeAll (originalFile.getAbsolutePath (), "1 \n\n2 3.3 4 5   6   ");
        IOUtils.writeAll (comparedFile.getAbsolutePath (), "1 2 3.2\n 4 5");


        VagueOutputGrading instance = new VagueOutputGrading ();
        OutputGradeResult result = (OutputGradeResult) instance.compare (originalFile, comparedFile);

        assertEquals (4, result.getCorrectLines ());
        assertEquals (4.0 / 6.0, result.getResult (), 0.01);
    }



    /**
     * Test of compare method, of class VagueOutputGrading.
     */
    @Test
    public void testCompareCorrect3 () throws Exception {
        System.out.println ("tokenizer and vague comparator test 3");
        File originalFile = File.createTempFile ("orig", "file");
        File comparedFile = File.createTempFile ("orig", "file");

        IOUtils.writeAll (originalFile.getAbsolutePath (), "   ");
        IOUtils.writeAll (comparedFile.getAbsolutePath (), "   ");


        VagueOutputGrading instance = new VagueOutputGrading ();
        OutputGradeResult result = (OutputGradeResult) instance.compare (originalFile, comparedFile);

        assertEquals (0, result.getCorrectLines ());
        assertEquals (0.0, result.getResult (), 0.0);
    }



    /**
     * Test of compare method, of class VagueOutputGrading.
     */
    @Test
    public void testCompareCorrect4 () throws Exception {
        System.out.println ("tokenizer and vague comparator test 4");
        File originalFile = File.createTempFile ("orig", "file");
        File comparedFile = File.createTempFile ("orig", "file");

        IOUtils.writeAll (originalFile.getAbsolutePath (), "  1  ");
        IOUtils.writeAll (comparedFile.getAbsolutePath (), "  1  ");


        VagueOutputGrading instance = new VagueOutputGrading ();
        OutputGradeResult result = (OutputGradeResult) instance.compare (originalFile, comparedFile);

        assertEquals (1, result.getCorrectLines ());
        assertEquals (1.0, result.getResult (), 0.0);
    }
}