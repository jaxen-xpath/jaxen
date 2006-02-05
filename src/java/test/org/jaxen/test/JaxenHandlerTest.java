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

import org.jaxen.JaxenHandler;
import org.jaxen.expr.DefaultXPathFactory;
import org.jaxen.expr.XPathExpr;
import org.jaxen.saxpath.SAXPathException;
import org.jaxen.saxpath.XPathReader;
import org.jaxen.saxpath.XPathSyntaxException;
import org.jaxen.saxpath.helpers.XPathReaderFactory;

public class JaxenHandlerTest extends TestCase
{
    
    private String[] paths = {
        "foo[.='bar']",
        "foo[.!='bar']",
        "/",
        "*",
        "//foo",
        "/*",
        "/.",
        "/foo[/bar[/baz]]",
        "/foo/bar/baz[(1 or 2) + 3 * 4 + 8 and 9]",
        "/foo/bar/baz",
        ".[1]",
        "self::node()",
        ".",
        "count(/)",
        "foo[1]",
        "/baz[(1 or 2) + 3 * 4 + 8 and 9]",
        "foo/bar[/baz[(1 or 2) - 3 mod 4 + 8 and 9 div 8]]",
        "foo/bar/yeah:baz[a/b/c and toast]",
        "/foo/bar[../x='123']",
        "/foo[@bar='1234']",
        "foo|bar",
        "/foo|/bar[@id='1234']",
        "count(//author/attribute::*)",
        "$author",
         "10 + $foo",
        "10 + (count(descendant::author) * 5)",
        "10 + count(descendant::author) * 5",
        "2 + (2 * 5)",
        "sum(count(//author), 5)",
        "sum(count(//author),count(//author/attribute::*))",
        "12 + sum(count(//author),count(//author/attribute::*)) div 2",
        "text()[.='foo']",
        "/*/*[@id='123']",
        "/child::node()/child::node()[@id='_13563275']",
        "$foo:bar",
        "//foo:bar",
        "/foo/bar[@a='1' and @c!='2']",
    };

    private String[] bogusPaths = { "//:p" ,       
        // this path is bogus because of a trailing /
        "/foo/bar/",
        
        // This path is bogus because '/' is not division, but
        // rather just the step separator.
        "12 + sum(count(//author),count(//author/attribute::*)) / 2",
        "id()/2",
        "+"
    };
    
    public JaxenHandlerTest(String name)
    {
        super( name );
    }

    public void testValidPaths()
    {
            
        String path = null;

        try
        {
            // XXX Jiffie solution?
            XPathReader reader = XPathReaderFactory.createReader();
            JaxenHandler handler = new JaxenHandler();
            handler.setXPathFactory( new DefaultXPathFactory() );
            reader.setXPathHandler( handler );

            for ( int i = 0; i < paths.length; i++ ) {
                path = paths[i];
                reader.parse(path);
                handler.getXPathExpr(false);
                handler.getXPathExpr();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail( path + " -> " + e.getMessage() );
        }
    }

    public void testBogusPaths() throws SAXPathException
    {
        XPathReader reader = XPathReaderFactory.createReader();
        JaxenHandler handler = new JaxenHandler();
        handler.setXPathFactory( new DefaultXPathFactory() );
        reader.setXPathHandler( handler );
        
        for ( int i = 0; i < bogusPaths.length; i++ ) {
            String path = bogusPaths[i];

            try
            {                    
                reader.parse(path);
                XPathExpr xpath = handler.getXPathExpr(false);
                fail( "Parsed bogus path as: " + xpath );
            }
            catch (XPathSyntaxException e)
            {
            }
        }
    }
}
