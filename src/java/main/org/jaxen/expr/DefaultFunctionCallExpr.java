// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.JaxenException;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

class DefaultFunctionCallExpr extends DefaultExpr implements FunctionCallExpr
{
    private String prefix;
    private String functionName;
    private List   parameters;

    public DefaultFunctionCallExpr(String prefix,
                                   String functionName)
    {
        this.prefix       = prefix;
        this.functionName = functionName;
        this.parameters   = new ArrayList();
    }

    public void addParameter(Expr parameter)
    {
        this.parameters.add( parameter );
    }

    public List getParameters()
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
        StringBuffer buf = new StringBuffer();

        String prefix = getPrefix();

        if ( prefix != null
             &&
             ! prefix.equals("") )
        {
            buf.append( prefix );
            buf.append( ":" );
        }

        buf.append( getFunctionName() );
        buf.append( "(" );

        Iterator paramIter = getParameters().iterator();
        Expr     eachParam = null;

        while ( paramIter.hasNext() )
        {
            eachParam = (Expr) paramIter.next();

            buf.append( eachParam.getText() );

            if ( paramIter.hasNext() )
            {
                buf.append( ", " );
            }
        }

        buf.append( ")" );
        
        return buf.toString();
    }

    public Expr simplify()
    {
        List paramExprs = getParameters();
        int  paramSize  = paramExprs.size();
        Expr eachParam  = null;

        List newParams  = new ArrayList( paramSize );

        for ( int i = 0 ; i < paramSize ; ++i )
        {
            eachParam = (Expr) paramExprs.get( i );

            newParams.add( eachParam.simplify() );
        }

        this.parameters = newParams;

        return this;
    }

    public String toString()
    {
        String prefix = getPrefix();

        if ( prefix == null )
        {
            return "[(DefaultFunctionCallExpr): " + getFunctionName() + "(" + getParameters() + ") ]";
        }

        return "[(DefaultFunctionCallExpr): " + getPrefix() + ":" + getFunctionName() + "(" + getParameters() + ") ]";
    }

    public Object evaluate(Context context) throws JaxenException
    {
        Function func = context.getFunction( getPrefix(),
                                             getFunctionName() );

        if ( func != null )
        {
            List paramExprs = getParameters();
            int  paramSize  = paramExprs.size();
            
            List paramValues = new ArrayList( paramSize );
            Expr eachParam   = null;
            Object eachValue = null;
            
            for ( int i = 0 ; i < paramSize ; ++i )
            {
                eachParam = (Expr) paramExprs.get( i );
                
                eachValue = eachParam.evaluate( context );
                
                paramValues.add( eachValue );
            }
            
            return func.call( context,
                              paramValues );
        }
        
        return null;
    }
}
