
package org.jaxen.jdom;

import org.jdom.Element;
import org.jdom.Namespace;

public class XPathNamespace
{
    private Element jdomElement;

    private Namespace jdomNamespace;

    public XPathNamespace( Element jdomElement, Namespace jdomNamespace )
    {
        this.jdomElement = jdomElement;
        this.jdomNamespace = jdomNamespace;
    }

    public Element getJDOMElement()
    {
        return jdomElement;
    }

    public Namespace getJDOMNamespace()
    {
        return jdomNamespace;
    }

    public String toString()
    {
        return ( "[xmlns:" + jdomNamespace.getPrefix() + "=\"" +
                 jdomNamespace.getURI() + "\", element=" +
                 jdomElement.getName() + "]" );
    }
} 
