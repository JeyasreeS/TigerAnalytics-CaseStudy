package com.retail;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class RetailStoreApplicationTest 
    extends TestCase
{
    /**
     * 
     */
    public RetailStoreApplicationTest( String testName )
    {
        super( testName );
    }

    /**
     * 
     */
    public static Test suite()
    {
        return new TestSuite( RetailStoreApplicationTest.class );
    }

    /**
     * 
     */
    public void testApp()
    {
        assertTrue( true );
    }
}
