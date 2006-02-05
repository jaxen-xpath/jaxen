/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright 2000-2003 bob mcwhirter & James Strachan.
 * All rights reserved.
 *
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 * 
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 * 
 *   * Neither the name of the Jaxen Project nor the names of its
 *     contributors may be used to endorse or promote products derived 
 *     from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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


package org.jaxen.xom;


import nu.xom.Attribute;
import nu.xom.Comment;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ProcessingInstruction;
import nu.xom.Text;
import nu.xom.Node;
import nu.xom.Builder;
import nu.xom.NodeFactory;
import nu.xom.ParentNode;

import org.jaxen.XPath;
import org.jaxen.UnsupportedAxisException;
import org.jaxen.FunctionCallException;
import org.jaxen.BaseXPath;
import org.jaxen.JaxenConstants;
import org.jaxen.util.SingleObjectIterator;

import org.jaxen.saxpath.SAXPathException;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

/**
 * Interface for navigating around the XOM object model.
 *
 * <p>
 * This class is not intended for direct usage, but is
 * used by the Jaxen engine during evaluation.
 * </p>
 *
 * @see XPath
 *
 */
public class DocumentNavigator extends org.jaxen.DefaultNavigator
{
    public boolean isAttribute(Object o) {
        return o instanceof Attribute;
    }

    public boolean isComment(Object o) {
        return o instanceof Comment;
    }

    public boolean isDocument(Object o) {
        return o instanceof Document;
    }

    public boolean isElement(Object o) {
        return o instanceof Element;
    }

    public boolean isNamespace(Object o) {
        return o instanceof XPathNamespace;
    }

    public boolean isProcessingInstruction(Object o) {
        return o instanceof ProcessingInstruction;
    }

    public boolean isText(Object o) {
        return o instanceof Text;
    }

    //
    
    public String getAttributeName(Object o) {
        return (isAttribute(o) ? ((Attribute)o).getLocalName() : null);
    }

    public String getAttributeNamespaceUri(Object o) {
        return (isAttribute(o) ? ((Attribute)o).getNamespaceURI() : null);
    }

    public String getAttributeQName(Object o) {
        return (isAttribute(o) ? ((Attribute)o).getQualifiedName() : null);
    }

    public String getAttributeStringValue(Object o) {
        return (isAttribute(o) ? ((Attribute)o).getValue() : null);
    }

    //
    
    public String getCommentStringValue(Object o) {
        return (isComment(o) ? ((Comment)o).getValue() : null);
    }

    public String getElementName(Object o) {
        return (isElement(o) ? ((Element)o).getLocalName() : null);
    }

    public String getElementNamespaceUri(Object o) {
        return (isElement(o) ? ((Element)o).getNamespaceURI() : null);
    }

    public String getElementQName(Object o) {
        return (isElement(o) ? ((Element)o).getQualifiedName() : null);
    }

    public String getElementStringValue(Object o) {
        return (o instanceof Node ? ((Node)o).getValue() : null);
    }

    //
    
    public String getNamespacePrefix(Object o) {
        if (isElement(o)) {
            return ((Element)o).getNamespacePrefix();
        } else if (isAttribute(o)) {
            return ((Attribute)o).getNamespacePrefix();
        } else if (o instanceof XPathNamespace) {
            return ((XPathNamespace)o).getNamespacePrefix();
        }
        return null;
    }

    public String getNamespaceStringValue(Object o) {
        if (isElement(o)) {
            return ((Element)o).getNamespaceURI();
        } else if (isAttribute(o)) {
            return ((Attribute)o).getNamespaceURI();
        } else if (o instanceof XPathNamespace) {
            return ((XPathNamespace)o).getNamespaceURI();
        }
        return null;
    }

    //
    
    public String getTextStringValue(Object o) {
        return (o instanceof Text ? ((Text)o).getValue() : null);
    }
    
    //

    public Object getDocument(String s) throws FunctionCallException {
        try {
            return new Builder(new NodeFactory()).build(s);
        } catch (Exception pe) {
            throw new FunctionCallException(pe);
        }
    }

    public Object getDocumentNode(Object o) {
        ParentNode parent = null;
        if (o instanceof ParentNode) {
            parent = (ParentNode)o;
        } else if (o instanceof Node) {
            parent = ((Node)o).getParent();
        }
        return parent.getDocument();
    }

    //
    
    private abstract static class IndexIterator implements Iterator {
        private Object o = null;
        private int pos = 0, end = -1;
        public IndexIterator(Object o, int pos, int end) {
            this.o = o;
            this.pos = pos;
            this.end = end;
        }
        public boolean hasNext() {
            return pos < end;
        }
        public abstract Object get(Object o, int i); 
        
        public Object next() {
            return get(o, pos++);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
    //
    
    public Iterator getAttributeAxisIterator(Object o) {
        if (isElement(o)) {
            return new IndexIterator(o, 0, ((Element)o).getAttributeCount()) {
                public Object get(Object o, int i) {
                    return ((Element)o).getAttribute(i);
                }
            };
        }
        return JaxenConstants.EMPTY_ITERATOR;
    }

    public Iterator getChildAxisIterator(Object o) {
        if (isElement(o) || (o instanceof Document)) {
            return new IndexIterator(o, 0, ((ParentNode)o).getChildCount()) {
                public Object get(Object o, int i) {
                    return ((ParentNode)o).getChild(i);
                }
            };
        }
        return JaxenConstants.EMPTY_ITERATOR;
    }

    //

    public Iterator getParentAxisIterator(Object o) {
        Object parent = null;
        if (o instanceof Node) {
            parent = ((Node)o).getParent();
        } else if (isNamespace(o)) {
            parent = ((XPathNamespace)o).getElement();
        }
        return (parent != null ? new SingleObjectIterator(parent) : null);
    }

    public Object getParentNode(Object o)  {
        return (o instanceof Node ? ((Node)o).getParent() : null);
    }

    //

    public Iterator getPrecedingAxisIterator(Object o) throws UnsupportedAxisException {
        return super.getPrecedingAxisIterator(o);
    }

    public Iterator getPrecedingSiblingAxisIterator(Object o) throws UnsupportedAxisException {
        return super.getPrecedingSiblingAxisIterator(o);
    }
    
    //

    public String getProcessingInstructionData(Object o) {
        return (o instanceof ProcessingInstruction ? ((ProcessingInstruction)o).getValue() : null);
    }

    public String getProcessingInstructionTarget(Object o) {
        return (o instanceof ProcessingInstruction ? ((ProcessingInstruction)o).getTarget() : null);
    }

    //

    public String translateNamespacePrefixToUri(String s, Object o) {
        Element element = null;
        if (o instanceof Element) {
            element = (Element) o;
        } else if (o instanceof ParentNode) {
        }
        else if (o instanceof Node) {
            element = (Element)((Node)o).getParent();
        }
        else if (o instanceof XPathNamespace)
        {
            element = ((XPathNamespace)o).getElement();
        }
        if (element != null) {
            return element.getNamespaceURI(s);
        }
        return null;
    }

    //
    
    public XPath parseXPath(String s) throws SAXPathException {
        return new BaseXPath(s, this);
    }

    //
    
    /** Wrapper for XOM namespace nodes to give them a parent,
     * as required by the XPath data model.
     *
     *  @author Erwin Bolwidt
     */
    private static class XPathNamespace
    {
        private Element element;

        private String uri, prefix;

        public XPathNamespace(Element elt, String uri, String prefix)
        {
            element = elt;
            this.uri = uri;
            this.prefix = prefix;
        }

        /** Returns the XOM element from which this namespace node has been 
         *  retrieved. The result may be null when the namespace node has not yet
         *  been assigned to an element.
         */
        public Element getElement()
        {
            return element;
        }

        public String getNamespaceURI()
        {
            return uri;
        }

        public String getNamespacePrefix()
        {
            return prefix;
        }

        public String toString()
        {
            return ( "[xmlns:" + prefix + "=\"" +
                    uri + "\", element=" +
                    element.getLocalName() + "]" );
        }
    }

    //
    
    private boolean addNamespaceForElement(Element elt, String uri, String prefix, Map map)
    {
        if (uri != null && uri.length() > 0 && (! map.containsKey(prefix))) {
            map.put(prefix, new XPathNamespace(elt, uri, prefix));
            return true;
        }
        return false;
    }
    
    public Iterator getNamespaceAxisIterator(Object o)
    {
        if (! isElement(o)) {
            return JaxenConstants.EMPTY_ITERATOR;
        }
        Map nsMap = new HashMap();
        Element elt = (Element)o;
        ParentNode parent = elt;
        
        while (parent instanceof Element) {
            elt = (Element)parent;
            String uri    = elt.getNamespaceURI();
            String prefix = elt.getNamespacePrefix();
            addNamespaceForElement(elt, uri, prefix, nsMap);
            int count = elt.getNamespaceDeclarationCount();
            for (int i = 0; i < count; i++) {
                prefix = elt.getNamespacePrefix(i);
                uri    = elt.getNamespaceURI(prefix);
                addNamespaceForElement(elt, uri, prefix, nsMap);
            }
            parent = elt.getParent();
        }
        addNamespaceForElement(elt, "http://www.w3.org/XML/1998/namespace", "xml", nsMap);

        return nsMap.values().iterator();
    }
}
