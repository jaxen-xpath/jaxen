package org.jaxen.dom;

import org.w3c.dom.*;

import org.jaxen.InvalidContextException;
import org.jaxen.Navigator;
import org.jaxen.Updater;

/**
 * 
 */
public class DocumentUpdater implements Updater
{
    /** The implied namespace uri bound to the xmlns prefix, but for
     *  attributes only. It is also the ns uri of any attribute named xmlns.
     */
    final static String NS_XMLNS = "http://www.w3.org/2000/xmlns/";

    /** The implied namespace uri bound to the xml prefix. */
    final static String NS_XML = "http://www.w3.org/XML/1998/namespace";

    /** The xmlns prefix. */
    final static String PREFIX_XMLNS = "xmlns";

    public Navigator getNavigator()
    {
        return DocumentNavigator.getInstance();
    }

    protected Document getDocumentNode( Node contextNode )
    {
        if (contextNode.getNodeType() == Node.DOCUMENT_NODE)
            return (Document) contextNode;
        else
            return contextNode.getOwnerDocument();
    }

    public Object createComment( Object contextNode, String comment )
        throws InvalidContextException
    {
        if (!(contextNode instanceof Node))
            throw new InvalidContextException( "Context is not a DOM node" );
        Node context = (Node)contextNode;
        Document doc = getDocumentNode(context);
        return doc.createComment(comment);
    }

    public Object createText( Object contextNode, String text )
        throws InvalidContextException
    {
        if (!(contextNode instanceof Node))
            throw new InvalidContextException( "Context is not a DOM node" );
        Node context = (Node)contextNode;
        Document doc = getDocumentNode(context);
        return doc.createTextNode(text);
    }

    public Object createElement( Object contextNode,
                                 String uri, String qname )
        throws InvalidContextException
    {
        if (!(contextNode instanceof Node))
            throw new InvalidContextException( "Context is not a DOM node" );
        Node context = (Node)contextNode;
        Document doc = getDocumentNode(context);
        return doc.createElementNS(uri, qname);
    }

    public Object createNamespace( Object contextNode,
                                   String prefix, String uri )
        throws InvalidContextException
    {
        if (!(contextNode instanceof Node))
            throw new InvalidContextException( "Context is not a DOM node" );
        Node context = (Node)contextNode;
        Document doc = getDocumentNode(context);
        // !! contextNode is not the parent, but namespace node expects a
        // parent node in the constructor.
        return new NamespaceNode(context, prefix, uri);
    }

    public Object createAttribute( Object contextNode, String uri,
                                   String qname, String value )
        throws InvalidContextException
    {
        if (!(contextNode instanceof Node))
            throw new InvalidContextException( "Context is not a DOM node" );
        Node context = (Node)contextNode;
        Document doc = getDocumentNode(context);
        Attr attr = doc.createAttributeNS(uri, qname);
        attr.setValue(value);
        return attr;
    }

    public Object createProcessingInstruction( Object contextNode,
                                               String target,
                                               String data )
        throws InvalidContextException
    {
        if (!(contextNode instanceof Node))
            throw new InvalidContextException( "Context is not a DOM node" );
        Node context = (Node)contextNode;
        Document doc = getDocumentNode(context);
        return doc.createProcessingInstruction(target, data);
    }

    public void insertBefore( Object refNode, Object node )
        throws InvalidContextException
    {
        if (!(refNode instanceof Node) || !(node instanceof Node))
            throw new InvalidContextException( "Node is not a DOM node" );
        Node domRefNode = (Node)refNode;
        Node domNode = (Node)node;
        Node parent = domRefNode.getParentNode();
        if (parent == null)
            throw new InvalidContextException( "refNode has no parent" );

        parent.insertBefore(domNode, domRefNode);
    }

    public void insertAfter( Object refNode, Object node )
        throws InvalidContextException
    {
        if (!(refNode instanceof Node) || !(node instanceof Node))
            throw new InvalidContextException( "Node is not a DOM node" );
        Node domRefNode = (Node)refNode;
        Node domNode = (Node)node;
        Node parent = domRefNode.getParentNode();
        if (parent == null)
            throw new InvalidContextException( "refNode has no parent" );
        Node nextRefNode = domRefNode.getNextSibling();
        if (nextRefNode == null)
            parent.appendChild(domNode);
        else 
            parent.insertBefore(domNode, nextRefNode);
    }

    public void appendChild( Object element, Object child, int position )
        throws InvalidContextException
    {
        if (!(element instanceof Node) || !(child instanceof Node))
            throw new InvalidContextException( "Node is not a DOM node" );
        Node domChild = (Node)child;
        Node parent = (Node)element;
        if (position == -1)
            parent.appendChild(domChild);
        else {
            NodeList children = parent.getChildNodes();
            if (position >= children.getLength()) 
                parent.appendChild(domChild);
            else {
                Node nextRefNode = children.item(position + 1);
                parent.insertBefore(domChild, nextRefNode);
            }
        }
    }

    public void remove( Object node )
        throws InvalidContextException
    {
        if (!(node instanceof Node))
            throw new InvalidContextException( "Node is not a DOM node" );
        Node domNode = (Node)node;
        Node parent = domNode.getParentNode();
        if (parent == null)
            throw new InvalidContextException( "refNode has no parent" );

        parent.removeChild(domNode);
    }

    public void setAttribute( Object element, Object attribute )
        throws InvalidContextException
    {
        if (!(element instanceof Node) || !(attribute instanceof Node))
            throw new InvalidContextException( "Node is not a DOM node" );
        Node domAttribute = (Node)attribute;
        Node domElement = (Node)element;
        NamedNodeMap attributes = domElement.getAttributes();
        attributes.setNamedItemNS(domAttribute);
    }

    public void setNamespace( Object element, Object namespace )
        throws InvalidContextException
    {
        if (!(element instanceof Node) ||
            !(namespace instanceof NamespaceNode)) {
            throw new InvalidContextException( "Node is not a DOM node" );
        }
        NamespaceNode domNamespace = (NamespaceNode)namespace;
        String prefix = domNamespace.getLocalName();

        // It's not possible to set or redefine the "xml" prefix; trying
        // to set it can be ignored.
        if ( "xml".equals( prefix ) ) {
            if ( NS_XML.equals( domNamespace.getNodeValue() ) )
                return;
            else
                throw new InvalidContextException
                    ( "Attemping to bind " +
                      "prefix 'xml' to something other than " + NS_XML );
        }

        Node domElement = (Node)element;
        NamedNodeMap attributes = domElement.getAttributes();

        Document doc = getDocumentNode(domElement);
        Attr nsAttr = doc.createAttributeNS( NS_XMLNS,
                                             PREFIX_XMLNS + ':' + prefix );
        nsAttr.setValue(domNamespace.getNodeValue());
        attributes.setNamedItemNS(nsAttr);
    }
}
