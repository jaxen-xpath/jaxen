
package org.jaxen;

import java.util.Map;
import java.util.HashMap;

public class SimpleNamespaceContext implements NamespaceContext
{
    private Map namespaces;

    public SimpleNamespaceContext()
    {
        this.namespaces = new HashMap();
    }

    public void addNamespacePrefixTranslation(String prefix,
                                              String namespaceUri)
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

        return "";
    }
}
