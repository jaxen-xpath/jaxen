
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
    /*
     * 
     * document being tested is:
     * 
     * <!-- I am a document comment -->
     * <foo taco="crunchy">
     *    <bar>
     *       <baz>
     *         <!-- I am a comment -->
     *       <cheese>
     *       <baz/>
     *       <baz bazThree="cheese"/>
     *    </bar>
     * </foo>
     * 
     * 
     */
    private ContextSupport support;
    private Context        context;

    private Object         document;
    private Object         initialContext;

    static private Expectation[] expectations = {

        new Expectation("bar/baz")
            .expectSize( 3 ),

        new Expectation("bar/baz/../../bar/baz")
            .expectSize( 3 ),

        new Expectation("bar/baz/self::node()")
            .expectSize( 3 ),

        new Expectation("bar/baz/parent::node()")
            .expectSize( 1 ),

        new Expectation("@taco")
            .expectSize( 1 )
            .expectStringValue( "crunchy"),

        new Expectation("bar[1]")
            .expectSize( 1 ),

        new Expectation("bar/baz[1]")
            .expectSize( 1 ),

        new Expectation("bar/baz[2]")
            .expectSize( 1 ),

        new Expectation("bar/baz[@bazThree]")
            .expectSize( 1 ),

        new Expectation("1 + 3")
            .expectNumberValue( 4 ),

        new Expectation("1 + 3.2")
            .expectNumberValue( 4.2, 0.1 ),

        new Expectation("3 * 4.1")
            .expectNumberValue( 12.3, 0.1 ),

        new Expectation("bar and bar")
            .expectSize( 1 ),

        new Expectation("cheese or tacos")
            .expectStringValue( "false" ),

        new Expectation("cheese or @taco")
            .expectStringValue( "true" ),

        new Expectation("comment()").
            expectSize( 1 ).
            expectStringValue( "I am a comment" ),

        new Expectation("attribute::*").
            expectSize( 1 ).
            expectStringValue( "crunchy" ),

        new Expectation("*/baz").
            expectSize( 3 ),

        new Expectation("count(bar)").
            expectNumberValue( 1 ),

        new Expectation("count(*/baz)").
            expectNumberValue( 3 ),

        new Expectation("count(*/baz) * ( 10 + ( 2 * 5 ) ) div 7"),
        new Expectation("count(*/baz) * ( 10 + ( 2 * 5 ) ) mod 7"),
        new Expectation("count(*/baz) * ( count(bar) + 3 )"),
        new Expectation("count(*/baz) * ( count(bar) + 3 ) + false()"),
        new Expectation("count(*/baz) * ( count(bar) + 3 ) + true()"),

        new Expectation("/foo").
            expectSize( 1 ),

        new Expectation("/bar").
            expectSize( 0 ),

        new Expectation("//foo").
            expectSize( 1 ),

        new Expectation("//bar").
            expectSize( 1 ),

        new Expectation("//baz").
            expectSize( 3 ),

        new Expectation("/../../..").
            expectSize( 1 ),

        new Expectation("/../../../foo").
            expectSize( 1 ),

        new Expectation("/../../../foo/..").
            expectSize( 1 ),

        new Expectation("bar/../..").
            expectSize( 1 ),

        new Expectation("attribute::*").
            expectSize( 1 ).
            expectStringValue( "crunchy" ),

        new Expectation("//attribute::*").
            expectSize( 2 ),

        new Expectation("/foo//baz").
            expectSize( 3 ),

        new Expectation(".//baz").
            expectSize( 3 ),

        new Expectation("false()").
            expectStringValue( "false" ),
        new Expectation("true()").
            expectStringValue( "true" ),

        new Expectation("/comment()").
            expectSize( 1 ).
            expectStringValue("I am a document comment"),

        new Expectation("//comment()").
            expectSize( 2 ),

        new Expectation("//attribute::*[.='crunchy']").
            expectSize( 1 ).
            expectStringValue( "crunchy" ),

        new Expectation("//attribute::*[.!='crunchy']").
            expectSize( 1 ).
            expectStringValue( "true" ),
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
    }

    public void tearDown()
    {
    }

    public void testExpectations()
    {
        Expectation expect = null;

        for ( int i = 0 ; i < expectations.length ; ++i )
        {
            expect = expectations[ i ];

            //System.err.println( "xpath: " + expect.getXPath() );

            this.context.setNodeSet( Collections.singletonList( this.getInitialContextNode() ) );

            try
            {
                String xpathStr = expect.getXPath();
                JaXPath xpath = new JaXPath( xpathStr );
                
                List results = xpath.selectNodes( this.context );
                
                assertNotNull( results );
                
                boolean met = expect.wasMet( results,
                                             this.context.getNavigator() );
                
                if ( met )
                {
                    System.err.println( "passed...... (" + expect.getNumberOfExpectations() + ")  " + xpathStr );
                }
                else
                {
                    String failMsg = "expected [" + expect.getFailedExpectation()
                        + "] but was ["
                        + expect.getFailedExpectation().getToCompare( results,
                                                                      this.context.getNavigator() )
                        + "]";

                    System.err.println( "failed...... " + xpathStr + " ## " + failMsg );

                    System.err.println(" actual...... " + results );
                    fail( failMsg );
                }
            }
            catch (SAXPathException e)
            {
                fail( e.getMessage() );
            }
        }
    }
}
