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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/** Simple default implementation for <code>VariableContext</code>.
 *
 *  <p>
 *  This is a simple table-based key-lookup implementation
 *  for <code>VariableContext</code> which can be programmatically
 *  extended by setting additional variables.
 *  </p>
 *
 *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 */
public class SimpleVariableContext implements VariableContext, Serializable
{
    /** Table of variable bindings. */
    private Map variables;

    /** Construct.
     *
     *  <p>
     *  Construct with an empty variable lookup table.
     *  </p>
     */
    public SimpleVariableContext()
    {
        variables = new HashMap();
    }

    /** Set the value associated with a variable.
     *
     *  <p>
     *  This method sets a variable that is 
     *  associated with any particular namespace.
     *  These variables appear such as <code>$prefix:foo</code>
     *  in an XPath expression.  Prefix to URI resolution
     *  is the responsibility of a <code>NamespaceContext</code>.
     *  Variables within a <code>VariableContext</code> are
     *  referred to purely based upon their namespace URI,
     *  if any.
     *  </p>
     *
     *  @param namespaceURI the namespace URI of the variable
     *  @param localName the local name of the variable
     *  @param value The value to be bound to the variable
     */
    public void setVariableValue( String namespaceURI,
                                  String localName,
                                  Object value )
    {
        this.variables.put( new QualifiedName(namespaceURI, localName),
                            value );
    }

    /** Set the value associated with a variable.
     *
     *  <p>
     *  This method sets a variable that is <strong>not</strong>
     *  associated with any particular namespace.
     *  These variables appear such as <code>$foo</code>
     *  in an XPath expression.
     *  </p>
     *
     *  @param localName the local name of the variable
     *  @param value the value to be bound to the variable
     */
    public void setVariableValue( String localName,
                                  Object value )
    {
        this.variables.put( new QualifiedName(null, localName), value );
    }

    public Object getVariableValue( String namespaceURI,
                                    String prefix,
                                    String localName )
        throws UnresolvableException
    {
        Object key = new QualifiedName( namespaceURI, localName );

        if ( this.variables.containsKey(key) )
        {
            return this.variables.get( key );
        }
        else
        {
            throw new UnresolvableException( "Variable {" + namespaceURI +
                                             "}" + prefix + ":" + localName );
        }
    }
}
