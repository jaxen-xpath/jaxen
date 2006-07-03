/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright 2000-2002 bob mcwhirter & James Strachan.
 * All rights reserved.
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

////////////////////////////////////////////////////////////////////
// Inner class for a Namespace node.
////////////////////////////////////////////////////////////////////

package org.jaxen.dom;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.jaxen.pattern.Pattern;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;


/**
 * Extension DOM2/DOM3 node type for a namespace node.
 *
 * <p>This class implements the DOM2 and DOM3 {@link Node} interface
 * to allow namespace nodes to be included in the result
 * set of an XPath selectNodes operation, even though DOM does
 * not model namespaces in scope as separate nodes.</p>
 *
 * <p>
 * While all of the DOM2 methods are implemented with reasonable
 * defaults, there will be some unexpected surprises, so users are
 * advised to test for NamespaceNodes and filter them out from the
 * result sets as early as possible.
  * </p>
 *
 * <ol>
 *
 * <li>The {@link #getNodeType} method returns {@link #NAMESPACE_NODE},
 * which is not one of the usual DOM2 node types.  Generic code may
 * fall unexpectedly out of switch statements, for example.</li>
 *
 * <li>The {@link #getOwnerDocument} method returns the owner document
 * of the parent node, but that owner document will know nothing about
 * the namespace node.</p>
 *
 * <li>The {@link #isSupported} method always returns false.</li>
 *
 * <li> The DOM3 methods sometimes throw UnsupportedOperationException.
 *      They're here only to allow this class to be compiled with Java 1.5.
 *       Do not call or rely on them.</li>
 * </ol>
 *
 * <p>All attempts to modify a <code>NamespaceNode</code> will fail with a {@link
 * DOMException} ({@link
 * DOMException#NO_MODIFICATION_ALLOWED_ERR}).</p>
 *
 * @author David Megginson
 * @author Elliotte Rusty Harold
 * @see DocumentNavigator
 */
public class NamespaceNode implements Node
{

    /**
     * Constant: this is a NamespaceNode.
     *
     * @see #getNodeType
     */
    public final static short NAMESPACE_NODE = Pattern.NAMESPACE_NODE;

    // FIXME "Note: Numeric codes up to 200 are reserved to W3C for possible future use."
    // We should be using higher codes. Here we're using 13, the same as DOM 3's type for XPathNamespace.
    // However, that's only a note not a recommendation.

    /**
     * Create a new NamespaceNode.
     *
     * @param parent the DOM node to which the namespace is attached
     * @param name the namespace prefix
     * @param value the namespace URI
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
     * @param parent the DOM node to which the namespace is attached
     * @param attribute the DOM attribute object containing the
     *        namespace declaration
     */
    NamespaceNode (Node parent, Node attribute)
    {
        String attributeName = attribute.getNodeName();
    
        if (attributeName.equals("xmlns")) {
            this.name = "";
        }
        else if (attributeName.startsWith("xmlns:")) {
            this.name = attributeName.substring(6); // the part after "xmlns:"
        }
        else { // workaround for Crimson bug; Crimson incorrectly reports the prefix as the node name
            this.name = attributeName;
        }
        this.parent = parent;
        this.value = attribute.getNodeValue();
    }



    ////////////////////////////////////////////////////////////////////
    // Implementation of org.w3c.dom.Node.
    ////////////////////////////////////////////////////////////////////


    /**
     * Get the namespace prefix.
     *
     * @return the namespace prefix, or "" for the default namespace
     */
    public String getNodeName ()
    {
        return name;
    }


    /**
     * Get the namespace URI.
     *
     * @return the namespace URI
     */
    public String getNodeValue ()
    {
        return value;
    }


    /**
     * Change the namespace URI (always fails).
     *
     * @param value the new URI
     * @throws DOMException always
     */
    public void setNodeValue (String value) throws DOMException
    {
        disallowModification();
    }


    /**
     * Get the node type.
     *
     * @return always {@link #NAMESPACE_NODE}.
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
        if (parent == null) return null;
        return parent.getOwnerDocument();
    }


    /**
     * Insert a new child node (always fails).
     * 
     * @param newChild the node to add
     * @param refChild ignored
     * @return never
     * @throws DOMException always
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
     * @param newChild the node to add
     * @param oldChild the child node to replace
     * @return never
     * @throws DOMException always
     * @see Node#replaceChild
     */
    public Node replaceChild (Node newChild, Node oldChild) throws DOMException
    {
        disallowModification();
        return null;
    }


    /**
     * Remove a child node (always fails).
     *
     * @param oldChild the child node to remove
     * @return never
     * @throws DOMException always
     * @see Node#removeChild
     */
    public Node removeChild(Node oldChild) throws DOMException
    {
        disallowModification();
        return null;
    }


    /**
     * Append a new child node (always fails).
     *
     * @param newChild the node to add
     * @return never
     * @throws DOMException always
     * @see Node#appendChild
     */
    public Node appendChild(Node newChild) throws DOMException
    {
        disallowModification();
        return null;
    }


    /**
     * Test for child nodes.
     *
     * @return false
     */
    public boolean hasChildNodes()
    {
        return false;
    }


    /**
     * Create a copy of this node.
     *
     * @param deep make a deep copy (no effect, since namespace nodes
     *        don't have children).
     * @return a new copy of this namespace node
     */
    public Node cloneNode (boolean deep)
    {
        return new NamespaceNode(parent, name, value);
    }


    /**
     * Normalize the text descendants of this node.
     *
     * <p>This method has no effect, since namespace nodes have no
     * descendants.</p>
     */
    public void normalize ()
    {
    // no op
    }


    /**
     * Test if a DOM2 feature is supported. (None are.)
     *
     * @param feature the feature name
     * @param version the feature version
     * @return false
     */
    public boolean isSupported(String feature, String version)
    {
        return false;
    }


    /**
     * Get the namespace URI of this node.
     *
     * <p>Namespace declarations are not themselves
     * Namespace-qualified.</p>
     *
     * @return null
     */
    public String getNamespaceURI()
    {
       return null;
    }


    /**
     * Get the namespace prefix of this node.
     *
     * <p>Namespace declarations are not themselves
     * namespace-qualified.</p>
     *
     * @return null
     * @see #getLocalName
     */
    public String getPrefix()
    {
        return null;
    }


    /**
     * Change the namespace prefix of this node (always fails).
     *
     * @param prefix the new prefix
     * @throws DOMException always thrown
     */
    public void setPrefix(String prefix)
    throws DOMException
    {
        disallowModification();
    }


    /**
     * Get the XPath name of the namespace node;; i.e. the
     * namespace prefix.
     *
     * @return the namespace prefix
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
     * Generate a hash code for a namespace node.
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
     * @param o the object to test for equality
     * @return true if the object is equivalent to this node, false
     *         otherwise
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
    private static class EmptyNodeList implements NodeList
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

    ////////////////////////////////////////////////////////////////////
    // DOM Level 3 methods
    ////////////////////////////////////////////////////////////////////

    /**
     * Return the base URI of the document containing this node. 
     * This only works in DOM 3.
     *
     * @return null
     */
    public String getBaseURI() {
        Class clazz = Node.class;
        try {
            Class[] args = new Class[0];
            Method getBaseURI = clazz.getMethod("getBaseURI", args);
            String base = (String) getBaseURI.invoke(this.getParentNode(), args);
            return base;
        }
        catch (Exception ex) {
            return null;
        }
    }


    /**
     * Compare relative position of this node to another nbode. (Always fails).
     * This method is included solely for compatibility with the superclass.
     * 
     * @param other the node to compare to
     *
     * @return never
     * @throws DOMException NOT_SUPPORTED_ERR
     */
    public short compareDocumentPosition(Node other) throws DOMException {
        DOMException ex = new DOMException(
          DOMException.NOT_SUPPORTED_ERR,
          "DOM level 3 interfaces are not fully implemented in Jaxen's NamespaceNode class"
        );
        throw ex;
    }


    /**
     * Return the namespace URI.
     *
     * @return the namespace URI
     * @see #getNodeValue
     */
    public String getTextContent() {
        return value;
    }


    /**
     * Change the value of this node (always fails).
     * This method is included solely for compatibility with the superclass.
     *
     * @param textContent the new content
     * @throws DOMException always
     */
    public void setTextContent(String textContent) throws DOMException {
        disallowModification();
    }


    /**
     * Returns true if and only if this object represents the same XPath namespace node
     * as the argument; that is, they have the same parent, the same prefix, and the
     * same URI.
     * 
     * @param other the node to compare to
     * @return true if this object represents the same XPath namespace node
     *     as other; false otherwise
     */
    public boolean isSameNode(Node other) {
        return this.isEqualNode(other) 
          // a bit flaky (should really be this.getParentNode().isEqual(other.getParentNode())
          // but we want this to compile in Java 1.4 without problems
          // XXX could use reflection
          && this.getParentNode() == other.getParentNode();
    }


    /**
     * Return the prefix bound to this namespace URI within the scope
     * of this node (always fails). This method is included solely 
     * for compatibility with the superclass.
     * 
     * @param namespaceURI the URI to find a prefix binding for
     *
     * @return never
     * @throws UnsupportedOperationException always
     */
    public String lookupPrefix(String namespaceURI) {
        // XXX This could be implemented. See
        // http://www.w3.org/TR/2004/REC-DOM-Level-3-Core-20040407/namespaces-algorithms.html#lookupNamespaceURIAlgo
        // It hardly seems worth the effort though.
        throw new UnsupportedOperationException("Changing interfaces in a JDK blows chunks!");
    }


    /**
     * Return true if the specified URI is the default namespace in
     * scope (always fails). This method is included solely for 
     * compatibility with the superclass.
     * 
     * @param namespaceURI the URI to check
     *
     * @return never
     * @throws UnsupportedOperationException always
     */
    public boolean isDefaultNamespace(String namespaceURI) {
        return namespaceURI.equals(this.lookupNamespaceURI(null));
    }


    /**
     * Return the namespace URI mapped to the specified
     * prefix within the scope of this namespace node (always fails).
     * This method is included solely for compatibility with the superclass.
     * 
     * @param prefix the prefix to search for
     *
     * @return never
     * @throws UnsupportedOperationException always
     */
    public String lookupNamespaceURI(String prefix) {
        // XXX This could be implemented. See
        // http://www.w3.org/TR/2004/REC-DOM-Level-3-Core-20040407/namespaces-algorithms.html#lookupNamespaceURIAlgo
        // It hardly seems worth the effort though.
        throw new UnsupportedOperationException("Changing interfaces in a JDK blows chunks!");
    }


    /**
     * Returns true if this object binds the same prefix to the same URI.
     * That is, this object has the same prefix and URI as the argument.
     * 
     * @param arg the node to compare to
     * @return true if this object has the same prefix and URI as the argument; false otherwise
     */
    public boolean isEqualNode(Node arg) {
        if (arg.getNodeType() == this.getNodeType()) {
            NamespaceNode other = (NamespaceNode) arg;
            if (other.name == null && this.name != null) return false;
            else if (other.name != null && this.name == null) return false;
            else if (other.value == null && this.value != null) return false;
            else if (other.value != null && this.value == null) return false;
            else if (other.name == null && this.name == null) {
                return other.value.equals(this.value);
            }

            return other.name.equals(this.name) && other.value.equals(this.value);
        }
        return false;
    }


    /**
     * Returns the value of the requested feature. Always returns null.
     * 
     * @return null
     */
    public Object getFeature(String feature, String version) {
        return null;
    }

    
    // XXX userdata needs testing
    private HashMap userData = new HashMap();

    /**
     * Associates an object with a key. 
     * 
     * @param key the key by which the data will be retrieved
     * @param data the object to store with the key
     * @param handler ignored since namespace nodes cannot be imported, cloned, or renamed
     * 
     * @return the value previously associated with this key; or null
     *     if there isn't any such previous value
     */
    public Object setUserData(String key, Object data, UserDataHandler handler) {
        Object oldValue = getUserData(key);
        userData.put(key, data);
        return oldValue;
    }


    /**
     * Returns the user data associated with the given key. 
     * 
     * @param key the lookup key
     * 
     * @return the object associated with the key; or null if no such object is available
     */
    public Object getUserData(String key) {
        return userData.get(key);
    }
    
}

// end of NamespaceNode.java
