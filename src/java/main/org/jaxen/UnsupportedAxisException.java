// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen;

/** Indicates attempt to evaluate an XPath axis that
 *  is unsupported by the current object-model.
 *
 *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 */
public class UnsupportedAxisException extends JaxenException
{
    /** Construct.
     *
     *  @param msg The error message.
     */
    public UnsupportedAxisException(String msg)
    {
        super( msg );
    }
}
