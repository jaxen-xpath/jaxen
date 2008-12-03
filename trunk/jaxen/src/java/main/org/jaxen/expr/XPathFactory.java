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
package org.jaxen.expr;

import org.jaxen.JaxenException;

/**
 * An abstract factory used to create individual path component objects.
 *
 */
public interface XPathFactory
{
    
    /**
     * Create a new <code>XPathExpr</code> from an <code>Expr</code>.
     * 
     * @param rootExpr the expression wrapped by the resulting XPathExpr
     * @return an XPathExpr wrapping the root expression
     * @throws JaxenException
     */
    XPathExpr createXPath( Expr rootExpr ) throws JaxenException;

    /**
     * Create a new path expression.
     * 
     * @param filterExpr the filter expression that starts the path expression
     * @param locationPath the location path that follows the filter expression
     * @return a path expression formed by concatenating the two arguments
     * @throws JaxenException
     */
    PathExpr createPathExpr( FilterExpr filterExpr,
                             LocationPath locationPath ) throws JaxenException;

    /**
     * Create a new empty relative location path.
     * 
     * @return an empty relative location path
     * @throws JaxenException
     */
    LocationPath createRelativeLocationPath() throws JaxenException;

    /**
     * Create a new empty absolute location path.
     * 
     * @return an empty absolute location path
     * @throws JaxenException
     */
    LocationPath createAbsoluteLocationPath() throws JaxenException;

    /**
     * Returns a new XPath Or expression.  
     * 
     * @param lhs the left hand side of the expression
     * @param rhs the right hand side of the expression
     * @return <code><i>lhs</i> or <i>rhs</i></code>
     * @throws JaxenException
     */
    BinaryExpr createOrExpr( Expr lhs,
                             Expr rhs ) throws JaxenException;

    /**
     * Returns a new XPath And expression.  
     * 
     * @param lhs the left hand side of the expression
     * @param rhs the right hand side of the expression
     * @return <code><i>lhs</i> and <i>rhs</i></code>
     * @throws JaxenException
     */
    BinaryExpr createAndExpr( Expr lhs,
                              Expr rhs ) throws JaxenException;

    /**
     * Returns a new XPath equality expression.  
     * 
     * @param lhs the left hand side of the expression
     * @param rhs the right hand side of the expression
     * @param equalityOperator <code>Operator.EQUALS</code> or <code>Operator.NOT_EQUALS</code>
     * @return <code><i>lhs</i> = <i>rhs</i></code> or <code><i>lhs</i> != <i>rhs</i></code>
     * @throws JaxenException if the third argument is not 
     *                        <code>Operator.EQUALS</code> or <code>Operator.NOT_EQUALS</code>
     */
    BinaryExpr createEqualityExpr( Expr lhs,
                                   Expr rhs,
                                   int equalityOperator ) throws JaxenException;

    /**
     * Returns a new XPath relational expression.  
     * 
     * @param lhs the left hand side of the expression
     * @param rhs the right hand side of the expression
     * @param relationalOperator <code>Operator.LESS_THAN</code>, <code>Operator.GREATER_THAN</code>, 
     *                           <code>Operator.LESS_THAN_EQUALS</code>, or <code>Operator.GREATER_THAN_EQUALS</code>
     * @return <code><i>lhs</i> <i>relationalOperator</i> <i>rhs</i></code> or <code><i>lhs</i> != <i>rhs</i></code>
     * @throws JaxenException if the third argument is not a relational operator constant
     */
    BinaryExpr createRelationalExpr( Expr lhs,
                                     Expr rhs,
                                     int relationalOperator ) throws JaxenException;

    /**
     * Returns a new XPath additive expression.  
     * 
     * @param lhs the left hand side of the expression
     * @param rhs the right hand side of the expression
     * @param additiveOperator <code>Operator.ADD</code> or <code>Operator.SUBTRACT</code>
     * @return <code><i>lhs</i> + <i>rhs</i></code> or <code><i>lhs</i> - <i>rhs</i></code>
     * @throws JaxenException if the third argument is not 
     *                        <code>Operator.ADD</code> or <code>Operator.SUBTRACT</code>
     */
    BinaryExpr createAdditiveExpr( Expr lhs,
                                   Expr rhs,
                                   int additiveOperator ) throws JaxenException;

    /**
     * Returns a new XPath multiplicative expression.  
     * 
     * @param lhs the left hand side of the expression
     * @param rhs the right hand side of the expression
     * @param multiplicativeOperator <code>Operator.MULTIPLY</code>, 
     *        <code>Operator.DIV</code>, or <code>Operator.MOD</code>
     * @return <code><i>lhs</i> * <i>rhs</i></code>, <code><i>lhs</i> div <i>rhs</i></code>,
     *         or <code><i>lhs</i> mod <i>rhs</i></code>
     * @throws JaxenException if the third argument is not a multiplicative operator constant
     */
    BinaryExpr createMultiplicativeExpr( Expr lhs,
                                         Expr rhs,
                                         int multiplicativeOperator ) throws JaxenException;

    /**
     * Returns a new XPath unary expression.  
     * 
     * @param expr the expression to be negated
     * @param unaryOperator <code>Operator.NEGATIVE</code>
     * @return <code>- <i>expr</i></code> or <code><i>expr</i></code>
     * @throws JaxenException
     */
    Expr createUnaryExpr( Expr expr,
                          int unaryOperator ) throws JaxenException;

    /**
     * Returns a new XPath union expression.  
     * 
     * @param lhs the left hand side of the expression
     * @param rhs the right hand side of the expression
     * @return <code><i>lhs</i> | <i>rhs</i></code></code>
     * @throws JaxenException
     */
    UnionExpr createUnionExpr( Expr lhs,
                               Expr rhs ) throws JaxenException;

    /**
     * Returns a new XPath filter expression.  
     * 
     * @param expr the basic expression to which the predicate will be added
     * @return the expression with an empty predicate set
     * @throws JaxenException
     */
    FilterExpr createFilterExpr( Expr expr ) throws JaxenException;

    
    /**
     * Create a new function call expression.
     * 
     * @param prefix the namespace prefix of the function
     * @param functionName the local name of the function 
     * @return a function with an empty argument list
     * @throws JaxenException
     */
    FunctionCallExpr createFunctionCallExpr( String prefix,
                                             String functionName ) throws JaxenException;

    /**
     * Create a number expression.
     * 
     * @param number the value
     * @return a number expression wrapping that value
     * @throws JaxenException
     */
    NumberExpr createNumberExpr( int number ) throws JaxenException;

    /**
     * Create a number expression.
     * 
     * @param number the value
     * @return a number expression wrapping that value
     * @throws JaxenException
     */
    NumberExpr createNumberExpr( double number ) throws JaxenException;

    /**
     * Create a string literal expression.
     * 
     * @param literal the value
     * @return a literal expression wrapping that value
     * @throws JaxenException
     */
    LiteralExpr createLiteralExpr( String literal ) throws JaxenException;

    /**
     * Create a new variable reference expression.
     * 
     * @param prefix the namespace prefix of the variable
     * @param variableName the local name of the variable 
     * @return a variable expression
     * @throws JaxenException
     */
    VariableReferenceExpr createVariableReferenceExpr( String prefix,
                                                       String variableName ) throws JaxenException;

    /**
     * Create a step with a named node-test.
     * 
     * @param axis the axis to create the name-test on
     * @param prefix the namespace prefix for the test
     * @param localName the local name for the test
     * @return a name step
     * @throws JaxenException if <code>axis</code> is not one of the axis constants????
     */
    Step createNameStep( int axis,
                         String prefix,
                         String localName ) throws JaxenException;

    /**
     * Create a step with a node() node-test.
     * 
     * @param axis the axis to create the node-test on
     * @return an all node step
     * @throws JaxenException if <code>axis</code> is not one of the axis constants????
     */
    Step createAllNodeStep( int axis ) throws JaxenException;

    /**
     * Create a step with a <code>comment()</code> node-test.
     * 
     * @param axis the axis to create the <code>comment()</code> node-test on
     * @return a comment node step
     * @throws JaxenException if <code>axis</code> is not one of the axis constants????
     */
    Step createCommentNodeStep( int axis ) throws JaxenException;

    /**
     * Create a step with a <code>text()</code> node-test.
     * 
     * @param axis the axis to create the <code>text()</code> node-test on
     * @return a text node step
     * @throws JaxenException if <code>axis</code> is not one of the axis constants????
     */
    Step createTextNodeStep( int axis ) throws JaxenException;

    /**
     * Create a step with a <code>processing-instruction()</code> node-test.
     * 
     * @param axis the axis to create the <code>processing-instruction()</code> node-test on
     * @param name the target to match, may be empty
     * @return a processing instruction node step
     * @throws JaxenException if <code>axis</code> is not one of the axis constants????
     */
    Step createProcessingInstructionNodeStep( int axis,
                                              String name ) throws JaxenException;

    /**
     * Create from the supplied expression.
     * 
     * @param predicateExpr the expression to evaluate in the predicate
     * @return a predicate
     * @throws JaxenException
     */
    Predicate createPredicate( Expr predicateExpr ) throws JaxenException;

    /**
     * Create an empty predicate set. 
     * 
     * @return an empty predicate set
     * @throws JaxenException
     */
    PredicateSet createPredicateSet() throws JaxenException;
    
}
