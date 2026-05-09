package org.jaxen.test;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.jaxen.JaxenException;
import org.jaxen.javabean.DocumentNavigator;
import org.jaxen.javabean.Element;
import org.jaxen.javabean.JavaBeanXPath;
import org.jaxen.saxpath.helpers.XPathReaderFactory;

public class JavaBeanNavigatorTest
    extends TestCase
{

    protected void setUp() throws Exception
    {
        System.setProperty( XPathReaderFactory.DRIVER_PROPERTY,
                            "" );
    }

    public void testSomething() throws JaxenException {
        
        // The position() function does not really have any meaning 
        // for JavaBeans, but we know three of them will come before the fourth,
        // even if we don't know which ones.
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
    
    public void testNamespaceAxisIncludesImplicitXmlNamespace() {
        DocumentNavigator navigator = (DocumentNavigator) DocumentNavigator.getInstance();
        Person bob = new Person( "bob", 30 );
        Iterator<?> namespaceAxis = navigator.getNamespaceAxisIterator(new Element(null, "root", bob));
        assertTrue(namespaceAxis.hasNext());
        Object xmlNamespace = namespaceAxis.next();
        assertEquals("xml", navigator.getNamespacePrefix(xmlNamespace));
        assertEquals("http://www.w3.org/XML/1998/namespace",
                navigator.getNamespaceStringValue(xmlNamespace));
        assertFalse(namespaceAxis.hasNext());
    }
    
    public void testSelectImplicitXmlNamespace() throws JaxenException {
        JavaBeanXPath xpath = new JavaBeanXPath("namespace::xml");
        Person bob = new Person( "bob", 30 );
        List<?> xmlNamespaceNodes = xpath.selectNodes(bob);
        assertEquals(1, xmlNamespaceNodes.size());
    }

}
