/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright (C) 2000-2003 bob mcwhirter & James Strachan.
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


package org.jaxen.dom.html;

import org.jaxen.JaxenException;

import org.jaxen.BaseXPath;

/**
 * An XPath implementation for the W3C HTML DOM model
 *
 * <p>This is an XPath providing element and attribute case-management
 * for HTML documents. Because HTML is case-insensitive, but stores DOM
 * elements in upper case, it is difficult to write XPath queries for HTML
 * documents which are also compatible with their XHTML equivalents.
 * This class converts the element, but not attribute, names of an HTML
 * document to upper or lower case, depending on your specification,
 * so that lower case [element] xpath expressions will work just as well on
 * HTML as they do on XHTML (which is case sensitive and enforces
 * lower case on elements and attributes).  Note that HTML attribute names are
 * stored as lower case in the HTML (and XHTML) DOM already which is why the
 * case of attribute names are not modified.
 *
 * <p>You create a compiled XPath object, then match it against
 * one or more context nodes using the {@link #selectNodes(Object)}
 * method, as in the following example:</p>
 *
 * <pre>
 * XPath path = new HTMLXPath("a/b/c", true); // convert to lower case
 * List results = path.selectNodes(htmlNode);
 * </pre>
 *
 * @see BaseXPath
 * @see org.jaxen.dom.DOMXPath
 *
 * @author David Peterson
 *
 * @version $Revision$
 */
public class HTMLXPath extends BaseXPath
{
    /**
     * Construct given an XPath expression string.
     *
     *  @param xpathExpr the XPath expression
     *  @param toLowerCase if true, all element names will be considered to
     *         be lower case. Otherwise, they will be upper case.
     *
     *  @throws JaxenException if there is a syntax error while
     *          parsing the expression
     */
    public HTMLXPath(String xpathExpr, boolean toLowerCase) throws JaxenException
    {
        super( xpathExpr, DocumentNavigator.getInstance(toLowerCase) );
    }

    /**
     * Constructs a new XPath, treating all elements as lower case.
     *
     * @param xpathExpr the XPath expression
     * @throws JaxenException if there is a syntax error while
     *          parsing the expression
     */
    public HTMLXPath(String xpathExpr) throws JaxenException
    {
        this( xpathExpr, true);
    }

}
