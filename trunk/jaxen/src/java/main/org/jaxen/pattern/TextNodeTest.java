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

/** <p><code>TextNodeTest</code> matches any text node.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class TextNodeTest extends NodeTest {
    
    public static final TextNodeTest SINGLETON = new TextNodeTest();
    
    public TextNodeTest()   
    {
    }
        
    /** @return true if the pattern matches the given node
      */
    public boolean matches( Object node, Context context ) 
    {
        return context.getNavigator().isText( node );
    }
    
    public double getPriority() 
    {
        return -0.5;
    }

    public String getText() 
    {
        return "text()";
    }
}
