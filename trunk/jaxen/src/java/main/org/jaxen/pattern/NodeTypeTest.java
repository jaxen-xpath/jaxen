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

/** <p><code>ElementTypeTest</code> matches if the node is of a certain type 
  * such as element, attribute, comment, text, processing instruction and so forth.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class NodeTypeTest extends NodeTest {
    
    public static final NodeTypeTest DOCUMENT_TEST 
        = new NodeTypeTest( DOCUMENT_NODE );
    
    public static final NodeTypeTest ELEMENT_TEST 
        = new NodeTypeTest( ELEMENT_NODE );
    
    public static final NodeTypeTest ATTRIBUTE_TEST 
        = new NodeTypeTest( ATTRIBUTE_NODE );
    
    public static final NodeTypeTest COMMENT_TEST 
        = new NodeTypeTest( COMMENT_NODE );
    
    public static final NodeTypeTest TEXT_TEST 
        = new NodeTypeTest( TEXT_NODE );
    
    public static final NodeTypeTest PROCESSING_INSTRUCTION_TEST 
        = new NodeTypeTest( PROCESSING_INSTRUCTION_NODE );
    
    public static final NodeTypeTest NAMESPACE_TEST 
        = new NodeTypeTest( NAMESPACE_NODE );
    
    
    private short nodeType;
    
    public NodeTypeTest(short nodeType)   
    {
        this.nodeType = nodeType;
    }
        
    /** @return true if the pattern matches the given node
      */
    public boolean matches( Object node, Context context ) 
    {
        return nodeType == context.getNavigator().getNodeType( node );
    }
    
    public double getPriority() 
    {
        return -0.5;
    }


    public short getMatchType() 
    {
        return nodeType;
    }
    
    public String getText() 
    {
        switch (nodeType) 
        {
            case ELEMENT_NODE:
                return "child()";
            case ATTRIBUTE_NODE:
                return "@*";
            case NAMESPACE_NODE:
                return "namespace()";
            case DOCUMENT_NODE:
                return "/";
            case COMMENT_NODE:
                return "comment()";
            case TEXT_NODE:
                return "text()";
            case PROCESSING_INSTRUCTION_NODE:
                return "processing-instruction()";
        }
        return "";
    }
    
    public String toString()
    {
        return super.toString() + "[ type: " + nodeType + " ]";
    }
}
