
package org.jaxen.exml;

import org.jaxen.DefaultNavigator;

import org.jaxen.util.SingleObjectIterator;

import electric.xml.Document;
import electric.xml.Element;
import electric.xml.Comment;
import electric.xml.Attribute;
import electric.xml.Text;
import electric.xml.CData;
import electric.xml.Instruction;
//import electric.xml.Namespace;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

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
        return obj instanceof Attribute;
    }

    public boolean isProcessingInstruction(Object obj)
    {
        return obj instanceof Instruction;
    }

    public boolean isDocument(Object obj)
    {
        return obj instanceof Document;
    }

    public boolean isNamespace(Object obj)
    {
        return false;
    }

    public String getElementName(Object obj)
    {
        Element elem = (Element) obj;

        return elem.getName();
    }

    public String getElementNamespaceUri(Object obj)
    {
        Element elem = (Element) obj;
        
        return elem.getNamespace();
    }

    public String getAttributeName(Object obj)
    {
        Attribute attr = (Attribute) obj;

        return attr.getName();
    }

    public String getAttributeNamespaceUri(Object obj)
    {
        Attribute attr = (Attribute) obj;

        return attr.getNamespace();
    }

    public Iterator getChildAxisIterator(Object contextNode)
    {
        if ( contextNode instanceof Element )
        {
            return new ElementsIterator( ((Element)contextNode).getElements() );
        }
        else if ( contextNode instanceof Document )
        {
            return new ElementsIterator( ((Document)contextNode).getElements() );
        }

        return null;
    }

    /*
    public Iterator getNamespaceAxisIterator(Object contextNode)
    {
        if ( ! ( contextNode instanceof Element ) )
        {
            return null;
        }

        Element elem = (Element) contextNode;

        List nsList = new ArrayList();

        Namespace ns = elem.getNamespace();

        if ( ns != Namespace.NO_NAMESPACE )
        {
            nsList.add( elem.getNamespace() );
        }

        nsList.addAll( elem.getAdditionalNamespaces() );

        return nsList.iterator();
    }
    */

    public Iterator getParentAxisIterator(Object contextNode)
    {
        Object parent = null;

        if ( contextNode instanceof Document )
        {
            parent = contextNode;
        }
        else if ( contextNode instanceof Element )
        {
            parent = ((Element)contextNode).getParent();

            if ( parent == null )
            {
                parent = ((Element)contextNode).getDocument();
            }
        }
        /*
        else if ( contextNode instanceof Attribute )
        {
            parent = ((Attribute)contextNode).getParent();
        }
        */
        else if ( contextNode instanceof Instruction )
        {
            parent = ((Instruction)contextNode).getParent();
        }
        else if ( contextNode instanceof Text )
        {
            parent = ((Text)contextNode).getParent();
        }
        else if ( contextNode instanceof Comment )
        {
            parent = ((Text)contextNode).getParent();
        }
        
        if ( parent != null )
        {
            return new SingleObjectIterator( parent );
        }

        return null;
    }

    public Iterator getAttributeAxisIterator(Object contextNode)
    {
        if ( ! ( contextNode instanceof Element ) )
        {
            return null;
        }

        Element elem = (Element) contextNode;

        return new AttributesIterator( elem.getAttributes() );
    }

    public Object getDocumentNode(Object contextNode)
    {
        if ( contextNode instanceof Document )
        {
            return contextNode;
        }

        Element elem = (Element) contextNode;

        return elem.getDocument();
    }

    public String getElementQName(Object obj)
    {
        Element elem = (Element) obj;

        return elem.getQName();
    }

    public String getAttributeQName(Object obj)
    {
        Attribute attr = (Attribute) obj;

        return attr.getQName();
    }

    public String getNamespaceStringValue(Object obj)
    {
        return "";
    }

    public String getAttributeStringValue(Object obj)
    {
        Attribute attr = (Attribute) obj;

        return attr.getValue();
    }

    public String getElementStringValue(Object obj)
    {
        Element elem = (Element) obj;

        StringBuffer buf = new StringBuffer();

        // List     content     = elem.getContent();
        // Iterator contentIter = content.iterator();

        Iterator contentIter = new ElementsIterator( elem.getElements() );

        Object   each        = null;

        while ( contentIter.hasNext() )
        {
            each = contentIter.next();

            if ( each instanceof String )
            {
                buf.append( each );
            }
            else if ( each instanceof Text )
            {
                buf.append( ((Text)each).getString() );
            }
            else if ( each instanceof CData )
            {
                buf.append( ((CData)each).getString() );
            }
            else if ( each instanceof Element )
            {
                buf.append( getElementStringValue( each ) );
            }
        }

        return buf.toString();
    }

    public String getCommentStringValue(Object obj)
    {
        Comment cmt = (Comment) obj;

        return cmt.getString();
    }
}
