// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.pattern;

import junit.framework.TestCase;

import org.jaxen.pattern.Pattern;
import org.jaxen.pattern.PatternParser;

import org.saxpath.XPathSyntaxException;

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
            for ( int i = 0; i < paths.length; i++ ) {
                String path = paths[i];
                
                System.err.println("-----------------");
                System.err.println( "parsing: " + path );
                System.err.println("-----------------");

                Pattern pattern = PatternParser.parse( path );

                System.err.println("-----------------");
                System.err.println("-----------------");

                System.err.println( pattern.toString() );
                System.err.println("-----------------");
                System.err.println( pattern.getText() );
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
