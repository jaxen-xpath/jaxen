/*
 * Copyright 2000-2002 bob mcwhirter & James Strachan.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 * 
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 * 
 *   * Neither the name of the Jaxen Project nor the names of its
 *     contributors may be used to endorse or promote products derived 
 *     from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * ====================================================================
 * This software consists of voluntary contributions made by many 
 * individuals on behalf of the Jaxen Project and was originally 
 * created by bob mcwhirter <bob@werken.com> and 
 * James Strachan <jstrachan@apache.org>.  For more information on the 
 * Jaxen Project, please see <https://github.com/jaxen-xpath/jaxen/>.
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
        if ( ancestorPattern != null )
        {
            ancestorPattern = ancestorPattern.simplify();
        }

        List<LocationPathPattern> chain = new ArrayList<LocationPathPattern>();
        chain.add(this);
        LocationPathPattern p = this;
        while (p.parentPattern instanceof LocationPathPattern)
        {
            p = (LocationPathPattern) p.parentPattern;
            if (p.ancestorPattern != null)
            {
                p.ancestorPattern = p.ancestorPattern.simplify();
            }
            chain.add(p);
        }

        Pattern result = chain.get(chain.size() - 1).parentPattern;
        if (result != null)
        {
            result = result.simplify();
        }

        for (int i = chain.size() - 1; i >= 0; i--)
        {
            LocationPathPattern pattern = chain.get(i);

            if (pattern.filters == null)
            {
                if (result == null && pattern.ancestorPattern == null)
                {
                    result = pattern.nodeTest;
                    continue;
                }
                if (result != null && pattern.ancestorPattern == null)
                {
                    if (pattern.nodeTest instanceof AnyNodeTest)
                    {
                        continue;
                    }
                }
            }

            pattern.parentPattern = result;
            result = pattern;
        }

        return result;
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

        if (! nodeTest.matches(node, context) )
        {
            return false;
        }

        // Collect the chain of all LocationPathPatterns from innermost to outermost
        List<LocationPathPattern> chain = new ArrayList<LocationPathPattern>(8);
        List<Object> chainNodes = new ArrayList<Object>(8);
        LocationPathPattern pattern = this;
        Object currentNode = node;

        while (true)
        {
            chain.add(pattern);
            chainNodes.add(currentNode);

            if (!(pattern.parentPattern instanceof LocationPathPattern))
            {
                break;
            }
            LocationPathPattern parent = (LocationPathPattern) pattern.parentPattern;
            currentNode = navigator.getParentNode( currentNode );
            if ( currentNode == null )
            {
                return false;
            }
            if ( ! parent.nodeTest.matches( currentNode, context ) )
            {
                return false;
            }
            pattern = parent;
        }

        // Handle a non-LocationPathPattern parent (rare; won't chain further)
        if (pattern.parentPattern != null)
        {
            currentNode = navigator.getParentNode( currentNode );
            if ( currentNode == null )
            {
                return false;
            }
            if ( ! pattern.parentPattern.matches( currentNode, context ) )
            {
                return false;
            }
        }

        // Process ancestorPattern and filters from outermost to innermost,
        // mirroring the post-order work the recursive matches() would do
        for (int i = chain.size() - 1; i >= 0; i--)
        {
            LocationPathPattern p = chain.get(i);
            Object n = chainNodes.get(i);

            if (p.ancestorPattern != null)
            {
                Object ancestor = navigator.getParentNode( n );
                while (true)
                {
                    if ( p.ancestorPattern.matches( ancestor, context ) )
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

            if (p.filters != null)
            {
                List list = Collections.singletonList( n );
                context.setNodeSet( list );
                for (Iterator iter = p.filters.iterator(); iter.hasNext(); )
                {
                    FilterExpr filter = (FilterExpr) iter.next();
                    if ( ! filter.asBoolean( context ) )
                    {
                        return false;
                    }
                }
                context.setNodeSet( list );
            }
        }

        return true;
    }
    
    public double getPriority() 
    {
        if ( filters != null ) 
        {
            return 0.5;
        }
        return nodeTest.getPriority();
    }


    public short getMatchType() 
    {
        return nodeTest.getMatchType();
    }
    
    public String getText() 
    {
        StringBuilder builder = new StringBuilder();
        if ( absolute )
        {
            builder.append( "/" );
        }
        if (ancestorPattern != null) 
        {
            String text = ancestorPattern.getText();
            if ( text.length() > 0 )
            {
                builder.append( text );
                builder.append( "//" );
            }
        }
        if (parentPattern != null) 
        {
            String text = parentPattern.getText();
            if ( text.length() > 0 )
            {
                builder.append( text );
                builder.append( "/" );
            }
        }
        builder.append( nodeTest.getText() );
        
        if ( filters != null ) 
        {
            builder.append( "[" );
            for (Iterator iter = filters.iterator(); iter.hasNext(); ) 
            {
                FilterExpr filter = (FilterExpr) iter.next();
                builder.append( filter.getText() );
            }
            builder.append( "]" );
        }        
        return builder.toString();
    }
    
    @Override
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
