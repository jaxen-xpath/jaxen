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




package org.jaxen.saxpath.base;

import junit.framework.TestCase;

public class XPathLexerTest extends TestCase
{
    private XPathLexer lexer;
    private Token      token;

    public XPathLexerTest(String name)
    {
        super( name );
    }

    public void tearDown()
    {
        this.lexer = null;
        this.token = null;
    }

    public void testNamespace()
    {
        setText( "a:b" );

        nextToken();
        assertEquals( TokenTypes.IDENTIFIER,
                      this.token.getTokenType() );
        assertEquals( "a",
                      this.token.getTokenText() );

        nextToken();
        assertEquals( TokenTypes.COLON,
                      this.token.getTokenType() );
        
        nextToken();
        assertEquals( TokenTypes.IDENTIFIER,
                      this.token.getTokenType() );
        assertEquals( "b",
                      this.token.getTokenText() );
    }

    public void testIdentifier()
    {
        setText( "foo" );

        nextToken();
        assertEquals( TokenTypes.IDENTIFIER,
                      this.token.getTokenType() );
        assertEquals( "foo",
                      this.token.getTokenText() );

        setText( "foo.bar" );

        nextToken();
        assertEquals( TokenTypes.IDENTIFIER,
                      this.token.getTokenType() );
        assertEquals( "foo.bar",
                      this.token.getTokenText() );
    }

    
    /**
     * This tests that characters added in XML 1.1 and Unicode 3.0
     * are not recognized as legal name characters. 
     */
    public void testBurmeseIdentifierStart()
    {
        setText( "\u1000foo" );

        nextToken();
        assertEquals( TokenTypes.ERROR,
                      this.token.getTokenType() );

    }

    public void testBurmeseIdentifierPart()
    {
        setText( "foo\u1000foo" );

        nextToken();
        assertEquals( "foo",
                      this.token.getTokenText() );
        nextToken();
        assertEquals( TokenTypes.ERROR,
                      this.token.getTokenType() );

    }

    public void testIdentifierAndOperator()
    {
        setText( "foo and bar" );

        nextToken();
        assertEquals( TokenTypes.IDENTIFIER,
                      this.token.getTokenType() );
        assertEquals( "foo",
                      this.token.getTokenText() );

        nextToken();
        assertEquals( TokenTypes.AND,
                      this.token.getTokenType() );
        nextToken();
        assertEquals( TokenTypes.IDENTIFIER,
                      this.token.getTokenType() );
        assertEquals( "bar",
                      this.token.getTokenText() );
    }

    public void testTrickyIdentifierAndOperator()
    {
        setText( "and and and" );

        nextToken();
        assertEquals( TokenTypes.IDENTIFIER,
                      this.token.getTokenType() );
        assertEquals( "and",
                      this.token.getTokenText() );

        nextToken();
        assertEquals( TokenTypes.AND,
                      this.token.getTokenType() );
        
        nextToken();
        assertEquals( TokenTypes.IDENTIFIER,
                      this.token.getTokenType() );
        assertEquals( "and",
                      this.token.getTokenText() );
    }

    public void testInteger()
    {
        setText( "1234" );

        nextToken();
        assertEquals( TokenTypes.INTEGER,
                      this.token.getTokenType() );
        assertEquals( "1234",
                      this.token.getTokenText() );
    }

    public void testDouble()
    {
        setText( "12.34" );

        nextToken();
        assertEquals( TokenTypes.DOUBLE,
                      this.token.getTokenType() );
        assertEquals( "12.34",
                      this.token.getTokenText() );
    }

    public void testDoubleOnlyDecimal()
    {
        setText( ".34" );

        nextToken();
        assertEquals( TokenTypes.DOUBLE,
                      this.token.getTokenType() );
        assertEquals( ".34",
                      this.token.getTokenText() );
    }

    public void testNumbersAndMode()
    {
        setText( "12.34 mod 3" );

        nextToken();
        assertEquals( TokenTypes.DOUBLE,
                      this.token.getTokenType() );
        assertEquals( "12.34",
                      this.token.getTokenText() );

        nextToken();
        assertEquals( TokenTypes.MOD,
                      this.token.getTokenType() );

        nextToken();
        assertEquals( TokenTypes.INTEGER,
                      this.token.getTokenType() );
  
    }

    public void testSlash()
    {
        setText( "/" );

        nextToken();
        assertEquals( TokenTypes.SLASH,
                      this.token.getTokenType() );
    }

    public void testDoubleSlash()
    {
        setText( "//" );

        nextToken();
        assertEquals( TokenTypes.DOUBLE_SLASH,
                      this.token.getTokenType() );
    }

    public void testIdentifierWithColon()
    {
        setText ( "foo:bar" );

        nextToken();
        assertEquals( TokenTypes.IDENTIFIER,
                      this.token.getTokenType() );
        assertEquals( "foo",
                      this.token.getTokenText() );

        nextToken();
        assertEquals( TokenTypes.COLON,
                      this.token.getTokenType() );
        
        nextToken();
        assertEquals( TokenTypes.IDENTIFIER,
                      this.token.getTokenType() );
        assertEquals( "bar",
                      this.token.getTokenText() );
    }

    public void testEOF()
    {
        setText( "foo" );
        
        nextToken();
        assertEquals( TokenTypes.IDENTIFIER,
                      this.token.getTokenType() );
        assertEquals( "foo",
                      this.token.getTokenText() );

        nextToken();
        assertEquals( TokenTypes.EOF,
                      this.token.getTokenType() );
    }
 
    public void testWhitespace()
    {
        setText ( " /   \tfoo:bar" );

        nextToken();

        assertEquals( TokenTypes.SLASH,
                      this.token.getTokenType() );
        nextToken();

        assertEquals( TokenTypes.IDENTIFIER,
                      this.token.getTokenType() );

        assertEquals( "foo",
                      this.token.getTokenText() );

        nextToken();

        assertEquals( TokenTypes.COLON,
                      this.token.getTokenType() );

        nextToken();

        assertEquals( TokenTypes.IDENTIFIER,
                      this.token.getTokenType() );

        assertEquals( "bar",
                      this.token.getTokenText() );
    }

    private void nextToken()
    {
        this.token = this.lexer.nextToken();
    }

    private void setText(String text)
    {
        this.lexer = new XPathLexer( text );
    }
}
