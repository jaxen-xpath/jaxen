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
 *      Jaxen Project <http://www.jaxen.org/>."
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

package org.jaxen.saxpath;


/**
 * Represents a syntax error in an XPath expression.
 * This is a compile-time error that is detectable irrespective of 
 * the context in which the XPath expression is evaluated.
 */
public class XPathSyntaxException extends SAXPathException
{
    private String xpath;
    private int    position;
    private final static String lineSeparator = System.getProperty("line.separator");

    /**
     * Creates a new XPathSyntaxException.
     * 
     * @param xpath the incorrect XPath expression 
     * @param position the index of the character at which the syntax error was detected
     * @param message the detail message
     */
    public XPathSyntaxException(String xpath,
                                int position,
                                String message)
    {
        super( message );
        this.position = position;
        this.xpath    = xpath;
    }

    /**
     * <p>
     * Returns the index of the character at which the syntax error was detected
     * in the XPath expression.
     * </p>
     * 
     * @return the character index in the XPath expression  
     *     at which the syntax error was detected
     */
    public int getPosition()
    {
        return this.position;
    }

    /**
     * <p>
     * Returns the syntactically incorrect XPath expression.
     * </p>
     * 
     * @return the syntactically incorrect XPath expression
     */
    public String getXPath()
    {
        return this.xpath;
    }

    public String toString()
    {
        return getClass() + ": " + getXPath() + ": " + getPosition() + ": " + getMessage();
    }

    /**
     * <p>
     * Returns a string in the form <code>"   ^"</code> which, when placed on the line
     * below the XPath expression in a monospaced font, should point to the
     * location of the error.
     * </p>
     * 
     * @return the position marker 
     */
    private String getPositionMarker()
    {
        int pos = getPosition();
        StringBuffer buf = new StringBuffer(pos+1);
        for ( int i = 0 ; i < pos ; ++i )
        {
            buf.append(" ");
        }

        buf.append("^");

        return buf.toString();
        
    }

    /**
     * <p>
     * Returns a long formatted description of the error,
     * including line breaks.
     * </p>
     * 
     * @return a longer description of the error on multiple lines
     */
    public String getMultilineMessage()
    {
        StringBuffer buf = new StringBuffer();

        buf.append( getMessage() );
        buf.append( lineSeparator );
        buf.append( getXPath() );
        buf.append( lineSeparator );

        buf.append( getPositionMarker() );

        return buf.toString();
    }

}
