
package org.jaxen.jdom;

import org.jdom.Element;
import org.jdom.Namespace;

/** Wrapper for jdom namespace nodes to give them a parent, as required
 *  by the XPath data model.
 *
 *  @author Erwin Bolwidt
 */
public class XPathNamespace
{
    private Element jdomElement;

    private Namespace jdomNamespace;

    /** Creates a namespace-node wrapper for a namespace node that hasn't been
     *  assigned to an element yet.
     */
    public XPathNamespace( Namespace jdomNamespace )
    {
        this.jdomNamespace = jdomNamespace;
    }

    /** Creates a namespace-node wrapper for a namespace node that is assigned
     *  to the given jdom element.
     */
    public XPathNamespace( Element jdomElement, Namespace jdomNamespace )
    {
        this.jdomElement = jdomElement;
        this.jdomNamespace = jdomNamespace;
    }

    /** Returns the jdom element from which this namespace node has been 
     *  retrieved. The result may be null when the namespace node has not yet
     *  been assigned to an element.
     */
    public Element getJDOMElement()
    {
        return jdomElement;
    }

    /** Sets or changes the element to which this namespace node is assigned.
     */
    public void setJDOMElement( Element jdomElement )
    {
        this.jdomElement = jdomElement;
    }

    /** Returns the jdom namespace object of this namespace node; the jdom
     *  namespace object contains the prefix and uri of the namespace.
     */
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
