/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright (C) 2000-2004 bob mcwhirter & James Strachan.
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

import org.jaxen.JaxenRuntimeException;



class TokenTypes
{
    static final int LEFT_PAREN = 1;
    static final int RIGHT_PAREN = 2;

    static final int LEFT_BRACKET = 3;
    static final int RIGHT_BRACKET = 4;

    static final int PLUS = 5;
    static final int MINUS = 6;
    static final int LESS_THAN = 7;
    static final int LESS_THAN_EQUALS = 8;
    static final int GREATER_THAN = 9;
    static final int GREATER_THAN_EQUALS = 10;

    static final int SLASH = 11;
    static final int DOUBLE_SLASH = 12;
    static final int DOT = 13;
    static final int DOT_DOT = 14;

    static final int IDENTIFIER = 15;

    static final int AT = 16;
    static final int PIPE = 17;
    static final int COLON = 18;
    static final int DOUBLE_COLON = 19;
    static final int STAR = 20;

    static final int EQUALS = 21;
    static final int NOT_EQUALS = 22;
    static final int NOT = 23;

    static final int DIV = 24;
    static final int MOD = 25;

    static final int DOLLAR = 26;

    static final int LITERAL = 27;

    static final int AND = 28;
    static final int OR = 29;

    static final int INTEGER = 30;
    static final int DOUBLE = 31;

    static final int COMMA = 32;

    //static final int SKIP = 99;
    //static final int EOF = 100;
    static final int SKIP = -2;
    static final int EOF = -1;
    static final int ERROR = -3;

    String getTokenText( int tokenType )
    {
        switch( tokenType )
        {
            case LEFT_PAREN:
                return "(";
            case RIGHT_PAREN:
                return ")";
            case LEFT_BRACKET:
                return "[";
            case RIGHT_BRACKET:
                return "]";
            case PLUS:
                return "+";
            case MINUS:
                return "-";
            case LESS_THAN:
                return "<";
            case LESS_THAN_EQUALS:
                return "<=";
            case GREATER_THAN:
                return ">";
            case GREATER_THAN_EQUALS:
                return ">=";
            case SLASH:
                return "/";
            case DOUBLE_SLASH:
                return "//";
            case DOT:
                return ".";
            case DOT_DOT:
                return "..";
            case IDENTIFIER:
                return "(identifier)";
            case AT:
                return "@";
            case PIPE:
                return "|";
            case COLON:
                return ":";
            case DOUBLE_COLON:
                return "::";
            case STAR:
                return "*";

            case EQUALS:
                return "=";
            case NOT_EQUALS:
                return "!=";
            case NOT:
                return "!";
            case DIV:
                return "div";
            case MOD:
                return "mod";

            case DOLLAR:
                return "$";

            case LITERAL:
                return "(literal)";
            case AND:
                return "and";
            case OR:
                return "or";
            case INTEGER:
                return "(integer)";
            case DOUBLE:
                return "(double)";
            case COMMA:
                return ",";
            case ERROR:
                return "(error)";
            default:
                throw new JaxenRuntimeException("Unrecognized token type: " + tokenType);
        }
    }
}
