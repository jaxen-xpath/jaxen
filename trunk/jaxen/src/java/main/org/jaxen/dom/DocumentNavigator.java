// DocumentNavigator.java - Jaxen adapter for the W3C DOM level 2.
// This file is in the Public Domain, and comes with NO WARRANTY.

package org.jaxen.dom;

import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jaxen.DefaultNavigator;
import org.jaxen.FunctionCallException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * W3C DOM Level 2 adapter for the Jaxen XPath library.
 *
 * <p>This class implements the org.jaxen.DefaultNavigator interface
 * for the Jaxen XPath library, version 1.0beta3 (it is not guaranteed
 * to work with subsequent releases).  This adapter allows the Jaxen
 * library to be used to execute XPath queries against any object tree
 * that implements the DOM level 2 interfaces.</p>
 *
 * <p>Note: DOM level 2 does not include a node representing an XML
 * Namespace declaration.  This navigator will return Namespace decls
 * as instantiations of the custom {@link NamespaceNode} class, and
 * users will have to check result sets to locate and isolate
 * these.</p>
 *
 * @author David Megginson
 * @author James Strachan
 * @see XPath
 * @see NamespaceNode
 */
public class DocumentNavigator extends DefaultNavigator
{

    
    ////////////////////////////////////////////////////////////////////
    // Constants.
    ////////////////////////////////////////////////////////////////////

    /**
     * Constant: empty iterator.
     */
    private final static Iterator EMPTY_ITERATOR =
	new HashMap().values().iterator();

    /**
     * Constant: singleton navigator.
     */
    private final static DocumentNavigator SINGLETON =
	new DocumentNavigator();


    
    ////////////////////////////////////////////////////////////////////
    // Constructor.
    ////////////////////////////////////////////////////////////////////


    /**
     * Default Constructor.
     */
    public DocumentNavigator ()
    {
    }


    /**
     * Get a singleton DocumentNavigator for efficiency.
     *
     * @return A singleton instance of a DocumentNavigator.
     */
    public static DocumentNavigator getInstance ()
    {
        return SINGLETON;
    }


    
    ////////////////////////////////////////////////////////////////////
    // Implementation of org.jaxen.DefaultNavigator.
    ////////////////////////////////////////////////////////////////////


    /**
     * Get an iterator over all of this node's children.
     *
     * @param contextNode The context node for the child axis.
     * @return A possibly-empty iterator (not null).
     */
    public Iterator getChildAxisIterator (Object contextNode)
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


    /**
     * Get a (single-member) iterator over this node's parent.
     *
     * @param contextNode the context node for the parent axis.
     * @return A possibly-empty iterator (not null).
     */
    public Iterator getParentAxisIterator (Object contextNode)
    {
        Node node = (Node)contextNode;

        if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
            return new NodeIterator (node) {
                    protected Node getFirstNode (Node node)
                    {
                        // FIXME: assumes castability.
                        return ((Attr)node).getOwnerElement();
                    }
                    protected Node getNextNode (Node node) {
                        return null;
                    }
                };
        } else {
            return new NodeIterator (node) {
                    protected Node getFirstNode (Node node)
                    {
                        return node.getParentNode();
                    }
                    protected Node getNextNode (Node node) {
                        return null;
                    }
                };
        }
    }


    /**
     * Get an iterator over all following siblings.
     *
     * @param contextNode the context node for the sibling iterator.
     * @return A possibly-empty iterator (not null).
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
     * @param contextNode The context node for the preceding sibling axis.
     * @return A possibly-empty iterator (not null).
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
     * @param contextNode The context node for the following axis.
     * @return A possibly-empty iterator (not null).
     */
    public Iterator getFollowingAxisIterator (Object contextNode)
    {
        return new NodeIterator ((Node)contextNode) {
                protected Node getFirstNode (Node node)
                {
                    if (node == null)
                        return null;
                    else {
                        Node sibling = node.getNextSibling();
                        if (sibling == null)
                            return getFirstNode(node.getParentNode());
                        else
                            return sibling;
                    }
                }
                protected Node getNextNode (Node node) {
                    if (node == null)
                        return null;
                    else {
                        Node n = node.getFirstChild();
                        if (n == null)
                            n = node.getNextSibling();
                        if (n == null)
                            return getFirstNode(node.getParentNode());
                        else
                            return n;
                    }
                }
            };
    }


    /**
     * Get an iterator over all preceding nodes, depth-first.
     *
     * @param contextNode The context node for the preceding axis.
     * @return A possibly-empty iterator (not null).
     */
    public Iterator getPrecedingAxisIterator (Object contextNode)
    {
        return new NodeIterator ((Node)contextNode) {
                protected Node getFirstNode (Node node)
                {
                    if (node == null)
                        return null;
                    else {
                        Node sibling = node.getPreviousSibling();
                        if (sibling == null)
                            return getFirstNode(node.getParentNode());
                        else
                            return sibling;
                    }
                }
                protected Node getNextNode (Node node) {
                    if (node == null)
                        return null;
                    else {
                        Node n = node.getLastChild();
                        if (n == null)
                            n = node.getPreviousSibling();
                        if (n == null)
                            return getFirstNode(node.getParentNode());
                        else
                            return n;
                    }
                }
            };
    }


    /**
     * Get an iterator over all attributes.
     *
     * @param contextNode The context node for the attribute axis.
     * @return A possibly-empty iterator (not null).
     */
    public Iterator getAttributeAxisIterator (Object contextNode)
    {
        if (isElement(contextNode)) {
            return new AttributeIterator((Node)contextNode);
        } else {
            return EMPTY_ITERATOR;
        }
    }


    /**
     * Get an iterator over all declared Namespaces.
     *
     * <p>Note: this iterator is not live: it takes a snapshot
     * and that snapshot remains static during the life of
     * the iterator (i.e. it won't reflect subsequent changes
     * to the DOM).</p>
     *
     * @param contextNode The context node for the Namespace axis.
     * @return A possibly-empty iterator (not null).
     */
    public Iterator getNamespaceAxisIterator (Object contextNode)
    {
        // Only elements have Namespace nodes
        if (isElement(contextNode)) {

            HashMap nsMap = new HashMap();

            // Start at the current node at walk
            // up to the root, noting what Namespace
            // declarations are in force.

            // TODO: deal with empty URI for
            // cancelling Namespace scope
            for (Node n = (Node)contextNode;
                 n != null;
                 n = n.getParentNode()) {
                if (n.hasAttributes()) {
                    NamedNodeMap atts = n.getAttributes();
                    int length = atts.getLength();
                    for (int i = 0; i < length; i++) {
                        Node att = atts.item(i);
                        if (att.getNodeName().startsWith("xmlns")) {
                            NamespaceNode ns =
                                new NamespaceNode((Node)contextNode, att);
                            // Add only if there's not a closer
                            // declaration in force.
                            String name = ns.getNodeName();
                            if (!nsMap.containsKey(name))
                                nsMap.put(name, ns);
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

            // An empty default Namespace cancels
            // any previous default.
            NamespaceNode defaultNS = (NamespaceNode)nsMap.get("");
            if (defaultNS != null && defaultNS.getNodeValue().equals(""))
                nsMap.remove("");
            return nsMap.values().iterator();
        } else {
            return EMPTY_ITERATOR;
        }
    }


    /**
     * Get the top-level document node.
     *
     * @param contextNode Any node in the document.
     * @return The root node.
     */
    public Object getDocumentNode (Object contextNode)
    {
        if (isDocument(contextNode))
            return contextNode;
        else
            return ((Node)contextNode).getOwnerDocument();
    }


    /**
     * Get the Namespace URI of an element.
     *
     * @param object The target node.
     * @return A string (possibly empty) if the node is an element,
     * and null otherwise.
     */
    public String getElementNamespaceUri (Object object)
    {
        String uri = ((Node)object).getNamespaceURI();
        if (uri == null)
            uri = "";
        return uri;
    }


    /**
     * Get the local name of an element.
     *
     * @param object The target node.
     * @return A string representing the unqualified local name
     * if the node is an element, or null otherwise.
     */
    public String getElementName (Object object)
    {
        String name = ((Node)object).getLocalName();
        if (name == null)
            name = ((Node)object).getNodeName();
        return name;
    }


    /**
     * Get the qualified name of an element.
     *
     * @param object The target node.
     * @return A string representing the qualified (i.e. possibly
     * prefixed) name if the node is an element, or null otherwise.
     */
    public String getElementQName (Object object)
    {
        String qname = ((Node)object).getNodeName();
        if (qname == null)
            qname = ((Node)object).getLocalName();
        return qname;
    }


    /**
     * Get the Namespace URI of an attribute.
     *
     * @param object The target node.
     * @param A possibly-empty string representing the Namespace URI
     * if the node is an attribute, or null otherwise.
     */
    public String getAttributeNamespaceUri (Object object)
    {
        String uri = ((Node)object).getNamespaceURI();
        if (uri == null)
            uri = "";
        return uri;
    }


    /**
     * Get the local name of an attribute.
     *
     * @param object The target node.
     * @return A string representing the unqualified local name
     * if the node is an attribute, or null otherwise.
     */
    public String getAttributeName (Object object)
    {
        String name = ((Node)object).getLocalName();
        if (name == null)
            name = ((Node)object).getNodeName();
        return name;
    }


    /**
     * Get the qualified name of an attribute.
     *
     * @param object The target node.
     * @return A string representing the qualified (i.e. possibly
     * prefixed) name if the node is an attribute, or null otherwise.
     */
    public String getAttributeQName (Object object)
    {
        String qname = ((Node)object).getNodeName();
        if (qname == null)
            qname = ((Node)object).getLocalName();
        return qname;
    }


    /**
     * Test if a node is a top-level document.
     *
     * @param object The target node.
     * @return true if the node is the document root, false otherwise.
     */
    public boolean isDocument (Object object)
    {
        return (object instanceof Node) &&
            (((Node)object).getNodeType() == Node.DOCUMENT_NODE);
    }


    /**
     * Test if a node is a Namespace.
     *
     * @param object The target node.
     * @return true if the node is a Namespace, false otherwise.
     */
    public boolean isNamespace (Object object)
    {
        return (object instanceof NamespaceNode);
    }


    /**
     * Test if a node is an element.
     *
     * @param object The target node.
     * @return true if the node is an element, false otherwise.
     */
    public boolean isElement (Object object)
    {
        return (object instanceof Node) &&
            (((Node)object).getNodeType() == Node.ELEMENT_NODE);
    }


    /**
     * Test if a node is an attribute.
     *
     * @param object The target node.
     * @return true if the node is an attribute, false otherwise.
     */
    public boolean isAttribute (Object object)
    {
        return (object instanceof Node) &&
            (((Node)object).getNodeType() == Node.ATTRIBUTE_NODE);
    }


    /**
     * Test if a node is a comment.
     *
     * @param object The target node.
     * @return true if the node is a comment, false otherwise.
     */
    public boolean isComment (Object object)
    {
        return (object instanceof Node) &&
            (((Node)object).getNodeType() == Node.COMMENT_NODE);
    }


    /**
     * Test if a node is plain text.
     *
     * @param object The target node.
     * @return true if the node is a text node, false otherwise.
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
     * @param object The target node.
     * @return true if the node is a processing instruction, false otherwise.
     */
    public boolean isProcessingInstruction (Object object)
    {
        return (object instanceof Node) &&
            (((Node)object).getNodeType() == Node.PROCESSING_INSTRUCTION_NODE);
    }


    /**
     * Get the string value of an element node.
     *
     * @param object The target node.
     * @return The text inside the node and its descendants if the node
     * is an element, null otherwise.
     */
    public String getElementStringValue (Object object)
    {
        if (isElement(object))
            return getStringValue((Node)object, new StringBuffer()).toString();
        else
            return null;
    }


    /**
     * Construct an element's string value recursively.
     *
     * @param node The current node.
     * @param buffer The buffer for building the text.
     * @return The buffer passed as a parameter (for convenience).
     */
    private StringBuffer getStringValue (Node node, StringBuffer buffer)
    {
        if (isText(node)) {
            buffer.append(node.getNodeValue());
        } else {
            NodeList children = node.getChildNodes();
            int length = children.getLength();
            for (int i = 0; i < length; i++)
                getStringValue(children.item(i), buffer);
        }
        return buffer;
    }


    /**
     * Get the string value of an attribute node.
     *
     * @param object The target node.
     * @return The text of the attribute value if the node is an
     * attribute, null otherwise.
     */
    public String getAttributeStringValue (Object object)
    {
        if (isAttribute(object))
            return ((Node)object).getNodeValue();
        else
            return null;
    }


    /**
     * Get the string value of text.
     *
     * @param object The target node.
     * @return The string of text if the node is text, null otherwise.
     */
    public String getTextStringValue (Object object)
    {
        if (isText(object))
            return ((Node)object).getNodeValue();
        else
            return null;
    }


    /**
     * Get the string value of a comment node.
     *
     * @param object The target node.
     * @return The text of the comment if the node is a comment,
     * null otherwise.
     */
    public String getCommentStringValue (Object object)
    {
        if (isComment(object))
            return ((Node)object).getNodeValue();
        else
            return null;
    }


    /**
     * Get the string value of a Namespace node.
     *
     * @param object The target node.
     * @return The Namespace URI as a (possibly empty) string if the
     * node is a comment, null otherwise.
     */
    public String getNamespaceStringValue (Object object)
    {
        if (isNamespace(object))
            return ((NamespaceNode)object).getNodeValue();
        else
            return null;
    }


    /**
     * Translate a Namespace prefix to a URI.
     */
    public String translateNamespacePrefixToUri (String prefix, Object element)
    {
        Iterator it = getNamespaceAxisIterator(element);
        while (it.hasNext()) {
            NamespaceNode ns = (NamespaceNode)it.next();
            if (prefix.equals(ns.getNodeName()))
                return ns.getNodeValue();
        }
        return null;
    }

    /**
     * Use JAXP to load a namespace aware document from a given URI 
     *
     * @param uri is the URI of the document to load
     * @return the new W3C DOM Level 2 Document instance
     * @throws FunctionCallException containing a nested exception
     *      if a problem occurs trying to parse the given document
     */
    public Object getDocument(String uri) throws FunctionCallException
    {
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse( uri );
        }
        catch (Exception e)
        {
            throw new FunctionCallException("Failed to parse doucment for URI: " + uri, e);
        }
    }

    public String getProcessingInstructionTarget(Object obj)
    {
        ProcessingInstruction pi = (ProcessingInstruction) obj;

        return pi.getTarget();
    }

    public String getProcessingInstructionData(Object obj)
    {
        ProcessingInstruction pi = (ProcessingInstruction) obj;

        return pi.getData();
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
         * @param contextNode The starting node.
         */
        public NodeIterator (Node contextNode)
        {
            node = getFirstNode(contextNode);
            while (!isXPathNode(node))
                node = getNextNode(node);
        }


        /**
         * @see Iterator#hasNext
         */
        public boolean hasNext ()
        {
            return (node != null);
        }


        /**
         * @see Iterator#next
         */
        public Object next ()
        {
            if (node == null)
                throw new NoSuchElementException();
            Node ret = node;
            node = getNextNode(node);
            while (!isXPathNode(node))
                node = getNextNode(node);
            return ret;
        }


        /**
         * @see Iterator#remove
         */
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
         * @param contextNode The starting node.
         * @return The first node in the iteration.
         * @see #getNextNode
         */
        protected abstract Node getFirstNode (Node contextNode);


        /**
         * Get the next node for iteration.
         *
         * <p>This method must locate a following node from the
         * current context node.</p>
         *
         * @param contextNode The current node in the iteration.
         * @return The following node in the iteration, or null
         * if there is none.
         * @see #getFirstNode
         */
        protected abstract Node getNextNode (Node contextNode);


        /**
         * Test whether a DOM node is usable by XPath.
         *
         * @param node The DOM node to test.
         * @return true if the node is usable, false if it should be
         * skipped.
         */
        private boolean isXPathNode (Node node)
        {
            // null is usable, because it means end
            if (node == null)
                return true;

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
    class AttributeIterator implements Iterator
    {

        /**
         * Constructor.
         *
         * @param parent The parent DOM element for the attributes.
         */
        AttributeIterator (Node parent)
        {
            this.parent = parent;
            this.map = parent.getAttributes();
            this.pos = 0;
        }


        /**
         * @see Iterator#hasNext
         */
        public boolean hasNext ()
        {
            return pos < map.getLength();
        }


        /**
         * @see Iterator#next
         */
        public Object next ()
        {
            Node attr = map.item(pos++);
            if (attr == null)
                throw new NoSuchElementException();
            else
                return attr;
        }


        /**
         * @see Iterator#remove
         */
        public void remove ()
        {
            throw new UnsupportedOperationException();
        }


        private Node parent;
        private NamedNodeMap map;
        private int pos;

    }

    /**
     *  Returns the element whose ID is given by elementId.
     *  If no such element exists, returns null.
     *  Attributes with the name "ID" are not of type ID unless so defined.
     *  Atribute types are only known if when the parser understands DTD's or
     *  schemas that declare attributes of type ID. When JAXP is used, you
     *  must call <code>setValidating(true)</code> on the
     *  DocumentBuilderFactory.
     *
     *  @param contextNode   a node from the document in which to look for the
     *                       id
     *  @param elementId   id to look for
     *
     *  @return   element whose ID is given by elementId, or null if no such
     *            element exists in the document or if the implementation
     *            does not know about attribute types
     *  @see   javax.xml.parsers.DocumentBuilderFactory
     */
    public Object getElementById(Object object, String elementId)
    {
        Document doc = (Document)getDocumentNode(object);
        if (doc != null)
            return doc.getElementById(elementId);
        else
            return null;
    }

}    

// end of DocumentNavigator.java
