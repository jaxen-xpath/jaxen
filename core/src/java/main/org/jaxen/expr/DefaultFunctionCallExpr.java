/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright 2000-2004 bob mcwhirter & James Strachan.
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.JaxenException;

class DefaultFunctionCallExpr extends DefaultExpr implements FunctionCallExpr
{

    private static final long serialVersionUID = -4747789292572193708L;
    private final String prefix;
    private final String functionName;
    private List<Expr> parameters;

    public DefaultFunctionCallExpr(String prefix, String functionName)
    {
        this.prefix = prefix;
        this.functionName = functionName;
        this.parameters = new ArrayList<Expr>();
    }

    public void addParameter(Expr parameter)
    {
        this.parameters.add(parameter);
    }


    public List<Expr> getParameters()
    {
        return this.parameters;
    }

    public String getPrefix()
    {
        return this.prefix;
    }

    public String getFunctionName()
    {
        return this.functionName;
    }


    public String getText()
    {
        StringBuilder builder = new StringBuilder();
        String prefix = getPrefix();

        if (prefix != null &&
                prefix.length() > 0)
        {
            builder.append(prefix);
            builder.append(":");
        }

        builder.append(getFunctionName());
        builder.append("(");

        Iterator<Expr> paramIter = getParameters().iterator();

        while (paramIter.hasNext()) {
            Expr eachParam = paramIter.next();

            builder.append(eachParam.getText());

            if (paramIter.hasNext())
            {
                builder.append(", ");
            }
        }

        builder.append(")");

        return builder.toString();
    }

    @Override
    public Expr simplify()
    {
        List<Expr> paramExprs = getParameters();
        int paramSize = paramExprs.size();

        List<Expr> newParams = new ArrayList<Expr>(paramSize);

        for (Expr eachParam : paramExprs) {
            newParams.add(eachParam.simplify());
        }

        this.parameters = newParams;

        return this;
    }


    public String toString()
    {
        String prefix = getPrefix();

        if (prefix == null)
        {
            return "[(DefaultFunctionCallExpr): " + getFunctionName() + "(" + getParameters() + ") ]";
        }

        return "[(DefaultFunctionCallExpr): " + getPrefix() + ":" + getFunctionName() + "(" + getParameters() + ") ]";
    }

    public Object evaluate(Context context) throws JaxenException
    {
        String prefix = getPrefix();
        String namespaceURI = null;
        // default namespace is not used within XPath expressions
        if (prefix != null && !"".equals(prefix)) {
            namespaceURI = context.translateNamespacePrefixToUri(prefix);
        }

        Function func = context.getFunction(namespaceURI,
                prefix,
                getFunctionName());
        List<Object> paramValues = evaluateParams(context);

        return func.call(context, paramValues);
    }

    public List<Object> evaluateParams(Context context) throws JaxenException
    {
        List<Expr> paramExprs = getParameters();
        int paramSize = paramExprs.size();

        List<Object> paramValues = new ArrayList<Object>(paramSize);

        for (Expr eachParam : paramExprs) {
            paramValues.add(eachParam.evaluate(context));
        }
        return paramValues;
    }

}

