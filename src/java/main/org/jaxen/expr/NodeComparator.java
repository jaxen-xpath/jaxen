/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright (C) 2005 Elliotte Rusty Harold.
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
package org.jaxen.expr;

import java.util.Comparator;
import java.util.Iterator;

import org.jaxen.Navigator;
import org.jaxen.UnsupportedAxisException;


class NodeComparator implements Comparator {
    
    private Navigator navigator;


    NodeComparator(Navigator navigator) {
        this.navigator = navigator;
    }
    
    public int compare(Object o1, Object o2) {
        
        if (navigator == null) return 0;
        
        try {
            int depth1 = getDepth(o1);
            int depth2 = getDepth(o2);
            
            Object a1 = o1;
            Object a2 = o2;
            
            while (depth1 > depth2) {
                a1 = navigator.getParentNode(a1);
                depth1--;
            }
            if (a1 == o2) return 1;
            
            while (depth2 > depth1) {
                a2 = navigator.getParentNode(a2);
                depth2--;
            }
            if (a2 == o1) return -1;
            
            // a1 and a2 are now at same depth; and are not the same
            while (true) {
                Object p1 = navigator.getParentNode(a1);
                Object p2 = navigator.getParentNode(a2);
                if (p1 == p2) {
                    return compareSiblings(p1, a1, a2);
                }
                a1 = p1;
                a2 = p2;
            }
            
            // FIXME this is going to NullPointerException when nodes come from different documents
        }
        catch (UnsupportedAxisException ex) {
            return 0; // ???? should I throw an exception instead?
        }
    }
    

    private int compareSiblings(Object parent, Object sib1, Object sib2) 
      throws UnsupportedAxisException {

        Iterator children = navigator.getChildAxisIterator(parent);
        while (children.hasNext()) {
            Object next = children.next();
            if (next == sib1) return -1;
            else if (next == sib2) return 1;
        }
        return 0;
        
    }

    private int getDepth(Object o) throws UnsupportedAxisException {

        int depth = 0;
        Object parent = o;
        
        while ((parent = navigator.getParentNode(parent)) != null) {
            depth++;
        }
        return depth;
        
    }
    
}
