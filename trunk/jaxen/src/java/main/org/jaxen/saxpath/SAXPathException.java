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




package org.jaxen.saxpath;

/** Base of all SAXPath exceptions.
 *
 *  @author bob mcwhirter (bob@werken.com)
 */
public class SAXPathException extends Exception
{

    /** The source exception, if any */
    private Throwable cause;

    /** Create a new SAXPathException with a given message.
     *
     *  @param msg the error message
     */
    public SAXPathException(String msg)
    {
        super( msg );
    }

    /** Create a new SAXPathException based on another exception
     *
     *  @param src the error source
     */
    public SAXPathException(Throwable src)
    {
    	super ( src.getMessage() );
    	cause = src;
    }

    /**
     * Create a new SAXPathException with the specified detail message
     * and root cause.
     * 
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public SAXPathException(String message, Throwable cause) {
        super( message );
        this.cause = cause;
    }
    
    /** If this exception was originally caused by another exception,
     *  return it; otherwise, return <code>null</code>.
     * 
     * @return the exception that caused this exception
     */
    public Throwable getCause()
    {
        return cause;
    }

    /** Overridden to print this exception's stack, followed by the
     *	source exception's, if any.
     */
    public void printStackTrace ()
    {
        printStackTrace ( System.err );
    }

    /** Overridden to print this exception's stack, followed by the
     *	source exception's, if any.
     *
     * @param s the stream on whcih to print the stack trace
     */
    public void printStackTrace ( java.io.PrintStream s )
    {
    	super.printStackTrace ( s );
    	if (cause != null) {
    	    s.println ( "root case:" );
    	    cause.printStackTrace ( s );
    	}
    }

    /** Overridden to print this exception's stack, followed by the
     *	source exception's, if any.
     *
     * @param s the writer on whcih to print the stack trace
     */
    public void printStackTrace (java.io.PrintWriter s)
    {
    	super.printStackTrace ( s );
    	if (cause != null) {
    	    s.println ( "root case:" );
    	    cause.printStackTrace ( s );
    	}
    }

}
