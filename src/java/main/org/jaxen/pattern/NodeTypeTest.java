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

/** <p><code>ElementTypeTest</code> matches if the node is an element.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class NodeTypeTest extends Pattern {
    
    private short matchesNodeType;
    
    public NodeTypeTest(short matchesNodeType)   
    {
        this.matchesNodeType = matchesNodeType;
    }
        
    /** @return true if the pattern matches the given node
      */
    public boolean matches( Object node, Context context ) 
    {
        return matchesNodeType == context.getNavigator().getNodeType( node );
    }
    
    public double getPriority() 
    {
        return -0.5;
    }


    public short getMatchType() 
    {
        return matchesNodeType;
    }
}
