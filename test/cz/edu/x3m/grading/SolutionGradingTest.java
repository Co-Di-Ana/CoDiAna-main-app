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
     * Test of addGrading method, of class SolutionGrading.
     */
    @Test(expected = GradingRuntimeException.class)
    public void testAddGrading () {
        System.out.println ("addGrading");
        IGrading grading = GradingFactory.getInstance (GradingType.Output, GradingFactory.TYPE_OUTPUT_STRICT);
        SolutionGrading instance = new SolutionGrading ();
        
        instance.addGrading (grading);
        instance.addGrading (grading);
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



    /**
     * Test of setSettings method, of class SolutionGrading.
     */
    @Test(expected = GradingRuntimeException.class)
    public void testSetSettings1 () {
        System.out.println ("setSettings null");
        SolutionGrading instance = new SolutionGrading ();
        instance.setSettings (null);
    }

    /**
     * Test of setSettings method, of class SolutionGrading.
     */
    @Test(expected = GradingRuntimeException.class)
    public void testSetSettings2 () {
        System.out.println ("setSettings wrong class");
        SolutionGrading instance = new SolutionGrading ();
        instance.setSettings (new TimeGradeSetting (true, null, null));
    }
    

    /**
     * Test of setSettings method, of class SolutionGrading.
     */
    @Test
    public void testSetSettings3 () {
        System.out.println ("setSettings correct");
        SolutionGrading instance = new SolutionGrading ();
        instance.setSettings (new SolutionGradingSetting (true));
    }
}