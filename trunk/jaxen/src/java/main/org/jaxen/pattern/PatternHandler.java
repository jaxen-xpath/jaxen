// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.pattern;

import org.jaxen.JaxenException;
import org.jaxen.JaxenHandler;
import org.jaxen.expr.XPathFactory;
import org.jaxen.expr.DefaultXPathFactory;
import org.jaxen.expr.FilterExpr;
import org.jaxen.expr.Expr;
import org.jaxen.expr.Step;
import org.jaxen.expr.Predicate;
import org.jaxen.expr.Predicated;

import org.saxpath.XPathHandler;
import org.saxpath.Axis;
import org.saxpath.Operator;

import java.util.LinkedList;
import java.util.Iterator;

/** SAXPath <code>XPathHandler</code> implementation capable
 *  of building Jaxen expression trees which can walk various
 *  different object models.
 *
 *  @author bob mcwhirter (bob@werken.com)
 */
public class PatternHandler extends JaxenHandler
{
    private Pattern pattern;
    
    public PatternHandler()
    {
    }
    
    /** Retrieve the simplified Jaxen Pattern expression tree.
     *
     *  <p>
     *  This method is only valid once <code>XPathReader.parse(...)</code>
     *  successfully returned.
     *  </p>
     *
     *  @return The Pattern expression tree.
     */
    public Pattern getPattern()
    {
        return getPattern( true );
    }

    /** Retrieve the Jaxen Pattern expression tree, optionally
     *  simplified.
     *
     *  <p>
     *  This method is only valid once <code>XPathReader.parse(...)</code>
     *  successfully returned.
     *  </p>
     *
     *  @return The Pattern expression tree.
     */
    public Pattern getPattern(boolean shouldSimplify)
    {
        if ( shouldSimplify && ! this.simplified )
        {
            //System.err.println("simplifyin....");
            this.pattern.simplify();
            this.simplified = true;
        }

        return this.pattern;
    }

    
    
    
    public void endXPath() throws JaxenException
    {
        this.pattern = (Pattern) pop();

        System.out.println( "stack is: " + stack );
        
        popFrame();
    }

    public void endPathExpr() throws JaxenException
    {
        //System.err.println("endPathExpr()");

        // PathExpr ::=   LocationPath
        //              | FilterExpr
        //              | FilterExpr / RelativeLocationPath
        //              | FilterExpr // RelativeLocationPath
        //
        // If the current stack-frame has two items, it's a
        // FilterExpr and a LocationPath (of some flavor).
        //
        // If the current stack-frame has one item, it's simply
        // a FilterExpr, and more than like boils down to a
        // primary expr of some flavor.  But that's for another
        // method...

        LinkedList frame = popFrame();
        
        System.out.println( "endPathExpr(): " + frame );
            
        push( frame.removeFirst() );
/*        
        LocationPathPattern locationPath = new LocationPathPattern();
        push( locationPath );
        while (! frame.isEmpty() )
        {
            Object filter = frame.removeLast();
            if ( filter instanceof NodeTest ) 
            {
                locationPath.setNodeTest( (NodeTest) filter );
            }
            else if ( filter instanceof FilterExpr )
            {
                locationPath.addFilter( (FilterExpr) filter );
            }
            else if ( filter instanceof LocationPathPattern ) 
            {
                LocationPathPattern parent = (LocationPathPattern) filter;
                locationPath.setParentPattern( parent );
                locationPath = parent;
            }
            else if ( filter != null ) 
            {
                throw new JaxenException( "Unknown filter: " + filter );
            }
        }
*/
    }

    public void startAbsoluteLocationPath() throws JaxenException
    {
        //System.err.println("startAbsoluteLocationPath()");
        pushFrame();

        push( createAbsoluteLocationPath() );
    }
    
    public void endAbsoluteLocationPath() throws JaxenException
    {
        //System.err.println("endAbsoluteLocationPath()");
        endLocationPath();
    }

    public void startRelativeLocationPath() throws JaxenException
    {
        //System.err.println("startRelativeLocationPath()");
        pushFrame();

        push( createRelativeLocationPath() );
    }

    public void endRelativeLocationPath() throws JaxenException
    {
        //System.err.println("endRelativeLocationPath()");
        endLocationPath();
    }

    protected void endLocationPath() throws JaxenException
    {
        // start at the back, its the main pattern then add everything else as 
        LinkedList list = popFrame();
        
        System.out.println( "endLocationPath: " + list );

        LocationPathPattern locationPath = (LocationPathPattern) list.removeFirst();
        push( locationPath );
        boolean doneNodeTest = false;
        while ( ! list.isEmpty() )
        {
            Object filter = list.removeFirst();
            if ( filter instanceof NodeTest ) 
            {
                if ( doneNodeTest ) 
                {
                    LocationPathPattern parent = new LocationPathPattern( (NodeTest) filter );
                    locationPath.setParentPattern( parent );
                    locationPath = parent;
                    doneNodeTest = false;
                }   
                else
                {
                    locationPath.setNodeTest( (NodeTest) filter );
                }
            }
            else if ( filter instanceof FilterExpr )
            {
                locationPath.addFilter( (FilterExpr) filter );
            }
            else if ( filter instanceof LocationPathPattern ) 
            {
                LocationPathPattern parent = (LocationPathPattern) filter;
                locationPath.setParentPattern( parent );
                locationPath = parent;
                doneNodeTest = false;
            }
        }
    }

    
    public void startNameStep(int axis,
                              String prefix,
                              String localName) throws JaxenException
    {
        //System.err.println("startNameStep(" + axis + ", " + prefix + ", " + localName + ")");
        pushFrame();

        short nodeType = Pattern.ELEMENT_NODE;            
        switch ( axis ) 
        {
            case Axis.ATTRIBUTE:
                nodeType = Pattern.ATTRIBUTE_NODE;
                break;
            case Axis.NAMESPACE:
                nodeType = Pattern.NAMESPACE_NODE;
                break;
        }
        
        if ( prefix != null && prefix.length() > 0 && ! prefix.equals( "*" ) ) 
        {                    
            push( new NamespaceTest( prefix, nodeType ) );
        }
        if ( localName != null && localName.length() > 0 && ! localName.equals( "*" ) ) 
        {
            push( new NameTest( localName, nodeType ) );
        }
    }

    public void startTextNodeStep(int axis) throws JaxenException
    {
        //System.err.println("startTextNodeStep()");
        pushFrame();
        
        push( new NodeTypeTest( Pattern.TEXT_NODE ) );
    }
    
    public void startCommentNodeStep(int axis) throws JaxenException
    {
        //System.err.println("startCommentNodeStep()");
        pushFrame();

        push( new NodeTypeTest( Pattern.COMMENT_NODE ) );
    }

    public void startAllNodeStep(int axis) throws JaxenException
    {
        //System.err.println("startAllNodeStep()");
        pushFrame();

        push( AnyNodeTest.getInstance() );
    }

    public void startProcessingInstructionNodeStep(int axis,
                                                   String name) throws JaxenException
    {
        //System.err.println("startProcessingInstructionStep()");
        pushFrame();

        // XXXX: should we throw an exception if name is present?            
        push( new NodeTypeTest( Pattern.PROCESSING_INSTRUCTION_NODE ) );
    }
    
    protected void endStep()
    {
        LinkedList list = popFrame();
        if ( ! list.isEmpty() ) 
        {
            push( list.removeFirst() );
            
            if ( ! list.isEmpty() )
            {
                System.out.println( "List should now be empty!" + list );
            }
        }
    }
    

    public void startUnionExpr() throws JaxenException
    {
        //System.err.println("startUnionExpr()");
    }

    public void endUnionExpr(boolean create) throws JaxenException
    {
        //System.err.println("endUnionExpr()");

        if ( create )
        {
            //System.err.println("makeUnionExpr");

            Expr rhs = (Expr) pop();
            Expr lhs = (Expr) pop();

            push( getXPathFactory().createUnionExpr( lhs,
                                                    rhs ) );
        }
    }

    protected Pattern createAbsoluteLocationPath() 
    {
        return new LocationPathPattern( NodeTypeTest.DOCUMENT_TEST );
    }

    protected Pattern createRelativeLocationPath() 
    {
        return new LocationPathPattern();
    }

}
