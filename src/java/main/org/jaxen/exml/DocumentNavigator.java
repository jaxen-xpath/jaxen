
package org.jaxen.exml;

import org.jaxen.BaseXPath;
import org.jaxen.DefaultNavigator;
import org.jaxen.FunctionCallException;

import org.jaxen.util.SingleObjectIterator;

import org.saxpath.SAXPathException;

import electric.xml.Document;
import electric.xml.Element;
import electric.xml.Comment;
import electric.xml.Attribute;
import electric.xml.Text;
import electric.xml.CData;
import electric.xml.Instruction;
import electric.xml.Child;
import electric.xml.Children;
import electric.xml.Parent;
import electric.xml.Attributes;
import electric.xml.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import java.io.File;

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
        if ( contextNode instanceof Parent )
        {
            return new ChildrenIterator( ((Parent)contextNode).getChildren() );
        }

        return null;
    }

    public Iterator getNamespaceAxisIterator(Object contextNode)
    {
        if ( contextNode instanceof Element )
        {
            Element elem = (Element) contextNode;

            return new NamespaceIterator( elem.getAttributes() );
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

    /** Returns a parsed form of the given xpath string, which will be suitable
     *  for queries on EXML documents.
     */
    public BaseXPath parseXPath (String xpath) throws SAXPathException
    {
        return new XPath(xpath);
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

    public String getTextStringValue(Object obj)
    {
        Text txt = (Text) obj;

        return txt.getString();
    }

    public String getAttributeStringValue(Object obj)
    {
        Attribute attr = (Attribute) obj;

        return attr.getValue();
    }

    public String getElementStringValue(Object obj)
    {
        Element elem = (Element) obj;

        return getStringValue( elem );
    }

    public String getProcessingInstructionTarget(Object obj)
    {
        Instruction pi = (Instruction) obj;

        return pi.getTarget();
    }

    public String getProcessingInstructionData(Object obj)
    {
        Instruction pi = (Instruction) obj;

        return pi.getContent();
    }

    private String getStringValue(Element e)
    {
        StringBuffer buf = new StringBuffer();

        Children children = e.getChildren();
        Child    eachChild = null;

        while ( ( eachChild = children.next() ) != null )
        {
            if ( eachChild instanceof Element )
            {
                buf.append( getStringValue( (Element) eachChild ) );
            }
            else if ( eachChild instanceof Text )
            {
                buf.append( ((Text)eachChild).getString() );
            }
        }
        
        return buf.toString();
    }

    public String getCommentStringValue(Object obj)
    {
        Comment cmt = (Comment) obj;

        return cmt.getString();
    }

    public String translateNamespacePrefixToUri(String prefix, Object context)
    {
        Element element = null;

        if ( context instanceof Element ) 
        {
            element = (Element) context;
        }

        if ( element != null )
        {
            return element.getNamespace( prefix );
        }

        return null;
    }

    public Object getDocument(String url) throws FunctionCallException
    {
        try
        {
            return new Document( new File( url ) );
        }
        catch (ParseException e)
        {
            throw new FunctionCallException( e.getMessage() );
        }
    }
}
