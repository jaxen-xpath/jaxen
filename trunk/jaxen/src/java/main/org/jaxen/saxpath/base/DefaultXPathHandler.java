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

import org.jaxen.saxpath.XPathHandler;

class DefaultXPathHandler implements XPathHandler
{
    static class Singleton
    {
        static final DefaultXPathHandler instance = new DefaultXPathHandler();
    }

    public static XPathHandler getInstance()
    {
        return Singleton.instance;
    }
    
    public void startXPath()
    {
    }
    
    public void endXPath()
    {
    }

    public void startPathExpr()
    {
    }

    public void endPathExpr()
    {
    }

    public void startAbsoluteLocationPath()
    {
    }

    public void endAbsoluteLocationPath()
    {
    }

    public void startRelativeLocationPath()
    {
    }

    public void endRelativeLocationPath()
    {
    }

    public void startNameStep(int axis,
                              String prefix,
                              String localName)
    {
    }
    public void startTextNodeStep(int axis)
    {
    }
    
    public void endTextNodeStep()
    {
    }

    public void startCommentNodeStep(int axis)
    {
    }

    public void endCommentNodeStep()
    {
    }
        
    public void startAllNodeStep(int axis)
    {
    }

    public void endAllNodeStep()
    {
    }

    public void startProcessingInstructionNodeStep(int axis,
                                                   String name)
    {
    }
    
    public void endProcessingInstructionNodeStep()
    {
    }
    
    public void endNameStep()
    {
    }

    public void startPredicate()
    {
    }
    
    public void endPredicate()
    {
    }

    public void startFilterExpr()
    {
    }

    public void endFilterExpr()
    {
    }

    public void startOrExpr()
    {
    }

    public void endOrExpr(boolean create)
    {
    }

    public void startAndExpr()
    {
    }

    public void endAndExpr(boolean create)
    {
    }

    public void startEqualityExpr()
    {
    }

    public void endEqualityExpr(int operator)
    {
    }

    public void startRelationalExpr()
    {
    }

    public void endRelationalExpr(int operator)
    {
    }

    public void startAdditiveExpr()
    {
    }

    public void endAdditiveExpr(int operator)
    {
    }

    public void startMultiplicativeExpr()
    {
    }

    public void endMultiplicativeExpr(int operator)
    {
    }

    public void startUnaryExpr()
    {
    }

    public void endUnaryExpr(int operator)
    {
    }

    public void startUnionExpr()
    {
    }
    
    public void endUnionExpr(boolean create)
    {
    }

    public void number(int number)
    {
    }

    public void number(double number)
    {
    }

    public void literal(String literal)
    {
    }

    public void variableReference(String prefix,
                                  String variableName)
    {
    }

    public void startFunction(String prefix,
                              String functionName)
    {
    }

    public void endFunction()
    {
    }
}
