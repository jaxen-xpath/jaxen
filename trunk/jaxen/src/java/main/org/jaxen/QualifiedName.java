package org.jaxen;

/** A local name (that matches the XML NCName production) and a namespace uri
 *  with which the local name is qualified.
 *
 *  @author Erwin Bolwidt ( ejb @ klomp.org )
 */
class QualifiedName
{
    private String namespaceURI;

    private String localName;

    /** Constructs a QualifiedName object.
     *
     *  @param namespaceURI namespace URI that qualifies the name, or
     *                      <code>null</code> for default namespace.
     *  @param localName    local name that is qualified by the namespace uri.
     *                      must not be <code>null</code>.
     */
    QualifiedName( String namespaceURI, String localName )
    {
        this.namespaceURI = namespaceURI;
        this.localName = localName;
    }

    public String getNamespaceURI()
    {
        return namespaceURI;
    }

    public String getLocalName()
    {
        return localName;
    }

    public int hashCode()
    {
        return ( localName.hashCode() ^
                 ( namespaceURI == null ? 0 : namespaceURI.hashCode() ) );
    }

    public boolean equals( Object o )
    {
        if ( !(o instanceof QualifiedName) )
            return false;
        
        QualifiedName other = (QualifiedName)o;
        
        if ( namespaceURI == null )
            return ( other.namespaceURI == null &&
                     other.localName.equals(localName) );
        else
            return ( namespaceURI.equals(other.namespaceURI) &&
                     other.localName.equals(localName) );
    }
}

