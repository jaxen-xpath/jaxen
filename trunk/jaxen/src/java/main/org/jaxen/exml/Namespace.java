
package org.jaxen.exml;

import electric.xml.Element;

/** An EXML declaration in the xpath data model.
 *
 * @author Erwin Bolwidt ( ejb @ klomp.org )
 */
public class Namespace
{
    private Element element;

    private String prefix;

    private String uri;

    public Namespace( Element element, String prefix, String uri )
    {
        this.element = element;
        this.prefix = prefix;
        this.uri = uri;
    }

    public String getURI()
    {
        return uri;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public Element getElement()
    {
        return element;
    }

    public String toString()
    {
        return ( "[xmlns:" + prefix + "=\"" + uri + "\", element=" +
                 element.getName() + "]" );
    }
}
