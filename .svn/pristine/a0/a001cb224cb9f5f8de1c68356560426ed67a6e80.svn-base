package com.bdv.pruebas.junit;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.bdv.pruebas.junit.sitme.SitmeTest;


public class PruebasSuites {
    
    /**
     * Assembles and returns a test suite
     * containing all known tests.
     *
     * New tests should be added here!
     *
     * @return A non-null test suite.
     */
    public static Test suite() {

        TestSuite suite = new TestSuite();    
        suite.addTest(SitmeTest.suite());
        
        //suite.addTest(xxxTest.suite());
        return suite;
    }

    /**
     * Runs the test suite.
     */
    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }
}