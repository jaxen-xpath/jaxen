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
import java.util.Collections;
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

    /** Whether this lcoation path is absolute or not */
    private boolean absolute;
    
    
    public LocationPathPattern()   
    {
    }

    public LocationPathPattern(NodeTest nodeTest)   
    {
        this.nodeTest = nodeTest;
    }

    public Pattern simplify()
    {
        if ( parentPattern != null )
        {
            parentPattern = parentPattern.simplify();
        }
        if ( ancestorPattern != null )
        {
            ancestorPattern = ancestorPattern.simplify();
        }
        if ( parentPattern == null && ancestorPattern == null && filters == null )
        {
            return nodeTest;
        }
        return this;
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
    
    /** Allows the NodeTest to be set
     */
    public void setNodeTest(NodeTest nodeTest) throws JaxenException
    {
        if ( this.nodeTest instanceof AnyNodeTest )
        {
            this.nodeTest = nodeTest;
        }   
        else 
        {
            throw new JaxenException( "Attempt to overwrite nodeTest: " + this.nodeTest + " with: " + nodeTest );
        }
    }
    
    /** @return true if the pattern matches the given node
      */
    public boolean matches( Object node, Context context ) throws JaxenException
    {
        Navigator navigator = context.getNavigator();

/*        
        if ( isAbsolute() )
        {
            node = navigator.getDocumentNode( node );
        }
*/
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
                if ( navigator.isDocument( ancestor ) )
                {
                    return false;
                }
                ancestor = navigator.getParentNode( ancestor );
            }
        }
        
        if (filters != null) 
        {
            context.setNodeSet( Collections.singletonList( node ) );
            
            // XXXX: filters aren't positional, so should we clone context?
            boolean answer = true;
            for (Iterator iter = filters.iterator(); iter.hasNext(); ) 
            {
                FilterExpr filter = (FilterExpr) iter.next();
                if ( ! filter.asBoolean( context ) )
                {
                    answer = false;
                    break;
                }
            }
            // restore context
            context.setNodeSet( Collections.singletonList( node ) );
            return answer;
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
    
    public String getText() 
    {
        StringBuffer buffer = new StringBuffer();
        if ( absolute )
        {
            buffer.append( "/" );
        }
        if (ancestorPattern != null) 
        {
            String text = ancestorPattern.getText();
            if ( text.length() > 0 )
            {
                buffer.append( text );
                buffer.append( "//" );
            }
        }
        if (parentPattern != null) 
        {
            String text = parentPattern.getText();
            if ( text.length() > 0 )
            {
                buffer.append( text );
                buffer.append( "/" );
            }
        }
        buffer.append( nodeTest.getText() );
        
        if ( filters != null ) 
        {
            buffer.append( "[" );
            for (Iterator iter = filters.iterator(); iter.hasNext(); ) 
            {
                FilterExpr filter = (FilterExpr) iter.next();
                buffer.append( filter.getText() );
            }
            buffer.append( "]" );
        }        
        return buffer.toString();
    }
    
    public String toString()
    {
        return super.toString() + "[ absolute: " + absolute + " parent: " + parentPattern + " ancestor: " 
            + ancestorPattern + " filters: " + filters + " nodeTest: " 
            + nodeTest + " ]";
    }
    
    public boolean isAbsolute()
    {
        return absolute;
    }
    
    public void setAbsolute(boolean absolute)
    {
        this.absolute = absolute;
    }
    
    public boolean hasAnyNodeTest()
    {
        return nodeTest instanceof AnyNodeTest;
    }
        
}
