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
}
