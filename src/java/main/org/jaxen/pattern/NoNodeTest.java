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

/** <p><code>NoNodeTest</code> matches no nodes.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class NoNodeTest extends Pattern {
    
    private static NoNodeTest instance = new NoNodeTest();
    
    public static NoNodeTest getInstance() 
    {
        return instance;
    }
    
    public NoNodeTest() 
    {
    }
        
    /** @return true if the pattern matches the given node
      */
    public boolean matches( Object node, Context context ) 
    {
        return false;
    }
    
    public double getPriority() 
    {
        return -0.5;
    }

    public short getMatchType() 
    {
        return NO_NODE;
    }

}
