package org.jaxen.util;

/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright (C) 2000-2005 bob mcwhirter & James Strachan.
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

import org.jaxen.JaxenConstants;
import org.jaxen.JaxenRuntimeException;
import org.jaxen.Navigator;
import org.jaxen.UnsupportedAxisException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * This implementation of 'preceding' works like so:
 * the preceding axis includes preceding-siblings of this node and their
 * descendants. Also, for each ancestor node of this node, it includes
 * all preceding-siblings of that ancestor, and their descendants. Finally, it
 * includes the ancestor nodes themselves.
 * <p/>
 * The reversed descendant-or-self axes that are required are calculated using a
 * stack of reversed 'child-or-self' axes. When asked for a node, it is always taken
 * from a child-or-self axis. If it was the last node on that axis, the node is returned.
 * Otherwise, this axis is pushed on the stack, and the process is repeated with the child-or-self
 * of the node. Eventually this recurses down to the last descendant of any node, then works
 * back up to the root.
 * <p/>
 * I reckon most object models could provide a faster implementation of the reversed
 * 'children-or-self' used here.
 */
public class PrecedingAxisIterator implements Iterator
{
    private Iterator ancestorOrSelf;
    private Iterator precedingSibling;
    private ListIterator childrenOrSelf;
    private Stack stack;

    private Navigator navigator;

    public PrecedingAxisIterator(Object contextNode,
                                 Navigator navigator) throws UnsupportedAxisException
    {
        this.navigator = navigator;
        this.ancestorOrSelf = navigator.getAncestorOrSelfAxisIterator(contextNode);
        this.precedingSibling = JaxenConstants.EMPTY_ITERATOR;
        this.childrenOrSelf = JaxenConstants.EMPTY_LIST_ITERATOR;
        this.stack = new Stack();
    }


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
                    childrenOrSelf = (ListIterator) stack.pop();
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
                stack.push(childrenOrSelf);
                childrenOrSelf = childrenOrSelf(result);
                continue;
            }
            return result;
        }
    }


    public void remove() throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }
}
