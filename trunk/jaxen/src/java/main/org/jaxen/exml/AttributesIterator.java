
package org.jaxen.exml;

import electric.xml.Attribute;
import electric.xml.Attributes;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class AttributesIterator implements Iterator
{
    private Attributes attributes;
    private Attribute  next;

    public AttributesIterator(Attributes attributes)
    {
        this.attributes = attributes;
    }

    public boolean hasNext()
    {
        findNext();

        return ( this.next != null );
    }

    public Object next() throws NoSuchElementException
    {
        if ( ! hasNext() )
        {
            throw new NoSuchElementException();
        }

        Attribute attr = this.next;
        this.next = null;

        findNext();

        return attr;
    }

    protected void findNext()
    {
        if ( this.next != null )
        { 
            return;
        }

        while ( this.attributes.current() != null )
        {
            Attribute attr = this.attributes.next();

            if ( validNode( attr ) )
            {
                this.next = attr;
                break;
            }
        }
    }

    protected boolean validNode(Attribute attr)
    {
        String name = attr.getName();

        return ( name == null
                 ||
                 ! name.startsWith( "xmlns" ) );
    }

    public void remove() throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }
}
