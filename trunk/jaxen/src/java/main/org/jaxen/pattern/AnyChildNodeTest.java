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

/** <p><code>AnyChildNodeTest</code> matches any child node.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class AnyChildNodeTest extends NodeTest {

    private static AnyChildNodeTest instance = new AnyChildNodeTest();
    
    public static AnyChildNodeTest getInstance() 
    {
        return instance;
    }
    
    public AnyChildNodeTest() 
    {
    }
    
    /** @return true if the pattern matches the given node
      */
    public boolean matches( Object node, Context context ) 
    {
        short type = context.getNavigator().getNodeType( node );
        return type == ELEMENT_NODE || type == TEXT_NODE
            || type == COMMENT_NODE || type == PROCESSING_INSTRUCTION_NODE;
    }
    
    public double getPriority() 
    {
        return -0.5;
    }

    public short getMatchType() 
    {
        return ANY_NODE;
    }
    
    public String getText() 
    {
        return "*";
    }
    
}
