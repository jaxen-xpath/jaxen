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

    public void setUp()
    {
    }

    public void tearDown()
    {
        setLexer( null );
        setToken( null );
    }

    public void testNamespace()
    {
        setText( "a:b" );

        nextToken();

        assertEquals( TokenTypes.IDENTIFIER,
                      tokenType() );

        assertEquals( "a",
                      tokenText() );

        nextToken();

        assertEquals( TokenTypes.COLON,
                      tokenType() );
        
        nextToken();

        assertEquals( TokenTypes.IDENTIFIER,
                      tokenType() );

        assertEquals( "b",
                      tokenText() );
    }

    public void testIdentifier()
    {
        setText( "foo" );

        nextToken();

        assertEquals( TokenTypes.IDENTIFIER,
                      tokenType() );

        assertEquals( "foo",
                      tokenText() );

        setText( "foo.bar" );

        nextToken();

        assertEquals( TokenTypes.IDENTIFIER,
                      tokenType() );

        assertEquals( "foo.bar",
                      tokenText() );
    }

    
    /**
     * This tests that characters added in XML 1.1 and Unicode 3.0
     * are not recognized as legal name characters. 
     */
    public void testBurmeseIdentifier()
    {
        setText( "\u1000foo" );

        nextToken();

        assertEquals( TokenTypes.ERROR,
                      tokenType() );

    }

    public void testIdentifierAndOperator()
    {
        setText( "foo and bar" );

        nextToken();

        assertEquals( TokenTypes.IDENTIFIER,
                      tokenType() );

        assertEquals( "foo",
                      tokenText() );

        nextToken();

        assertEquals( TokenTypes.AND,
                      tokenType() );
        nextToken();

        assertEquals( TokenTypes.IDENTIFIER,
                      tokenType() );

        assertEquals( "bar",
                      tokenText() );
    }

    public void testTrickyIdentifierAndOperator()
    {
        setText( "and and and" );

        nextToken();

        assertEquals( TokenTypes.IDENTIFIER,
                      tokenType() );

        assertEquals( "and",
                      tokenText() );

        nextToken();

        assertEquals( TokenTypes.AND,
                      tokenType() );
        nextToken();

        assertEquals( TokenTypes.IDENTIFIER,
                      tokenType() );

        assertEquals( "and",
                      tokenText() );
    }

    public void testInteger()
    {
        setText( "1234" );

        nextToken();

        assertEquals( TokenTypes.INTEGER,
                      tokenType() );

        assertEquals( "1234",
                      tokenText() );
    }

    public void testDouble()
    {
        setText( "12.34" );

        nextToken();

        assertEquals( TokenTypes.DOUBLE,
                      tokenType() );

        assertEquals( "12.34",
                      tokenText() );
    }

    public void testDoubleOnlyDecimal()
    {
        setText( ".34" );

        nextToken();

        assertEquals( TokenTypes.DOUBLE,
                      tokenType() );

        assertEquals( ".34",
                      tokenText() );
    }

    public void testNumbersAndMode()
    {
        setText( "12.34 mod 3" );

        nextToken();

        assertEquals( TokenTypes.DOUBLE,
                      tokenType() );

        assertEquals( "12.34",
                      tokenText() );

        nextToken();

        assertEquals( TokenTypes.MOD,
                      tokenType() );

        nextToken();

        assertEquals( TokenTypes.INTEGER,
                      tokenType() );

            
    }

    public void testSlash()
    {
        setText( "/" );

        nextToken();

        assertEquals( TokenTypes.SLASH,
                      tokenType() );
    }

    public void testDoubleSlash()
    {
        setText( "//" );

        nextToken();

        assertEquals( TokenTypes.DOUBLE_SLASH,
                      tokenType() );
    }

    public void testIdentifierWithColon()
    {
        setText ( "foo:bar" );

        nextToken();

        assertEquals( TokenTypes.IDENTIFIER,
                      tokenType() );

        assertEquals( "foo",
                      tokenText() );

        nextToken();

        assertEquals( TokenTypes.COLON,
                      tokenType() );

        nextToken();

        assertEquals( TokenTypes.IDENTIFIER,
                      tokenType() );

        assertEquals( "bar",
                      tokenText() );
    }

    public void testEOF()
    {
        setText( "foo" );

        nextToken();

        assertEquals( TokenTypes.IDENTIFIER,
                      tokenType() );

        assertEquals( "foo",
                      tokenText() );

        nextToken();

        assertEquals( TokenTypes.EOF,
                      tokenType() );
    }

/*    
    public void testAttributeWithUnderscore()
    {
        setText( "@_foo" );

        nextToken();

        assertEquals( TokenTypes.IDENTIFIER,
                      tokenType() );

        assertEquals( "_foo",
                      tokenText() );

        nextToken();

        assertEquals( TokenTypes.EOF,
                      tokenType() );
    }
 */
    public void testWhitespace()
    {
        setText ( " /   \tfoo:bar" );

        nextToken();

        assertEquals( TokenTypes.SLASH,
                      tokenType() );
        nextToken();

        assertEquals( TokenTypes.IDENTIFIER,
                      tokenType() );

        assertEquals( "foo",
                      tokenText() );

        nextToken();

        assertEquals( TokenTypes.COLON,
                      tokenType() );

        nextToken();

        assertEquals( TokenTypes.IDENTIFIER,
                      tokenType() );

        assertEquals( "bar",
                      tokenText() );
    }

    private void nextToken()
    {
        setToken( getLexer().nextToken() );
    }

    private int tokenType()
    {
        return getToken().getTokenType();
    }

    private String tokenText()
    {
        return getToken().getTokenText();
    }

    private Token getToken()
    {
        return this.token;
    }

    private void setToken(Token token)
    {
        this.token = token;
    }

    private void setText(String text)
    {
        this.lexer = new XPathLexer( text );
    }

    private void setLexer(XPathLexer lexer)
    {
        this.lexer = lexer;
    }

    private XPathLexer getLexer()
    {
        return this.lexer;
    }
}
