// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.expr;

import org.jaxen.ContextSupport;
import org.jaxen.Navigator;

import org.jaxen.expr.iter.IterableAxis;


class DefaultNameStep extends DefaultStep
{
    private String prefix;
    private String localName;

    public DefaultNameStep(IterableAxis axis,
                           String prefix,
                           String localName)
    {
        super( axis );

        this.prefix    = prefix;
        this.localName = localName;
    }

    public String getPrefix()
    {
        return this.prefix;
    }

    public String getLocalName()
    {
        return this.localName;
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
        Navigator nav  = contextSupport.getNavigator();

        String nodeUri  = null;
        String nodeName = null;

        if (  nav.isElement( node ) )
        {
            nodeUri  = nav.getElementNamespaceUri( node );
            nodeName = nav.getElementName( node );

        }
        else if ( nav.isAttribute( node ) )
        {
            nodeUri  = nav.getAttributeNamespaceUri( node );
            nodeName = nav.getAttributeName( node );
        }
        else
        {
            return false;
        }


        boolean matches = false;

        String myPrefix = getPrefix();
        String myLocalName = getLocalName();

        if ( "*".equals( myPrefix ) )
        {
            if ( "*".equals( myLocalName ) )
            {
                matches = true;
            }
            else if ( myLocalName.equals( nodeName ) )
            {
                matches = true;
            }
        }
        else
        {
            String myUri = contextSupport.translateNamespacePrefixToUri( getPrefix() );

            if ( myUri.equals( nodeUri ) )
            {
                if ( "*".equals( myLocalName ) )
                {
                    matches = true;
                }
                else if ( myLocalName.equals( nodeName ) )
                {
                    matches = true;
                }
            }
        }

        return matches;

        /*
        return ( testUri.equals( nodeUri )
                 &&
                 getLocalName().equals( nodeName ) );
        */
    }
}
