/*
 * Copyright 2001 (C) bob mcwhirter and James Strachan. All Rights Reserved.
 * 
 * This software is open source. 
 * See the LICENCE.txt that came with this distribution for the licence.
 * 
 * $Id$
 */

package org.jaxen.pattern;

import org.jaxen.Context;
import org.jaxen.Navigator;

/** <p><code>NamespaceTest</code> tests for a given namespace URI.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class NamespaceTest extends NodeTest {
    
    /** The prefix to match against */
    private String prefix;
    
    /** The type of node to match - either attribute or element */
    private short nodeType;
    
    public NamespaceTest(String prefix, short nodeType)   
    {
        if ( prefix == null ) 
        {
            prefix = "";
        }
        this.prefix = prefix;
        this.nodeType = nodeType;
    }
        
    /** @return true if the pattern matches the given node
      */
    public boolean matches( Object node, Context context ) 
    {
        Navigator navigator = context.getNavigator();
        String uri = getURI( node, context );
        
        if ( nodeType == Pattern.ELEMENT_NODE ) 
        {
            return navigator.isElement( node ) 
                && uri.equals( navigator.getElementNamespaceUri( node ) );
        }
        else if ( nodeType == Pattern.ATTRIBUTE_NODE ) 
        {
            return navigator.isAttribute( node )
                && uri.equals( navigator.getAttributeNamespaceUri( node ) );
        }
        return false;
    }
    
    public double getPriority() 
    {
    	return -0.25;
    }


    public short getMatchType() 
    {
        return nodeType;
    }
    
    public String getText() 
    {
        return prefix + ":";
    }
    
    public String toString()
    {
        return super.toString() + "[ prefix: " + prefix + " type: " + nodeType + " ]";
    }
    
    /** Returns the URI of the current prefix or "" if no URI can be found
     */
    protected String getURI(Object node, Context context)
    {
        String uri = context.getNavigator().translateNamespacePrefixToUri( prefix, node );
        if ( uri == null )
        {
            uri = context.getContextSupport().translateNamespacePrefixToUri( prefix );
        }
        if ( uri == null ) 
        {
            uri = "";
        }
        return uri;
    }
}
