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


package org.jaxen;

import java.io.PrintStream;
import java.io.PrintWriter;


/**
 * Generic Jaxen exception.
 *
 * <p> This is the root of all Jaxen exceptions. It may wrap other exceptions.
 *
 * @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 */
public class JaxenException extends org.jaxen.saxpath.SAXPathException
{
    /**
     * Construct with a message.
     *
     * @param message The error message.
     */
    public JaxenException( String message )
    {
        super( message );
    }

    /**
     * Construct with a root cause.
     *
     * @param rootCause Root cause of the error.
     */
    public JaxenException( Throwable rootCause )
    {
        super( rootCause );
    }
    
    /**
     * Create a new JaxenException with the specified detail message
     * and root cause.
     * 
     * @param message the detail message
     * @param nestedException the cause of this exception
     */
    public JaxenException(String message, Throwable nestedException) {
        super( message, nestedException );
    }
    
    private Throwable cause;
    private boolean causeSet = false;

    /**
     * Returns the exception that caused this exception.
     * This is necessary to implement Java 1.4 chained exception 
     * functionality in a Java 1.3-compatible way.
     * 
     * @return the exception that caused this exception
     */
    public Throwable getCause() {
        return cause;
    }
    

    /**
     * Sets the exception that caused this exception.
     * This is necessary to implement Java 1.4 chained exception 
     * functionality in a Java 1.3-compatible way.
     * 
     * @param cause the exception wrapped in this runtime exception
     * 
     * @return this exception
     */
    public Throwable initCause(Throwable cause) {
        if (causeSet) throw new IllegalStateException("Cause cannot be reset");
        if (cause == this) throw new IllegalArgumentException("Exception cannot be its own cause");
        causeSet = true;
        this.cause = cause;
        return this;
    }


    // XXX These are not compatible with Java 1.3
    public void printStackTrace( PrintStream s ) {
        super.printStackTrace( s );
        if ( getCause() != null ) 
        {
            s.println( "Root cause:" );
            getCause().printStackTrace( s );
        }
    }
    
    public void printStackTrace( PrintWriter w ) {
        super.printStackTrace( w );
        if ( getCause() != null ) 
        {
            w.println( "Root cause:" );
            getCause().printStackTrace( w );
        }
    }
    
    public void printStackTrace() {
        printStackTrace(System.out);
    }
    
    // XXX Is this compatible with Java 1.3?
    public Throwable fillInStackTrace() {
        if ( getCause() == null ) {
            return super.fillInStackTrace(); 
        } 
        else {
            return getCause().fillInStackTrace();
        }
    }    
    
}

