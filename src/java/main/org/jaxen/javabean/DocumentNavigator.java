/*
 $Id$

 Copyright 2003 (C) The Werken Company. All Rights Reserved.
 
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
package org.jaxen.javabean;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Collection;

import org.jaxen.DefaultNavigator;
import org.jaxen.FunctionCallException;
import org.jaxen.NamedAccessNavigator;
import org.jaxen.Navigator;
import org.jaxen.XPath;
import org.jaxen.JaxenConstants;
import org.jaxen.util.SingleObjectIterator;

/** 
 * Interface for navigating around a JavaBean object model.
 *
 * <p>
 * This class is not intended for direct usage, but is
 * used by the Jaxen engine during evaluation.
 * </p>
 *
 * @see XPath
 *
 * @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 */
public class DocumentNavigator
    extends DefaultNavigator
    implements NamedAccessNavigator
{

    /** Empty Class array. */
    private static final Class[] EMPTY_CLASS_ARRAY = new Class[0];

    /** Empty Object array. */
    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

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
    public static Navigator getInstance()
    {
        return Singleton.instance;
    }

    public boolean isElement(Object obj)
    {
        return (obj instanceof Element);
    }

    public boolean isComment(Object obj)
    {
        return false;
    }

    public boolean isText(Object obj)
    {
        return ( obj instanceof String );
    }

    public boolean isAttribute(Object obj)
    {
        return false;
    }

    public boolean isProcessingInstruction(Object obj)
    {
        return false;
    }

    public boolean isDocument(Object obj)
    {
        return false;
    }

    public boolean isNamespace(Object obj)
    {
        return false;
    }

    public String getElementName(Object obj)
    {
        return ((Element)obj).getName();
    }

    public String getElementNamespaceUri(Object obj)
    {
        return "";
    }

    public String getElementQName(Object obj)
    {
        return "";
    }

    public String getAttributeName(Object obj)
    {
        return "";
    }

    public String getAttributeNamespaceUri(Object obj)
    {
        return "";
    }

    public String getAttributeQName(Object obj)
    {
        return "";
    }

    public Iterator getChildAxisIterator(Object contextNode)
    {
        return JaxenConstants.EMPTY_ITERATOR;
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
    public Iterator getChildAxisIterator(Object contextNode,
                                         String localName,
                                         String namespacePrefix,
                                         String namespaceURI)
    {
        Class cls = ((Element)contextNode).getObject().getClass();

        String methodName = javacase( localName );

        Method method = null;
        
        try
        {
            method = cls.getMethod( "get" + methodName, EMPTY_CLASS_ARRAY );
        }
        catch (NoSuchMethodException e)
        {
            try
            {
                method = cls.getMethod( "get" + methodName + "s", EMPTY_CLASS_ARRAY );
            }
            catch (NoSuchMethodException ee)
            {
                try
                {
                    method = cls.getMethod( localName, EMPTY_CLASS_ARRAY );
                }
                catch (NoSuchMethodException eee)
                {
                    method = null;
                }
            }
        }

        if ( method == null )
        {
            return JaxenConstants.EMPTY_ITERATOR;
        }

        try
        {
            Object result = method.invoke( ((Element)contextNode).getObject(), EMPTY_OBJECT_ARRAY );
            
            if ( result == null )
            {
                return JaxenConstants.EMPTY_ITERATOR;
            } 
            
            if ( result instanceof Collection )
            {
                return new ElementIterator( (Element) contextNode, localName, ((Collection)result).iterator() );
            }
            
            if ( result.getClass().isArray() )
            {
                return JaxenConstants.EMPTY_ITERATOR;
            }
            
            return new SingleObjectIterator( new Element( (Element) contextNode, localName, result ) );
        }
        catch (IllegalAccessException e)
        {
            // swallow
        }
        catch (InvocationTargetException e)
        {
            // swallow
        }

        return JaxenConstants.EMPTY_ITERATOR;
    }

    public Iterator getParentAxisIterator(Object contextNode)
    {
        if ( contextNode instanceof Element )
        {
            return new SingleObjectIterator( ((Element)contextNode).getParent() );
        }

        return JaxenConstants.EMPTY_ITERATOR;
    }

    public Iterator getAttributeAxisIterator(Object contextNode)
    {
        return JaxenConstants.EMPTY_ITERATOR;
    }

    /**
     * Retrieves an <code>Iterator</code> over the attribute elements that
     * match the supplied name.
     *
     * @param contextNode  the origin context node
     * @param localName  the local name of the attributes to return, always present
     * @param namespacePrefix  the prefix of the namespace of the attributes to return
     * @param namespaceURI  the uri of the namespace of the attributes to return
     * @return an Iterator that traverses the named attributes, not null
     */
    public Iterator getAttributeAxisIterator(Object contextNode,
                                             String localName,
                                             String namespacePrefix,
                                             String namespaceURI) {
        return JaxenConstants.EMPTY_ITERATOR;
    }
        
    public Iterator getNamespaceAxisIterator(Object contextNode)
    {
        return JaxenConstants.EMPTY_ITERATOR;
    }

    public Object getDocumentNode(Object contextNode)
    {
        return null;
    }

    public Object getParentNode(Object contextNode)
    {
        if ( contextNode instanceof Element )
        {
            return ((Element)contextNode).getParent();
        }

        return JaxenConstants.EMPTY_ITERATOR;
    }

    public String getTextStringValue(Object obj)
    {
        if ( obj instanceof Element )
        {
            return ((Element)obj).getObject().toString();
        }
        return obj.toString();
    }

    public String getElementStringValue(Object obj)
    {
        if ( obj instanceof Element )
        {
            return ((Element)obj).getObject().toString();
        }
        return obj.toString();
    }

    public String getAttributeStringValue(Object obj)
    {
        return obj.toString();
    }

    public String getNamespaceStringValue(Object obj)
    {
        return obj.toString();
    }

    public String getNamespacePrefix(Object obj)
    {
        return null;
    }

    public String getCommentStringValue(Object obj)
    {
        return null;
    }
    
    public String translateNamespacePrefixToUri(String prefix, Object context)
    {
        return null;
    }
    
    public short getNodeType(Object node) 
    {
        return 0;
    }
    
    public Object getDocument(String uri) throws FunctionCallException
    {
        return null;
    }

    public String getProcessingInstructionTarget(Object obj)
    {
        return null;
    }

    public String getProcessingInstructionData(Object obj)
    {
        return null;
    }

    public XPath parseXPath(String xpath)
        throws org.jaxen.saxpath.SAXPathException
    {
        return new JavaBeanXPath( xpath );
    }

    protected String javacase(String name)
    {
        if ( name.length() == 0 )
        {
            return name;
        }
        else if ( name.length() == 1 )
        {
            return name.toUpperCase();
        } 

        return name.substring( 0, 1 ).toUpperCase() + name.substring( 1 );
    }
}
