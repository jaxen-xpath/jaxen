package org.jaxen;

/**
 * 
 */
public interface Updater
{
    Object createComment( Object contextNode, String comment )
        throws InvalidContextException;

    Object createText( Object contextNode, String text )
        throws InvalidContextException;

    Object createElement( Object contextNode, String uri, String qname )
        throws InvalidContextException;

    Object createNamespace( Object contextNode, String prefix, String uri )
        throws InvalidContextException;

    Object createAttribute( Object contextNode, String uri,
                            String qname, String value )
        throws InvalidContextException;

    Object createProcessingInstruction( Object contextNode, String target,
                                        String data )
        throws InvalidContextException;

    void insertBefore( Object refNode, Object node )
        throws InvalidContextException;

    void insertAfter( Object refNode, Object node )
        throws InvalidContextException;

    /**
     *  @param position -1 for "at end"
     */
    void appendChild( Object element, Object child, int position )
        throws InvalidContextException;

    void remove( Object node )
        throws InvalidContextException;

    void setAttribute( Object element, Object attribute )
        throws InvalidContextException;

    void setNamespace( Object element, Object namespace )
        throws InvalidContextException;

    Navigator getNavigator();
}
