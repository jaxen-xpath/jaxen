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
import org.jaxen.JaxenException;

/** <p><code>UnionPattern</code> represents a union pattern.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class UnionPattern extends Pattern {

    private Pattern lhs;
    private Pattern rhs;
    private short nodeType = ANY_NODE;
    private String matchesNodeName = null;
    
    
    public UnionPattern() 
    {
    }
    
    public UnionPattern(Pattern lhs, Pattern rhs) 
    {
        this.lhs = lhs;
        this.rhs = rhs;
        init();
    }
    
    
    public Pattern getLHS() 
    {
        return lhs;
    }
    
    public void setLHS(Pattern lhs) 
    {
        this.lhs = lhs;
        init();
    }
    
    public Pattern getRHS() 
    {
        return rhs;
    }
    
    public void setRHS(Pattern rhs) 
    {
        this.rhs = rhs;
        init();
    }
    
    
    // Pattern interface
    //-------------------------------------------------------------------------    
    
    /** @return true if the pattern matches the given node
      */
    public boolean matches( Object node, Context context ) throws JaxenException
    {
        return lhs.matches( node, context ) || rhs.matches( node, context );
    }
    
    public Pattern[] getUnionPatterns() 
    {
        return new Pattern[] { lhs, rhs };
    }

    
    public short getMatchType() 
    {
        return nodeType;
    }


    public String getMatchesNodeName() 
    {
        return matchesNodeName;
    }
    
    
    public Pattern simplify() 
    {
        this.lhs = lhs.simplify();
        this.rhs = lhs.simplify();
        init();
        return this;
    }
    
    
    // Implementation methods
    //-------------------------------------------------------------------------    
    private void init() 
    {
        short type1 = lhs.getMatchType();
        short type2 = rhs.getMatchType();
        this.nodeType = ( type1 == type2 ) ? type1 : ANY_NODE;
        
        String name1 = lhs.getMatchesNodeName();
        String name2 = rhs.getMatchesNodeName();
        
        this.matchesNodeName = null;
        if ( name1 != null && name2 != null && name1.equals( name2 ) ) 
        {
            this.matchesNodeName = name1;
        }
    }    
}
