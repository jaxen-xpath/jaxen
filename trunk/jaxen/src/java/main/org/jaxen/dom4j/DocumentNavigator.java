
package org.jaxen.dom4j;

import org.jaxen.DefaultNavigator;

import org.jaxen.util.SingleObjectIterator;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Comment;
import org.dom4j.Attribute;
import org.dom4j.Text;
import org.dom4j.CDATA;
import org.dom4j.ProcessingInstruction;
import org.dom4j.Namespace;
import org.dom4j.Branch;
import org.dom4j.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/** A Navigator for the <a href="http://dom4j.org">dom4j</a> model.
 *
 * @author bob mcwhirter (bob @ werken.com)
 * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 * @version $Revision$
 */
public class DocumentNavigator extends DefaultNavigator
{
    /** Should we just return new namespace declarations in //namespace::** or
     * return a namespace node for all elements in a namespace
     */
    private static final boolean ONLY_RETURN_NAMESPACE_DECLARATIONS = true;
    
    
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
        return ( obj instanceof Text 
                 ||
                 obj instanceof CDATA );
    }

    public boolean isAttribute(Object obj)
    {
        return obj instanceof Attribute;
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
        return obj instanceof Namespace;
    }

    public String getElementName(Object obj)
    {
        Element elem = (Element) obj;

        return elem.getName();
    }

    public String getElementNamespaceUri(Object obj)
    {
        Element elem = (Element) obj;
        
        return elem.getNamespaceURI();
    }

    public String getElementQName(Object obj)
    {
        Element elem = (Element) obj;

        return elem.getQualifiedName();
    }

    public String getAttributeName(Object obj)
    {
        Attribute attr = (Attribute) obj;

        return attr.getName();
    }

    public String getAttributeNamespaceUri(Object obj)
    {
        Attribute attr = (Attribute) obj;

        return attr.getNamespaceURI();
    }

    public String getAttributeQName(Object obj)
    {
        Attribute attr = (Attribute) obj;

        return attr.getQualifiedName();
    }

    public Iterator getChildAxisIterator(Object contextNode)
    {
        if ( contextNode instanceof Branch )
        {
            Branch node = (Branch) contextNode;
            
            return node.nodeIterator();
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

        Object parent = node.getParent();

        if ( parent == null )
        {
            parent = node.getDocument();
        }
        
        return new SingleObjectIterator( parent );
    }

    public Iterator getAttributeAxisIterator(Object contextNode)
    {
        if ( ! ( contextNode instanceof Element ) )
        {
            return null;
        }

        Element elem = (Element) contextNode;

        return elem.attributeIterator();
    }

    public Iterator getNamespaceAxisIterator(Object contextNode)
    {
        if ( ! ( contextNode instanceof Element ) )
        {
            return null;
        }

        Element element = (Element) contextNode;
        if ( ONLY_RETURN_NAMESPACE_DECLARATIONS ) 
        {
            List nsList = new ArrayList();
            Namespace elementNS = element.getNamespace();
            String uri = elementNS.getURI();
            if ( uri != null && uri.length() > 0 ) 
            {
                // the namespace may have already been declared by a parent element
                Element parent = element.getParent();
                if ( parent != null ) 
                {
                    if ( parent.getNamespaceForPrefix( elementNS.getPrefix() ) == null )
                    {
                        nsList.add( elementNS.asXPathResult( element ) );
                    }
                }
                else 
                {            
                    nsList.add( elementNS.asXPathResult( element ) );
                }
            }

            List additionalNS = element.additionalNamespaces();
            for ( Iterator iter = additionalNS.iterator(); iter.hasNext(); )
            {
                Namespace namespace = (Namespace) iter.next();            
                nsList.add( namespace.asXPathResult( element ) );
            }
            
            return nsList.iterator();
        }
        else 
        {
            List nsList = new ArrayList();
            List declaredNS = element.declaredNamespaces();
            for ( Iterator iter = declaredNS.iterator(); iter.hasNext(); )
            {
                Namespace namespace = (Namespace) iter.next();

                // the namespace may have already been declared by a parent
                Element parent = element.getParent();
                if ( parent == null 
                    || parent.getNamespaceForURI( namespace.getURI() ) == null )
                {            
                    nsList.add( namespace.asXPathResult( element ) );
                }
            }

            return nsList.iterator();
        }
    }

    public Object getDocumentNode(Object contextNode)
    {
        if ( contextNode instanceof Document ) 
        {
            return contextNode;
        }
        else if ( contextNode instanceof Node ) 
        {
            Node node = (Node) contextNode;
            return node.getDocument();
        }
        return null;
    }

    public String getTextStringValue(Object obj)
    {
        return getNodeStringValue( (Node) obj );
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
        return node.getStringValue();
    }

    public String getNamespaceStringValue(Object obj)
    {
        Namespace ns = (Namespace) obj;

        return ns.getURI();
    }

    public String getCommentStringValue(Object obj)
    {
        Comment cmt = (Comment) obj;

        return cmt.getText();
    }
    
    public String translateNamespacePrefixToUri(String prefix, Object context)
    {
        Element element = null;
        if ( context instanceof Element ) 
        {
            element = (Element) context;
        }
        else if ( context instanceof Node )
        {
            Node node = (Node) context;
            element = node.getParent();
        }
        if ( element != null )
        {
            Namespace namespace = element.getNamespaceForPrefix( prefix );

            if ( namespace != null ) 
            {
                return namespace.getURI();
            }
        }
        return null;
    }
}
