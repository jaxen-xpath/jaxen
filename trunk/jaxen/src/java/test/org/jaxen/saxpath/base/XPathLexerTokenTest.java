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

public class XPathLexerTokenTest extends TestCase
{
    public XPathLexerTokenTest(String name)
    {
        super( name );
    }

    public void testIdentifier()
    {
        runTest( "identifier", new int[]{ TokenTypes.IDENTIFIER, TokenTypes.EOF } );
    }

    public void testNumberInteger()
    {
        runTest( "42", new int[]{ TokenTypes.INTEGER, TokenTypes.EOF } );
    }

    public void testNumberDouble()
    {
        runTest( "42.42", new int[]{ TokenTypes.DOUBLE, TokenTypes.EOF } );
    }

    public void testComma()
    {
        runTest( ",", new int[]{ TokenTypes.COMMA, TokenTypes.EOF } );
    }

    public void testEquals()
    {
        runTest( "=", new int[]{ TokenTypes.EQUALS, TokenTypes.EOF } );
    }

    public void testMinus()
    {
        runTest( "-", new int[]{ TokenTypes.MINUS, TokenTypes.EOF } );
    }

    public void testPlus()
    {
        runTest( "+", new int[]{ TokenTypes.PLUS, TokenTypes.EOF } );
    }

    public void testDollar()
    {
        runTest( "$", new int[]{ TokenTypes.DOLLAR, TokenTypes.EOF } );
    }

    public void testPipe()
    {
        runTest( "|", new int[]{ TokenTypes.PIPE, TokenTypes.EOF } );
    }

    public void testAt()
    {
        runTest( "@", new int[]{ TokenTypes.AT, TokenTypes.EOF } );
    }

    public void testColon()
    {
        runTest( ":", new int[]{ TokenTypes.COLON, TokenTypes.EOF } );
    }

    public void testDoubleColon()
    {
        runTest( "::", new int[]{ TokenTypes.DOUBLE_COLON, TokenTypes.EOF } );
    }

    public void testNot()
    {
        runTest( "!", new int[]{ TokenTypes.NOT, TokenTypes.EOF } );
    }

    public void testNotEquals()
    {
        runTest( "!=", new int[]{ TokenTypes.NOT_EQUALS, TokenTypes.EOF } );
    }

    public void testStar()
    {
        runTest( "*", new int[]{ TokenTypes.STAR, TokenTypes.EOF } );
    }

    public void testLiteralSingleQuote()
    {
        runTest( "'literal'", new int[]{ TokenTypes.LITERAL, TokenTypes.EOF } );
    }

    public void testLiteralDoubleQuote()
    {
        runTest( "\"literal\"", new int[]{ TokenTypes.LITERAL, TokenTypes.EOF } );
    }

    public void testSingleDot()
    {
        runTest( ".", new int[]{ TokenTypes.DOT, TokenTypes.EOF } );
    }

    public void testDoubleDot()
    {
        runTest( "..", new int[]{ TokenTypes.DOT_DOT, TokenTypes.EOF });
    }

    public void testLeftBracket()
    {
        runTest( "[", new int[]{ TokenTypes.LEFT_BRACKET, TokenTypes.EOF } );
    }

    public void testRightBracket()
    {
        runTest( "]", new int[]{ TokenTypes.RIGHT_BRACKET, TokenTypes.EOF } );
    }

    public void testLeftParen()
    {
        runTest( "(", new int[]{ TokenTypes.LEFT_PAREN, TokenTypes.EOF } );
    }

    public void testSingleSlash()
    {
        runTest( "/", new int[]{ TokenTypes.SLASH, TokenTypes.EOF } );
    }

    public void testDoubleSlash()
    {
        runTest( "//", new int[]{ TokenTypes.DOUBLE_SLASH, TokenTypes.EOF } );
    }

    public void testLessThan()
    {
        runTest( "<", new int[]{ TokenTypes.LESS_THAN_SIGN, TokenTypes.EOF } );
    }

    public void testLessThanEquals()
    {
        runTest( "<=", new int[]{ TokenTypes.LESS_THAN_OR_EQUALS_SIGN, TokenTypes.EOF } );
    }

    public void testGreaterThan()
    {
        runTest( ">", new int[]{ TokenTypes.GREATER_THAN_SIGN, TokenTypes.EOF } );
    }

    public void testGreaterThanEquals()
    {
        runTest( ">=", new int[]{ TokenTypes.GREATER_THAN_OR_EQUALS_SIGN, TokenTypes.EOF } );
    }

    public void testOperatorAnd()
    {
        runTest( "identifier and", new int[]{ TokenTypes.IDENTIFIER, TokenTypes.AND, TokenTypes.EOF } );
    }

    public void testOperatorOr()
    {
        runTest( "identifier or", new int[]{ TokenTypes.IDENTIFIER, TokenTypes.OR, TokenTypes.EOF } );
    }

    public void testOperatorMod()
    {
        runTest( "identifier mod", new int[]{ TokenTypes.IDENTIFIER, TokenTypes.MOD, TokenTypes.EOF } );
    }

    public void testOperatorDiv()
    {
        runTest( "identifier div", new int[]{ TokenTypes.IDENTIFIER, TokenTypes.DIV } );
    }

    public void testWhitespace()
    {
        runTest( " \t \t \t", new int[]{ TokenTypes.EOF } );
    }

    private void runTest(String text,
                         int[] expectedTokens)
    {
        XPathLexer lexer = new XPathLexer( text );

        int   tokenType = 0;
        Token token     = null;

        for ( int i = 0 ; i < expectedTokens.length ; ++i )
        {
            tokenType = expectedTokens[i];
            token = lexer.nextToken();
            assertNotNull( token );
            assertEquals( tokenType,
                          token.getTokenType() );
        }
    }
    
}
