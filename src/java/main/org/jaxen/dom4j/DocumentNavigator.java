// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.dom4j;

import org.jaxen.BaseXPath;
import org.jaxen.DefaultNavigator;
import org.jaxen.FunctionCallException;
import org.jaxen.util.SingleObjectIterator;

import org.saxpath.SAXPathException;

import org.dom4j.Attribute;
import org.dom4j.Branch;
import org.dom4j.CDATA;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.ProcessingInstruction;
import org.dom4j.Text;
import org.dom4j.io.SAXReader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Iterator;

/** Interface for navigating around the EXML object model.
 *
 *  <p>
 *  This class is not intended for direct usage, but is
 *  used by the Jaxen engine during evaluation.
 *  </p>
 *
 *  @see XPath
 *
 *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 */
public class DocumentNavigator extends DefaultNavigator
{
    private transient SAXReader reader;

    /** Singleton implementation.
     */
    private static class Singleton
    {
        /** Singleton instance.
         */
        private static DocumentNavigator instance = new DocumentNavigator();
    }

    /** Retrieve the singleton instance of this <code>DocumentNavigator</code>.
     */
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
        
        String uri = elem.getNamespaceURI();
        if ( uri != null && uri.length() == 0 ) 
            return null;
        else
            return uri;
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

        String uri = attr.getNamespaceURI();
        if ( uri != null && uri.length() == 0 ) 
            return null;
        else
            return uri;
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
        List nsList = new ArrayList();
        HashSet prefixes = new HashSet();
        for ( Element context = element; context != null; context = context.getParent() ) {
            List declaredNS = context.declaredNamespaces();
            for ( Iterator iter = declaredNS.iterator(); iter.hasNext(); )
            {
                Namespace namespace = (Namespace) iter.next();
                String prefix = namespace.getPrefix();
                if ( ! prefixes.contains( prefix ) ) {
                    prefixes.add( prefix );
                    nsList.add( namespace.asXPathResult( element ) );
                }
            }
        }
        nsList.add( Namespace.XML_NAMESPACE.asXPathResult( element ) );
        return nsList.iterator();
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

    /** Returns a parsed form of the given xpath string, which will be suitable
     *  for queries on DOM4J documents.
     */
    public BaseXPath parseXPath (String xpath) throws SAXPathException
    {
        return new XPath(xpath);
    }

    public Object getParentNode(Object contextNode)
    {
        if ( contextNode instanceof Node ) 
        {
            Node node = (Node) contextNode;
            Object answer = node.getParent();
            if ( answer == null ) 
            {
                answer = node.getDocument();
                if ( answer == null )
                {
                    answer = node;
                }
            }
            return answer;            
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

    public String getNamespacePrefix(Object obj)
    {
        Namespace ns = (Namespace) obj;

        return ns.getPrefix();
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
    
    public short getNodeType(Object node) 
    {
        if ( node instanceof Node )
        {
            return ((Node) node).getNodeType();
        }
        return 0;
    }
    
    public Object getDocument(String uri) throws FunctionCallException
    {
        try
        {
            return getSAXReader().read( uri );
        }
        catch (DocumentException e)
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

        return pi.getText();
    }
    
    // Properties
    //-------------------------------------------------------------------------    
    public SAXReader getSAXReader()
    {
        if ( reader == null ) 
        {
            reader = new SAXReader();
        }
        return reader;
    }
    
    public void setSAXReader(SAXReader reader)
    {
        this.reader = reader;
    }
    
}
