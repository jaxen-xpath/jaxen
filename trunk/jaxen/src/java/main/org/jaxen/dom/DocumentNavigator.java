
package org.jaxen.dom;

import org.jaxen.DefaultNavigator;
import org.jaxen.util.SingleObjectIterator;

import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
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

        String answer = node.getLocalName();
        if ( answer == null )
        {
            answer = node.getNodeName();
        }        
        return answer;
    }

    public String getElementNamespaceUri(Object obj)
    {
        Node node = (Node) obj;

        return node.getNamespaceURI();
    }

    public String getElementQName(Object obj)
    {
        Node node = (Node) obj;

        System.out.println( "getElementQName(): " + node.getNodeName() );
        
        return node.getNodeName();
    }

    public String getAttributeName(Object obj)
    {
        Node node = (Node) obj;

        String answer = node.getLocalName();
        if ( answer == null )
        {
            answer = node.getNodeName();
        }        
        return answer;
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
            return new Iterator() 
            {
                boolean first = true;
                Node current = parent.getFirstChild();
                Node next;

                public boolean hasNext()
                {
                    while ( current != null )
                    {
                        if ( first ) 
                        {
                            first = false;
                        }
                        else 
                        {
                            current = current.getNextSibling();
                            if ( current == null )
                            {
                                return false;
                            }
                        }
                        int type = current.getNodeType();
                        if ( type == Node.DOCUMENT_TYPE_NODE || type == Node.NOTATION_NODE ) {
                            continue;
                        }
                        break;
                    }
                    return true;
                }

                public Object next()
                {
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

        return null;
    }

    public Iterator getParentAxisIterator(Object contextNode)
    {
        if ( contextNode instanceof Document )
        {
            return null;
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
        if ( ! ( contextNode instanceof Element ) )
        {
            return null;
        }

        Element element = (Element) contextNode;
        final NamedNodeMap attributes = element.getAttributes();
        return new Iterator() 
        {
            int index = -1;
            int size = attributes.getLength();

            public boolean hasNext()
            {
                return ++index < size;
            }

            public Object next()
            {
                return attributes.item(index);
            }

            public void remove()
            {
                throw new UnsupportedOperationException( "remove() is not supported by this iterator" );
            }
        };
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
        Node element = (Node) obj;
        StringBuffer buffer = new StringBuffer();
        if (element.hasChildNodes()) 
        {
            for ( Node child = (Node)element.getFirstChild(); 
                child != null; 
                child = child.getNextSibling() )
            {
                switch (child.getNodeType()) {
                    case Node.TEXT_NODE:
                        buffer.append( child.getNodeValue());
                        break;
                    case Node.ELEMENT_NODE:
                        buffer.append( getElementStringValue( child ) );
                        break;
                }
            }
        }
        return buffer.toString();
        //return getNodeStringValue( (Node) obj );
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
