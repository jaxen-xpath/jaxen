/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright (C) 2000-2002 werken digital.
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
 * 3. The name "SAXPath" must not be used to endorse or promote products
 *    derived from this software without prior written permission.  For
 *    written permission, please contact license@saxpath.org.
 * 
 * 4. Products derived from this software may not be called "SAXPath", nor
 *    may "SAXPath" appear in their name, without prior written permission
 *    from the SAXPath Project Management (pm@saxpath.org).
 * 
 * In addition, we request (but do not require) that you include in the 
 * end-user documentation provided with the redistribution and/or in the 
 * software itself an acknowledgement equivalent to the following:
 *     "This product includes software developed by the
 *      SAXPath Project (http://www.saxpath.org/)."
 * Alternatively, the acknowledgment may be graphical using the logos 
 * available at http://www.saxpath.org/
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE SAXPath AUTHORS OR THE PROJECT
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
 * individuals on behalf of the SAXPath Project and was originally 
 * created by bob mcwhirter <bob@werken.com> and 
 * James Strachan <jstrachan@apache.org>.  For more information on the 
 * SAXPath Project, please see <http://www.saxpath.org/>.
 * 
 * $Id$
 */



package org.jaxen.saxpath.helpers;

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

    /** Should the default driver be used */
    private static boolean USE_DEFAULT = true;
    
    
    /** Create an <code>XPathReader</code> using the value of
     *  the <code>org.saxpath.driver</code> system property.
     *
     *  @return An instance of the <code>XPathReader</code> specified
     *          by the <code>org.saxpath.driver</code> property.
     *
     *  @throws org.jaxen.saxpath.SAXPathException if the property is unset, or if
     *          the class can not be instantiated for some reason.,
     *          or if the class doesn't implement the <code>XPathReader</code>
     *          interface.
     */
    public static XPathReader createReader() throws org.jaxen.saxpath.SAXPathException
    {
        String className = null;

        boolean securityException = false;

        try
        {
            className = System.getProperty( DRIVER_PROPERTY );
        }
        catch (SecurityException e)
        {
            securityException = true;
        }

        if ( className == null
             ||
             "".equals( className ) )
        {
            if ( USE_DEFAULT )
            {
                className = DEFAULT_DRIVER;
            }
            else
            {
                if ( securityException )
                {
                    throw new org.jaxen.saxpath.SAXPathException( "Reading of property " + DRIVER_PROPERTY + " disallowed." );
                }
                else
                {
                    throw new org.jaxen.saxpath.SAXPathException( "Property " + DRIVER_PROPERTY + " not set" );
                }
            }
        }

        return createReader( className );
    }
    
    /** Create an <code>XPathReader</code> using the passed
     *  in class name.
     *
     *  @param className The name of the class which implements
     *         the <code>XPathReader</code> interface.
     *
     *  @throws org.jaxen.saxpath.SAXPathException if the class can not be
     *          instantiated for some reason, or if the
     *          class doesn't implement the <code>XPathReader</code>
     *          interface.
     */
    public static XPathReader createReader(String className) throws org.jaxen.saxpath.SAXPathException
    {
        Class readerClass  = null;
        XPathReader reader = null;

        try
        {
            // Use the full version of Class.forName(), so as to
            // work better in sandboxed environments, such as
            // Servlet contains, and Applets.

            readerClass = Class.forName( className,
                                         true,
                                         XPathReaderFactory.class.getClassLoader() );
            
            // Double-check that it's actually the right kind of class
            // before attempting instantiation.
            
            if ( ! XPathReader.class.isAssignableFrom( readerClass ) )
            {
                throw new org.jaxen.saxpath.SAXPathException( "Class [" + className + "] does not implement the org.jaxen.saxpath.XPathReader interface." );
            }
        }
        catch (ClassNotFoundException e)
        {
            throw new org.jaxen.saxpath.SAXPathException( e.getMessage() );
        }

        try
        {
            reader = (XPathReader) readerClass.newInstance();
        }
        catch (IllegalAccessException e)
        {
            throw new org.jaxen.saxpath.SAXPathException( e.getMessage() );
        }
        catch (InstantiationException e)
        {
            throw new org.jaxen.saxpath.SAXPathException( e.getMessage() );
        }

        if ( reader == null )
        {
            throw new org.jaxen.saxpath.SAXPathException( "Unable to create XPathReader" );
        }
        
        return reader;
    }
}
