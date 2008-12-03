/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright 2000-2002 bob mcwhirter & James Strachan.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 * 
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 * 
 *   * Neither the name of the Jaxen Project nor the names of its
 *     contributors may be used to endorse or promote products derived 
 *     from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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

package org.jaxen.test;

import junit.framework.TestCase;

import org.jaxen.saxpath.SAXPathException;
import org.jaxen.saxpath.XPathReader;
import org.jaxen.saxpath.helpers.XPathReaderFactory;

public class XPathReaderFactoryTest extends TestCase
{
    public XPathReaderFactoryTest(String name)
    {
        super( name );
    }
    
    
    protected void tearDown() {
        System.setProperty( XPathReaderFactory.DRIVER_PROPERTY, "" );
    }

    public void testDefault() throws SAXPathException
    {
        System.setProperty( XPathReaderFactory.DRIVER_PROPERTY,
                            "" );
        XPathReader reader = XPathReaderFactory.createReader();
        assertNotNull( reader );
    }

    public void testValidByProperty() throws SAXPathException
    {
        System.setProperty( XPathReaderFactory.DRIVER_PROPERTY,
                            "org.jaxen.test.MockXPathReader"  );

        XPathReader reader = XPathReaderFactory.createReader();
        assertNotNull( reader );
        assertSame( MockXPathReader.class, reader.getClass() );
    }

    public void testInvalidByProperty() 
    {
        System.setProperty( XPathReaderFactory.DRIVER_PROPERTY,
                            "java.lang.String" );

        try
        {
            XPathReaderFactory.createReader();
            fail( "Should have thrown SAXPathException" );
        }
        catch (SAXPathException e) {
            // expected and correct
            assertNotNull(e.getMessage());
        }
    }

    public void testNonExistantByProperty()
    {
        System.setProperty( XPathReaderFactory.DRIVER_PROPERTY,
                            "i.am.a.class.that.does.not.Exist" );

        try
        {
            XPathReaderFactory.createReader();
            fail( "Should have thrown SAXPathException" );
        }
        catch (org.jaxen.saxpath.SAXPathException e)
        {
            // expected and correct
        }
    }

    public void testValidExplicit() throws SAXPathException
    {
        XPathReader reader = XPathReaderFactory.createReader( "org.jaxen.test.MockXPathReader" );
        assertNotNull( reader );
        assertSame( MockXPathReader.class, reader.getClass() );
    }

    public void testInvalidExplicit()
    {
        try
        {
            XPathReaderFactory.createReader( "java.lang.String" );
            fail( "Should have thrown SAXPathException" );
        }
        catch (org.jaxen.saxpath.SAXPathException e)
        {
            // expected and correct
        }
    }

    public void testNonExistantExplicit()
    {
        try
        {
            XPathReaderFactory.createReader( "i.am.a.class.that.does.not.Exist" );
            fail( "Should have thrown SAXPathException" );
        }
        catch (org.jaxen.saxpath.SAXPathException e)
        {
            // expected and correct
        }
    }
}
