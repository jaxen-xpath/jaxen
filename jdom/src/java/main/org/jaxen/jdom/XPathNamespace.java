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


package org.jaxen.jdom;

import org.jdom.Element;
import org.jdom.Namespace;

/** Wrapper for JDOM namespace nodes to give them a parent, as required
 *  by the XPath data model.
 *
 *  @author Erwin Bolwidt
 */
public class XPathNamespace
{
    private Element jdomElement;

    private Namespace jdomNamespace;

    /** Creates a namespace-node wrapper for a namespace node that hasn't been
     *  assigned to an element yet.
     */
    public XPathNamespace( Namespace jdomNamespace )
    {
        this.jdomNamespace = jdomNamespace;
    }

    /** Creates a namespace-node wrapper for a namespace node that is assigned
     *  to the given JDOM element.
     */
    public XPathNamespace( Element jdomElement, Namespace jdomNamespace )
    {
        this.jdomElement = jdomElement;
        this.jdomNamespace = jdomNamespace;
    }

    /** Returns the JDOM element from which this namespace node has been 
     *  retrieved. The result may be null when the namespace node has not yet
     *  been assigned to an element.
     */
    public Element getJDOMElement()
    {
        return jdomElement;
    }

    /** Sets or changes the element to which this namespace node is assigned.
     */
    public void setJDOMElement( Element jdomElement )
    {
        this.jdomElement = jdomElement;
    }

    /** Returns the JDOM namespace object of this namespace node; the JDOM
     *  namespace object contains the prefix and URI of the namespace.
     */
    public Namespace getJDOMNamespace()
    {
        return jdomNamespace;
    }

    public String toString()
    {
        return ( "[xmlns:" + jdomNamespace.getPrefix() + "=\"" +
                 jdomNamespace.getURI() + "\", element=" +
                 jdomElement.getName() + "]" );
    }
} 
