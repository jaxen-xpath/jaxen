/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright 2000-2002 bob mcwhirter & James Strachan.
 * All rights reserved.
 *
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 * 
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 * 
 *   * Neither the name of the Jaxen Project nor the names of its
 *     contributors may be used to endorse or promote products derived 
 *     from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jaxen.Context;
import org.jaxen.ContextSupport;
import org.jaxen.JaxenException;

abstract class DefaultLocationPath extends DefaultExpr implements LocationPath
{
    private List steps;
    
    /**
     * Create a new empty location path.
     */
    DefaultLocationPath()
    {
        this.steps = new LinkedList();
    }

    public void addStep(Step step)
    {
        getSteps().add(step);
    }

    public List getSteps()
    {
        return this.steps;
    }

    public Expr simplify()
    {
        Iterator stepIter = getSteps().iterator();
        Step eachStep = null;
        while (stepIter.hasNext())
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
        while (stepIter.hasNext())
        {
            buf.append(((Step) stepIter.next()).getText());
            if (stepIter.hasNext())
            {
                buf.append("/");
            }
        }
        return buf.toString();
    }

    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        Iterator stepIter = getSteps().iterator();
        while (stepIter.hasNext())
        {
            buf.append(stepIter.next().toString());
            if (stepIter.hasNext())
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
        List nodeSet = context.getNodeSet();
        List contextNodeSet = new ArrayList(nodeSet);
        ContextSupport support = context.getContextSupport();
        Context stepContext = new Context(support);
        Iterator stepIter = getSteps().iterator();
        while ( stepIter.hasNext() )
        {
            Step eachStep = (Step) stepIter.next();
            stepContext.setNodeSet(contextNodeSet);
            contextNodeSet = eachStep.evaluate(stepContext);
            // now we need to reverse the list if this is a reverse axis
            if (isReverseAxis(eachStep)) {
                Collections.reverse(contextNodeSet);
            }
        }
        
        if (getSteps().size() > 1) {
            Collections.sort(contextNodeSet, new NodeComparator(support.getNavigator()));
        }
        
        return contextNodeSet;
    }

    private boolean isReverseAxis(Step step) {

        int axis = step.getAxis();
        return axis == org.jaxen.saxpath.Axis.PRECEDING
          || axis == org.jaxen.saxpath.Axis.PRECEDING_SIBLING
          || axis == org.jaxen.saxpath.Axis.ANCESTOR
          || axis == org.jaxen.saxpath.Axis.ANCESTOR_OR_SELF;
    }

}

