
package org.jaxen;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

/**
 *  Provides mappings from namespace prefix to namespace URI to the xpath
 *  engine.
 */
public class SimpleNamespaceContext implements NamespaceContext
{
    private Map namespaces;

    public SimpleNamespaceContext()
    {
        this.namespaces = new HashMap();
    }

    public SimpleNamespaceContext(Map namespaces)
    {
        this.namespaces = namespaces;
    }

    /**
     *  Adds all the namespace declarations that are in scope on the given
     *  element. In the case of an XSLT stylesheet, this would be the element
     *  that has the xpath expression in one of its attributes; i.e.
     *  <code>&lt;xsl:if test="condition/xpath/expression"&gt;</code>.
     *
     *  @param nav  the navigator for use in conjunction with
     *              <code>element</code>
     *  @param element  the element to copy the namespaces from
     */
    public void addElementNamespaces( Navigator nav, Object element )
        throws UnsupportedAxisException
    {
        Iterator namespaceAxis = nav.getNamespaceAxisIterator( element );

        while ( namespaceAxis.hasNext() ) {
            Object namespace = namespaceAxis.next();
            String prefix = nav.getNamespacePrefix( namespace );
            String uri = nav.getNamespaceStringValue( namespace );
            if ( translateNamespacePrefixToUri(prefix) == null ) {
                addNamespace( prefix, uri );
            }
        }
    }    

    public void addNamespace(String prefix, String namespaceUri)
    {
        this.namespaces.put( prefix,
                             namespaceUri );
    }

    public String translateNamespacePrefixToUri(String prefix)
    {
        if ( this.namespaces.containsKey( prefix ) )
        {
            return (String) this.namespaces.get( prefix );
        }

        return null;
    }
}
