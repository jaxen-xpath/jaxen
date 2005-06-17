package org.jaxen.javabean;

import java.util.List;

import junit.framework.TestCase;

import org.jaxen.JaxenException;
import org.jaxen.saxpath.helpers.XPathReaderFactory;

public class DocumentNavigatorTest
    extends TestCase
{

    protected void setUp() throws Exception
    {
        System.setProperty( XPathReaderFactory.DRIVER_PROPERTY,
                            "" );
    }

    public void testSomething() throws JaxenException {
        
        // XXX Does the position() function really have any meaning for JavaBeans?
        // How do we know which one comes first? 
        JavaBeanXPath xpath = new JavaBeanXPath( "brother[position()<4]/name" );

        Person bob = new Person( "bob", 30 );

        bob.addBrother( new Person( "billy", 34 ) );
        bob.addBrother( new Person( "seth", 29 ) );
        bob.addBrother( new Person( "dave", 32 ) );
        bob.addBrother( new Person( "jim", 29 ) );
        bob.addBrother( new Person( "larry", 42 ) );
        bob.addBrother( new Person( "ted", 22 ) );

        List result = (List) xpath.evaluate( bob );
        assertEquals(3, result.size());
        
    }

}
