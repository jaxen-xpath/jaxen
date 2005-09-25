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


package org.jaxen;

/** Indicates an error during parsing of an XPath expression.
 *
 *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 *  @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 */
public class XPathSyntaxException extends JaxenException
{
    /** The textual XPath expression */
    private String xpath;

    /** The position of the error */
    private int    position;

    /**
     * Create a new XPathSyntaxException wrapping an existing
     * <code>org.jaxen.saxpath.XPathSyntaxException</code>.
     * 
     * @param e the exception that caused this exception
     */
    public XPathSyntaxException(org.jaxen.saxpath.XPathSyntaxException e)
    {
        super( e );

        this.xpath    = e.getXPath();
        this.position = e.getPosition();
    }

    /** Constructor
     *
     *  @param xpath the erroneous XPath expression
     *  @param position the position of the error
     *  @param message the error message
     */
    public XPathSyntaxException(String xpath,
                                int position,
                                String message)
    {
        super( message );

        this.xpath    = xpath;
        this.position = position;
    }

    /** Retrieve the position of the error.
     *
     *  @return the position of the error
     */
    public int getPosition()
    {
        return this.position;
    }

    /** Retrieve the expression containing the error.
     *
     *  @return the erroneous expression
     */
    public String getXPath()
    {
        return this.xpath;
    }

    /** Retrieve a string useful for denoting where
     *  the error occurred.
     *
     *  <p>
     *  This is a string composed of whitespace and
     *  a marker at the position (see {@link #getPosition})
     *  of the error.  This is useful for creating
     *  friendly multi-line error displays.
     *  </p>
     *
     *  @return the error position marker
     */
    public String getPositionMarker()
    {
        StringBuffer buf = new StringBuffer();

        int pos = getPosition();

        for ( int i = 0 ; i < pos ; ++i )
        {
            buf.append(" ");
        }

        buf.append("^");

        return buf.toString();
        
    }

    /** Retrieve the friendly multi-line error message.
     *
     *  <p>
     *  This returns a multi-line string that contains
     *  the original erroneous XPath expression with a
     *  marker underneath indicating exactly where the
     *  error occurred.
     *  </p>
     *
     *  @return the multi-line error message
     */
    public String getMultilineMessage()
    {
        StringBuffer buf = new StringBuffer(getMessage());
        buf.append( "\n" );
        buf.append( getXPath() );
        buf.append( "\n" );

        buf.append( getPositionMarker() );

        return buf.toString();
    }
}
