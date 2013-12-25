package cz.edu.x3m.grading;

import cz.edu.x3m.grading.exception.GradingException;
import cz.edu.x3m.grading.exception.GradingRuntimeException;
import cz.edu.x3m.grading.time.TimeGradeSetting;
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
public class SolutionGradingTest {

    public SolutionGradingTest () {
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
     * Test of grade method, of class SolutionGrading.
     */
    @Test(expected = GradingException.class)
    public void testGrade () throws Exception {
        System.out.println ("grade");
        SolutionGrading instance = new SolutionGrading ();
        instance.grade ();
    }
}