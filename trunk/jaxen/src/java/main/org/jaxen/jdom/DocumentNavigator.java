/*
 $Id$

 Copyright 2003-2004 (C) The Werken Company. All Rights Reserved.
 
 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.
 
 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.
 
 3. The name "jaxen" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Werken Company.  For written permission,
    please contact bob@werken.com.
 
 4. Products derived from this Software may not be called "jaxen"
    nor may "jaxen" appear in their names without prior written
    permission of The Werken Company. "jaxen" is a registered
    trademark of The Werken Company.
 
 5. Due credit should be given to The Werken Company.
    (http://jaxen.werken.com/).
 
 THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.

 */
package org.jaxen.jdom;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jaxen.DefaultNavigator;
import org.jaxen.FunctionCallException;
import org.jaxen.NamedAccessNavigator;
import org.jaxen.Navigator;
import org.jaxen.XPath;
import org.jaxen.saxpath.SAXPathException;
import org.jaxen.util.SingleObjectIterator;
import org.jdom.Attribute;
import org.jdom.CDATA;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.ProcessingInstruction;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;

/** 
 * Interface for navigating around the JDOM object model.
 *
 * <p>
 * This class is not intended for direct usage, but is
 * used by the Jaxen engine during evaluation.
 * </p>
 *
 * @see XPath
 *
 * @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 * @author Stephen Colebourne
 */
public class DocumentNavigator extends DefaultNavigator implements NamedAccessNavigator
{
    /** Singleton implementation.
     */
    private static class Singleton
    {
        /** Singleton instance.
         */
        private static DocumentNavigator instance = new DocumentNavigator();
    }

    public static Navigator getInstance()
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
        return obj instanceof Namespace || obj instanceof XPathNamespace;
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

    /**
     * Retrieves an <code>Iterator</code> over the child elements that
     * match the supplied name.
     *
     * @param contextNode  the origin context node
     * @param localName  the local name of the children to return, always present
     * @param namespacePrefix  the prefix of the namespace of the children to return
     * @param namespaceURI  the uri of the namespace of the children to return
     * @return an Iterator that traverses the named children, or null if none
     */
    public Iterator getChildAxisIterator(
            Object contextNode, String localName, String namespacePrefix, String namespaceURI) {

        if ( contextNode instanceof Element ) {
            Element node = (Element) contextNode;
            if (namespaceURI == null) {
                return node.getChildren(localName).iterator();
            }
            return node.getChildren(localName, Namespace.getNamespace(namespacePrefix, namespaceURI)).iterator();
        }
        if ( contextNode instanceof Document ) {
            Document node = (Document) contextNode;
            
            Element el = node.getRootElement();
            if (el.getName().equals(localName) == false) {
                return null;
            }
            if (namespaceURI != null) {
                if (Namespace.getNamespace(namespacePrefix, namespaceURI).equals(el.getNamespace()) == false) {
                    return null;
                }
            }
            return new SingleObjectIterator(el);
        }

        return null;
    }
    
    public Iterator getNamespaceAxisIterator(Object contextNode)
    {
        if ( ! ( contextNode instanceof Element ) )
        {
            return null;
        }

        Element elem = (Element) contextNode;

        Map nsMap = new HashMap();

        Element current = elem;

        while ( current != null ) {
        
            Namespace ns = current.getNamespace();
            
            if ( ns != Namespace.NO_NAMESPACE ) {
                if ( !nsMap.containsKey(ns.getPrefix()) )
                    nsMap.put( ns.getPrefix(), new XPathNamespace(elem, ns) );
            }
        
            Iterator additional = current.getAdditionalNamespaces().iterator();

            while ( additional.hasNext() ) {

                ns = (Namespace)additional.next();
                if ( !nsMap.containsKey(ns.getPrefix()) )
                    nsMap.put( ns.getPrefix(), new XPathNamespace(elem, ns) );
            }

            if (current.getParent() instanceof Element) {
                current = (Element)current.getParent();
            } else {
                current = null;
            }
        }

        nsMap.put( "xml", new XPathNamespace(elem, Namespace.XML_NAMESPACE) );

        return nsMap.values().iterator();
    }

    public Iterator getParentAxisIterator(Object contextNode)
    {
        Object parent = null;

        if ( contextNode instanceof Document )
        {
            return null;
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
        else if ( contextNode instanceof XPathNamespace )
        {
            parent = ((XPathNamespace)contextNode).getJDOMElement();
        }
        else if ( contextNode instanceof ProcessingInstruction )
        {
            parent = ((ProcessingInstruction)contextNode).getParent();
        }
        else if ( contextNode instanceof Comment )
        {
            parent = ((Comment)contextNode).getParent();
        }
        else if ( contextNode instanceof Text )
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

    /**
     * Retrieves an <code>Iterator</code> over the attribute elements that
     * match the supplied name.
     *
     * @param contextNode  the origin context node
     * @param localName  the local name of the attributes to return, always present
     * @param namespacePrefix  the prefix of the namespace of the attributes to return
     * @param namespaceURI  the uri of the namespace of the attributes to return
     * @return an Iterator that traverses the named attributes, or null if none
     */
    public Iterator getAttributeAxisIterator(
            Object contextNode, String localName, String namespacePrefix, String namespaceURI) {

        if ( contextNode instanceof Element ) {
            Element node = (Element) contextNode;
            Namespace namespace = (namespaceURI == null ? Namespace.NO_NAMESPACE : 
                                    Namespace.getNamespace(namespacePrefix, namespaceURI));
            Attribute attr = node.getAttribute(localName, namespace);
            if (attr != null) {
                return new SingleObjectIterator(attr);
            }
        }
        return null;
    }

    /** Returns a parsed form of the given xpath string, which will be suitable
     *  for queries on JDOM documents.
     */
    public XPath parseXPath (String xpath) throws SAXPathException
    {
        return new JDOMXPath(xpath);
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

        String prefix = elem.getNamespacePrefix();

        if ( prefix == null || prefix.length() == 0 )
        {
            return elem.getName();
        }

        return prefix + ":" + elem.getName();
    }

    public String getAttributeQName(Object obj)
    {
        Attribute attr = (Attribute) obj;

        String prefix = attr.getNamespacePrefix();

        if ( prefix == null || "".equals( prefix ) )
        {
            return attr.getName();
        }

        return prefix + ":" + attr.getName();
    }

    public String getNamespaceStringValue(Object obj)
    {
        if (obj instanceof Namespace) {

            Namespace ns = (Namespace) obj;
            return ns.getURI();
        } else {

            XPathNamespace ns = (XPathNamespace) obj;
            return ns.getJDOMNamespace().getURI();
        }
        
    }

    public String getNamespacePrefix(Object obj)
    {
        if (obj instanceof Namespace) {

            Namespace ns = (Namespace) obj;
            return ns.getPrefix();
        } else {

            XPathNamespace ns = (XPathNamespace) obj;
            return ns.getJDOMNamespace().getPrefix();
        }
    }

    public String getTextStringValue(Object obj)
    {
        if ( obj instanceof Text )
        {
            return ((Text)obj).getText();
        }

        if ( obj instanceof CDATA )
        {
            return ((CDATA)obj).getText();
        }

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

        List     content     = elem.getContent();
        Iterator contentIter = content.iterator();
        Object   each        = null;

        while ( contentIter.hasNext() )
        {
            each = contentIter.next();

            if ( each instanceof Text )
            {
                buf.append( ((Text)each).getText() );
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
        else if ( context instanceof Text )
        {
            element = (Element)((Text)context).getParent();
        }
        else if ( context instanceof Attribute )
        {
            element = ((Attribute)context).getParent();
        }
        else if ( context instanceof XPathNamespace )
        {
            element = ((XPathNamespace)context).getJDOMElement();
        }
        else if ( context instanceof Comment )
        {
            element = (Element)((Comment)context).getParent();
        }
        else if ( context instanceof ProcessingInstruction )
        {
            element = (Element)((ProcessingInstruction)context).getParent();
        }

        if ( element != null )
        {
            Namespace namespace = element.getNamespace( prefix );

            if ( namespace != null ) 
            {
                return namespace.getURI();
            }
        }
        return null;
    }

    public Object getDocument(String url) throws FunctionCallException
    {
        try
        {
            SAXBuilder builder = new SAXBuilder();
            
            return builder.build( url );
        }
        catch (Exception e)
        {
            throw new FunctionCallException( e.getMessage() );
        }
    }
}
