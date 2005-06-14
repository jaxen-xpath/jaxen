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

package org.jaxen.saxpath;

/**
 * Constants used to represent XPath operators.
 */
public interface Operator
{
    /**
     * Indicates that we're passing through a grammar production without
     * actually activating it. For example in the expression
     * <code>1</code> is matches AdditiveExpr and MultiplicativeExpr in 
     * the XPath grammar, even though it has neither a plus, 
     * minus, multiplication, or other sign.
     */
    final static int NO_OP               =  0;
    
    // Equality
    /**
     * The equal to operator <code>=</code>. This is equivalent to <code>==</code>
     * in Java. This is a comparison operator, not an assignment operator.
     */
    final static int EQUALS              =  1;
    
    /**
     * The not equal to operator <code>!=</code>
     */
    final static int NOT_EQUALS          =  2;
    
    // Relational
    /**
     * The less-than operator <code>&lt;</code>
     */
    final static int LESS_THAN           =  3;
    
    /**
     * The less-than-or-equal-to operator <code>&lt;=</code>
     */
    final static int LESS_THAN_EQUALS    =  4;

    /**
     * The greater-than operator <code>></code>
     */
    final static int GREATER_THAN        =  5;

    /**
     * The greater-than or equals operator <code>>=</code>
     */
    final static int GREATER_THAN_EQUALS =  6;
    
    // Additive

    /**
     * The addition operator <code>+</code>
     */
    final static int ADD                 =  7;

    /**
     * The subtraction operator <code>-</code>
     */
    final static int SUBTRACT            =  8;

    // Multiplicative

    /**
     * The multiplication operator <code>*</code>
     */
    final static int MULTIPLY            =  9;

    /**
     * The remainder operator <code>mod</code>. This is equivalent to
     * <code>%</code> in Java. 
     */
    final static int MOD                 = 10;

    /**
     * The floating point division operator <code>div</code>.  This is equivalent to
     * <code>/</code> in Java. 
     */
    final static int DIV                 = 11;

    // Unary

    /**
     * Unary <code>-</code>
     */
    final static int NEGATIVE            = 12;
}
