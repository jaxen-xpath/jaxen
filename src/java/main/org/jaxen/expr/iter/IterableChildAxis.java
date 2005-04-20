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
package org.jaxen.expr.iter;

import java.util.Iterator;

import org.jaxen.ContextSupport;
import org.jaxen.NamedAccessNavigator;
import org.jaxen.UnsupportedAxisException;

/**
 * Provide access to the child xpath axis.
 * 
 * @author Bob McWhirter
 * @author James Strachan
 * @author Stephen Colebourne
 */
public class IterableChildAxis extends IterableAxis {
    
    /**
     * Constructor.
     * 
     * @param value the axis value
     */
    public IterableChildAxis(int value) {
        super(value);
    }

    /**
     * Gets the iterator for the child xpath axis.
     * 
     * @param contextNode  the current context node to work from
     * @param support  the additional context information
     */
    public Iterator iterator(Object contextNode, ContextSupport support) 
      throws UnsupportedAxisException {
        return support.getNavigator().getChildAxisIterator(contextNode);
    }

    /**
     * Gets the iterator for the child xpath axis that supports named access.
     * 
     * @param contextNode  the current context node to work from
     * @param support  the additional context information
     * @param localName  the local name of the children to return
     * @param namespacePrefix  the prefix of the namespace of the children to return
     * @param namespaceURI  the URI of the namespace of the children to return
     */
    public Iterator namedAccessIterator(
        Object contextNode,
        ContextSupport support,
        String localName,
        String namespacePrefix,
        String namespaceURI)
            throws UnsupportedAxisException {
                
        NamedAccessNavigator nav = (NamedAccessNavigator) support.getNavigator();
        return nav.getChildAxisIterator(contextNode, localName, namespacePrefix, namespaceURI);
    }

    /**
     * Does this axis support named access?
     * 
     * @param support the additional context information
     * @return true if named access supported. If not iterator() will be used
     */
    public boolean supportsNamedAccess(ContextSupport support) {
        return (support.getNavigator() instanceof NamedAccessNavigator);
    }

}
