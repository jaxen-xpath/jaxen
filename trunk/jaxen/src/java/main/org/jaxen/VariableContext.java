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

/** Resolves variable bindings within an XPath expression.
 *
 *  <p>
 *  Variables within an XPath expression are denoted using
 *  notation such as <code>$varName</code> or 
 *  <code>$nsPrefix:varName</code>, and may
 *  refer to a <code>Boolean</code>, <code>Double</code>, <code>String</code>,
 *  node-set (<code>List</code>) or individual XML node.
 *  </p>
 *
 *  <p>
 *  When a variable is bound to a node-set, the
 *  actual Java object returned should be a <code>java.util.List</code>
 *  containing XML nodes from the object-model (e.g. dom4j, JDOM, DOM, etc.)
 *  being used with the XPath.
 *  </p>
 *
 *  <p>
 *  A variable may validly be assigned the <code>null</code> value,
 *  but an unbound variable (one that this context does not know about)
 *  should cause an {@link UnresolvableException} to be thrown.
 *  </p>
 *
 *  @see SimpleVariableContext
 *  @see NamespaceContext
 *
 *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 *  @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 */
public interface VariableContext
{
    /** An implementation should return the value of an XPath variable
     *  based on the namespace URI and local name of the variable-reference
     *  expression.
     *
     *  <p>
     *  It must not use the prefix parameter to select a variable,
     *  because a prefix could be bound to any namespace; the prefix parameter
     *  could be used in debugging output or other generated information.
     *  The prefix may otherwise be ignored.
     *  </p>
     *
     *  @param namespaceURI  the namespace URI to which the prefix parameter
     *                       is bound in the XPath expression. If the variable
     *                       reference expression had no prefix, the namespace
     *                       URI is <code>null</code>.
     *  @param prefix        the prefix that was used in the variable reference
     *                       expression
     *  @param localName     the local name of the variable-reference
     *                       expression. If there is no prefix, then this is
     *                       the whole name of the variable.
     *
     *  @return  the variable's value (which can be <code>null</code>)
     *  @throws UnresolvableException  when the variable cannot be resolved
     */
    public Object getVariableValue( String namespaceURI,
                                    String prefix,
                                    String localName )
        throws UnresolvableException;
}
