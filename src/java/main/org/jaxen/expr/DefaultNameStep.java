// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.expr;

import org.jaxen.ContextSupport;
import org.jaxen.Navigator;

import org.jaxen.expr.iter.IterableAxis;


/** Expression object that represents any flavor
 *  of name-test steps within an XPath.
 *
 *  <p>
 *  This includes simple steps, such as "foo",
 *  non-default-axis steps, such as "following-sibling::foo"
 *  or "@foo", and namespace-aware steps, such
 *  as "foo:bar".
 *  </p>
 *
 *  @author bob mcwhirter (bob@werken.com)
 */
public class DefaultNameStep extends DefaultStep
{
    /** Our prefix, bound through the current Context.
     *  The empty-string ("") if no prefix was specified.
     *  Decidedly NOT-NULL, due to SAXPath constraints.
     */
    private String prefix;

    /** Our local-name.*/
    private String localName;

    /** Quick flag denoting if the localname was '*' */
    private boolean matchesAnyName;

    public DefaultNameStep(IterableAxis axis,
                           String prefix,
                           String localName)
    {
        super( axis );

        this.prefix              = prefix;
        this.localName           = localName;
        this.matchesAnyName      = "*".equals( localName );
    }

    public String getPrefix()
    {
        return this.prefix;
    }

    public String getLocalName()
    {
        return this.localName;
    }
    
    public boolean isMatchesAnyName() 
    {
        return matchesAnyName;
    }

    public String getText()
    {
        if ( ( getPrefix() != null )
             &&
             ( ! getPrefix().equals("") ) )
        {
            return getAxisName() + "::" + getPrefix() + ":" + getLocalName() + super.getText();
        }

        return getAxisName() 
            + "::" + getLocalName()
            + super.getText();
    }

    public String toString()
    {
        return "[(DefaultNameStep): " + getPrefix() + ":" + getLocalName() + "[" + super.toString() + "]]";
    }

    public boolean matches(Object node,
                           ContextSupport contextSupport)
    {
        //System.err.println( "DefaultNameStep.matches(" + node + ")" );

        Navigator nav  = contextSupport.getNavigator();

        String  myPrefix  = getPrefix();         
        String  myUri     = null;
        boolean hasPrefix = ( myPrefix != null ) && (! ( "".equals( myPrefix ) ) );

        String nodeUri  = null;
        String nodeName = null;

        if ( nav.isElement( node ) )
        {
            nodeUri  = nav.getElementNamespaceUri( node );
            nodeName = nav.getElementName( node );
        }
        else if ( nav.isAttribute( node ) )
        {
            nodeUri  = nav.getAttributeNamespaceUri( node );
            nodeName = nav.getAttributeName( node );
        }
        else if ( nav.isDocument( node ) )
        {
            return ( ! hasPrefix ) && matchesAnyName;
        }
        else if ( nav.isNamespace( node ) )
        {
            nodeUri = null;
            nodeName = nav.getNamespacePrefix( node );
        }
        else
        {
            return false;
        }

        // System.out.println( "Matching nodeURI: " + nodeUri + " name: " + nodeName );
        
        
        if ( hasPrefix )
        {
            myUri = contextSupport.translateNamespacePrefixToUri( myPrefix );
        }
        else if ( matchesAnyName )
        {
            return true;
        }
        
        // If we have a prefix that does not map to no namespace,
        // but the node doesn't have *any* namespace-uri, then we fast-fail.
        
        if ( ( myUri != null   && !"".equals( myUri ) )
             &&
             ( nodeUri == null || "".equals( nodeUri ) ) )
        {
            return false;
        }
        
        // If we don't have a prefix, but the node does
        // have any namespace-uri, then we fast-fail.
        
        if ( ! hasPrefix
             &&
             ( nodeUri != null
               &&
               ! "".equals( nodeUri ) ) )
        {
                return false;
        }
             
        // To fail-fast, we check the equality of
        // local-names first.  Shorter strings compare
        // quicker.

        if ( getLocalName().equals( nodeName )
             ||
             matchesAnyName )
        {
            if ( ! hasPrefix )
            {
                return true;
            }

            return matchesNamespaceURIs( myUri,
                                         nodeUri );
        }
        
        return false;
    }
    
    /** @return true if the two namespace URIs are equal
     *   Note that we may wish to consider null being equal to ""
     */
    protected boolean matchesNamespaceURIs( String u1, String u2 ) {
        //System.out.println( "Comparing URI: " + u1 + " against URI: " + u2 );
        
        if ( u1 == u2 ) {
            return true;
        }
        if ( u1 == null ) 
        {
            u1 = "";
        }
        if ( u2 == null ) 
        {
            u2 = "";
        }
        return u1.equals( u2 );
    }
    
}
