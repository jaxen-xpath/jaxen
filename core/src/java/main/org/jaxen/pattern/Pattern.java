/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
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
 * Jaxen Project, please see <http://www.jaxen.org/>.
 * 
 * $Id$
 */

package org.jaxen.pattern;


/** <p>Node type constants defined here for legacy reasons.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  * @deprecated will be removed in Jaxen 3.0
  */
public class Pattern {

    // These node numbers are compatible with both DOM and dom4j's node types
    /** Matches Element nodes */
    public static final short ELEMENT_NODE = 1;
    /** Matches attribute nodes */
    public static final short ATTRIBUTE_NODE = 2;
    /** Matches text nodes */
    public static final short TEXT_NODE = 3;
    /** Matches CDATA section nodes */
    public static final short CDATA_SECTION_NODE = 4;
    /** Matches entity reference nodes */
    public static final short ENTITY_REFERENCE_NODE = 5;
    /** Matches entity nodes */
    //public static final short ENTITY_NODE = 6;
    /** Matches ProcessingInstruction */
    public static final short PROCESSING_INSTRUCTION_NODE = 7;
    /** Matches comment nodes */
    public static final short COMMENT_NODE = 8;
    /** Matches document nodes */
    public static final short DOCUMENT_NODE = 9;
    /** Matches DocumentType nodes */
    public static final short DOCUMENT_TYPE_NODE = 10;
    //public static final short DOCUMENT_FRAGMENT_NODE = 11;
    //public static final short NOTATION_NODE = 12;
    
    /** Matches a Namespace Node */
    // This has the same value as the DOM Level 3 XPathNamespace type
    public static final short NAMESPACE_NODE = 13;
    
    /** Does not match any valid node */
    public static final short UNKNOWN_NODE = 14;
    
    /** The maximum number of node types for sizing purposes */
    public static final short MAX_NODE_TYPE = 14;

    /** Matches any node */
    public static final short ANY_NODE = 0;
    
    /** Matches no nodes */
    public static final short NO_NODE = 14;
}
