
package org.jaxen.exml;

import java.util.Iterator;
import java.util.NoSuchElementException;

import electric.xml.Attribute;
import electric.xml.Attributes;

/** An iterator over EXML namespaces
 *
 * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 * @version $Revision$
 */
public class NamespaceIterator extends AttributesIterator
{
    public NamespaceIterator(Attributes attributes)
    {
        super( attributes );
    }

    /** @return true if this node is valid - i.e. the attribute starts with xmlns */
    protected boolean validNode(Attribute attr)
    {
        String name = attr.toString();

        return ( name != null
                 &&
                 name.startsWith( "xmlns" ) );
    }
}
