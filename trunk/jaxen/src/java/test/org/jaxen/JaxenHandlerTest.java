// Copyright 2001 werken digital. All rights reserved.

package org.jaxen;

import org.saxpath.XPathReader;
import org.saxpath.XPathSyntaxException;
import org.saxpath.helpers.XPathReaderFactory;

import org.jaxen.JaxenHandler;
import org.jaxen.expr.DefaultXPathFactory;
import org.jaxen.expr.XPath;


import junit.framework.TestCase;

public class JaxenHandlerTest extends TestCase
{
    String[] ignore_paths = {
        "foo[.='bar']",
        "foo[.!='bar']",
    };
    
    String[] paths = {
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
        "foo:bar()",
        "/foo/bar[@a='1' and @c!='2']",
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
    
    public JaxenHandlerTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
    }

    public void tearDown()
    {
    }

    public void testValidPaths()
    {
        try
        {
            XPathReader reader = XPathReaderFactory.createReader();
            
            JaxenHandler handler = new JaxenHandler();
            
            handler.setXPathFactory( new DefaultXPathFactory() );
            
            reader.setXPathHandler( handler );
            
            for ( int i = 0; i < paths.length; i++ ) {
                String path = paths[i];
                
                // System.err.println("-----------------");
                // System.err.println( "parsing: " + path );
                // System.err.println("-----------------");

                reader.parse(path);

                XPath xpath = handler.getXPath(false);

                // System.err.println("-----------------");
                // System.err.println( xpath.toString() );
                // System.err.println("-----------------");
                // System.err.println( xpath.getText() );

                xpath = handler.getXPath();

                // System.err.println("-----------------");
                // System.err.println("-----------------");

                // System.err.println( xpath.toString() );
                // System.err.println("-----------------");
                // System.err.println( xpath.getText() );
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
            XPathReader reader = XPathReaderFactory.createReader();
            
            JaxenHandler handler = new JaxenHandler();
            
            handler.setXPathFactory( new DefaultXPathFactory() );
            
            reader.setXPathHandler( handler );
            
            for ( int i = 0; i < bogusPaths.length; i++ ) {
                String path = bogusPaths[i];
                
                System.err.println("-----------------");
                System.err.println( "parsing bogus path : " + path );
                System.err.println("-----------------");

                try
                {                    
                    reader.parse(path);

                    XPath xpath = handler.getXPath(false);

                    System.err.println( "Parsed as: " + xpath );
                    
                    fail( "Parsed bogus path as: " + xpath );
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
