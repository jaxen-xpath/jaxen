/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright (C) 2000-2002 bob mcwhirter & James Strachan.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions, and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions, and the disclaimer that follows 
 *    these conditions in the documentation and/or other materials 
 *    provided with the distribution.
 *
 * 3. The name "Jaxen" must not be used to endorse or promote products
 *    derived from this software without prior written permission.  For
 *    written permission, please contact license@jaxen.org.
 * 
 * 4. Products derived from this software may not be called "Jaxen", nor
 *    may "Jaxen" appear in their name, without prior written permission
 *    from the Jaxen Project Management (pm@jaxen.org).
 * 
 * In addition, we request (but do not require) that you include in the 
 * end-user documentation provided with the redistribution and/or in the 
 * software itself an acknowledgement equivalent to the following:
 *     "This product includes software developed by the
 *      Jaxen Project (http://www.jaxen.org/)."
 * Alternatively, the acknowledgment may be graphical using the logos 
 * available at http://www.jaxen.org/
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE Jaxen AUTHORS OR THE PROJECT
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 * ====================================================================
 * This software consists of voluntary contributions made by many 
 * individuals on behalf of the Jaxen Project and was originally 
 * created by bob mcwhirter <bob@werken.com> and 
 * James Strachan <jstrachan@apache.org>.  For more information on the 
 * Jaxen Project, please see <http://www.jaxen.org/>.
 * 
 * $Id$
 */


package org.jaxen.exml;

import org.jaxen.XPath;
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

import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import java.io.File;

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
            Map nsMap = new HashMap();

            Element current = elem;

            while ( current != null ) {
                Dictionary namespaces = current.getNamespaces();

                if ( namespaces != null ) {
                    Enumeration keys = namespaces.keys();
                    
                    while ( keys.hasMoreElements() ) {
                        
                        String prefix = (String)keys.nextElement();
                        if ( !nsMap.containsKey(prefix) ) {

                            String uri = (String)namespaces.get(prefix);
                            nsMap.put( prefix,
                                       new Namespace(elem, prefix, uri) );
                        }
                    }
                }
                Parent parent = current.getParent();
                if ( parent instanceof Element )
                    current = (Element)parent;
                else
                    break;
            }

            Namespace xml =
                new Namespace( elem, "xml",
                               "http://www.w3.org/XML/1998/namespace" );
            nsMap.put( "xml", xml );

            return nsMap.values().iterator();
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
        else if ( contextNode instanceof Attribute )
        {
            parent = ((Attribute)contextNode).getElement();
        }
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
        else if ( contextNode instanceof Namespace )
        {
            parent = ((Namespace)contextNode).getElement();
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
    public XPath parseXPath (String xpath) throws SAXPathException
    {
        return new ElectricXPath(xpath);
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

    public String getNamespacePrefix(Object obj)
    {
        Namespace ns = (Namespace) obj;
        return ns.getPrefix();
    }

    public String getNamespaceStringValue(Object obj)
    {
        Namespace ns = (Namespace) obj;
        return ns.getURI();
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
