/*
 * Copyright 2001 (C) bob mcwhirter and James Strachan. All Rights Reserved.
 * 
 * This software is open source. 
 * See the LICENCE.txt that came with this distribution for the licence.
 * 
 * $Id$
 */

package org.jaxen.pattern;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jaxen.Context;
import org.jaxen.JaxenException;
import org.jaxen.Navigator;
import org.jaxen.expr.FilterExpr;

/** <p><code>LocationPathPattern</code> matches any node using a
  * location path such as A/B/C.
  * The parentPattern and ancestorPattern properties are used to
  * chain location path patterns together</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class LocationPathPattern extends Pattern {

    /** The node test to perform on this step of the path */
    private NodeTest nodeTest = AnyNodeTest.getInstance();
    
    /** Patterns matching my parent node */
    private Pattern parentPattern;
    
    /** Patterns matching one of my ancestors */
    private Pattern ancestorPattern;
        
    /** The filters to match against */
    private List filters;
    
    
    public LocationPathPattern()   
    {
    }

    /** Adds a filter to this pattern
     */
    public void addFilter(FilterExpr filter) 
    {
        if ( filters == null )
        {
            filters = new ArrayList();
        }
        filters.add( filter );
    }
    
    /** Adds a pattern for the parent of the current
     * context node used in this pattern.
     */
    public void setParentPattern(Pattern parentPattern) 
    {
        this.parentPattern = parentPattern;
    }
    
    /** Adds a pattern for an ancestor of the current
     * context node used in this pattern.
     */
    public void setAncestorPattern(Pattern ancestorPattern) 
    {
        this.ancestorPattern = ancestorPattern;
    }
    
    
    /** @return true if the pattern matches the given node
      */
    public boolean matches( Object node, Context context ) throws JaxenException
    {
        Navigator navigator = context.getNavigator();
        
        if (! nodeTest.matches(node, context) )
        {
            return false;
        }
        
        if (parentPattern != null) 
        {
            Object parent = navigator.getParentNode( node );
            if ( parent == null ) 
            {
                return false;
            }
            if ( ! parentPattern.matches( parent, context ) ) 
            {
                return false;
            }
        }

        if (ancestorPattern != null) {
            Object ancestor = navigator.getParentNode( node );
            while (true)
            {
                if ( ancestorPattern.matches( ancestor, context ) )
                {
                    break;
                }
                if ( ancestor == null )
                {
                    return false;
                }
                ancestor = navigator.getParentNode( ancestor );
            }
        }
        
        if (filters != null) 
        {
            // XXXX: filters aren't positional, so should we clone context?
            for (Iterator iter = filters.iterator(); iter.hasNext(); ) {
                FilterExpr filter = (FilterExpr) iter.next();
                if ( ! filter.asBoolean( context ) )
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    public double getPriority() 
    {
        return 0.0;
    }


    public short getMatchType() 
    {
        return nodeTest.getMatchType();
    }
}
