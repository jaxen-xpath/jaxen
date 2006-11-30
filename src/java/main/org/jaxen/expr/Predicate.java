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

import java.io.Serializable;
import org.jaxen.Context;
import org.jaxen.JaxenException;


/**
 * Represents an XPath predicate such as <code>[position() = last()]</code>.
 * This is production 8 and 9 in the 
 * <a href="http://www.w3.org/TR/xpath#NT-Predicate">XPath 1.0 specification</a>:
 * 
 * <pre> [8] Predicate     ::= '[' PredicateExpr ']'   
 * [9] PredicateExpr ::= Expr</pre>
 * 
 */
public interface Predicate extends Serializable
{
    /**
     * Returns the expression in this predicate.
     * 
     * @return the expression between the brackets
     */
    Expr getExpr();
    
    /**
     * Change the expression used by this predicate.
     * 
     * @param the new expression
     */
    void setExpr(Expr expr);

    /**
     * Simplify the expression in this predicate.
     * 
     * @see Expr#simplify()
     */
    void simplify();

    /**
     * Returns the string form of the predicate, 
     * including the square brackets.
     * 
     * @return the bracketed form of this predicate
     */
    String getText();

    /**
     * Evaluates this predicate's expression and returns the result.
     * The result will be a <code>java.lang.Double</code> for expressions that 
     * return a number, a <code>java.lang.String</code> for expressions that 
     * return a string, a <code>java.lang.Boolean</code> for expressions that 
     * return a boolean, and a <code>java.util.List</code> for expressions that
     * return a node-set. In the latter case, the elements of the list are 
     * the actual objects from the source document model. Copies are not made.
     *
     * @param context the context in which the expression is evaluated
     * @return an object representing the result of the evaluation
     * @throws JaxenException
     * @see Expr#evaluate(Context)
     */
    Object evaluate(Context context) throws JaxenException;

}
