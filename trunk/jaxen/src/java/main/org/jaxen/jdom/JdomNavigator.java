
package org.jaxen.jdom;

import org.jaxen.DefaultNavigator;

import org.jaxen.util.SingleObjectIterator;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Comment;
import org.jdom.Attribute;
import org.jdom.Text;
import org.jdom.CDATA;
import org.jdom.ProcessingInstruction;
import org.jdom.Namespace;

import java.util.List;
import java.util.Iterator;

public class JdomNavigator extends DefaultNavigator
{
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
        return obj instanceof ProcessingInstruction;
    }

    public boolean isDocument(Object obj)
    {
        return obj instanceof Document;
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

    public Iterator getChildAxisIterator(Object contextNode)
    {
        if ( contextNode instanceof Element )
        {
            return ((Element)contextNode).getContent().iterator();
        }
        else if ( contextNode instanceof Document )
        {
            return ((Document)contextNode).getContent().iterator();
        }

        return null;
    }

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
                if ( ((Element)contextNode).isRootElement() )
                {
                    parent = ((Element)contextNode).getDocument();
                }
            }
        }
        else if ( contextNode instanceof Attribute )
        {
            parent = ((Attribute)contextNode).getParent();
        }
        else if ( contextNode instanceof ProcessingInstruction )
        {
            parent = ((ProcessingInstruction)contextNode).getParent();
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

        return elem.getAttributes().iterator();
    }

    public Object getDocumentNode(Object contextNode)
    {
        Element elem = (Element) contextNode;

        return elem.getDocument();
    }

    public String getElementQName(Object obj)
    {
        Element elem = (Element) obj;

        String prefix = elem.getNamespacePrefix();

        if ( "".equals( prefix ) )
        {
            return elem.getName();
        }

        return prefix + ":" + elem.getName();
    }

    public String getAttributeQName(Object obj)
    {
        Attribute attr = (Attribute) obj;

        String prefix = attr.getNamespacePrefix();

        if ( "".equals( prefix ) )
        {
            return attr.getName();
        }

        return prefix + ":" + attr.getName();
    }

    public String getNamespaceStringValue(Object obj)
    {
        Namespace ns = (Namespace) obj;

        return ns.getURI();
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

        List     content     = elem.getContent();
        Iterator contentIter = content.iterator();
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
                buf.append( ((Text)each).getValue() );
            }
            else if ( each instanceof CDATA )
            {
                buf.append( ((CDATA)each).getText() );
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

        return cmt.getText();
    }
}
