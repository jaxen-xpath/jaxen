// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;

class DefaultVariableReferenceExpr extends DefaultExpr implements VariableReferenceExpr
{
    private String prefix;
    private String variableName;

    public DefaultVariableReferenceExpr(String prefix,
                                        String variableName)
    {
        this.prefix       = prefix;
        this.variableName = variableName;
    }

    public String getPrefix()
    {
        return this.prefix;
    }

    public String getVariableName()
    {
        return this.variableName;
    }

    public String toString()
    {
        String prefix = getPrefix();

        if ( prefix == null )
        {
            return "[(DefaultVariableReferenceExpr): " + getVariableName() + "]";
        }

        return "[(DefaultVariableReferenceExpr): " + getPrefix() + ":" + getVariableName() + "]";
    }

    public String getText()
    {
        String prefix = getPrefix();

        if ( prefix == null )
        {
            return "$" + getVariableName();
        }

        return "$" + prefix + ":" + getVariableName();
    }

    public Object evaluate(Context context)
    {
        return context.getVariableValue( getPrefix(),
                                         getVariableName() );
    }
}
