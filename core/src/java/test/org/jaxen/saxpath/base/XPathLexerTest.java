package org.jaxen.saxpath.base;

import junit.framework.TestCase;

public class XPathLexerTest extends TestCase
{

    public void testUnterminatedLiteralAtEndOfInputIsErrorToken()
    {
        XPathLexer lexer = new XPathLexer( "'unterminated" );
        Token token = lexer.nextToken();

        assertEquals( TokenTypes.ERROR, token.getTokenType() );
    }

}
