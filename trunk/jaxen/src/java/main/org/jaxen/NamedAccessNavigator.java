/*
 $Id$

 Copyright 2003 (C) The Werken Company. All Rights Reserved.
 
 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.
 
 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.
 
 3. The name "jaxen" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Werken Company.  For written permission,
    please contact bob@werken.com.
 
 4. Products derived from this Software may not be called "jaxen"
    nor may "jaxen" appear in their names without prior written
    permission of The Werken Company. "jaxen" is a registered
    trademark of The Werken Company.
 
 5. Due credit should be given to The Werken Company.
    (http://jaxen.werken.com/).
 
 THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.

 */
package org.jaxen;

import java.util.Iterator;

/** 
 * Interface for navigating around an arbitrary object model
 * accessing certain parts by name for performance.
 * <p>
 * This interface must only be implemented by those models that
 * can support this named access behavior.
 *
 * @author Stephen Colebourne
 */
public interface NamedAccessNavigator extends Navigator {

    /**
     * Retrieve an <code>Iterator</code> that returns the <code>child</code>
     * XPath axis where the names of the children match the supplied name
     * and optional namespace.
     * <p>
     * This method must only return element nodes with the correct name.
     * <p>
     * If the namespaceURI is null, no namespace should be used.
     * The prefix will never be null.
     *
     * @param contextNode  the origin context node
     * @param localName  the local name of the children to return, always present
     * @param namespacePrefix  the prefix of the namespace of the children to return
     * @param namespaceURI  the namespace URI of the children to return
     *
     * @return an Iterator capable of traversing the named children, or null if none
     *
     * @throws UnsupportedAxisException if the child axis is
     *         not supported by this object model
     */
    Iterator getChildAxisIterator(
        Object contextNode, 
        String localName, String namespacePrefix, String namespaceURI)
            throws UnsupportedAxisException;

    /**
     * Retrieve an <code>Iterator</code> that returns the <code>attribute</code>
     * XPath axis where the names of the attributes match the supplied name
     * and optional namespace.
     * <p>
     * This method must only return attribute nodes with the correct name.
     * <p>
     * If the namespaceURI is null, no namespace should be used.
     * The prefix will never be null.
     *
     * @param contextNode  the origin context node
     * @param localName  the local name of the attributes to return, always present
     * @param namespacePrefix  the prefix of the namespace of the attributes to return
     * @param namespaceURI  the URI of the namespace of the attributes to return
     *
     * @return an Iterator capable of traversing the named attributes, or null if none
     *
     * @throws UnsupportedAxisException if the attribute axis is
     *         not supported by this object model
     */
    Iterator getAttributeAxisIterator(
        Object contextNode, 
        String localName, String namespacePrefix, String namespaceURI)
            throws UnsupportedAxisException;

}
