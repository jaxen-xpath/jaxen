/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright (C) 2000-2002 werken digital.
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
 * 3. The name "SAXPath" must not be used to endorse or promote products
 *    derived from this software without prior written permission.  For
 *    written permission, please contact license@saxpath.org.
 * 
 * 4. Products derived from this software may not be called "SAXPath", nor
 *    may "SAXPath" appear in their name, without prior written permission
 *    from the SAXPath Project Management (pm@saxpath.org).
 * 
 * In addition, we request (but do not require) that you include in the 
 * end-user documentation provided with the redistribution and/or in the 
 * software itself an acknowledgement equivalent to the following:
 *     "This product includes software developed by the
 *      SAXPath Project (http://www.saxpath.org/)."
 * Alternatively, the acknowledgment may be graphical using the logos 
 * available at http://www.saxpath.org/
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE SAXPath AUTHORS OR THE PROJECT
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
 * individuals on behalf of the SAXPath Project and was originally 
 * created by bob mcwhirter <bob@werken.com> and 
 * James Strachan <jstrachan@apache.org>.  For more information on the 
 * SAXPath Project, please see <http://www.saxpath.org/>.
 * 
 * $Id$
 */



package org.jaxen.saxpath.helpers;

import org.jaxen.saxpath.XPathHandler;

/**

   Default base class for SAXPath event handlers. 

   This class is available as a convenience base class for SAXPath
   applications: it provides a default implementation for all of the
   callbacks in the core SAXPath handler class, {@link
   org.jaxen.saxpath.XPathHandler}

   Application writers can extend this class when they need to
   implement only part of an interface; parser writers can instantiate
   this class to provide default handlers when the application has not
   supplied its own. */

public class DefaultXPathHandler implements XPathHandler
{
    public DefaultXPathHandler()
    {
    }

    public void startXPath() throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void endXPath() throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void startPathExpr() throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void endPathExpr() throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void startAbsoluteLocationPath() throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void endAbsoluteLocationPath() throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void startRelativeLocationPath() throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void endRelativeLocationPath() throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void startNameStep(int axis,
                              String prefix,
                              String localName) throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void endNameStep() throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void startTextNodeStep(int axis) throws org.jaxen.saxpath.SAXPathException
    {
    }
    public void endTextNodeStep() throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void startCommentNodeStep(int axis) throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void endCommentNodeStep() throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void startAllNodeStep(int axis) throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void endAllNodeStep() throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void startProcessingInstructionNodeStep(int axis,
                                                   String name) throws org.jaxen.saxpath.SAXPathException
    {
    }
    public void endProcessingInstructionNodeStep() throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void startPredicate() throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void endPredicate() throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void startFilterExpr() throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void endFilterExpr() throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void startOrExpr() throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void endOrExpr(boolean create) throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void startAndExpr() throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void endAndExpr(boolean create) throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void startEqualityExpr() throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void endEqualityExpr(int operator) throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void startRelationalExpr() throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void endRelationalExpr(int operator) throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void startAdditiveExpr() throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void endAdditiveExpr(int operator) throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void startMultiplicativeExpr() throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void endMultiplicativeExpr(int operator) throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void startUnaryExpr() throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void endUnaryExpr(int operator) throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void startUnionExpr() throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void endUnionExpr(boolean create) throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void number(int number) throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void number(double number) throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void literal(String literal) throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void variableReference(String prefix,
                                  String variableName) throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void startFunction(String prefix,
                              String functionName) throws org.jaxen.saxpath.SAXPathException
    {
    }

    public void endFunction() throws org.jaxen.saxpath.SAXPathException
    {
    }

}
