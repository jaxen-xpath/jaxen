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



import java.util.Iterator;

import java.util.List;

import java.util.ListIterator;



import org.jaxen.JaxenException;

import org.jaxen.JaxenHandler;

import org.jaxen.expr.*;

import org.jaxen.pattern.*;



import org.saxpath.Axis;

import org.saxpath.SAXPathException;

import org.saxpath.XPathReader;

import org.saxpath.XPathSyntaxException;

import org.saxpath.helpers.XPathReaderFactory;



/** <code>PatternParser</code> is a helper class for parsing

  * XSLT patterns

  *

  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>

  */

public class PatternParser 

{

    private static final boolean TRACE = false;

    private static final boolean USE_HANDLER = false;

    

    public static Pattern parse(String text) throws JaxenException, SAXPathException

    {

        if ( USE_HANDLER )

        {

            XPathReader reader = XPathReaderFactory.createReader();            

            PatternHandler handler = new PatternHandler();       

            

            handler.setXPathFactory( new DefaultXPathFactory() );            

            reader.setXPathHandler( handler );

            reader.parse( text );

            

            return handler.getPattern();

        }

        else

        {

            XPathReader reader = XPathReaderFactory.createReader();            

            JaxenHandler handler = new JaxenHandler();

            

            handler.setXPathFactory( new DefaultXPathFactory() );            

            reader.setXPathHandler( handler );

            reader.parse( text );



            Pattern pattern = convertExpr( handler.getXPathExpr().getRootExpr() );

            return pattern.simplify();

        }

    }

    

    protected static Pattern convertExpr(Expr expr) throws JaxenException 

    {

        if ( TRACE )

        {

            System.out.println( "Converting: " + expr + " into a pattern." );

        }

        

        if ( expr instanceof LocationPath )

        {

            return convertExpr( (LocationPath) expr );

        }

        else if ( expr instanceof FilterExpr )

        {

            LocationPathPattern answer = new LocationPathPattern();

            answer.addFilter( (FilterExpr) expr );

            return answer;

        }

        else if ( expr instanceof UnionExpr )

        {

            UnionExpr unionExpr = (UnionExpr) expr;

            Pattern lhs = convertExpr( unionExpr.getLHS() );

            Pattern rhs = convertExpr( unionExpr.getRHS() );

            return new UnionPattern( lhs, rhs );

        }

        else 

        {

            LocationPathPattern answer = new LocationPathPattern();

            answer.addFilter( new DefaultFilterExpr( expr,
                                new PredicateSet()) );

            return answer;

        }

    }

    

    protected static LocationPathPattern convertExpr(LocationPath locationPath) throws JaxenException

    {

        LocationPathPattern answer = new LocationPathPattern();        

        //answer.setAbsolute( locationPath.isAbsolute() );

        List steps = locationPath.getSteps();

        

        // go through steps backwards

        LocationPathPattern path = answer;

        boolean first = true;

        for ( ListIterator iter = steps.listIterator( steps.size() ); iter.hasPrevious(); ) 

        {

            Step step = (Step) iter.previous();

            if ( first )

            {

                first = false;

                path = convertStep( path, step );

            }

            else

            {

                if ( navigationStep( step ) ) 

                {

                    LocationPathPattern parent = new LocationPathPattern();

                    int axis = step.getAxis();

                    if ( axis == Axis.DESCENDANT || axis == Axis.DESCENDANT_OR_SELF ) 

                    {

                        path.setAncestorPattern( parent );

                    }

                    else

                    {

                        path.setParentPattern( parent );

                    }

                    path = parent;

                }

                path = convertStep( path, step );

            }

        }

        if ( locationPath.isAbsolute() )

        {

            LocationPathPattern parent = new LocationPathPattern( NodeTypeTest.DOCUMENT_TEST );

            path.setParentPattern( parent );

        }

        return answer;

    }   

    

    protected static LocationPathPattern convertStep(LocationPathPattern path, Step step) throws JaxenException

    {

        if ( step instanceof DefaultAllNodeStep )

        {

            int axis = step.getAxis();

            if ( axis == Axis.ATTRIBUTE )

            {

                path.setNodeTest( NodeTypeTest.ATTRIBUTE_TEST );

            }

            else 

            {

                path.setNodeTest( NodeTypeTest.ELEMENT_TEST );

            }

        }

        else if ( step instanceof DefaultCommentNodeStep )

        {

            path.setNodeTest( NodeTypeTest.COMMENT_TEST );

        }

        else if ( step instanceof DefaultProcessingInstructionNodeStep )

        {

            path.setNodeTest( NodeTypeTest.PROCESSING_INSTRUCTION_TEST );

        }

        else if ( step instanceof DefaultTextNodeStep )

        {

            path.setNodeTest( TextNodeTest.SINGLETON );

        }

        else if ( step instanceof DefaultCommentNodeStep )

        {

            path.setNodeTest( NodeTypeTest.COMMENT_TEST );

        }

        else if ( step instanceof DefaultNameStep )

        {

            DefaultNameStep nameStep = (DefaultNameStep) step;

            String localName = nameStep.getLocalName();

            String prefix = nameStep.getPrefix();

            int axis = nameStep.getAxis();

            short nodeType = Pattern.ELEMENT_NODE;

            if ( axis == Axis.ATTRIBUTE )

            {

                nodeType = Pattern.ATTRIBUTE_NODE;

            }

            if ( nameStep.isMatchesAnyName() )

            {

                if ( prefix.length() == 0 || prefix.equals( "*" ) ) 

                {

                    if ( axis == Axis.ATTRIBUTE )

                    {

                        path.setNodeTest( NodeTypeTest.ATTRIBUTE_TEST );

                    }

                    else 

                    {

                        path.setNodeTest( NodeTypeTest.ELEMENT_TEST );

                    }

                }

                else 

                {

                    path.setNodeTest( new NamespaceTest( prefix, nodeType ) );

                }

            }

            else 

            {

                path.setNodeTest( new NameTest( localName, nodeType ) );

                // XXXX: should support namespace in the test too

            }

            return convertDefaultStep(path, nameStep);

        }

        else if ( step instanceof DefaultStep )

        {

            return convertDefaultStep(path, (DefaultStep) step);

        }

        else 

        {

            throw new JaxenException( "Cannot convert: " + step + " to a Pattern" );            

        }

        return path;

    }

    

    protected static LocationPathPattern convertDefaultStep(LocationPathPattern path, DefaultStep step) throws JaxenException

    {

        List predicates = step.getPredicates();

        if ( ! predicates.isEmpty() ) 

        {

            DefaultFilterExpr filter = new DefaultFilterExpr(new PredicateSet());

            for ( Iterator iter = predicates.iterator(); iter.hasNext(); )

            {

                filter.addPredicate( (Predicate) iter.next() );

            }

            path.addFilter( filter );

        }         

        return path;

    }

    

    protected static boolean navigationStep( Step step )

    {

        if ( step instanceof DefaultNameStep )

        {

            return true;

        }

        else

        if ( step.getClass().equals( DefaultStep.class ) )

        {

            DefaultStep defaultStep = (DefaultStep) step;

            return ! step.getPredicates().isEmpty();

        }

        else 

        {

            return true;

        }

    }

}

