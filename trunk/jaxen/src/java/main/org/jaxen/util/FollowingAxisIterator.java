package org.jaxen.util;

/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright 2000-2005 bob mcwhirter & James Strachan.
 * All rights reserved.
 *
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 * 
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 * 
 *   * Neither the name of the Jaxen Project nor the names of its
 *     contributors may be used to endorse or promote products derived 
 *     from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.jaxen.Navigator;
import org.jaxen.UnsupportedAxisException;
import org.jaxen.JaxenRuntimeException;
import org.jaxen.JaxenConstants;

/**
 * Represents the XPath <code>following</code> axis. 
 * The "<code>following</code> axis contains all nodes in the same document as the context 
 * node that are after the context node in document order, excluding any descendants 
 * and excluding attribute nodes and namespace nodes."
 * 
 * @version 1.2b12
 */
public class FollowingAxisIterator implements Iterator
{
    private Object contextNode;
    
    private Navigator navigator;

    private Iterator siblings;

    private Iterator currentSibling;

    /**
     * Create a new <code>following</code> axis iterator.
     * 
     * @param contextNode the node to start from
     * @param navigator the object model specific navigator
     */
    public FollowingAxisIterator(Object contextNode,
                                 Navigator navigator) throws UnsupportedAxisException
    {
        this.contextNode = contextNode;
        this.navigator = navigator;
        this.siblings = navigator.getFollowingSiblingAxisIterator(contextNode);
        this.currentSibling = JaxenConstants.EMPTY_ITERATOR;
    }

    private boolean goForward()
    {
        while ( ! siblings.hasNext() )
        {
            if ( !goUp() )
            {
                return false;
            }
        }

        Object nextSibling = siblings.next();

        this.currentSibling = new DescendantOrSelfAxisIterator(nextSibling, navigator);

        return true;
    }

    private boolean goUp()
    {
        if ( contextNode == null
             ||
             navigator.isDocument(contextNode) )
        {
            return false;
        }

        try
        {
            contextNode = navigator.getParentNode( contextNode );

            if ( contextNode != null
                 &&
                 !navigator.isDocument(contextNode) )
            {
                siblings = navigator.getFollowingSiblingAxisIterator(contextNode);
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (UnsupportedAxisException e)
        {
            throw new JaxenRuntimeException(e);
        }
    }

    /**
     * Returns true if there are any following nodes remaining; 
     * false otherwise.
     * 
     * @return true if any following nodes remain
     * 
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext()
    {
        while ( ! currentSibling.hasNext() )
        {
            if ( ! goForward() )
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the next following node.
     * 
     * @return the next following node
     * 
     * @throws NoSuchElementException if no following nodes remain
     * 
     * @see java.util.Iterator#next()
     */
    public Object next() throws NoSuchElementException
    {
        if ( ! hasNext() )
        {
            throw new NoSuchElementException();
        }

        return currentSibling.next();
    }

    /**
     * This operation is not supported.
     * 
     * @throws UnsupportedOperationException always
     */
    public void remove() throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }
}
