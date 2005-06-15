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
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED TokenTypes.OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE Jaxen AUTHORS TokenTypes.OR THE PROJECT
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, TokenTypes.OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS TokenTypes.OR SERVICES; LOSS OF
 * USE, DATA, TokenTypes.OR PROFITS; TokenTypes.OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * TokenTypes.OR TORT (INCLUDING NEGLIGENCE TokenTypes.OR OTHERWISE) ARISING IN ANY WAY OUT
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

import java.util.LinkedList;

import org.jaxen.saxpath.Axis;
import org.jaxen.saxpath.Operator;
import org.jaxen.saxpath.SAXPathException;
import org.jaxen.saxpath.XPathHandler;
import org.jaxen.saxpath.XPathSyntaxException;
import org.jaxen.saxpath.helpers.DefaultXPathHandler;

/** Implementation of SAXPath's <code>XPathReader</code> which
 *  generates callbacks to an <code>XPathHandler</code>.
 *
 *  @author bob mcwhirter (bob@werken.com)
 */
public class XPathReader implements org.jaxen.saxpath.XPathReader
{
    private LinkedList tokens;
    private XPathLexer lexer;

    private XPathHandler handler;
    
    private static XPathHandler defaultHandler = new DefaultXPathHandler();

    /**
     * Create a new <code>XPathReader</code> with a do-nothing
     * <code>XPathHandler</code>.
     */
    public XPathReader()
    {
        setXPathHandler( defaultHandler );
    }

    public void setXPathHandler(XPathHandler handler)
    {
        this.handler = handler;
    }

    public XPathHandler getXPathHandler()
    {
        return this.handler;
    }

    public void parse(String xpath) throws SAXPathException
    {
        setUpParse( xpath );

        getXPathHandler().startXPath();

        expr();

        getXPathHandler().endXPath();

        if ( LA(1) != TokenTypes.EOF )
        {
            throwUnexpected();
        }

        lexer  = null;
        tokens = null;
    }

    void setUpParse(String xpath)
    {
        this.tokens = new LinkedList();
        this.lexer = new XPathLexer( xpath );
    }

    private void pathExpr() throws SAXPathException
    {
        getXPathHandler().startPathExpr();

        switch ( LA(1) )
        {
            case TokenTypes.INTEGER:
            case TokenTypes.DOUBLE:
            case TokenTypes.LITERAL:
            {
                filterExpr();

                if ( LA(1) == TokenTypes.SLASH || LA(1) == TokenTypes.DOUBLE_SLASH )
                {
                    XPathSyntaxException ex = this.createSyntaxException("Node-set expected");
                    throw ex;
                }

                break;
            }                
            case TokenTypes.LEFT_PAREN:
            case TokenTypes.DOLLAR:
            {
                filterExpr();
                    
                if ( LA(1) == TokenTypes.SLASH || LA(1) == TokenTypes.DOUBLE_SLASH)
                {
                    locationPath( false );
                }
                break;
            }
            case TokenTypes.IDENTIFIER:
            {

                if ( ( LA(2) == TokenTypes.LEFT_PAREN
                     &&
                       ! isNodeTypeName( LT(1) ) )
                     ||
                    ( LA(2) == TokenTypes.COLON
                      &&
                      LA(4) == TokenTypes.LEFT_PAREN) ) 
                {
                    filterExpr();
                    
                    if ( LA(1) == TokenTypes.SLASH || LA(1) == TokenTypes.DOUBLE_SLASH)
                    {
                        locationPath( false );
                    }
                }
                else
                {
                    locationPath( false );
                }
                break;
            }
            case TokenTypes.DOT:
            case TokenTypes.DOT_DOT:
            case TokenTypes.STAR:
            case TokenTypes.AT:
            {
                locationPath( false );
                break;
            }
            case TokenTypes.SLASH:
            case TokenTypes.DOUBLE_SLASH:
            {
                locationPath( true );
                break;
            }
            default:
            {
                throwUnexpected();
            }
        }

        getXPathHandler().endPathExpr();
    }

    private void numberDouble() throws SAXPathException
    {
        Token token = match( TokenTypes.DOUBLE );

        getXPathHandler().number( Double.parseDouble( token.getTokenText() ) );
    }

    private void numberInteger() throws SAXPathException
    {
        Token token = match( TokenTypes.INTEGER );
        
        String text = token.getTokenText();
        try {
            getXPathHandler().number( Integer.parseInt( text ) );
        }
        catch (NumberFormatException ex) {
            getXPathHandler().number( Double.parseDouble( text ) );
        }
        
    }

    private void literal() throws SAXPathException
    {
        Token token = match( TokenTypes.LITERAL );

        getXPathHandler().literal( token.getTokenText() );
    }

    private void functionCall() throws SAXPathException
    {
        String prefix       = null;
        String functionName = null;

        if ( LA(2) == TokenTypes.COLON )
        {
            prefix = match( TokenTypes.IDENTIFIER ).getTokenText();
            match( TokenTypes.COLON );
        }
        else
        {
            prefix = "";
        }

        functionName = match( TokenTypes.IDENTIFIER ).getTokenText();

        getXPathHandler().startFunction( prefix,
                                         functionName );

        match ( TokenTypes.LEFT_PAREN );

        arguments();

        match ( TokenTypes.RIGHT_PAREN );

        getXPathHandler().endFunction();
    }

    private void arguments() throws SAXPathException
    {
        while ( LA(1) != TokenTypes.RIGHT_PAREN )
        {
            expr();

            if ( LA(1) == TokenTypes.COMMA )
            {
                match( TokenTypes.COMMA );
            }
            else
            {
                break;
            }
        }
    }

    private void filterExpr() throws SAXPathException
    {

        getXPathHandler().startFilterExpr();

        switch ( LA(1) )
        {
            case TokenTypes.INTEGER:
            {
                numberInteger();
                break;
            }
            case TokenTypes.DOUBLE:
            {
                numberDouble();
                break;
            }
            case TokenTypes.LITERAL:
            {
                literal();
                break;
            }
            case TokenTypes.LEFT_PAREN:
            {
                match( TokenTypes.LEFT_PAREN );
                expr();
                match( TokenTypes.RIGHT_PAREN );
                break;
            }
            case TokenTypes.IDENTIFIER:
            {
                functionCall();
                break;
            }
            case TokenTypes.DOLLAR:
            {
                variableReference();
                break;
            }
        }

        predicates();

        getXPathHandler().endFilterExpr();
    }

    private void variableReference() throws SAXPathException
    {
        match( TokenTypes.DOLLAR );

        String prefix       = null;
        String variableName = null;

        if ( LA(2) == TokenTypes.COLON )
        {
            prefix = match( TokenTypes.IDENTIFIER ).getTokenText();
            match( TokenTypes.COLON );
        }
        else
        {
            prefix = "";
        }

        variableName = match( TokenTypes.IDENTIFIER ).getTokenText();

        getXPathHandler().variableReference( prefix,
                                             variableName );
    }

    void locationPath(boolean isAbsolute) throws SAXPathException
    {
        switch ( LA(1) )
        {
            case TokenTypes.SLASH:
            case TokenTypes.DOUBLE_SLASH:
            {
                if ( isAbsolute )
                {
                    absoluteLocationPath();
                }
                else
                {
                    relativeLocationPath();
                }
                break;
            }
            case TokenTypes.AT:
            case TokenTypes.IDENTIFIER:
            case TokenTypes.DOT:
            case TokenTypes.DOT_DOT:
            case TokenTypes.STAR:
            {
                relativeLocationPath();
                break;
            }
            default:
            {
                throwUnexpected();
                break;
            }
        }
    }

    private void absoluteLocationPath() throws SAXPathException
    {
        getXPathHandler().startAbsoluteLocationPath();

        switch ( LA(1) )
        {
            case TokenTypes.SLASH:
            {
                match( TokenTypes.SLASH );

                switch ( LA(1) )
                {

                    case TokenTypes.DOT:
                    case TokenTypes.DOT_DOT:
                    case TokenTypes.AT:
                    case TokenTypes.IDENTIFIER:
                    case TokenTypes.STAR:
                    {
                        steps();
                        break;
                    }
                }
                break;
            }
            case TokenTypes.DOUBLE_SLASH:
            {
                getXPathHandler().startAllNodeStep( Axis.DESCENDANT_OR_SELF );
                getXPathHandler().endAllNodeStep();

                match( TokenTypes.DOUBLE_SLASH );
                switch ( LA(1) )
                {
                    case TokenTypes.DOT:
                    case TokenTypes.DOT_DOT:
                    case TokenTypes.AT:
                    case TokenTypes.IDENTIFIER:
                    case TokenTypes.STAR:
                    {
                        steps();
                        break;
                    }
                    default:
                        XPathSyntaxException ex = this.createSyntaxException("Location path cannot end with //");
                        throw ex;
                }
                break;
            }
        }
        
        getXPathHandler().endAbsoluteLocationPath();
    }

    private void relativeLocationPath() throws SAXPathException
    {
        getXPathHandler().startRelativeLocationPath();

        switch ( LA(1) )
        {
            case TokenTypes.SLASH:
            {
                match( TokenTypes.SLASH );
                break;
            }
            case TokenTypes.DOUBLE_SLASH:
            {
                getXPathHandler().startAllNodeStep( Axis.DESCENDANT_OR_SELF );
                getXPathHandler().endAllNodeStep();

                match( TokenTypes.DOUBLE_SLASH );

                break;
            }
        }

        steps();

        getXPathHandler().endRelativeLocationPath();
    }

    private void steps() throws SAXPathException
    {
        switch ( LA(1) )
        {

            case TokenTypes.DOT:
            case TokenTypes.DOT_DOT:
            case TokenTypes.AT:
            case TokenTypes.IDENTIFIER:
            case TokenTypes.STAR:
            {
                step();
                break;
            }
            case TokenTypes.EOF:
            {
                return;
            }
            default:
            {
                throw createSyntaxException( "Expected one of '.', '..', '@', '*', <QName>" );
            }
        }

        do
        {
            if ( ( LA(1) == TokenTypes.SLASH)
                 ||
                 ( LA(1) == TokenTypes.DOUBLE_SLASH ) )
            {
                switch ( LA(1) )
                {
                    case TokenTypes.SLASH:
                    {
                        match( TokenTypes.SLASH );
                        break;
                    }
                    case TokenTypes.DOUBLE_SLASH:
                    {
                        getXPathHandler().startAllNodeStep( Axis.DESCENDANT_OR_SELF );
                        getXPathHandler().endAllNodeStep();

                        match( TokenTypes.DOUBLE_SLASH );
                        break;
                    }
                }
            }
            else
            {
                return;
            }
            
            switch ( LA(1) )
            {
                case TokenTypes.DOT:
                case TokenTypes.DOT_DOT:
                case TokenTypes.AT:
                case TokenTypes.IDENTIFIER:
                case TokenTypes.STAR:
                {
                    step();
                    break;
                }
                default:
                {
                    throw createSyntaxException( "Expected one of '.', '..', '@', '*', <QName>" );
                }
            }

        } while ( true );
    }

    void step() throws SAXPathException
    {
        int axis = 0;

        switch ( LA(1) )
        {
            case TokenTypes.DOT:
            case TokenTypes.DOT_DOT:
            {
                abbrStep();
                return;
            }
            case TokenTypes.AT:
            {
                axis = axisSpecifier();
                break;
            }
            case TokenTypes.IDENTIFIER:
            {
                if ( LA(2) == TokenTypes.DOUBLE_COLON )
                {
                    axis = axisSpecifier();
                }
                else
                {
                    axis = Axis.CHILD;
                }
                break;
            }
            case TokenTypes.STAR:
            {
                axis = Axis.CHILD;
                break;
            }
        }

        nodeTest( axis );
    }

    private int axisSpecifier() throws SAXPathException
    {
        int axis = 0;

        switch ( LA(1) )
        {
            case TokenTypes.AT:
            {
                match( TokenTypes.AT );
                axis = Axis.ATTRIBUTE;
                break;
            }
            case TokenTypes.IDENTIFIER:
            {
                Token token = LT( 1 );

                axis = Axis.lookup( token.getTokenText() );

                if ( axis == Axis.INVALID_AXIS )
                {
                    throwInvalidAxis( token.getTokenText() );
                }

                match( TokenTypes.IDENTIFIER );
                match( TokenTypes.DOUBLE_COLON );

                break;
            }
        }

        return axis;
    }

    private void nodeTest(int axis) throws SAXPathException
    {
        switch ( LA(1) )
        {
            case TokenTypes.IDENTIFIER:
            {
                switch ( LA(2) )
                {
                    case TokenTypes.LEFT_PAREN:
                    {
                        nodeTypeTest( axis );
                        break;
                    }
                    default:
                    {
                        nameTest( axis );
                        break;
                    }
                }
                break;
            }
            case TokenTypes.STAR:
            {
                nameTest( axis );
                break;
            }
            default:
                throw createSyntaxException("Expected <QName> or *");
        }
    }

    private void nodeTypeTest(int axis) throws SAXPathException
    {
        Token  nodeTypeToken = match( TokenTypes.IDENTIFIER );
        String nodeType      = nodeTypeToken.getTokenText();

        match( TokenTypes.LEFT_PAREN );

        if ( "processing-instruction".equals( nodeType ) )
        {
            String piName = "";

            if ( LA(1) == TokenTypes.LITERAL )
            {
                piName = match( TokenTypes.LITERAL ).getTokenText();
            }

            match( TokenTypes.RIGHT_PAREN );

            getXPathHandler().startProcessingInstructionNodeStep( axis,
                                                                  piName );

            predicates();

            getXPathHandler().endProcessingInstructionNodeStep();
        }
        else if ( "node".equals( nodeType ) )
        {
            match( TokenTypes.RIGHT_PAREN );

            getXPathHandler().startAllNodeStep( axis );

            predicates();

            getXPathHandler().endAllNodeStep();
        }
        else if ( "text".equals( nodeType ) )
        {
            match( TokenTypes.RIGHT_PAREN );

            getXPathHandler().startTextNodeStep( axis );

            predicates();

            getXPathHandler().endTextNodeStep();
        }
        else if ( "comment".equals( nodeType ) )
        {
            match( TokenTypes.RIGHT_PAREN );

            getXPathHandler().startCommentNodeStep( axis );

            predicates();

            getXPathHandler().endCommentNodeStep();
        }
        else
        {
            throw createSyntaxException( "Expected node-type" );
        }
    }

    private void nameTest(int axis) throws SAXPathException
    {
        String prefix    = null;
        String localName = null;

        switch ( LA(2) )
        {
            case TokenTypes.COLON:
            {
                switch ( LA(1) )
                {
                    case TokenTypes.IDENTIFIER:
                    {
                        prefix = match( TokenTypes.IDENTIFIER ).getTokenText();
                        match( TokenTypes.COLON );
                        break;
                    }
                }
                break;
            }
        }
        
        switch ( LA(1) )
        {
            case TokenTypes.IDENTIFIER:
            {
                localName = match( TokenTypes.IDENTIFIER ).getTokenText();
                break;
            }
            case TokenTypes.STAR:
            {
                match( TokenTypes.STAR );
                localName = "*";
                break;
            }
        }

        if ( prefix == null )
        {
            prefix = "";
        }
        
        getXPathHandler().startNameStep( axis,
                                         prefix,
                                         localName );

        predicates();

        getXPathHandler().endNameStep();
    }

    private void abbrStep() throws SAXPathException
    {
        switch ( LA(1) )
        {
            case TokenTypes.DOT:
            {
                match( TokenTypes.DOT );
                getXPathHandler().startAllNodeStep( Axis.SELF );
                predicates();
                getXPathHandler().endAllNodeStep();
                break;
            }
            case TokenTypes.DOT_DOT:
            {
                match( TokenTypes.DOT_DOT );
                getXPathHandler().startAllNodeStep( Axis.PARENT );
                predicates();
                getXPathHandler().endAllNodeStep();
                break;
            }
        }
    }

    private void predicates() throws SAXPathException
    {
        while (true )
        {
            if ( LA(1) == TokenTypes.LEFT_BRACKET )
            {
                predicate();
            }
            else
            {
                break;
            }
        }
    }
    
    void predicate() throws SAXPathException
    {
        getXPathHandler().startPredicate();
        
        match( TokenTypes.LEFT_BRACKET );
        
        predicateExpr();

        match( TokenTypes.RIGHT_BRACKET );

        getXPathHandler().endPredicate();
    }

    private void predicateExpr() throws SAXPathException
    {
        expr();
    }

    private void expr() throws SAXPathException
    {
        orExpr();
    }

    private void orExpr() throws SAXPathException
    {
        getXPathHandler().startOrExpr();
        
        andExpr();

        boolean create = false;

        switch ( LA(1) )
        {
            case TokenTypes.OR:
            {
                create = true;
                match( TokenTypes.OR );
                orExpr();
                break;
            }
        }

        getXPathHandler().endOrExpr( create );
    }

    private void andExpr() throws SAXPathException
    {
        getXPathHandler().startAndExpr();

        equalityExpr();

        boolean create = false;

        switch ( LA(1) )
        {
            case TokenTypes.AND:
            {
                create = true;
                match( TokenTypes.AND );
                andExpr();
                break;
            }
        }

        getXPathHandler().endAndExpr( create );
    }

    private void equalityExpr() throws SAXPathException
    {
        relationalExpr();

        int la = LA(1);
        while (la == TokenTypes.EQUALS || la == TokenTypes.NOT_EQUALS)
        {
            switch ( la )
            {
                case TokenTypes.EQUALS:
                {
                    match( TokenTypes.EQUALS );
                    getXPathHandler().startEqualityExpr();
                    relationalExpr();
                    getXPathHandler().endEqualityExpr( Operator.EQUALS );
                    break;
                }
                case TokenTypes.NOT_EQUALS:
                {
                    match( TokenTypes.NOT_EQUALS );
                    getXPathHandler().startEqualityExpr();
                    relationalExpr();
                    getXPathHandler().endEqualityExpr( Operator.NOT_EQUALS );
                    break;
                }
            }
            la = LA(1);
        }
    }
    
    private void relationalExpr() throws SAXPathException
    {

        additiveExpr();

        int la = LA(1);
        // Very important: TokenTypes.LESS_THAN != Operator.LESS_THAN
        //                 TokenTypes.GREATER_THAN != Operator.GREATER_THAN
        //                 TokenTypes.GREATER_THAN_EQUALS != Operator.GREATER_THAN_EQUALS
        //                 TokenTypes.LESS_THAN_EQUALS != Operator.LESS_THAN_EQUALS
        while (la == TokenTypes.LESS_THAN_SIGN 
            || la == TokenTypes.GREATER_THAN_SIGN 
            || la == TokenTypes.LESS_THAN_OR_EQUALS_SIGN 
            || la == TokenTypes.GREATER_THAN_OR_EQUALS_SIGN ) {
            switch ( la )
            {
                case TokenTypes.LESS_THAN_SIGN:
                {
                    match( TokenTypes.LESS_THAN_SIGN );
                    getXPathHandler().startRelationalExpr();
                    additiveExpr();
                    getXPathHandler().endRelationalExpr( Operator.LESS_THAN );
                    break;
                }
                case TokenTypes.GREATER_THAN_SIGN:
                {
                    match( TokenTypes.GREATER_THAN_SIGN );
                    getXPathHandler().startRelationalExpr();
                    additiveExpr();
                    getXPathHandler().endRelationalExpr( Operator.GREATER_THAN );
                    break;
                }
                case TokenTypes.GREATER_THAN_OR_EQUALS_SIGN:
                {
                    match( TokenTypes.GREATER_THAN_OR_EQUALS_SIGN );
                    getXPathHandler().startRelationalExpr();
                    additiveExpr();
                    getXPathHandler().endRelationalExpr( Operator.GREATER_THAN_EQUALS );
                    break;
                }
                case TokenTypes.LESS_THAN_OR_EQUALS_SIGN:
                {
                    match( TokenTypes.LESS_THAN_OR_EQUALS_SIGN );
                    getXPathHandler().startRelationalExpr();
                    additiveExpr();
                    getXPathHandler().endRelationalExpr( Operator.LESS_THAN_EQUALS );
                    break;
                }
            }
            la = LA(1);
        }
    } 

    
    private void additiveExpr() throws SAXPathException
    {
        multiplicativeExpr();

        int la = LA(1);
        while (la == TokenTypes.PLUS || la == TokenTypes.MINUS)
        {
            switch ( la )
            {
                case TokenTypes.PLUS:
                {
                    match( TokenTypes.PLUS );
                    getXPathHandler().startAdditiveExpr();
                    multiplicativeExpr();
                    getXPathHandler().endAdditiveExpr( Operator.ADD );
                    break;
                }
                case TokenTypes.MINUS:
                {
                    match( TokenTypes.MINUS );
                    getXPathHandler().startAdditiveExpr();
                    multiplicativeExpr();
                    getXPathHandler().endAdditiveExpr( Operator.SUBTRACT );
                    break;
                }
            }
            la = LA(1);
        }
    }

    private void multiplicativeExpr() throws SAXPathException
    {
        unaryExpr();

        int la = LA(1);
        while (la == TokenTypes.STAR || la == TokenTypes.DIV || la == TokenTypes.MOD)
        {
            switch ( la )
            {
                case TokenTypes.STAR:
                {
                    match( TokenTypes.STAR );
                    getXPathHandler().startMultiplicativeExpr();
                    unaryExpr();
                    getXPathHandler().endMultiplicativeExpr( Operator.MULTIPLY );
                    break;
                }
                case TokenTypes.DIV:
                {
                    match( TokenTypes.DIV );
                    getXPathHandler().startMultiplicativeExpr();
                    unaryExpr();
                    getXPathHandler().endMultiplicativeExpr( Operator.DIV );
                    break;
                }
                case TokenTypes.MOD:
                {
                    match( TokenTypes.MOD );
                    getXPathHandler().startMultiplicativeExpr();
                    unaryExpr();
                    getXPathHandler().endMultiplicativeExpr( Operator.MOD );
                    break;
                }
            }
            la = LA(1);
        }

    }

    private void unaryExpr() throws SAXPathException
    {
        getXPathHandler().startUnaryExpr();

        int operator = Operator.NO_OP;

        switch ( LA(1) )
        {
            case TokenTypes.MINUS:
            {
                match( TokenTypes.MINUS );
                operator = Operator.NEGATIVE;
                unaryExpr();
                break;
            }
            default:
            {
                unionExpr();
                break;
            }
        }

        getXPathHandler().endUnaryExpr( operator );
    }

    private void unionExpr() throws SAXPathException
    {
        getXPathHandler().startUnionExpr();

        pathExpr();

        boolean create = false;

        switch ( LA(1) )
        {
            case TokenTypes.PIPE:
            {
                match( TokenTypes.PIPE );
                create = true;
                expr();
                break;
            }
        }

        getXPathHandler().endUnionExpr( create );
    }

    private Token match(int tokenType) throws XPathSyntaxException
    {
        LT(1);

        Token token = (Token) tokens.get( 0 );

        if ( token.getTokenType() == tokenType )
        {
            tokens.removeFirst();
            return token;
        }

        throw createSyntaxException( "Expected: " + TokenTypes.getTokenText( tokenType ) );
    }

    private int LA(int position)
    {
        return LT(position).getTokenType();
    }

    private Token LT(int position)
    {
        if ( tokens.size() <= ( position - 1 ) )
        {
            for ( int i = 0 ; i < position ; ++i )
            {
                tokens.add( lexer.nextToken() );
            }
        }

        return (Token) tokens.get( position - 1 );
    }

    private boolean isNodeTypeName(Token name)
    {
        String text = name.getTokenText();

        if ( "node".equals( text )
             ||
             "comment".equals( text )
             ||
             "text".equals( text )
             ||
             "processing-instruction".equals( text ) )
        {
            return true;
        }

        return false;
    }

    private XPathSyntaxException createSyntaxException(String message)
    {
        String xpath    = this.lexer.getXPath();
        int    position = LT(1).getTokenBegin();

        return new XPathSyntaxException( xpath,
                                         position,
                                         message );
    }

    private void throwInvalidAxis(String invalidAxis) throws SAXPathException
    {
        String xpath    = this.lexer.getXPath();
        int    position = LT(1).getTokenBegin();

        String message  = "Expected valid axis name instead of [" + invalidAxis + "]";

        throw new XPathSyntaxException( xpath,
                                        position,
                                        message );
    }

    private void throwUnexpected() throws SAXPathException
    {
        throw createSyntaxException( "Unexpected '" + LT(1).getTokenText() + "'" );
    }
}
