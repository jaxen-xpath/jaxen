/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright (C) 2000-2002 bob mcwhirter & James Strachan.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions, and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions, and the disclaimer that follows 
 *    these conditions in the documentation and/or other materials 
 *    provided with the distribution.
 *
 * 3. The name "Jaxen" must not be used to endorse or promote products
 *    derived from this software without prior written permission.  For
 *    written permission, please contact license@jaxen.org.
 * 
 * 4. Products derived from this software may not be called "Jaxen", nor
 *    may "Jaxen" appear in their name, without prior written permission
 *    from the Jaxen Project Management (pm@jaxen.org).
 * 
 * In addition, we request (but do not require) that you include in the 
 * end-user documentation provided with the redistribution and/or in the 
 * software itself an acknowledgement equivalent to the following:
 *     "This product includes software developed by the
 *      Jaxen Project (http://www.jaxen.org/)."
 * Alternatively, the acknowledgment may be graphical using the logos 
 * available at http://www.jaxen.org/
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE Jaxen AUTHORS OR THE PROJECT
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 * ====================================================================
 * This software consists of voluntary contributions made by many 
 * individuals on behalf of the Jaxen Project and was originally 
 * created by bob mcwhirter <bob@werken.com> and 
 * James Strachan <jstrachan@apache.org>.  For more information on the 
 * Jaxen Project, please see <http://www.jaxen.org/>.
 * 
 * $Id$
 */


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
