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

public class XPathLexerTokenTest extends TestCase implements TokenTypes
{
    private XPathLexer lexer;

    public XPathLexerTokenTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.lexer = new XPathLexer();
    }

    public void tearDown()
    {
        this.lexer = null;
    }

    public void testIdentifier()
    {
        runTest( "identifier", new int[]{ IDENTIFIER, EOF } );
    }

    public void testNumberInteger()
    {
        runTest( "42", new int[]{ INTEGER, EOF } );
    }

    public void testNumberDouble()
    {
        runTest( "42.42", new int[]{ DOUBLE, EOF } );
    }

    public void testComma()
    {
        runTest( ",", new int[]{ COMMA, EOF } );
    }

    public void testEquals()
    {
        runTest( "=", new int[]{ EQUALS, EOF } );
    }

    public void testMinus()
    {
        runTest( "-", new int[]{ MINUS, EOF } );
    }

    public void testPlus()
    {
        runTest( "+", new int[]{ PLUS, EOF } );
    }

    public void testDollar()
    {
        runTest( "$", new int[]{ DOLLAR, EOF } );
    }

    public void testPipe()
    {
        runTest( "|", new int[]{ PIPE, EOF } );
    }

    public void testAt()
    {
        runTest( "@", new int[]{ AT, EOF } );
    }

    public void testColon()
    {
        runTest( ":", new int[]{ COLON, EOF } );
    }

    public void testDoubleColon()
    {
        runTest( "::", new int[]{ DOUBLE_COLON, EOF } );
    }

    public void testNot()
    {
        runTest( "!", new int[]{ NOT, EOF } );
    }

    public void testNotEquals()
    {
        runTest( "!=", new int[]{ NOT_EQUALS, EOF } );
    }

    public void testStar()
    {
        runTest( "*", new int[]{ STAR, EOF } );
    }

    public void testLiteralSingleQuote()
    {
        runTest( "'literal'", new int[]{ LITERAL, EOF } );
    }

    public void testLiteralDoubleQuote()
    {
        runTest( "\"literal\"", new int[]{ LITERAL, EOF } );
    }

    public void testSingleDot()
    {
        runTest( ".", new int[]{ DOT, EOF } );
    }

    public void testDoubleDot()
    {
        runTest( "..", new int[]{ DOT_DOT, EOF });
    }

    public void testLeftBracket()
    {
        runTest( "[", new int[]{ LEFT_BRACKET, EOF } );
    }

    public void testRightBracket()
    {
        runTest( "]", new int[]{ RIGHT_BRACKET, EOF } );
    }

    public void testLeftParen()
    {
        runTest( "(", new int[]{ LEFT_PAREN, EOF } );
    }

    public void testSingleSlash()
    {
        runTest( "/", new int[]{ SLASH, EOF } );
    }

    public void testDoubleSlash()
    {
        runTest( "//", new int[]{ DOUBLE_SLASH, EOF } );
    }

    public void testLessThan()
    {
        runTest( "<", new int[]{ LESS_THAN, EOF } );
    }

    public void testLessThanEquals()
    {
        runTest( "<=", new int[]{ LESS_THAN_EQUALS, EOF } );
    }

    public void testGreaterThan()
    {
        runTest( ">", new int[]{ GREATER_THAN, EOF } );
    }

    public void testGreaterThanEquals()
    {
        runTest( ">=", new int[]{ GREATER_THAN_EQUALS, EOF } );
    }

    public void testOperatorAnd()
    {
        runTest( "identifier and", new int[]{ IDENTIFIER, AND, EOF } );
    }

    public void testOperatorOr()
    {
        runTest( "identifier or", new int[]{ IDENTIFIER, OR, EOF } );
    }

    public void testOperatorMod()
    {
        runTest( "identifier mod", new int[]{ IDENTIFIER, MOD, EOF } );
    }

    public void testOperatorDiv()
    {
        runTest( "identifier div", new int[]{ IDENTIFIER, DIV } );
    }

    public void testWhitespace()
    {
        runTest( " \t \t \t", new int[]{ EOF } );
    }

    private void runTest(String text,
                         int[] expectedTokens)
    {
        this.lexer.setXPath( text );

        int   tokenType = 0;
        Token token     = null;

        for ( int i = 0 ; i < expectedTokens.length ; ++i )
        {
            tokenType = expectedTokens[i];

            token = this.lexer.nextToken();

            assertNotNull( token );

            assertEquals( tokenType,
                          token.getTokenType() );
        }
    }
}
