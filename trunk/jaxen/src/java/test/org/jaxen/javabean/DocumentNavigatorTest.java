package org.jaxen.javabean;

import junit.framework.TestCase;

public class DocumentNavigatorTest
    extends TestCase
{

    public void testNothing()
        throws Exception
    {
        JavaBeanXPath xpath = new JavaBeanXPath( "./brother[name='ted']/name" );

        Person bob = new Person( "bob", 30 );
        Person billy = new Person( "billy", 34 );
        Person ted = new Person( "ted", 55 );

        bob.addBrother( billy );
        bob.addBrother( ted );

        System.err.println( xpath.evaluate( bob ) );
    }

}
