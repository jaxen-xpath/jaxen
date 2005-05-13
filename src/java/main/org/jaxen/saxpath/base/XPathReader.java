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
import org.jaxen.saxpath.XPathHandler;
import org.jaxen.saxpath.XPathSyntaxException;

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

    /**
     * Create a new <code>XPathReader</code> with a do-nothing
     * <code>XPathHandler</code>.
     */
    public XPathReader()
    {
        setXPathHandler( DefaultXPathHandler.getInstance() );
    }

    public void setXPathHandler(XPathHandler handler)
    {
        this.handler = handler;
    }

    public XPathHandler getXPathHandler()
    {
        return this.handler;
    }

    public void parse(String xpath) throws org.jaxen.saxpath.SAXPathException
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

    void pathExpr() throws org.jaxen.saxpath.SAXPathException
    {
        getXPathHandler().startPathExpr();

        switch ( LA(1) )
        {
            case INTEGER:
            case DOUBLE:
            case LITERAL:
            // FIXME parentheses should be allowed when content of parentheses evaluates to a node-set
            case LEFT_PAREN:
            // case DOLLAR:
            {
                filterExpr();

                if ( LA(1) == SLASH || LA(1) == DOUBLE_SLASH )
                {
                    XPathSyntaxException ex = this.createSyntaxException("Node-set expected");
                    throw ex;
                }

                break;
            }
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

                    break;
                }
                else
                {
                    locationPath( false );
                    break;
                }
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

    void numberDouble() throws org.jaxen.saxpath.SAXPathException
    {
        Token token = match( DOUBLE );

        getXPathHandler().number( Double.parseDouble( token.getTokenText() ) );
    }

    void numberInteger() throws org.jaxen.saxpath.SAXPathException
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

    void literal() throws org.jaxen.saxpath.SAXPathException
    {
        Token token = match( LITERAL );

        getXPathHandler().literal( token.getTokenText() );
    }

    void functionCall() throws org.jaxen.saxpath.SAXPathException
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

    void arguments() throws org.jaxen.saxpath.SAXPathException
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

    void filterExpr() throws org.jaxen.saxpath.SAXPathException
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

    void variableReference() throws org.jaxen.saxpath.SAXPathException
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

    void locationPath(boolean isAbsolute) throws org.jaxen.saxpath.SAXPathException
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

    void absoluteLocationPath() throws org.jaxen.saxpath.SAXPathException
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

    void relativeLocationPath() throws org.jaxen.saxpath.SAXPathException
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

    void steps() throws org.jaxen.saxpath.SAXPathException
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

    void step() throws org.jaxen.saxpath.SAXPathException
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

    int axisSpecifier() throws org.jaxen.saxpath.SAXPathException
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

    void nodeTest(int axis) throws org.jaxen.saxpath.SAXPathException
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

    void nodeTypeTest(int axis) throws org.jaxen.saxpath.SAXPathException
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

    void nameTest(int axis) throws org.jaxen.saxpath.SAXPathException
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

    void abbrStep() throws org.jaxen.saxpath.SAXPathException
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

    void predicates() throws org.jaxen.saxpath.SAXPathException
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
    
    void predicate() throws org.jaxen.saxpath.SAXPathException
    {
        getXPathHandler().startPredicate();
        
        match( LEFT_BRACKET );
        
        predicateExpr();

        match( RIGHT_BRACKET );

        getXPathHandler().endPredicate();
    }

    void predicateExpr() throws org.jaxen.saxpath.SAXPathException
    {
        expr();
    }

    void expr() throws org.jaxen.saxpath.SAXPathException
    {
        orExpr();
    }

    void orExpr() throws org.jaxen.saxpath.SAXPathException
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

    void andExpr() throws org.jaxen.saxpath.SAXPathException
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

    void equalityExpr() throws org.jaxen.saxpath.SAXPathException
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

    void relationalExpr() throws org.jaxen.saxpath.SAXPathException
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

    void additiveExpr() throws org.jaxen.saxpath.SAXPathException
    {
        getXPathHandler().startAdditiveExpr();
        getXPathHandler().startAdditiveExpr();

        multiplicativeExpr();

        int operator = Operator.NO_OP;

        switch ( LA(1) )
        {
            case PLUS:
            {
                match( PLUS );
                operator = Operator.ADD;
                multiplicativeExpr();
                break;
            }
            case MINUS:
            {
                match( MINUS );
                operator = Operator.SUBTRACT;
                multiplicativeExpr();
                break;
            }
        }

        getXPathHandler().endAdditiveExpr( operator );


        operator = Operator.NO_OP; 
        switch ( LA(1) )
        {
            case PLUS:
            {
                match( PLUS );
                operator = Operator.ADD;
                additiveExpr();
                break;
            }
            case MINUS:
            {
                match( MINUS );
                operator = Operator.SUBTRACT;
                additiveExpr();
                break;
            }
            default:
            {
                operator = Operator.NO_OP;
                break;
            }
        }

        getXPathHandler().endAdditiveExpr( operator );
    }

    void multiplicativeExpr() throws org.jaxen.saxpath.SAXPathException
    {
        getXPathHandler().startMultiplicativeExpr();
        getXPathHandler().startMultiplicativeExpr();

        unaryExpr();

        int operator = Operator.NO_OP;

        switch ( LA(1) )
        {
            case STAR:
            {
                match( STAR );
                unaryExpr();
                operator = Operator.MULTIPLY;
                break;                
            }
            case DIV:
            {
                match( DIV );
                unaryExpr();
                operator = Operator.DIV;
                break;
            }
            case MOD:
            {
                match( MOD );
                unaryExpr();
                operator = Operator.MOD;
                break;
            }
        }

        getXPathHandler().endMultiplicativeExpr( operator );

        operator = Operator.NO_OP;

        switch ( LA(1) )
        {
            case STAR:
            {
                match( STAR );
                multiplicativeExpr();
                operator = Operator.MULTIPLY;
                break;
            }
            case DIV:
            {
                match( DIV );
                multiplicativeExpr();
                operator = Operator.DIV;
                break;
            }
            case MOD:
            {
                match( MOD );
                multiplicativeExpr();
                operator = Operator.MOD;
                break;
            }
        }

        getXPathHandler().endMultiplicativeExpr( operator );
    }

    void unaryExpr() throws org.jaxen.saxpath.SAXPathException
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

    void unionExpr() throws org.jaxen.saxpath.SAXPathException
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

    Token match(int tokenType) throws XPathSyntaxException
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

    int LA(int position)
    {
        return LT(position).getTokenType();
    }

    Token LT(int position)
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

    boolean isNodeTypeName(Token name)
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

    XPathSyntaxException createSyntaxException(String message)
    {
        String xpath    = this.lexer.getXPath();
        int    position = LT(1).getTokenBegin();

        return new XPathSyntaxException( xpath,
                                         position,
                                         message );
    }

    void throwInvalidAxis(String invalidAxis) throws org.jaxen.saxpath.SAXPathException
    {
        String xpath    = this.lexer.getXPath();
        int    position = LT(1).getTokenBegin();

        String message  = "Expected valid axis name instead of [" + invalidAxis + "]";

        throw new XPathSyntaxException( xpath,
                                        position,
                                        message );
    }

    void throwUnexpected() throws org.jaxen.saxpath.SAXPathException
    {
        throw createSyntaxException( "Unexpected '" + LT(1).getTokenText() + "'" );
    }
}
