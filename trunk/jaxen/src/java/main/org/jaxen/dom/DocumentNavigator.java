
package org.jaxen.dom;

import org.jaxen.DefaultNavigator;
import org.jaxen.util.SingleObjectIterator;

import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
        if ( obj instanceof Attr )
        {
            Attr node = (Attr) obj;
            String name = node.getNodeName();
            return name != null && name.startsWith( "xmlns" );
        }
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
        //System.out.println( "getElementName() : " + answer );
/*        
        // hack to fix getLocalName() not working
        int idx = answer.indexOf(':');
        if ( idx >= 0 ) 
        {
            answer = answer.substring(idx+1);
        }
*/        
        return answer;
    }

    public String getElementNamespaceUri(Object obj)
    {
        Node node = (Node) obj;

        String answer = node.getNamespaceURI();
        if ( answer == null )
        {
            answer = "";
        }
        return answer;        
    }

    public String getElementQName(Object obj)
    {
        Node node = (Node) obj;

        String answer = node.getNodeName();
        if ( answer == null )
        {
            answer = node.getLocalName();
        }
        //System.out.println( "getElementQName() : " + answer );
        return answer;
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

        String answer = node.getNamespaceURI();
        return ( answer == null ) ? "" : answer;        
    }

    public String getAttributeQName(Object obj)
    {
        Node node = (Node) obj;

        String answer = node.getNodeName();
        if ( answer == null )
        {
            answer = node.getLocalName();
        }    
        return answer;
    }

    public Iterator getChildAxisIterator(Object contextNode)
    {
        //System.err.println( "Navigator.getChildAxisIterator(" + contextNode + ")");

        if ( contextNode instanceof Node )
        {            
            //System.err.println( "isNode" );
            Node node = (Node) contextNode;
            NodeList children = node.getChildNodes();

            //System.err.println(" children: " + children );

            /*
            Iterator childIter = new ChildIterator( node.getChildNodes() );

            while ( childIter.hasNext() )
            {
                //System.err.println( "child--->" + childIter.next() );
            }
            */
            if ( children != null )
            {
                return new ChildIterator( children );
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
        if ( contextNode instanceof Element )
        {
            Element element = (Element) contextNode;
            return new AttributeIterator( element.getAttributes() );
        }
        return null;
    }

    public Iterator getNamespaceAxisIterator(Object contextNode)
    {
        if ( contextNode instanceof Element )
        {
            Element element = (Element) contextNode;
            return new NamespaceIterator( element.getAttributes() );
        }
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

    public String getTextStringValue(Object obj)
    {
        return getNodeStringValue( (Node) obj );
    }

    private String getNodeStringValue(Node node)
    {
        return node.getNodeValue();
    }

    public String getNamespaceStringValue(Object obj)
    {
        Node node = (Node) obj;
        String answer = node.getNodeValue();
        if ( answer == null )
        {
            answer = "";
        }
        return answer;
    }

    public String getCommentStringValue(Object obj)
    {
        Comment cmt = (Comment) obj;

        return cmt.getNodeValue();
    }
    
    public String translateNamespacePrefixToUri(String prefix, Object context)
    {
        if ( prefix == null || prefix.length() == 0 )  
        {
            prefix = "xmlns";
        }
        else 
        {
            prefix = "xmlns:" + prefix;
        }

        String answer = null;
        
        if ( context instanceof Element ) 
        {
            answer = walkHierachyForURI( prefix, (Element) context );
        }
        else if ( context instanceof Node ) 
        {
            Node node = (Node) context;
            Node parent = node.getParentNode();
            if ( parent instanceof Element )
            {
                answer = walkHierachyForURI( prefix, (Element) parent);
            }
        }
        
        //System.out.println( "translateNamespacePrefixToUri matched prefix: " + prefix + " to: " + answer + " from node: " + context );
        
        return answer;        
    }
    
    public String walkHierachyForURI(String prefix, Element element)
    {
        String answer = element.getAttribute( prefix );
        if ( answer == null || answer.length() <= 0 ) 
        {
            answer = element.getAttributeNS( prefix, "" );
            if ( answer == null || answer.length() <= 0 ) 
            {
                Node parent = element.getParentNode();
                if ( parent != null && parent instanceof Element )
                {
                    answer = walkHierachyForURI(prefix, (Element) parent);
                }
            }
        }
        return answer;
    }
}
