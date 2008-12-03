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

import org.jaxen.JaxenException;
import org.jaxen.pattern.Pattern;
import org.jaxen.pattern.PatternParser;
import org.jaxen.saxpath.SAXPathException;
import org.jaxen.saxpath.XPathSyntaxException;

public class PatternHandlerTest extends TestCase
{
    
    String[] paths = {
        "foo",
        "*",
        "/",
        "foo/bar",
        "foo//bar",
        "/*/foo",
        "*[@name]",
        "foo/bar[1]",
        "foo[bar=\"contents\"]",
        "foo[bar='contents']",
        "foo|bar",
        "foo/title | bar/title | xyz/title",
        "/foo//*",
        "foo/text()",
        "foo/@*",
    };

    String[] bogusPaths = { };
    
    String[] ignore_bogusPaths = {        
        // this path is bogus because of a trailing /
        "/foo/bar/",
        
        // This path is bogus because '/' is not division, but
        // rather just the step separator.
        "12 + sum(count(//author),count(//author/attribute::*)) / 2",
        "id()/2",
        "+"
    };
    
    public PatternHandlerTest(String name)
    {
        super( name );
    }

    public void testValidPaths() throws JaxenException, SAXPathException
    {
        for ( int i = 0; i < paths.length; i++ ) {
            String path = paths[i];
            PatternParser.parse( path );
        }
    }

    public void testBogusPaths() throws JaxenException, SAXPathException
    {
        for ( int i = 0; i < bogusPaths.length; i++ ) {
            String path = bogusPaths[i];
            try
            {                    
                Pattern pattern = PatternParser.parse( path );
                fail( "Parsed bogus path as: " + pattern );
            }
            catch (XPathSyntaxException e)
            {                    
            }
        }

    }
}
