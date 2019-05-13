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

import org.jaxen.JaxenConstants;
import org.jaxen.JaxenRuntimeException;
import org.jaxen.Navigator;
import org.jaxen.UnsupportedAxisException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * <p>
 * Represents the XPath <code>preceding</code> axis. 
 * The "<code>preceding</code> axis contains all nodes in the same document as the context 
 * node that are before the context node in document order, excluding any ancestors 
 * and excluding attribute nodes and namespace nodes."
 * 
 * <p>
 * This implementation of '<code>preceding</code>' works like so:
 * the <code>preceding</code> axis includes preceding siblings of this node and 
 * their descendants. Also, for each ancestor node of this node, it includes
 * all preceding siblings of that ancestor, and their descendants. Finally, it
 * includes the ancestor nodes themselves.
 * </p>
 * 
 * <p>
 * The reversed <code>descendant-or-self</code> axes that are required are calculated using a
 * stack of reversed 'child-or-self' axes. When asked for a node, it is always taken
 * from a child-or-self axis. If it was the last node on that axis, the node is returned.
 * Otherwise, this axis is pushed on the stack, and the process is repeated with the child-or-self
 * of the node. Eventually this recurses down to the last descendant of any node, then works
 * back up to the root.
 * </p>
 * 
 * <p>
 * Most object models could provide a faster implementation of the reversed
 * 'children-or-self' used here.</p>
 * 
 * @version 1.2b12
 */
public class PrecedingAxisIterator implements Iterator
{
    private Iterator ancestorOrSelf;
    private Iterator precedingSibling;
    private ListIterator childrenOrSelf;
    private ArrayList stack;

    private Navigator navigator;

    /**
     * Create a new <code>preceding</code> axis iterator.
     * 
     * @param contextNode the node to start from
     * @param navigator the object model specific navigator
     */
    public PrecedingAxisIterator(Object contextNode,
                                 Navigator navigator) throws UnsupportedAxisException
    {
        this.navigator = navigator;
        this.ancestorOrSelf = navigator.getAncestorOrSelfAxisIterator(contextNode);
        this.precedingSibling = JaxenConstants.EMPTY_ITERATOR;
        this.childrenOrSelf = JaxenConstants.EMPTY_LIST_ITERATOR;
        this.stack = new ArrayList();
    }


    /**
     * Returns true if there are any preceding nodes remaining; false otherwise.
     * 
     * @return true if any preceding nodes remain; false otherwise
     * 
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext()
    {
        try
        {
            while (!childrenOrSelf.hasPrevious())
            {
                if (stack.isEmpty())
                {
                    while (!precedingSibling.hasNext())
                    {
                        if (!ancestorOrSelf.hasNext())
                        {
                            return false;
                        }
                        Object contextNode = ancestorOrSelf.next();
                        precedingSibling = new PrecedingSiblingAxisIterator(contextNode, navigator);
                    }
                    Object node = precedingSibling.next();
                    childrenOrSelf = childrenOrSelf(node);
                }
                else
                {
                    childrenOrSelf = (ListIterator) stack.remove(stack.size()-1);
                }
            }
            return true;
        }
        catch (UnsupportedAxisException e)
        {
            throw new JaxenRuntimeException(e);
        }
    }

    private ListIterator childrenOrSelf(Object node)
    {
        try
        {
            ArrayList reversed = new ArrayList();
            reversed.add(node);
            Iterator childAxisIterator = navigator.getChildAxisIterator(node);
            if (childAxisIterator != null)
            {
                while (childAxisIterator.hasNext())
                {
                    reversed.add(childAxisIterator.next());
                }
            }
            return reversed.listIterator(reversed.size());
        }
        catch (UnsupportedAxisException e)
        {
            throw new JaxenRuntimeException(e);
        }
    }

    /**
     * Returns the next preceding node.
     * 
     * @return the next preceding node
     * 
     * @throws NoSuchElementException if no preceding nodes remain
     * 
     * @see java.util.Iterator#next()
     */
    public Object next() throws NoSuchElementException
    {
        if (!hasNext())
        {
            throw new NoSuchElementException();
        }
        while (true)
        {
            Object result = childrenOrSelf.previous();
            if (childrenOrSelf.hasPrevious())
            {
                // if this isn't 'self' construct 'descendant-or-self'
                stack.add(childrenOrSelf);
                childrenOrSelf = childrenOrSelf(result);
                continue;
            }
            return result;
        }
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
