package org.jaxen.javabean;

import junit.framework.TestCase;
import org.jaxen.saxpath.helpers.XPathReaderFactory;

public class DocumentNavigatorTest
    extends TestCase
{

    protected void setUp() throws Exception
    {
        System.setProperty( XPathReaderFactory.DRIVER_PROPERTY,
                            "" );
    }

    public void testNothing()
        throws Exception
    {
        JavaBeanXPath xpath = new JavaBeanXPath( "brother[position()<4]/name" );

        Person bob = new Person( "bob", 30 );

        bob.addBrother( new Person( "billy", 34 ) );
        bob.addBrother( new Person( "seth", 29 ) );
        bob.addBrother( new Person( "dave", 32 ) );
        bob.addBrother( new Person( "jim", 29 ) );
        bob.addBrother( new Person( "larry", 42 ) );
        bob.addBrother( new Person( "ted", 22 ) );

        System.out.println( xpath.evaluate( bob ) );
    }

}