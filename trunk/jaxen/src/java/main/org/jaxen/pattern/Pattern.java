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

/** <p><code>Pattern</code> defines the behaviour for pattern in
  * the XSLT processing model.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public abstract class Pattern {

    // These node numbers are compatable with both DOM and dom4j's node types
    /** Matches Element nodes */
    public static final short ELEMENT_NODE = 1;
    /** Matches elements nodes */
    public static final short ATTRIBUTE_NODE = 2;
    /** Matches elements nodes */
    public static final short TEXT_NODE = 3;
    /** Matches elements nodes */
    public static final short CDATA_SECTION_NODE = 4;
    /** Matches elements nodes */
    public static final short ENTITY_REFERENCE_NODE = 5;
    /** Matches elements nodes */
    //public static final short ENTITY_NODE = 6;
    /** Matches ProcessingInstruction */
    public static final short PROCESSING_INSTRUCTION_NODE = 7;
    /** Matches Comments nodes */
    public static final short COMMENT_NODE = 8;
    /** Matches Document nodes */
    public static final short DOCUMENT_NODE = 9;
    /** Matches DocumentType nodes */
    public static final short DOCUMENT_TYPE_NODE = 10;
    //public static final short DOCUMENT_FRAGMENT_NODE = 11;
    //public static final short NOTATION_NODE = 12;
    
    /** Matchs a Namespace Node - NOTE this differs from DOM */
    // XXXX: ????
    public static final short NAMESPACE_NODE = 13;
    
    /** Does not match any valid node */
    public static final short UNKNOWN_NODE = 14;
    
    /** The maximum number of node types for sizing purposes */
    public static final short MAX_NODE_TYPE = 14;

    /** Matches any node */
    public static final short ANY_NODE = 0;
    
    /** Matches no nodes */
    public static final short NO_NODE = 14;
    
    
    /** @return true if the pattern matches the given node
      */
    public abstract boolean matches( Object node, Context context ) throws JaxenException;
    
    /** Returns the default resolution policy of the pattern according to the
      * <a href="http://www.w3.org/TR/xslt11/#conflict">
      * XSLT conflict resolution spec</a>. 
      * 
      */
    public double getPriority() 
    {
        return 0.5;
    }
    
    /** If this pattern is a union pattern then this
      * method should return an array of patterns which
      * describe the union pattern, which should contain more than one pattern.
      * Otherwise this method should return null.
      *
      * @return an array of the patterns which make up this union pattern
      * or null if this pattern is not a union pattern
      */
    public Pattern[] getUnionPatterns() 
    {
        return null;
    }

    
    /** @return the type of node the pattern matches
      * which by default should return ANY_NODE if it can
      * match any kind of node.
      */
    public short getMatchType() 
    {
        return ANY_NODE;
    }


    /** For patterns which only match an ATTRIBUTE_NODE or an 
      * ELEMENT_NODE then this pattern may return the name of the
      * element or attribute it matches. This allows a more efficient
      * rule matching algorithm to be performed, rather than a brute 
      * force approach of evaluating every pattern for a given Node.
      *
      * @return the name of the element or attribute this pattern matches
      * or null if this pattern matches any or more than one name.
      */
    public String getMatchesNodeName() 
    {
        return null;
    }
    
    
    public Pattern simplify() 
    {
        return this;
    }
}
