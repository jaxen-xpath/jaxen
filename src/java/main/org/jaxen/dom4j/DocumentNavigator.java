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



package org.jaxen.dom4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

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
import org.jaxen.DefaultNavigator;
import org.jaxen.FunctionCallException;
import org.jaxen.XPath;
import org.jaxen.util.SingleObjectIterator;

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
    public XPath parseXPath (String xpath) throws org.jaxen.saxpath.SAXPathException
    {
        return new Dom4jXPath(xpath);
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
            throw new FunctionCallException("Failed to parse document for URI: " + uri, e);
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
            reader.setMergeAdjacentText( true );
        }
        return reader;
    }
    
    public void setSAXReader(SAXReader reader)
    {
        this.reader = reader;
    }
    
}
