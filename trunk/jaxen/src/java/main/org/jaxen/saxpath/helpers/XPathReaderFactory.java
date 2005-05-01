/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright (C) 2000-2002 bob mcwhirter & James Strachan.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions, and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions, and the disclaimer that follows
 *    these conditions in the documentation and/or other materials
 *    provided with the distribution.
 *
 * 3. The name "Jaxen" must not be used to endorse or promote products
 *    derived from this software without prior written permission.  For
 *    written permission, please contact license@jaxen.org.
 *
 * 4. Products derived from this software may not be called "Jaxen", nor
 *    may "Jaxen" appear in their name, without prior written permission
 *    from the Jaxen Project Management (pm@jaxen.org).
 *
 * In addition, we request (but do not require) that you include in the
 * end-user documentation provided with the redistribution and/or in the
 * software itself an acknowledgement equivalent to the following:
 *     "This product includes software developed by the
 *      Jaxen Project (http://www.jaxen.org/)."
 * Alternatively, the acknowledgment may be graphical using the logos
 * available at http://www.jaxen.org/
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE Jaxen AUTHORS OR THE PROJECT
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 * ====================================================================
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Jaxen Project and was originally
 * created by bob mcwhirter <bob@werken.com> and
 * James Strachan <jstrachan@apache.org>.  For more information on the
 * Jaxen Project, please see <http://www.jaxen.org/>.
 *
 * $Id$
 */




package org.jaxen.saxpath.helpers;

import org.jaxen.saxpath.SAXPathException;
import org.jaxen.saxpath.XPathReader;

/** Create an {@link org.jaxen.saxpath.XPathReader} from
 *  either a system property, or a named class.
 *
 *  <p>
 *  Similar to the SAX API, the <code>XPathReaderFactory</code>
 *  can create an <code>XPathReader</code> from a name of a
 *  class passed in directly, or by inspecting the system
 *  property <code>org.saxpath.driver</code>.
 *
 *  @author bob mcwhirter (bob@werken.com)
 */
public class XPathReaderFactory
{
    /** The <code>org.saxpath.driver</code> property name. */
    public static final String DRIVER_PROPERTY = "org.saxpath.driver";

    /** The default driver to use if none is configured. */
    protected static final String DEFAULT_DRIVER = "org.jaxen.saxpath.base.XPathReader";
    
    
    /** Create an <code>XPathReader</code> using the value of
     *  the <code>org.saxpath.driver</code> system property.
     *
     *  @return an instance of the <code>XPathReader</code> specified
     *          by the <code>org.saxpath.driver</code> property
     *
     *  @throws SAXPathException if the property is  not set, or if
     *          the class can not be instantiated for some reason,
     *          or if the class doesn't implement the <code>XPathReader</code>
     *          interface
     */
    public static XPathReader createReader() throws SAXPathException
    {
        String className = null;

        try
        {
            className = System.getProperty( DRIVER_PROPERTY );
        }
        catch (SecurityException e)
        {
            // we'll use the default
        }

        if ( className == null
             ||
             className.length() == 0 )
        {
            className = DEFAULT_DRIVER;
        }

        return createReader( className );
    }
    
    /** Create an <code>XPathReader</code> using the passed
     *  in class name.
     *
     *  @param className the name of the class that implements
     *         the <code>XPathReader</code> interface.
     * 
     * @return an XPathReader
     *
     *  @throws SAXPathException if the class can not be
     *          instantiated for some reason, or if the
     *          class doesn't implement the <code>XPathReader</code>
     *          interface
     */
    public static XPathReader createReader(String className) throws SAXPathException
    {
        Class readerClass  = null;
        XPathReader reader = null;

        try
        {
            // Use the full version of Class.forName(), so as to
            // work better in sandboxed environments, such as
            // Servlet contains, and applets.

            readerClass = Class.forName( className,
                                         true,
                                         XPathReaderFactory.class.getClassLoader() );
            
            // Double-check that it's actually the right kind of class
            // before attempting instantiation.
            
            if ( ! XPathReader.class.isAssignableFrom( readerClass ) )
            {
                throw new SAXPathException( "Class [" + className 
                  + "] does not implement the org.jaxen.saxpath.XPathReader interface." );
            }
        }
        catch (ClassNotFoundException e)
        {
            throw new SAXPathException( e );
        }

        try
        {
            reader = (XPathReader) readerClass.newInstance();
        }
        catch (IllegalAccessException e)
        {
            throw new SAXPathException( e );
        }
        catch (InstantiationException e)
        {
            throw new SAXPathException( e );
        }

        if ( reader == null )
        {
            throw new SAXPathException( "Unable to create XPathReader" );
        }
        
        return reader;
    }
}
