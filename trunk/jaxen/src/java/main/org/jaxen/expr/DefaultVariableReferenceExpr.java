// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;
import org.jaxen.UnresolvableException;

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
        throws UnresolvableException
    {
        String namespaceURI =
            context.translateNamespacePrefixToUri( getPrefix() );

        return context.getVariableValue( namespaceURI,
                                         getPrefix(),
                                         getVariableName() );
    }
}
