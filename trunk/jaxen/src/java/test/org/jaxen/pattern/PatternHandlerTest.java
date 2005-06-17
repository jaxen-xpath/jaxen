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



package org.jaxen.pattern;

import junit.framework.TestCase;

import org.jaxen.saxpath.XPathSyntaxException;

public class PatternHandlerTest extends TestCase
{
    String[] ignore_paths = {
    };
    
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

    public void testValidPaths()
    {
        try
        {
            for ( int i = 0; i < paths.length; i++ ) {
                String path = paths[i];
                
                System.out.println("-----------------");
                System.out.println( "parsing: " + path );
                System.out.println("-----------------");

                Pattern pattern = PatternParser.parse( path );

                System.out.println("-----------------");
                System.out.println("-----------------");

                System.out.println( pattern.toString() );
                System.out.println("-----------------");
                System.out.println( pattern.getText() );
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail( e.getMessage() );
        }
    }

    public void testBogusPaths()
    {
        try
        {
            for ( int i = 0; i < bogusPaths.length; i++ ) {
                String path = bogusPaths[i];
                
                System.err.println("-----------------");
                System.err.println( "parsing bogus path : " + path );
                System.err.println("-----------------");

                try
                {                    
                    Pattern pattern = PatternParser.parse( path );

                    System.err.println( "Parsed as: " + pattern );
                    
                    fail( "Parsed bogus path as: " + pattern );
                }
                catch (XPathSyntaxException e)
                {                    
                    
                    System.err.println("-----------------");
                    //System.err.println( "Exception: " + e.getMessage() );
                    System.err.println( "Exception: ");
                    System.err.println( e.getMultilineMessage() );
                    System.err.println("-----------------");
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail( e.getMessage() );
        }
    }
}
