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

/** Implemented by classes that know how to resolve xpath function names and
 *  namespaces to implementations of these functions.
 *
 *  <p>
 *  By using a custom <code>FunctionContext</code>, new or different
 *  functions may be installed and available to XPath expression writers.
 *  </p>
 *
 *  @see XPathFunctionContext
 *
 *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 */
public interface FunctionContext
{
    /** An implementation should return a <code>Function</code> implementation object
     *  based on the namespace uri and local name of the function-call
     *  expression.
     *
     *  <p>
     *  It must not use the prefix parameter to select an implementation,
     *  because a prefix could be bound to any namespace; the prefix parameter
     *  could be used in debugging output or other generated information.
     *  The prefix may otherwise be completely ignored.
     *  </p>
     *
     *  @param namespaceURI  the namespace uri to which the prefix parameter
     *                       is bound in the xpath expression. If the function
     *                       call expression had no prefix, the namespace uri
     *                       is <code>null</code>.
     *  @param prefix        the prefix that was used in the function call
     *                       expression.
     *  @param localName     the local name of the function-call expression;
     *                       if there is no prefix, then this is the whole
     *                       name of the function.
     *
     *  @return  a Function implementation object.
     *  @throws UnresolvableException  when the function cannot be resolved.
     */
    Function getFunction( String namespaceURI,
                          String prefix,
                          String localName ) throws UnresolvableException;
}
