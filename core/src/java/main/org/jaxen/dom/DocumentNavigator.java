package org.jaxen.dom;

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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.jaxen.DefaultNavigator;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;
import org.jaxen.XPath;
import org.jaxen.JaxenConstants;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.xml.sax.SAXException;

/** Interface for navigating around the W3C DOM Level 2 object model.
 *
 *  <p>
 *  This class is not intended for direct usage, but is
 *  used by the Jaxen engine during evaluation.
 *  </p>
 *
 *  <p>This class implements the {@link org.jaxen.DefaultNavigator} interface
 *  for the Jaxen XPath library.  This adapter allows the Jaxen
 *  library to be used to execute XPath queries against any object tree
 *  that implements the DOM level 2 interfaces.</p>
 *
 *  <p>Note: DOM level 2 does not include a node representing an XPath
 *  namespace node.  This navigator will return namespace nodes
 *  as instances of the custom {@link NamespaceNode} class, and
 *  users will have to check result sets to locate and isolate
 *  these.</p>
 *
 *  @author David Megginson
 *  @author James Strachan
 *
 *  @see XPath
 *  @see NamespaceNode
 */
public class DocumentNavigator extends DefaultNavigator
{

    
    ////////////////////////////////////////////////////////////////////
    // Constants.
    ////////////////////////////////////////////////////////////////////

    /**
     * 
     */
    private static final long serialVersionUID = 8460943068889528115L; 
    
    private final static DocumentNavigator SINGLETON = new DocumentNavigator();


    
    ////////////////////////////////////////////////////////////////////
    // Constructor.
    ////////////////////////////////////////////////////////////////////


    /**
     * Default constructor.
     */
    public DocumentNavigator ()
    {
    }


    /**
     * Get a constant DocumentNavigator for efficiency.
     *
     * @return a constant instance of a DocumentNavigator.
     */
    public static Navigator getInstance ()
    {
        return SINGLETON;
    }


    
    ////////////////////////////////////////////////////////////////////
    // Implementation of org.jaxen.DefaultNavigator.
    ////////////////////////////////////////////////////////////////////


    /**
     * Get an iterator over all of this node's children.
     *
     * @param contextNode the context node for the child axis.
     * @return a possibly-empty iterator (not null)
     */
    public Iterator getChildAxisIterator (Object contextNode)
    {
        Node node = (Node) contextNode;

        if ( node.getNodeType() == Node.ELEMENT_NODE || node.getNodeType() == Node.DOCUMENT_NODE)
        {
            return new NodeIterator ((Node)contextNode) {
                protected Node getFirstNode (Node node)
                {
                    return node.getFirstChild();
                }
                protected Node getNextNode (Node node)
                {
                    return node.getNextSibling();
                }
            };
        }

        return JaxenConstants.EMPTY_ITERATOR;

    }


    /**
     * Get a (single-member) iterator over this node's parent.
     *
     * @param contextNode the context node for the parent axis
     * @return a possibly-empty iterator (not null)
     */
    public Iterator getParentAxisIterator (Object contextNode)
    {
        Node node = (Node)contextNode;

        if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
            return new NodeIterator (node) {
                    protected Node getFirstNode (Node n)
                    {
                        // We can assume castability here because we've already
                        // tested the node type.
                        return ((Attr)n).getOwnerElement();
                    }
                    protected Node getNextNode (Node n) {
                        return null;
                    }
                };
        } else {
            return new NodeIterator (node) {
                    protected Node getFirstNode (Node n)
                    {
                        return n.getParentNode();
                    }
                    protected Node getNextNode (Node n) {
                        return null;
                    }
                };
        }
    }
    
    
    /** 
     * Return the XPath parent of the supplied DOM node.
     * XPath has slightly different definition of parent than DOM does.
     * In particular, the parent of an attribute is not null.
     * 
     * @param child the child node
     * 
     * @return the parent of the specified node; or null if
     *     the node does not have a parent
     */
    public Object getParentNode(Object child) {
        Node node = (Node) child;
        if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
            return ((Attr) node).getOwnerElement();
        }
        return node.getParentNode();
    }


    /**
     * Get an iterator over all following siblings.
     *
     * @param contextNode the context node for the sibling iterator
     * @return a possibly-empty iterator (not null)
     */
    public Iterator getFollowingSiblingAxisIterator (Object contextNode)
    {
        return new NodeIterator ((Node)contextNode) {
                protected Node getFirstNode (Node node)
                {
                    return getNextNode(node);
                }
                protected Node getNextNode (Node node) {
                    return node.getNextSibling();
                }
            };
    }


    /**
     * Get an iterator over all preceding siblings.
     *
     * @param contextNode the context node for the preceding sibling axis
     * @return a possibly-empty iterator (not null)
     */
    public Iterator getPrecedingSiblingAxisIterator (Object contextNode)
    {
        return new NodeIterator ((Node)contextNode) {
                protected Node getFirstNode (Node node)
                {
                    return getNextNode(node);
                }
                protected Node getNextNode (Node node) {
                    return node.getPreviousSibling();
                }
            };
    }


    /**
     * Get an iterator over all following nodes, depth-first.
     *
     * @param contextNode the context node for the following axis
     * @return a possibly-empty iterator (not null)
     */
    public Iterator getFollowingAxisIterator (Object contextNode)
    {
        return new NodeIterator ((Node)contextNode) {
                protected Node getFirstNode (Node node)
                {
                    if (node == null) {
                        return null;
                    }
                    else {
                        Node sibling = node.getNextSibling();
                        if (sibling == null) {
                            return getFirstNode(node.getParentNode());
                        }
                        else {
                            return sibling;
                        }
                    }
                }
                protected Node getNextNode (Node node) {
                    if (node == null) {
                        return null;
                    }
                    else {
                        Node n = node.getFirstChild();
                        if (n == null) n = node.getNextSibling();
                        if (n == null) return getFirstNode(node.getParentNode());
                        else return n;
                    }
                }
            };
    }


    /**
     * Get an iterator over all attributes.
     *
     * @param contextNode the context node for the attribute axis
     * @return a possibly-empty iterator (not null)
     */
    public Iterator getAttributeAxisIterator (Object contextNode)
    {
        if (isElement(contextNode)) {
            return new AttributeIterator((Node)contextNode);
        } 
        else {
            return JaxenConstants.EMPTY_ITERATOR;
        }
    }


    /**
     * Get an iterator over all declared namespaces.
     *
     * <p>Note: this iterator is not live: it takes a snapshot
     * and that snapshot remains static during the life of
     * the iterator (i.e. it won't reflect subsequent changes
     * to the DOM).</p>
     * 
     * <p>
     * In the event that the DOM is inconsistent; for instance a 
     * <code>pre:foo</code> element is declared by DOM to be in the 
     * http://www.a.com/ namespace but also has an 
     * <code>xmlns:pre="http://www.b.com"</code> attribute; then only 
     * one of the namespaces will be counted. This will be the intrinsic
     * namespace of the <code>Element</code> or <code>Attr</code> object
     * rather than the one provide by the contradictory namespace 
     * declaration attribute. In the event of a contradiction between two
     * attributes on the same element--e.g. <code>pre:foo</code> in the
     * http://www.a.com/ namespace and <code>pre:bar</code> in the 
     * http://www.b.com/ namespace--it is undefined which namespace
     * will be returned. 
     * </p>
     *
     * @param contextNode the context node for the namespace axis
     * @return a possibly-empty iterator (not null)
     */
    public Iterator getNamespaceAxisIterator (Object contextNode)
    {
        // Only elements have namespace nodes
        if (isElement(contextNode)) {

            HashMap nsMap = new HashMap();

            // Starting at the current node, walk
            // up to the root, noting the namespace
            // declarations in scope.
            for (Node n = (Node) contextNode;
                 n != null;
                 n = n.getParentNode()) {
                
                // 1. Look for the namespace of the element itself
                String myNamespace = n.getNamespaceURI();
                if (myNamespace != null && ! "".equals(myNamespace)) {
                    String myPrefix = n.getPrefix();
                    if (!nsMap.containsKey(myPrefix)) {
                        NamespaceNode ns = new NamespaceNode((Node) contextNode, myPrefix, myNamespace);
                        nsMap.put(myPrefix, ns);
                    }
                }

                if (n.hasAttributes()) {
                    NamedNodeMap atts = n.getAttributes();
                    int length = atts.getLength();
                    // 2. Look for namespaces of attributes
                    for (int i = 0; i < length; i++) {
                        Attr att = (Attr) atts.item(i);
                        // Work around Crimson bug by testing URI rather than name
                        String attributeNamespace = att.getNamespaceURI();
                        if ("http://www.w3.org/2000/xmlns/".equals(attributeNamespace)) {
                        }
                        else if (attributeNamespace != null) {
                            String prefix = att.getPrefix();
                            NamespaceNode ns =
                                new NamespaceNode((Node)contextNode, prefix, attributeNamespace);
                            // Add only if there's not a closer declaration in force.
                            if (!nsMap.containsKey(prefix)) nsMap.put(prefix, ns);
                            
                        }
                    }
                    
                    // 3. Look for namespace declaration attributes
                    for (int i = 0; i < length; i++) {
                        Attr att = (Attr) atts.item(i);
                        // work around crimson bug by testing URI rather than name
                        String attributeNamespace = att.getNamespaceURI();
                        if ("http://www.w3.org/2000/xmlns/".equals(attributeNamespace)) {
                            NamespaceNode ns =
                              new NamespaceNode( (Node)contextNode, att);
                            // Add only if there's not a closer declaration in force.
                            String name = ns.getNodeName();
                            if (!nsMap.containsKey(name)) nsMap.put(name, ns);
                        }
                    }
                    
                }
                
            }
            // Section 5.4 of the XPath rec requires
            // this to be present.
            nsMap.put("xml",
                      new
                      NamespaceNode((Node)contextNode,
                                    "xml",
                                    "http://www.w3.org/XML/1998/namespace"));

            // An empty default namespace cancels
            // any previous default.
            NamespaceNode defaultNS = (NamespaceNode)nsMap.get("");
            if (defaultNS != null && defaultNS.getNodeValue().length() == 0) {
                nsMap.remove("");
            }
            return nsMap.values().iterator();
        } 
        else {
            return JaxenConstants.EMPTY_ITERATOR;
        }
    }

    /** Returns a parsed form of the given XPath string, which will be suitable
     *  for queries on DOM documents.
     *  
     * @param xpath the XPath expression
     * @return a parsed form of the given XPath string
     * @throws org.jaxen.saxpath.SAXPathException if the string is syntactically incorrect
     */
    public XPath parseXPath (String xpath) throws org.jaxen.saxpath.SAXPathException
    {
        return new DOMXPath(xpath);
    }

    /**
     * Get the top-level document node.
     *
     * @param contextNode any node in the document
     * @return the root node
     */
    public Object getDocumentNode (Object contextNode)
    {
        if (isDocument(contextNode)) return contextNode;
        else return ((Node)contextNode).getOwnerDocument();
    }

    // Why are there separate methods for getElementNamespaceURI and 
    // getAttributeNamespaceURI when they do exactly the same thing?
    // This should be combined in a future version.
    /**
     * Get the namespace URI of an element.
     *
     * @param element the target node
     * @return a string (possibly empty) if the node is an element,
     * and null otherwise
     */
    public String getElementNamespaceUri (Object element)
    {
        try {
            Node node = (Node) element;
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                return node.getNamespaceURI();
            }
        }
        catch (ClassCastException ex) {
        }
        return null;
    }


    /**
     * Get the local name of an element.
     *
     * @param element the target node
     * @return a string representing the unqualified local name
     *     if the node is an element, or null otherwise
     */
    public String getElementName (Object element)
    {
        if (isElement(element)) {
            String name = ((Node)element).getLocalName();
            if (name == null) name = ((Node)element).getNodeName();
            return name;
        }
        return null;
    }


    /**
     * Get the qualified name of an element.
     *
     * @param element the target node
     * @return a string representing the qualified (i.e. possibly
     *   prefixed) name if the argument is an element, or null otherwise
     */
    public String getElementQName (Object element)
    {
        try {
            Node node = (Node) element;
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                return node.getNodeName();
            }
        }
        catch (ClassCastException ex) {
        }
        return null;
    }


    /**
     * Get the namespace URI of an attribute.
     *
     * @param attribute the target node
     * 
     * @return the namespace name of the specified node
     * 
     */
    public String getAttributeNamespaceUri (Object attribute)
    {
        try {
            Node node = (Node) attribute;
            if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
                return node.getNamespaceURI();
            }
        }
        catch (ClassCastException ex) {
        }
        return null;
    }


    /**
     * Get the local name of an attribute.
     *
     * @param attribute the target node
     * @return a string representing the unqualified local name
     * if the node is an attribute, or null otherwise
     */
    public String getAttributeName (Object attribute)
    {
        if (isAttribute(attribute)) {
            String name = ((Node)attribute).getLocalName();
            if (name == null) name = ((Node)attribute).getNodeName();
            return name;
        }
        return null;
    }


    /**
     * Get the qualified name of an attribute.
     *
     * @param attribute the target node
     * 
     * @return a string representing the qualified (i.e. possibly
     * prefixed) name if the argument is an attribute, or null otherwise
     */
    public String getAttributeQName (Object attribute)
    {
        try {
            Node node = (Node) attribute;
            if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
                return node.getNodeName();
            }
        }
        catch (ClassCastException ex) {
        }
        return null;
    }


    /**
     * Test if a node is a top-level document.
     *
     * @param object the target node
     * @return true if the node is the document root, false otherwise
     */
    public boolean isDocument (Object object)
    {
        return (object instanceof Node) &&
            (((Node)object).getNodeType() == Node.DOCUMENT_NODE);
    }


    /**
     * Test if a node is a namespace.
     *
     * @param object the target node
     * @return true if the node is a namespace, false otherwise
     */
    public boolean isNamespace (Object object)
    {
        return (object instanceof NamespaceNode);
    }


    /**
     * Test if a node is an element.
     *
     * @param object the target node
     * @return true if the node is an element, false otherwise
     */
    public boolean isElement (Object object)
    {
        return (object instanceof Node) &&
            (((Node)object).getNodeType() == Node.ELEMENT_NODE);
    }


    /**
     * Test if a node is an attribute. <code>xmlns</code> and 
     * <code>xmlns:pre</code> attributes do not count as attributes
     * for the purposes of XPath. 
     *
     * @param object the target node
     * @return true if the node is an attribute, false otherwise
     */
    public boolean isAttribute (Object object)
    {
        return (object instanceof Node) &&
            (((Node)object).getNodeType() == Node.ATTRIBUTE_NODE)
            && ! "http://www.w3.org/2000/xmlns/".equals(((Node) object).getNamespaceURI());
    }


    /**
     * Test if a node is a comment.
     *
     * @param object the target node
     * @return true if the node is a comment, false otherwise
     */
    public boolean isComment (Object object)
    {
        return (object instanceof Node) &&
            (((Node)object).getNodeType() == Node.COMMENT_NODE);
    }


    /**
     * Test if a node is plain text.
     *
     * @param object the target node
     * @return true if the node is a text node, false otherwise
     */
    public boolean isText (Object object)
    {
        if (object instanceof Node) {
            switch (((Node)object).getNodeType()) {
                case Node.TEXT_NODE:
                case Node.CDATA_SECTION_NODE:
                    return true;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }


    /**
     * Test if a node is a processing instruction.
     *
     * @param object the target node
     * @return true if the node is a processing instruction, false otherwise
     */
    public boolean isProcessingInstruction (Object object)
    {
        return (object instanceof Node) &&
            (((Node)object).getNodeType() == Node.PROCESSING_INSTRUCTION_NODE);
    }


    /**
     * Get the string value of an element node.
     *
     * @param object the target node
     * @return the text inside the node and its descendants if the node
     * is an element, null otherwise
     */
    public String getElementStringValue (Object object)
    {
        if (isElement(object)) {
            return getStringValue((Node)object, new StringBuffer()).toString();
        }
        else {
            return null;
        }
    }


    /**
     * Construct a node's string value recursively.
     *
     * @param node the current node
     * @param buffer the buffer for building the text
     * @return the buffer passed as a parameter (for convenience)
     */
    private StringBuffer getStringValue (Node node, StringBuffer buffer)
    {
        if (isText(node)) {
            buffer.append(node.getNodeValue());
        } else {
            NodeList children = node.getChildNodes();
            int length = children.getLength();
            for (int i = 0; i < length; i++) {
                getStringValue(children.item(i), buffer);
            }
        }
        return buffer;
    }


    /**
     * Get the string value of an attribute node.
     *
     * @param object the target node
     * @return the text of the attribute value if the node is an
     *     attribute, null otherwise
     */
    public String getAttributeStringValue (Object object)
    {
        if (isAttribute(object)) return ((Node)object).getNodeValue();
        else return null;
    }


    /**
     * Get the string value of text.
     *
     * @param object the target node
     * @return the string of text if the node is text, null otherwise
     */
    public String getTextStringValue (Object object)
    {
        if (isText(object)) return ((Node)object).getNodeValue();
        else return null;
    }


    /**
     * Get the string value of a comment node.
     *
     * @param object the target node
     * @return the text of the comment if the node is a comment, null otherwise
     */
    public String getCommentStringValue (Object object)
    {
        if (isComment(object)) return ((Node)object).getNodeValue();
        else return null;
    }


    /**
     * Get the string value of a namespace node.
     *
     * @param object the target node
     * @return the namespace URI as a (possibly empty) string if the
     *     node is a namespace node, null otherwise
     */
    public String getNamespaceStringValue (Object object)
    {
        if (isNamespace(object)) return ((NamespaceNode)object).getNodeValue();
        else return null;
    }

    /**
     * Get the prefix value of a namespace node.
     *
     * @param object the target node
     * @return the namespace prefix a (possibly empty) string if the
     *     node is a namespace node, null otherwise
     */
    public String getNamespacePrefix (Object object)
    {
        if (isNamespace(object)) return ((NamespaceNode)object).getLocalName();
        else return null;
    }

    /**
     * Translate a namespace prefix to a URI.
     * 
     * @param prefix the namespace prefix
     * @param element the namespace context
     * @return the namespace URI bound to the prefix in the scope of <code>element</code>;
     *     null if the prefix is not bound
     */
    public String translateNamespacePrefixToUri (String prefix, Object element)
    {
        Iterator it = getNamespaceAxisIterator(element);
        while (it.hasNext()) {
            NamespaceNode ns = (NamespaceNode)it.next();
            if (prefix.equals(ns.getNodeName())) return ns.getNodeValue();
        }
        return null;
    }

    /**
     * Use JAXP to load a namespace aware document from a given URI.
     *
     * @param uri the URI of the document to load
     * @return the new W3C DOM Level 2 Document instance
     * @throws FunctionCallException containing a nested exception
     *      if a problem occurs trying to parse the given document
     *
     * @todo Possibly we could make the factory a thread local.
     */
    public Object getDocument(String uri) throws FunctionCallException
    {
        try
        {
            // We really do need to construct a new factory here each time.
            // DocumentBuilderFactory is not guaranteed to be thread safe? 
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse( uri );
        }
        catch (ParserConfigurationException e) {
            throw new FunctionCallException("JAXP setup error in document() function: " + e.getMessage(), e);
        }
        catch (SAXException e) {
           throw new FunctionCallException("XML error in document() function: " + e.getMessage(), e);
        }
        catch (IOException e) {
           throw new FunctionCallException("I/O error in document() function: " + e.getMessage(), e);
        }
        
    }
    
    /**
     * Get the target of a processing instruction node.
     * 
     * @param obj the processing instruction
     * @return the target of the processing instruction
     * @throws ClassCastException if obj is not a processing instruction
     * 
     */
    public String getProcessingInstructionTarget(Object obj)
    {      
        if (isProcessingInstruction(obj)) {
            ProcessingInstruction pi = (ProcessingInstruction) obj;
            return pi.getTarget();
        }
        throw new ClassCastException(obj + " is not a processing instruction");
    }

    /**
     * Get the data of a processing instruction node.
     * 
     * @param obj the processing instruction
     * @return the target of the processing instruction
     * @throws ClassCastException if obj is not a processing instruction
     * 
     */
    public String getProcessingInstructionData(Object obj)
    {
        if (isProcessingInstruction(obj)) {
            ProcessingInstruction pi = (ProcessingInstruction) obj;
            return pi.getData();
        }
        throw new ClassCastException(obj + " is not a processing instruction");
    }

    
    ////////////////////////////////////////////////////////////////////
    // Inner class: iterate over DOM nodes.
    ////////////////////////////////////////////////////////////////////


    // FIXME: needs to recurse into
    // DocumentFragment and EntityReference
    // to use their children.

    /**
     * A generic iterator over DOM nodes.
     *
     * <p>Concrete subclasses must implement the {@link #getFirstNode}
     * and {@link #getNextNode} methods for a specific iteration
     * strategy.</p>
     */
    abstract class NodeIterator
    implements Iterator
    {


        /**
         * Constructor.
         *
         * @param contextNode the starting node
         */
        public NodeIterator (Node contextNode)
        {
            node = getFirstNode(contextNode);
            while (!isXPathNode(node)) {
                node = getNextNode(node);
            }
        }

        public boolean hasNext ()
        {
            return (node != null);
        }

        public Object next ()
        {
            if (node == null) throw new NoSuchElementException();
            Node ret = node;
            node = getNextNode(node);
            while (!isXPathNode(node)) {
                node = getNextNode(node);
            }
            return ret;
        }

        public void remove ()
        {
            throw new UnsupportedOperationException();
        }


        /**
         * Get the first node for iteration.
         *
         * <p>This method must derive an initial node for iteration
         * from a context node.</p>
         *
         * @param contextNode the starting node
         * @return the first node in the iteration
         * @see #getNextNode
         */
        protected abstract Node getFirstNode (Node contextNode);


        /**
         * Get the next node for iteration.
         *
         * <p>This method must locate a following node from the
         * current context node.</p>
         *
         * @param contextNode the current node in the iteration
         * @return the following node in the iteration, or null
         * if there is none
         * @see #getFirstNode
         */
        protected abstract Node getNextNode (Node contextNode);


        /**
         * Test whether a DOM node is usable by XPath.
         *
         * @param node the DOM node to test
         * @return true if the node is usable, false if it should be skipped
         */
        private boolean isXPathNode (Node node)
        {
            // null is usable, because it means end
            if (node == null) return true;

            switch (node.getNodeType()) {
                case Node.DOCUMENT_FRAGMENT_NODE:
                case Node.DOCUMENT_TYPE_NODE:
                case Node.ENTITY_NODE:
                case Node.ENTITY_REFERENCE_NODE:
                case Node.NOTATION_NODE:
                    return false;
                default:
                    return true;
            }
        }

        private Node node;
    }


    
    ////////////////////////////////////////////////////////////////////
    // Inner class: iterate over a DOM named node map.
    ////////////////////////////////////////////////////////////////////


    /**
     * An iterator over an attribute list.
     */
    private static class AttributeIterator implements Iterator
    {

        /**
         * Constructor.
         *
         * @param parent the parent DOM element for the attributes.
         */
        AttributeIterator (Node parent)
        {
            this.map = parent.getAttributes();
            this.pos = 0;
            for (int i = this.map.getLength()-1; i >= 0; i--) {
                Node node = map.item(i);
                if (! "http://www.w3.org/2000/xmlns/".equals(node.getNamespaceURI())) {
                    this.lastAttribute  = i;
                    break;
                }
            }
        }

        public boolean hasNext ()
        {
            return pos <= lastAttribute;
        }

        public Object next ()
        {
            Node attr = map.item(pos++);
            if (attr == null) throw new NoSuchElementException();
            else if ("http://www.w3.org/2000/xmlns/".equals(attr.getNamespaceURI())) {
              // XPath doesn't consider namespace declarations to be attributes 
              // so skip it and go to the next one
              return next();
            }
            else return attr;
        }

        public void remove ()
        {
            throw new UnsupportedOperationException();
        }


        private NamedNodeMap map;
        private int pos;
        private int lastAttribute = -1;

    }

    /**
     *  Returns the element whose ID is given by elementId.
     *  If no such element exists, returns null.
     *  Attributes with the name "ID" are not of type ID unless so defined.
     *  Attribute types are only known if when the parser understands DTD's or
     *  schemas that declare attributes of type ID. When JAXP is used, you
     *  must call <code>setValidating(true)</code> on the
     *  DocumentBuilderFactory.
     *
     *  @param object   a node from the document in which to look for the id
     *  @param elementId   id to look for
     *
     *  @return   element whose ID is given by elementId, or null if no such
     *            element exists in the document or if the implementation
     *            does not know about attribute types
     *  @see   javax.xml.parsers.DocumentBuilderFactory
     *  
     *  @throws ClassCastException if object is not an <code>org.w3c.dom.Node</code> object
     *  
     */
    public Object getElementById(Object object, String elementId)
    {
        Document doc = (Document)getDocumentNode(object);
        if (doc != null) return doc.getElementById(elementId);
        else return null;
    }

}

// end of DocumentNavigator.java
