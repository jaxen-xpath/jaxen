
package org.jaxen;

import junit.framework.TestCase;

import org.jaxen.Context;
import org.jaxen.Navigator;
import org.jaxen.ContextSupport;
import org.jaxen.XPathFunctionContext;
import org.jaxen.JaXPath;

import org.saxpath.SAXPathException;
import org.saxpath.XPathSyntaxException;

import java.util.Iterator;
import java.util.List;
import java.util.Collections;

public abstract class NavigatorTestBase extends TestCase
{
    private ContextSupport support;
    private Context        context;

    private Object         document;
    private Object         initialContext;

    static private String[] ignore_paths = {
    };

    static private String[] paths = {
        "bar/baz",
        "bar/baz/../../bar/baz",
        "bar/baz/self::node()",
        "bar/baz/parent::node()",
        "@taco",
        "bar[1]",
        "bar/baz[1]",
        "bar/baz[2]",
        "bar/baz[@bazThree]",
        "1 + 3",
        "1 + 3.2",
        "3 * 4.1",
        "bar and bar",
        "cheese or tacos",
        "cheese or @taco",
        "comment()",
        "attribute::*",
        "*/baz",
        "count(bar)",
        "count(*/baz)",
        "count(*/baz) * ( 10 + ( 2 * 5 ) ) div 7",
        "count(*/baz) * ( 10 + ( 2 * 5 ) ) mod 7",
        "count(*/baz) * ( count(bar) + 3 )",
        "count(*/baz) * ( count(bar) + 3 ) + false()",
        "count(*/baz) * ( count(bar) + 3 ) + true()",
        "/foo",
        "/bar",
        "//foo",
        "//bar",
        "//baz",
        "/../../..",
        "/../../../foo",
        "/../../../foo/..",
        "bar/../..",
        "attribute::*",
        "//attribute::*",
        "/foo//baz",
        ".//baz",
        "false()",
        "true()",
        "/comment()",
        "//comment()",
        "//attribute::*[.='crunchy']",
        "//attribute::*[.!='crunchy']",
    };

    public NavigatorTestBase(String name)
    {
        super( name );
    }

    abstract public Object getDocument();
    abstract public Object getInitialContextNode();
    abstract public Navigator getNavigator();

    public void setUp()
    {
        this.document = getDocument();
        this.initialContext = getInitialContextNode();

        this.support = new ContextSupport( null,
                                           XPathFunctionContext.getInstance(),
                                           null,
                                           getNavigator() );

        this.context = new Context( this.support );
        System.err.println(" context->" + this.context);
    }

    public void tearDown()
    {
    }

    public void testOneStep()
    {

        for ( int i = 0 ; i < paths.length ; ++i )
        {
            this.context.setNodeSet( Collections.singletonList( this.getInitialContextNode() ) );
            
            try
            {
                JaXPath xpath = new JaXPath( paths[i] );
                //JaXPath xpath = new JaXPath("bar/baz/../../bar/baz");
                // JaXPath xpath = new JaXPath("bar/baz");
                
                System.err.println("------------------------------------------------");
                System.err.println("applying: " + xpath );
                System.err.println("applying: " + xpath.debug() );
                System.err.println("------------------------------------------------");
                
                List results = xpath.select( this.context );
                
                assertNotNull( results );
                
                Iterator resultIter = results.iterator();
                
                while ( resultIter.hasNext() )
                {
                    System.err.println( "  == " + resultIter.next() );
                }
                System.err.println("------------------------------------------------");
            }
            catch (XPathSyntaxException e)
            {
                fail( e.getMultilineMessage() );
            }
            catch (SAXPathException e)
            {
                fail( e.getMessage() );
            }
        }

    }
}
