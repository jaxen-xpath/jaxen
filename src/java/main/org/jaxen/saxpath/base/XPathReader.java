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
public class XPathReader extends TokenTypes implements org.jaxen.saxpath.XPathReader
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

        if ( LA(1) != EOF )
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
            case INTEGER:
            case DOUBLE:
            case LITERAL:
            {
                filterExpr();

                if ( LA(1) == SLASH || LA(1) == DOUBLE_SLASH )
                {
                    XPathSyntaxException ex = this.createSyntaxException("Node-set expected");
                    throw ex;
                }

                break;
            }                
            case LEFT_PAREN:
            case DOLLAR:
            {
                filterExpr();
                    
                if ( LA(1) == SLASH || LA(1) == DOUBLE_SLASH)
                {
                    locationPath( false );
                }
                break;
            }
            case IDENTIFIER:
            {

                if ( ( LA(2) == LEFT_PAREN
                     &&
                       ! isNodeTypeName( LT(1) ) )
                     ||
                    ( LA(2) == COLON
                      &&
                      LA(4) == LEFT_PAREN) ) 
                {
                    filterExpr();
                    
                    if ( LA(1) == SLASH || LA(1) == DOUBLE_SLASH)
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
            case DOT:
            case DOT_DOT:
            case STAR:
            case AT:
            {
                locationPath( false );
                break;
            }
            case SLASH:
            case DOUBLE_SLASH:
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
        Token token = match( DOUBLE );

        getXPathHandler().number( Double.parseDouble( token.getTokenText() ) );
    }

    private void numberInteger() throws SAXPathException
    {
        Token token = match( INTEGER );
        
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
        Token token = match( LITERAL );

        getXPathHandler().literal( token.getTokenText() );
    }

    private void functionCall() throws SAXPathException
    {
        String prefix       = null;
        String functionName = null;

        if ( LA(2) == COLON )
        {
            prefix = match( IDENTIFIER ).getTokenText();
            match( COLON );
        }
        else
        {
            prefix = "";
        }

        functionName = match( IDENTIFIER ).getTokenText();

        getXPathHandler().startFunction( prefix,
                                         functionName );

        match ( LEFT_PAREN );

        arguments();

        match ( RIGHT_PAREN );

        getXPathHandler().endFunction();
    }

    private void arguments() throws SAXPathException
    {
        while ( LA(1) != RIGHT_PAREN )
        {
            expr();

            if ( LA(1) == COMMA )
            {
                match( COMMA );
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
            case INTEGER:
            {
                numberInteger();
                break;
            }
            case DOUBLE:
            {
                numberDouble();
                break;
            }
            case LITERAL:
            {
                literal();
                break;
            }
            case LEFT_PAREN:
            {
                match( LEFT_PAREN );
                expr();
                match( RIGHT_PAREN );
                break;
            }
            case IDENTIFIER:
            {
                functionCall();
                break;
            }
            case DOLLAR:
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
        match( DOLLAR );

        String prefix       = null;
        String variableName = null;

        if ( LA(2) == COLON )
        {
            prefix = match( IDENTIFIER ).getTokenText();
            match( COLON );
        }
        else
        {
            prefix = "";
        }

        variableName = match( IDENTIFIER ).getTokenText();

        getXPathHandler().variableReference( prefix,
                                             variableName );
    }

    void locationPath(boolean isAbsolute) throws SAXPathException
    {
        switch ( LA(1) )
        {
            case SLASH:
            case DOUBLE_SLASH:
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
            case AT:
            case IDENTIFIER:
            case DOT:
            case DOT_DOT:
            case STAR:
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
            case SLASH:
            {
                match( SLASH );

                switch ( LA(1) )
                {

                    case DOT:
                    case DOT_DOT:
                    case AT:
                    case IDENTIFIER:
                    case STAR:
                    {
                        steps();
                        break;
                    }
                }
                break;
            }
            case DOUBLE_SLASH:
            {
                getXPathHandler().startAllNodeStep( Axis.DESCENDANT_OR_SELF );
                getXPathHandler().endAllNodeStep();

                match( DOUBLE_SLASH );
                switch ( LA(1) )
                {
                    case DOT:
                    case DOT_DOT:
                    case AT:
                    case IDENTIFIER:
                    case STAR:
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
            case SLASH:
            {
                match( SLASH );
                break;
            }
            case DOUBLE_SLASH:
            {
                getXPathHandler().startAllNodeStep( Axis.DESCENDANT_OR_SELF );
                getXPathHandler().endAllNodeStep();

                match( DOUBLE_SLASH );

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

            case DOT:
            case DOT_DOT:
            case AT:
            case IDENTIFIER:
            case STAR:
            {
                step();
                break;
            }
            case EOF:
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
            if ( ( LA(1) == SLASH)
                 ||
                 ( LA(1) == DOUBLE_SLASH ) )
            {
                switch ( LA(1) )
                {
                    case SLASH:
                    {
                        match( SLASH );
                        break;
                    }
                    case DOUBLE_SLASH:
                    {
                        getXPathHandler().startAllNodeStep( Axis.DESCENDANT_OR_SELF );
                        getXPathHandler().endAllNodeStep();

                        match( DOUBLE_SLASH );
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
                case DOT:
                case DOT_DOT:
                case AT:
                case IDENTIFIER:
                case STAR:
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
            case DOT:
            case DOT_DOT:
            {
                abbrStep();
                return;
            }
            case AT:
            {
                axis = axisSpecifier();
                break;
            }
            case IDENTIFIER:
            {
                if ( LA(2) == DOUBLE_COLON )
                {
                    axis = axisSpecifier();
                }
                else
                {
                    axis = Axis.CHILD;
                }
                break;
            }
            case STAR:
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
            case AT:
            {
                match( AT );
                axis = Axis.ATTRIBUTE;
                break;
            }
            case IDENTIFIER:
            {
                Token token = LT( 1 );

                axis = Axis.lookup( token.getTokenText() );

                if ( axis == Axis.INVALID_AXIS )
                {
                    throwInvalidAxis( token.getTokenText() );
                }

                match( IDENTIFIER );
                match( DOUBLE_COLON );

                break;
            }
        }

        return axis;
    }

    private void nodeTest(int axis) throws SAXPathException
    {
        switch ( LA(1) )
        {
            case IDENTIFIER:
            {
                switch ( LA(2) )
                {
                    case LEFT_PAREN:
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
            case STAR:
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
        Token  nodeTypeToken = match( IDENTIFIER );
        String nodeType      = nodeTypeToken.getTokenText();

        match( LEFT_PAREN );

        if ( "processing-instruction".equals( nodeType ) )
        {
            String piName = "";

            if ( LA(1) == LITERAL )
            {
                piName = match( LITERAL ).getTokenText();
            }

            match( RIGHT_PAREN );

            getXPathHandler().startProcessingInstructionNodeStep( axis,
                                                                  piName );

            predicates();

            getXPathHandler().endProcessingInstructionNodeStep();
        }
        else if ( "node".equals( nodeType ) )
        {
            match( RIGHT_PAREN );

            getXPathHandler().startAllNodeStep( axis );

            predicates();

            getXPathHandler().endAllNodeStep();
        }
        else if ( "text".equals( nodeType ) )
        {
            match( RIGHT_PAREN );

            getXPathHandler().startTextNodeStep( axis );

            predicates();

            getXPathHandler().endTextNodeStep();
        }
        else if ( "comment".equals( nodeType ) )
        {
            match( RIGHT_PAREN );

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
            case COLON:
            {
                switch ( LA(1) )
                {
                    case IDENTIFIER:
                    {
                        prefix = match( IDENTIFIER ).getTokenText();
                        match( COLON );
                        break;
                    }
                }
                break;
            }
        }
        
        switch ( LA(1) )
        {
            case IDENTIFIER:
            {
                localName = match( IDENTIFIER ).getTokenText();
                break;
            }
            case STAR:
            {
                match( STAR );
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
            case DOT:
            {
                match( DOT );
                getXPathHandler().startAllNodeStep( Axis.SELF );
                predicates();
                getXPathHandler().endAllNodeStep();
                break;
            }
            case DOT_DOT:
            {
                match( DOT_DOT );
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
            if ( LA(1) == LEFT_BRACKET )
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
        
        match( LEFT_BRACKET );
        
        predicateExpr();

        match( RIGHT_BRACKET );

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
            case OR:
            {
                create = true;
                match( OR );
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
            case AND:
            {
                create = true;
                match( AND );
                andExpr();
                break;
            }
        }

        getXPathHandler().endAndExpr( create );
    }

    private void equalityExpr() throws SAXPathException
    {
        getXPathHandler().startEqualityExpr();
        getXPathHandler().startEqualityExpr();

        relationalExpr();

        int operator = Operator.NO_OP;

        switch ( LA(1) )
        {
            case EQUALS:
            {
                match( EQUALS );
                relationalExpr();
                operator = Operator.EQUALS;
                break;
            }
            case NOT_EQUALS:
            {
                match( NOT_EQUALS );
                relationalExpr();
                operator = Operator.NOT_EQUALS;
                break;
            }
        }

        getXPathHandler().endEqualityExpr( operator );

        operator = Operator.NO_OP;

        switch ( LA(1) )
        {
            case EQUALS:
            {
                match( EQUALS );
                equalityExpr();
                operator = Operator.EQUALS;
                break;
            }
            case NOT_EQUALS:
            {
                match( NOT_EQUALS );
                equalityExpr();
                operator = Operator.NOT_EQUALS;
                break;
            }
        }

        getXPathHandler().endEqualityExpr( operator );
    }

    private void relationalExpr() throws SAXPathException
    {
        getXPathHandler().startRelationalExpr();
        getXPathHandler().startRelationalExpr();

        additiveExpr();

        int operator = Operator.NO_OP;

        switch ( LA(1) )
        {
            case LESS_THAN:
            {
                match( LESS_THAN );
                additiveExpr();
                operator = Operator.LESS_THAN;
                break;
            }
            case GREATER_THAN:
            {
                match( GREATER_THAN );
                additiveExpr();
                operator = Operator.GREATER_THAN;
                break;
            }
            case LESS_THAN_EQUALS:
            {
                match( LESS_THAN_EQUALS );
                additiveExpr();
                operator = Operator.LESS_THAN_EQUALS;
                break;
            }
            case GREATER_THAN_EQUALS:
            {
                match( GREATER_THAN_EQUALS );
                additiveExpr();
                operator = Operator.GREATER_THAN_EQUALS;
                break;
            }
        }

        getXPathHandler().endRelationalExpr( operator );

        operator = Operator.NO_OP;

        switch ( LA(1) )
        {
            case LESS_THAN:
            {
                match( LESS_THAN );
                relationalExpr();
                operator = Operator.LESS_THAN;
                break;
            }
            case GREATER_THAN:
            {
                match( GREATER_THAN );
                relationalExpr();
                operator = Operator.GREATER_THAN;
                break;
            }
            case LESS_THAN_EQUALS:
            {
                match( LESS_THAN_EQUALS );
                relationalExpr();
                operator = Operator.LESS_THAN_EQUALS;
                break;
            }
            case GREATER_THAN_EQUALS:
            {
                match( GREATER_THAN_EQUALS );
                relationalExpr();
                operator = Operator.GREATER_THAN_EQUALS;
                break;
            }
        }

        getXPathHandler().endRelationalExpr( operator );
    }

    private void additiveExpr() throws SAXPathException
    {
        multiplicativeExpr();

        int la = LA(1);
        while (la == PLUS || la == MINUS)
        {
            switch ( la )
            {
                case PLUS:
                {
                    match( PLUS );
                    getXPathHandler().startAdditiveExpr();
                    multiplicativeExpr();
                    getXPathHandler().endAdditiveExpr( Operator.ADD );
                    break;
                }
                case MINUS:
                {
                    match( MINUS );
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
        while (la == STAR || la == DIV || la == MOD)
        {
            switch ( LA(1) )
            {
                case STAR:
                {
                    match( STAR );
                    getXPathHandler().startMultiplicativeExpr();
                    unaryExpr();
                    getXPathHandler().endMultiplicativeExpr( Operator.MULTIPLY );
                    break;
                }
                case DIV:
                {
                    match( DIV );
                    getXPathHandler().startMultiplicativeExpr();
                    unaryExpr();
                    getXPathHandler().endMultiplicativeExpr( Operator.DIV );
                    break;
                }
                case MOD:
                {
                    match( MOD );
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
            case MINUS:
            {
                match( MINUS );
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
            case PIPE:
            {
                match( PIPE );
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

        throw createSyntaxException( "Expected: " + getTokenText( tokenType ) );
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
