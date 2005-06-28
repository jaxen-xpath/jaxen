/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright (C) 2000-2002 bob mcwhirter & James Strachan.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions, and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions, and the disclaimer that follows 
 *    these conditions in the documentation and/or other materials 
 *    provided with the distribution.
 *
 * 3. The name "Jaxen" must not be used to endorse or promote products
 *    derived from this software without prior written permission.  For
 *    written permission, please contact license@jaxen.org.
 * 
 * 4. Products derived from this software may not be called "Jaxen", nor
 *    may "Jaxen" appear in their name, without prior written permission
 *    from the Jaxen Project Management (pm@jaxen.org).
 * 
 * In addition, we request (but do not require) that you include in the 
 * end-user documentation provided with the redistribution and/or in the 
 * software itself an acknowledgement equivalent to the following:
 *     "This product includes software developed by the
 *      Jaxen Project <http://www.jaxen.org/>."
 * Alternatively, the acknowledgment may be graphical using the logos 
 * available at http://www.jaxen.org/
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE Jaxen AUTHORS OR THE PROJECT
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 * ====================================================================
 * This software consists of voluntary contributions made by many 
 * individuals on behalf of the Jaxen Project and was originally 
 * created by bob mcwhirter <bob@werken.com> and 
 * James Strachan <jstrachan@apache.org>.  For more information on the 
 * Jaxen Project, please see <http://www.jaxen.org/>.
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
        this.rhs = rhs.simplify();
        init();
        return this;
    }
    
    public String getText() 
    {
        return lhs.getText() + " | " + rhs.getText();
    }
        
    public String toString()
    {
        return super.toString() + "[ lhs: " + lhs + " rhs: " + rhs + " ]";
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
