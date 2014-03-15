package cz.edu.x3m.processing;

import cz.edu.x3m.processing.execption.ExecutionException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Jan Hybs <x3mSpeedy@gmail.com>
 */
public class LanguageFactoryTest {
    
    public LanguageFactoryTest () {
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
     * Test of getInstance method, of class LanguageFactory.
     */
    @Test(expected = ExecutionException.class)
    public void testGetInstance () throws Exception {
        System.out.println ("getInstance");
        LanguageSupportFactory.getInstance (null);
    }
}