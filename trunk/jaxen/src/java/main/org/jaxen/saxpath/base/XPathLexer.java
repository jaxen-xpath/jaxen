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

package org.jaxen.saxpath.base;

class XPathLexer
{
    private String xpath;
    private int    currentPosition;
    private int    endPosition;
    private boolean expectOperator = false;

    XPathLexer(String xpath)
    {
        setXPath( xpath );
    }

    private void setXPath(String xpath)
    {
        this.xpath           = xpath;
        this.currentPosition = 0;
        this.endPosition     = xpath.length();
    }

    String getXPath()
    {
        return this.xpath;
    }

    Token nextToken()
    {
        Token token = null;

        do
        {
            token = null;

            switch ( LA(1) )
            {
                case '$':
                {
                    token = dollar();
                    break;
                }
                    
                case '"':
                case '\'':
                {
                    token = literal();
                    break;
                }
                    
                case '/':
                {
                    token = slashes();
                    break;
                }

                case ',':
                {
                    token = comma();
                    break;
                }
                    
                case '(':
                {
                    token = leftParen();
                    break;
                }
                    
                case ')':
                {
                    token = rightParen();
                    break;
                }
                    
                case '[':
                {
                    token = leftBracket();
                    break;
                }
                    
                case ']':
                {
                    token = rightBracket();
                    break;
                }
                    
                case '+':
                {
                    token = plus();
                    break;
                }
                    
                case '-':
                {
                    token = minus();
                    break;
                }
                    
                case '<':
                case '>':
                {
                    token = relationalOperator();
                    break;
                }        

                case '=':
                {
                    token = equals();
                    break;
                }
                    
                case '!':
                {
                    if ( LA(2) == '=' )
                    {
                        token = notEquals();
                    }
                    break;
                }
                    
                case '|':
                {
                    token = pipe();
                    break;
                }
                    
                case '@':
                {
                    token = at();
                    break;
                }
                    
                case ':':
                {
                    if ( LA(2) == ':' )
                    {
                        token = doubleColon();
                    }
                    else
                    {
                        token = colon();
                    }
                    break;
                }
                    
                case '*':
                {
                    token = star();
                    break;
                }
                    
                case '.':
                {
                    switch ( LA(2) )
                    {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                        {
                            token = number();
                            break;
                        }
                        default:
                        {
                            token = dots();
                            break;
                        }
                    }
                    break;
                }

                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                {
                    token = number();
                    break;
                }

                case ' ':
                case '\t':
                case '\n':
                case '\r':
                {
                    token = whitespace();
                    break;
                }
                    
                default:
                {
                    if ( Verifier.isXMLNCNameStartCharacter( LA(1) ) )
                    {
                        token = identifierOrOperatorName();
                    }
                }
            }

            if ( token == null )
            {
                if (!hasMoreChars())
                {
                    token = new Token( TokenTypes.EOF,
                                   getXPath(),
                                   this.currentPosition,
                                   this.endPosition );
            }
                else
                {
                    token = new Token( TokenTypes.ERROR,
                                   getXPath(),
                                   this.currentPosition,
                                   this.endPosition );
                }
            }

        }
        while (token.getTokenType() == TokenTypes.SKIP );
        
        // For some reason, section 3.7, Lexical structure,
        // doesn't seem to feel like it needs to mention the
        // SLASH, DOUBLE_SLASH, and COLON tokens for the test
        // if an NCName is an operator or not.
        //
        // According to section 3.7, "/foo" should be considered
        // as a SLASH following by an OperatorName being 'foo'.
        // Which is just simply, clearly, wrong, in my mind.
        //
        //     -bob

        switch ( token.getTokenType() )
        {
            case TokenTypes.AT:
            case TokenTypes.DOUBLE_COLON:
            case TokenTypes.LEFT_PAREN:
            case TokenTypes.LEFT_BRACKET:
            case TokenTypes.AND:
            case TokenTypes.OR:
            case TokenTypes.MOD:
            case TokenTypes.DIV:
            case TokenTypes.COLON:
            case TokenTypes.SLASH:
            case TokenTypes.DOUBLE_SLASH:
            case TokenTypes.PIPE:
            case TokenTypes.DOLLAR:
            case TokenTypes.PLUS:
            case TokenTypes.MINUS:
            case TokenTypes.STAR_OPERATOR:
            case TokenTypes.COMMA:
            case TokenTypes.LESS_THAN_SIGN:
            case TokenTypes.GREATER_THAN_SIGN:
            case TokenTypes.LESS_THAN_OR_EQUALS_SIGN:
            case TokenTypes.GREATER_THAN_OR_EQUALS_SIGN:
            case TokenTypes.EQUALS:
            case TokenTypes.NOT_EQUALS:
            {
                expectOperator = false;
                break;
            }
            default:
            {
                expectOperator = true;
                break;
            }
        }

         return token;
     }

    private Token identifierOrOperatorName()
    {
        Token token = null;
        if ( expectOperator ) {
            token = operatorName();
        } else {
            token = identifier();
        }
        return token;
    }
    
    private Token identifier()
    {
        Token token = null;
    
        int start = this.currentPosition;
    
        while ( hasMoreChars() )
        {
            if ( Verifier.isXMLNCNameCharacter( LA(1) ) )
            {
                consume();
            }
            else
            {
                break;
            }
        }
    
        token = new Token( TokenTypes.IDENTIFIER,
                           getXPath(),
                           start,
                           this.currentPosition );
    
        return token;
    }
    
    private Token operatorName()
    {
        Token token = null;
    
        switch ( LA(1) )
        {
            case 'a':
            {
                token = and();
                break;
            }
    
            case 'o':
            {
                token = or();
                break;
            }
    
            case 'm':
            {
                token = mod();
                break;
            }
    
            case 'd':
            {
                token = div();
                break;
            }
        }
    
        return token;
    }
    
    private Token mod()
    {
        Token token = null;
    
        if ( ( LA(1) == 'm' )
             &&
             ( LA(2) == 'o' )
             &&
             ( LA(3) == 'd' )
           )
        {
            token = new Token( TokenTypes.MOD,
                               getXPath(),
                               this.currentPosition,
                               this.currentPosition+3 );
    
            consume();
            consume();
            consume();
        }
    
        return token;
    }
    
    private Token div()
    {
        Token token = null;
    
        if ( ( LA(1) == 'd' )
             &&
             ( LA(2) == 'i' )
             &&
             ( LA(3) == 'v' )
            )
        {
            token = new Token( TokenTypes.DIV,
                               getXPath(),
                               this.currentPosition,
                               this.currentPosition+3 );
    
            consume();
            consume();
            consume();
        }
    
        return token;
    }
    
    private Token and()
    {
        Token token = null;
    
        if ( ( LA(1) == 'a' )
             &&
             ( LA(2) == 'n' )
             &&
             ( LA(3) == 'd' )
           )
        {
            token = new Token( TokenTypes.AND,
                               getXPath(),
                               this.currentPosition,
                               this.currentPosition+3 );
    
            consume();
            consume();
            consume();
        }
    
        return token;
    }
    
    private Token or()
    {
        Token token = null;
    
        if ( ( LA(1) == 'o' )
             &&
             ( LA(2) == 'r' )
           )
        {
            token = new Token( TokenTypes.OR,
                               getXPath(),
                               this.currentPosition,
                               this.currentPosition+2 );
    
            consume();
            consume();
        }
    
        return token;
    }
    
    private Token number()
    {
        int     start         = this.currentPosition;
        boolean periodAllowed = true;
    
      loop:
        while( true )
        {
            switch ( LA(1) )
            {
                case '.':
                    if ( periodAllowed )
                    {
                        periodAllowed = false;
                        consume();
                    }
                    else
                    {
                        break loop;
                    }
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    consume();
                    break;
                default:
                    break loop;
            }
        }
    
        return new Token( TokenTypes.DOUBLE,
                               getXPath(),
                               start,
                               this.currentPosition );
    }
    
    private Token whitespace()
    {
        consume();
            
      loop:
        while( hasMoreChars() )
        {
            switch ( LA(1) )
            {
                case ' ':
                case '\t':
                case '\n':
                case '\r':
                {
                    consume();
                    break;
                }
                    
                default:
                {
                    break loop;
                }
            }
        }
    
        return new Token( TokenTypes.SKIP,
                          getXPath(),
                          0,
                          0 );
    }
    
    private Token comma()
    {
        Token token = new Token( TokenTypes.COMMA,
                                 getXPath(),
                                 this.currentPosition,
                                 this.currentPosition+1 );
    
        consume();
    
        return token;
    }
    
    private Token equals()
    {
        Token token = new Token( TokenTypes.EQUALS,
                                 getXPath(),
                                 this.currentPosition,
                                 this.currentPosition+1 );
    
        consume();
    
        return token;
    }
    
    private Token minus()
    {
        Token token = new Token( TokenTypes.MINUS,
                                 getXPath(),
                                 this.currentPosition,
                                 this.currentPosition+1 );
        consume();
            
        return token;
    }
    
    private Token plus()
    {
        Token token = new Token( TokenTypes.PLUS,
                                 getXPath(),
                                 this.currentPosition,
                                 this.currentPosition+1 );
        consume();
    
        return token;
    }
    
    private Token dollar()
    {
        Token token = new Token( TokenTypes.DOLLAR,
                                 getXPath(),
                                 this.currentPosition,
                                 this.currentPosition+1 );
        consume();
    
        return token;
    }
    
    private Token pipe()
    {
        Token token = new Token( TokenTypes.PIPE,
                                 getXPath(),
                                 this.currentPosition,
                                 this.currentPosition+1 );
    
        consume();
    
        return token;
    }
    
    private Token at()
    {
        Token token = new Token( TokenTypes.AT,
                                 getXPath(),
                                 this.currentPosition,
                                 this.currentPosition+1 );
    
        consume();
    
        return token;
    }
    
    private Token colon()
    {
        Token token = new Token( TokenTypes.COLON,
                                 getXPath(),
                                 this.currentPosition,
                                 this.currentPosition+1 );
        consume();
    
        return token;
    }
    
    private Token doubleColon()
    {
        Token token = new Token( TokenTypes.DOUBLE_COLON,
                                 getXPath(),
                                 this.currentPosition,
                                 this.currentPosition+2 );
    
        consume();
        consume();
    
        return token;
    }
    
    private Token notEquals()
    {
        Token token = new Token( TokenTypes.NOT_EQUALS,
                                 getXPath(),
                                 this.currentPosition,
                                 this.currentPosition + 2 );
    
        consume();
        consume();
    
        return token;
    }
    
    private Token relationalOperator()
    {
        Token token = null;
    
        switch ( LA(1) )
        {
            case '<':
            {
                if ( LA(2) == '=' )
                {
                    token = new Token( TokenTypes.LESS_THAN_OR_EQUALS_SIGN,
                                       getXPath(),
                                       this.currentPosition,
                                       this.currentPosition + 2 );
                    consume();
                }
                else
                {
                    token = new Token( TokenTypes.LESS_THAN_SIGN,
                                       getXPath(),
                                       this.currentPosition,
                                       this.currentPosition + 1);
                }
    
                consume();
                break;
            }
            case '>':
            {
                if ( LA(2) == '=' )
                {
                    token = new Token( TokenTypes.GREATER_THAN_OR_EQUALS_SIGN,
                                       getXPath(),
                                       this.currentPosition,
                                       this.currentPosition + 2 );
                    consume();
                }
                else
                {
                    token = new Token( TokenTypes.GREATER_THAN_SIGN,
                                       getXPath(),
                                       this.currentPosition,
                                       this.currentPosition + 1 );
                }
    
                consume();
                break;
            }
        }
    
        return token;
                
    }
    
    // ????
    private Token star()
    {
        int tokenType = expectOperator ? TokenTypes.STAR_OPERATOR : TokenTypes.STAR;
         Token token = new Token( tokenType,
                         getXPath(),
                         this.currentPosition,
                         this.currentPosition+1 );
    
        consume();
            
        return token;
    }
    
    private Token literal()
    {
        Token token = null;
    
        char match  = LA(1);
    
        consume();
    
        int start = this.currentPosition;
            
        while ( ( token == null )
                &&
                hasMoreChars() )
        {
            if ( LA(1) == match )
            {
                token = new Token( TokenTypes.LITERAL,
                                   getXPath(),
                                   start,
                                   this.currentPosition );
            }
            consume();
        }
    
        return token;
    }
    
    private Token dots()
    {
        Token token = null;
    
        switch ( LA(2) )
        {
            case '.':
            {
                token = new Token( TokenTypes.DOT_DOT,
                                   getXPath(),
                                   this.currentPosition,
                                   this.currentPosition+2 ) ;
                consume();
                consume();
                break;
            }
            default:
            {
                token = new Token( TokenTypes.DOT,
                                   getXPath(),
                                   this.currentPosition,
                                   this.currentPosition+1 );
                consume();
                break;
            }
        }
    
        return token;
    }
    
    private Token leftBracket()
    {
        Token token = new Token( TokenTypes.LEFT_BRACKET,
                                 getXPath(),
                                 this.currentPosition,
                                 this.currentPosition+1 );
    
        consume();
    
        return token;
    }
    
    private Token rightBracket()
    {
        Token token = new Token( TokenTypes.RIGHT_BRACKET,
                                 getXPath(),
                                 this.currentPosition,
                                 this.currentPosition+1 );
    
        consume();
    
        return token;
    }
    
    private Token leftParen()
    {
        Token token = new Token( TokenTypes.LEFT_PAREN,
                                 getXPath(),
                                 this.currentPosition,
                                 this.currentPosition+1 );
    
        consume();
    
        return token;
    }
    
    private Token rightParen()
    {
        Token token = new Token( TokenTypes.RIGHT_PAREN,
                                 getXPath(),
                                 this.currentPosition,
                                 this.currentPosition+1 );
    
        consume();
    
        return token;
    }
    
    private Token slashes()
    {
        Token token = null;
    
        switch ( LA(2) )
        {
            case '/':
            {
                token = new Token( TokenTypes.DOUBLE_SLASH,
                                   getXPath(),
                                   this.currentPosition,
                                   this.currentPosition+2 );
                consume();
                consume();
                break;
            }
            default:
            {
                token = new Token( TokenTypes.SLASH,
                                   getXPath(),
                                   this.currentPosition,
                                   this.currentPosition+1 );
                consume();
            }
        }
    
        return token;
    }
    
    private char LA(int i) 
    {
        if ( currentPosition + ( i - 1 ) >= this.endPosition )
        {
            return (char) -1;
        }
    
        return getXPath().charAt( this.currentPosition + (i - 1) );
    }
    
    private void consume()
    {
        ++this.currentPosition;
    }
    
    private boolean hasMoreChars()
    {
        return this.currentPosition < this.endPosition;
    }

}
