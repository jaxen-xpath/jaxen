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

////////////////////////////////////////////////////////////////////
// Inner class for a Namespace node.
////////////////////////////////////////////////////////////////////

package org.jaxen.dom;

import org.jaxen.pattern.Pattern;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Extension DOM2 node type for a Namespace Declaration.
 *
 * <p>This class implements the DOM2 {@link Node} interface to
 * allow Namespace declarations to be included in the result
 * set of an XPath selectNodes operation, even though DOM2 does
 * not model Namespace declarations as separate nodes.</p>
 *
 * <p>While all of the methods are implemented with reasonable
 * defaults, there will be some unexpected surprises, so users are
 * advised to test for NamespaceNodes and filter them out from the
 * result sets as early as possible:</p>
 *
 * <ol>
 *
 * <li>The {@link #getNodeType} method returns {@link #NAMESPACE_NODE},
 * which is not one of the usual DOM2 node types.  Generic code may
 * fall unexpectedly out of switch statements, for example.</li>
 *
 * <li>The {@link #getOwnerDocument} method returns the owner document
 * of the parent node, but that owner document will know nothing about
 * the Namespace node.</p>
 *
 * <li>The {@link #isSupported} method always returns false.</li>
 *
 * </ol>
 *
 * <p>All attempts to modify a NamespaceNode will fail with a {@link
 * DOMException} ({@link
 * DOMException#NO_MODIFICATION_ALLOWED_ERR}).</p>
 *
 * <p>This class has only protected constructors, so that it can be
 * instantiated only by {@link DocumentNavigator}.</p>
 *
 * @author David Megginson
 * @see DocumentNavigator
 */
public class NamespaceNode implements Node
{


    ////////////////////////////////////////////////////////////////////
    // Constants.
    ////////////////////////////////////////////////////////////////////

    /**
     * Constant: this is a NamespaceNode.
     *
     * @see #getNodeType
     */
    public final static short NAMESPACE_NODE = Pattern.NAMESPACE_NODE;



    ////////////////////////////////////////////////////////////////////
    // Protected Constructors.
    ////////////////////////////////////////////////////////////////////


    /**
     * Constructor.
     *
     * @param parent the DOM node to which the Namespace is attached
     * @param name the namespace prefix
     * @param value the Namespace URI
     */
    public NamespaceNode (Node parent, String name, String value)
    {
        this.parent = parent;
        this.name = name;
        this.value = value;
    }


    /**
     * Constructor.
     *
     * @param parent the DOM node to which the Namespace is attached
     * @param attribute the DOM attribute object containing the
     *        namespace declaration
     */
    NamespaceNode (Node parent, Node attribute)
    {
        String name = attribute.getNodeName();
    
        if (name.equals("xmlns")) {
            this.name = "";
        }
        else {
            this.name = name.substring(6); // the part after "xmlns:"
        }
        this.parent = parent;
        this.value = attribute.getNodeValue();
    }



    ////////////////////////////////////////////////////////////////////
    // Implementation of org.w3c.dom.Node.
    ////////////////////////////////////////////////////////////////////


    /**
     * Get the Namespace prefix.
     *
     * @return the Namespace prefix, or "" for the default Namespace.
     */
    public String getNodeName ()
    {
        return name;
    }


    /**
     * Get the Namespace URI.
     *
     * @return the Namespace URI
     */
    public String getNodeValue ()
    {
        return value;
    }


    /**
     * Change the Namespace URI (always fails).
     *
     * @param value the new URI
     * @throws DOMException always thrown
     */
    public void setNodeValue (String value) throws DOMException
    {
        disallowModification();
    }


    /**
     * Get the node type.
     *
     * @return Always {@link #NAMESPACE_NODE}.
     */
    public short getNodeType ()
    {
        return NAMESPACE_NODE;
    }


    /**
     * Get the parent node.
     *
     * <p>This method returns the element that was queried for Namespaces
     * in effect, <em>not</em> necessarily the actual element containing
     * the Namespace declaration.</p>
     *
     * @return the parent node (not null)
     */
    public Node getParentNode ()
    {
        return parent;
    }


    /**
     * Get the list of child nodes.
     *
     * @return an empty node list
     */
    public NodeList getChildNodes ()
    {
        return new EmptyNodeList();
    }


    /**
     * Get the first child node.
     *
     * @return null
     */
    public Node getFirstChild ()
    {
        return null;
    }


    /**
     * Get the last child node.
     *
     * @return null
     */
    public Node getLastChild ()
    {
        return null;
    }


    /**
     * Get the previous sibling node.
     *
     * @return null
     */
    public Node getPreviousSibling ()
    {
        return null;
    }


    /**
     * Get the next sibling node.
     *
     * @return null
     */
    public Node getNextSibling ()
    {
        return null;
    }


    /**
     * Get the attribute nodes.
     *
     * @return null
     */
    public NamedNodeMap getAttributes ()
    {
        return null;
    }


    /**
     * Get the owner document.
     *
     * @return the owner document <em>of the parent node</em>
     */
    public Document getOwnerDocument ()
    {
                    // FIXME: this could cause confusion
        return (parent == null ? null : parent.getOwnerDocument());
    }


    /**
     * Insert a new child node (always fails).
     *
     * @throws DOMException always thrown
     * @see Node#insertBefore
     */
    public Node insertBefore (Node newChild, Node refChild)
    throws DOMException
    {
        disallowModification();
        return null;
    }


    /**
     * Replace a child node (always fails).
     *
     * @throws DOMException always thrown
     * @see Node#replaceChild
     */
    public Node replaceChild (Node newChild, Node oldChild)
    throws DOMException
    {
        disallowModification();
        return null;
    }


    /**
     * Remove a child node (always fails).
     *
     * @throws DOMException always thrown
     * @see Node#removeChild
     */
    public Node removeChild (Node oldChild)
    throws DOMException
    {
        disallowModification();
        return null;
    }


    /**
     * Append a new child node (always fails).
     *
     * @throws DOMException always thrown
     * @see Node#appendChild
     */
    public Node appendChild (Node newChild)
    throws DOMException
    {
        disallowModification();
        return null;
    }


    /**
     * Test for child nodes.
     *
     * @return false
     */
    public boolean hasChildNodes ()
    {
        return false;
    }


    /**
     * Create a copy of this node.
     *
     * @param deep Make a deep copy (no effect, since Namespace nodes
     *        don't have children).
     * @return a new copy of this Namespace node
     */
    public Node cloneNode (boolean deep)
    {
        return new NamespaceNode(parent, name, value);
    }


    /**
     * Normalize the text descendants of this node.
     *
     * <p>This method has no effect, since Namespace nodes have no
     * descendants.</p>
     */
    public void normalize ()
    {
    // no op
    }


    /**
     * Test if a DOM2 feature is supported.
     *
     * @param feature the feature name
     * @param version the feature version
     * @return false
     */
    public boolean isSupported (String feature, String version)
    {
        return false;
    }


    /**
     * Get the Namespace URI for this node.
     *
     * <p>Namespace declarations are not themselves
     * Namespace-qualified.</p>
     *
     * @return null
     */
    public String getNamespaceURI ()
    {
       return null;
    }


    /**
     * Get the Namespace prefix for this node.
     *
     * <p>Namespace declarations are not themselves
     * Namespace-qualified.</p>
     *
     * @return null
     */
    public String getPrefix ()
    {
        return null;
    }


    /**
     * Change the Namespace prefix for this node (always fails).
     *
     * @param prefix the new prefix
     * @throws DOMException always thrown
     */
    public void setPrefix (String prefix)
    throws DOMException
    {
        disallowModification();
    }


    /**
     * Get the local name for this node.
     *
     * @return null
     */
    public String getLocalName ()
    {
        return name;
    }


    /**
     * Test if this node has attributes.
     *
     * @return false
     */
    public boolean hasAttributes ()
    {
        return false;
    }


    /**
     * Throw a NO_MODIFICATION_ALLOWED_ERR DOMException.
     *
     * @throws DOMException always thrown
     */
    private void disallowModification () throws DOMException
    {
        throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
                   "Namespace node may not be modified");
    }



    ////////////////////////////////////////////////////////////////////
    // Override default methods from java.lang.Object.
    ////////////////////////////////////////////////////////////////////


    /**
     * Generate a hash code for a Namespace node.
     *
     * <p>The hash code is the sum of the hash codes of the parent node,
     * name, and value.</p>
     *
     * @return a hash code for this node
     */
    public int hashCode ()
    {
    return hashCode(parent) + hashCode(name) + hashCode(value);
    }


    /**
     * Test for equivalence with another object.
     *
     * <p>Two Namespace nodes are considered equivalent if their parents,
     * names, and values are equal.</p>
     *
     * @param o The object to test for equality.
     * @return true if the object is equivalent to this node, false
     *         otherwise.
     */
    public boolean equals (Object o)
    {
        if (o == this) return true;
        else if (o == null) return false;
        else if (o instanceof NamespaceNode) {
            NamespaceNode ns = (NamespaceNode)o;
            return (equals(parent, ns.getParentNode()) &&
                equals(name, ns.getNodeName()) &&
                equals(value, ns.getNodeValue()));
        } else {
            return false;
        }
    }


    /**
     * Helper method for generating a hash code.
     *
     * @param o the object for generating a hash code (possibly null)
     * @return the object's hash code, or 0 if the object is null
     * @see java.lang.Object#hashCode
     */
    private int hashCode (Object o)
    {
    return (o == null ? 0 : o.hashCode());
    }


    /**
     * Helper method for comparing two objects.
     *
     * @param a the first object to compare (possibly null)
     * @param b the second object to compare (possibly null)
     * @return true if the objects are equivalent or are both null
     * @see java.lang.Object#equals
     */
    private boolean equals (Object a, Object b)
    {
        return ((a == null && b == null) ||
          (a != null && a.equals(b)));
    }


    ////////////////////////////////////////////////////////////////////
    // Internal state.
    ////////////////////////////////////////////////////////////////////

    private Node parent;
    private String name;
    private String value;



    ////////////////////////////////////////////////////////////////////
    // Inner class: empty node list.
    ////////////////////////////////////////////////////////////////////


    /**
     * A node list with no members.
     *
     * <p>This class is necessary for the {@link Node#getChildNodes}
     * method, which must return an empty node list rather than
     * null when there are no children.</p>
     */
    class EmptyNodeList implements NodeList
    {

    /**
     * @see NodeList#getLength
     */
    public int getLength ()
    {
        return 0;
    }


    /**
     * @see NodeList#item
     */
    public Node item(int index)
    {
        return null;
    }
    
    }
}

// end of Namespace.java
