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




package org.jaxen.saxpath.base;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jaxen.saxpath.XPathHandler;

class ConformanceXPathHandler implements XPathHandler
{
    private List events;

    ConformanceXPathHandler()
    {
        this.events = new LinkedList();
    }

    public void startXPath()
    {
        addEvent( "startXPath()" );
    }

    public void endXPath()
    {
        addEvent( "endXPath()" );
    }

    public void startPathExpr()
    {
        addEvent( "startPathExpr()" );
    }

    public void endPathExpr()
    {
        addEvent( "endPathExpr()" );
    }

    public void startAbsoluteLocationPath()
    {
        addEvent( "startAbsoluteLocationPath()" );
    }
    public void endAbsoluteLocationPath()
    {
        addEvent( "endAbsoluteLocationPath()" );
    }

    public void startRelativeLocationPath()
    {
        addEvent( "startRelativeLocationPath()" );
    }

    public void endRelativeLocationPath()
    {
        addEvent( "endRelativeLocationPath()" );
    }

    public void startNameStep(int axis,
                              String prefix,
                              String localName)
    {
        addEvent( "startNameStep(" + axis + ", \"" + prefix + "\", \"" + localName + "\")" );
    }

    public void endNameStep()
    {
        addEvent( "endNameStep()" );
    }

    public void startTextNodeStep(int axis)
    {
        addEvent( "startTextNodeStep(" + axis + ")" );
    }
    public void endTextNodeStep()
    {
        addEvent( "endTextNodeStep()" );
    }

    public void startCommentNodeStep(int axis)
    {
        addEvent( "startCommentNodeStep(" + axis + ")" );
    }

    public void endCommentNodeStep()
    {
        addEvent( "endCommentNodeStep()" );
    }

    public void startAllNodeStep(int axis)
    {
        addEvent( "startAllNodeStep(" + axis + ")" );
    }

    public void endAllNodeStep()
    {
        addEvent( "endAllNodeStep()" );
    }

    public void startProcessingInstructionNodeStep(int axis,
                                                   String name)
    {
        addEvent( "startProcessingInstructionNodeStep(" + axis + ", \"" + name + "\")" );
    }
    public void endProcessingInstructionNodeStep()
    {
        addEvent( "endProcessingInstructionNodeStep()" );
    }

    public void startPredicate()
    {
        addEvent( "startPredicate()" );
    }

    public void endPredicate()
    {
        addEvent( "endPredicate()" );
    }

    public void startFilterExpr()
    {
        addEvent( "startFilterExpr()" );
    }

    public void endFilterExpr()
    {
        addEvent( "endFilterExpr()" );
    }

    public void startOrExpr()
    {
        addEvent( "startOrExpr()" );
    }

    public void endOrExpr(boolean create)
    {
        addEvent( "endOrExpr(" + create + ")" );
    }

    public void startAndExpr()
    {
        addEvent( "startAndExpr()" );
    }

    public void endAndExpr(boolean create)
    {
        addEvent( "endAndExpr(" + create + ")" );
    }

    public void startEqualityExpr()
    {
        addEvent( "startEqualityExpr()" );
    }

    public void endEqualityExpr(int operator)
    {
        addEvent( "endEqualityExpr(" + operator + ")" );
    }

    public void startRelationalExpr()
    {
        addEvent( "startRelationalExpr()" );
    }

    public void endRelationalExpr(int operator)
    {
        addEvent( "endRelationalExpr(" + operator + ")" );
    }

    public void startAdditiveExpr()
    {
        addEvent( "startAdditiveExpr()" );
    }

    public void endAdditiveExpr(int operator)
    {
        addEvent( "endAdditiveExpr(" + operator + ")" );
    }

    public void startMultiplicativeExpr()
    {
        addEvent( "startMultiplicativeExpr()" );
    }

    public void endMultiplicativeExpr(int operator)
    {
        addEvent( "endMultiplicativeExpr(" + operator + ")" );
    }

    public void startUnaryExpr()
    {
        addEvent( "startUnaryExpr()" );
    }

    public void endUnaryExpr(int operator)
    {
        addEvent( "endUnaryExpr(" + operator + ")" );
    }

    public void startUnionExpr()
    {
        addEvent( "startUnionExpr()" );
    }

    public void endUnionExpr(boolean create)
    {
        addEvent( "endUnionExpr(" + create + ")" );
    }

    public void number(int number)
    {
        addEvent( "number(" + number + ")" );
    }

    public void number(double number)
    {
        addEvent( "number(" + number + ")" );
    }

    public void literal(String literal)
    {
        addEvent( "literal(\"" + literal + "\")" );
    }

    public void variableReference(String prefix,
                                  String variableName)
    {
        addEvent( "variableReference(\"" + prefix + ":" + variableName + "\")" );
    }

    public void startFunction(String prefix,
                              String functionName)
    {
        addEvent( "startFunction(\"" + prefix + ":" + functionName + "\")" );
    }

    public void endFunction()
    {
        addEvent( "endFunction()" );
    }

    private void addEvent(String eventStr)
    {
        this.events.add( eventStr );
    }

    public boolean equals(Object thatObj)
    {
        if ( thatObj instanceof ConformanceXPathHandler )
        {
            ConformanceXPathHandler that = (ConformanceXPathHandler) thatObj;

            return ( this.events.equals( that.events ) );
        }

        return false;
    }
    
    public int hashCode() {
        return this.events.hashCode();
    }
    

    public String toString()
    {
        Iterator eventIter = this.events.iterator();
        int      i = 0;

        StringBuffer buf = new StringBuffer();

        while( eventIter.hasNext() )
        {
            buf.append("(").append(i).append(") ").append( eventIter.next().toString() ).append("\n");
            ++i;
        }

        return buf.toString();
    }
}
