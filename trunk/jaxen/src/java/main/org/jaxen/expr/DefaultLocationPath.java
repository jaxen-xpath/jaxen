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







package org.jaxen.expr;



import org.jaxen.Context;

import org.jaxen.ContextSupport;

import org.jaxen.UnsupportedAxisException;

import org.jaxen.JaxenException;



import org.jaxen.util.IdentityHashMap;



import org.jaxen.util.SingleObjectIterator;

import org.jaxen.util.LinkedIterator;



import java.util.List;

import java.util.ArrayList;

import java.util.LinkedList;

import java.util.Iterator;

import java.util.Map;

import java.util.Collections;



abstract class DefaultLocationPath extends DefaultExpr implements LocationPath

{

    private List steps;



    private final static Object PRESENT = new Object();



    public DefaultLocationPath()

    {

        this.steps = new LinkedList();

    }



    public void addStep(Step step)

    {

        getSteps().add( step );

    }



    public List getSteps()

    {

        return this.steps;

    }



    public Expr simplify()

    {

        Iterator stepIter = getSteps().iterator();

        Step     eachStep = null;



        while ( stepIter.hasNext() )

        {

            eachStep = (Step) stepIter.next();



            eachStep.simplify();

        }

        return this;

    }



    public String getText()

    {

        StringBuffer buf = new StringBuffer();

        Iterator stepIter = getSteps().iterator();



        while ( stepIter.hasNext() )

        {

            buf.append( ((Step)stepIter.next()).getText() );



            if ( stepIter.hasNext() )

            {

                buf.append( "/" );

            }

        }



        return buf.toString();

    }



    public String toString()

    {

        StringBuffer buf = new StringBuffer();



        Iterator stepIter = getSteps().iterator();



        while( stepIter.hasNext() )

        {

            buf.append( stepIter.next().toString() );



            if ( stepIter.hasNext() )

            {

                buf.append("/");

            }

        }



        return buf.toString();

    }



    public boolean isAbsolute()

    {

        return false;

    }

    public Object evaluate(Context context) throws JaxenException
    {
        List     contextNodeSet  = new ArrayList();
        Map      unique          = new IdentityHashMap();

        contextNodeSet.addAll( context.getNodeSet() );

        Object   eachContextNode = null;

        Iterator stepIter        = getSteps().iterator();

        Step     eachStep        = null;
        int      contextSize     = 0;


        while ( stepIter.hasNext() )
        {
            eachStep = (Step) stepIter.next();
            contextSize = contextNodeSet.size();

            List interimSet = new ArrayList();
            List newNodeSet = new ArrayList();

            INNER:

            for ( int i = 0 ; i < contextSize ; ++i )
            {
                eachContextNode = contextNodeSet.get( i );
                Iterator axisNodeIter = eachStep.axisIterator( eachContextNode,
                                                               context.getContextSupport() );

                if ( axisNodeIter == null )
                {
                    continue INNER;
                }

                Object   eachAxisNode = null;

                while ( axisNodeIter.hasNext() )
                {
                    eachAxisNode = axisNodeIter.next();
                    // System.err.println( "----> " + eachAxisNode + " // " + eachStep.matches( eachAxisNode, context.getContextSupport()  ) );

                    if ( eachStep.matches( eachAxisNode,
                                           context.getContextSupport() ) )
                    {
                        if ( ! unique.containsKey( eachAxisNode ) )
                        {
                            unique.put( eachAxisNode, PRESENT );
                            interimSet.add( eachAxisNode );
                        }
                    }
                }
           		newNodeSet.addAll(eachStep.getPredicateSet().evaluatePredicates(
                                    interimSet,context.getContextSupport() ));
                interimSet.clear();
            }
            unique.clear();
            contextNodeSet = newNodeSet;
        }
        return contextNodeSet;
    }

}

