
package org.jaxen.dom;

import org.jaxen.DefaultNavigator;

import org.jaxen.util.SingleObjectIterator;

import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.ProcessingInstruction;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/** A Navigator for the W3C DOM model
 *
 * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 * @version $Revision$
 */
public class DocumentNavigator extends DefaultNavigator
{
    private static class Singleton
    {
        private static DocumentNavigator instance = new DocumentNavigator();
    }

    public static DocumentNavigator getInstance()
    {
        return Singleton.instance;
    }

    public boolean isElement(Object obj)
    {
        return obj instanceof Element;
    }

    public boolean isComment(Object obj)
    {
        return obj instanceof Comment;
    }

    public boolean isText(Object obj)
    {
        return obj instanceof Text;
    }

    public boolean isAttribute(Object obj)
    {
        return obj instanceof Attr;
    }

    public boolean isProcessingInstruction(Object obj)
    {
        return obj instanceof ProcessingInstruction;
    }

    public boolean isDocument(Object obj)
    {
        return obj instanceof Document;
    }

    public boolean isNamespace(Object obj)
    {
        //return obj instanceof Namespace;
        return false;
    }

    public String getElementName(Object obj)
    {
        Node node = (Node) obj;

        return node.getLocalName();
    }

    public String getElementNamespaceUri(Object obj)
    {
        Node node = (Node) obj;

        return node.getNamespaceURI();
    }

    public String getElementQName(Object obj)
    {
        Node node = (Node) obj;

        return node.getNodeName();
    }

    public String getAttributeName(Object obj)
    {
        Node node = (Node) obj;

        return node.getLocalName();
    }

    public String getAttributeNamespaceUri(Object obj)
    {
        Node node = (Node) obj;

        return node.getNamespaceURI();
    }

    public String getAttributeQName(Object obj)
    {
        Node node = (Node) obj;

        return node.getNodeName();
    }

    public Iterator getChildAxisIterator(Object contextNode)
    {
        if ( contextNode instanceof Node )
        {            
            final Node parent = (Node) contextNode;
            final Node child = parent.getFirstChild();
            if ( child != null ) 
            {
                return new Iterator() 
                {
                    Node current = child;
                    
                    public boolean hasNext()
                    {
                        if ( current != null ) 
                        {
                            return current.getNextSibling() != null;
                        }
                        return false;
                    }
                    
                    public Object next()
                    {
                        if ( current != null ) 
                        {
                            current = current.getNextSibling();
                        }
                        return current;
                    }
                    
                    public void remove()
                    {
                        if ( current != null )
                        {
                            parent.removeChild( current );
                        }
                    }
                };
            }
        }

        return null;
    }

    public Iterator getParentAxisIterator(Object contextNode)
    {
        if ( contextNode instanceof Document )
        {
            return new SingleObjectIterator( contextNode );
        }

        Node node = (Node) contextNode;

        Object parent = node.getParentNode();

        if ( parent == null )
        {
            parent = node.getOwnerDocument();
        }
        
        return new SingleObjectIterator( parent );
    }

    public Iterator getAttributeAxisIterator(Object contextNode)
    {
/*
        if ( ! ( contextNode instanceof Element ) )
        {
            return null;
        }

        Element elem = (Element) contextNode;

        return elem.attributeIterator();
*/
        return null;
    }

    public Iterator getNamespaceAxisIterator(Object contextNode)
    {
        return null;
    }

    public Object getDocumentNode(Object contextNode)
    {
        if ( contextNode instanceof Document )
        {
            return contextNode;
        }
        if ( contextNode instanceof Node ) 
        {
            Node node = (Node) contextNode;
            return node.getOwnerDocument();
        }
        return null;
    }

    public String getElementStringValue(Object obj)
    {
        return getNodeStringValue( (Node) obj );
    }

    public String getAttributeStringValue(Object obj)
    {
        return getNodeStringValue( (Node) obj );
    }

    private String getNodeStringValue(Node node)
    {
        return node.getNodeValue();
    }

    public String getNamespaceStringValue(Object obj)
    {
/*
        Namespace ns = (Namespace) obj;

        return ns.getURI();
*/
        return null;
    }

    public String getCommentStringValue(Object obj)
    {
        Comment cmt = (Comment) obj;

        return cmt.getNodeValue();
    }
    
    public String translateNamespacePrefixToUri(String prefix, Object context)
    {
        return null;
    }
}
