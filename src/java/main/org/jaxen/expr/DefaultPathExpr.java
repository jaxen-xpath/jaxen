// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.expr;

import org.jaxen.Context;
import org.jaxen.JaxenException;

import java.util.Iterator;

class DefaultPathExpr extends DefaultExpr implements PathExpr
{
    private Expr         filterExpr;
    private LocationPath locationPath;

    public DefaultPathExpr(Expr filterExpr,
                           LocationPath locationPath)
    {
        this.filterExpr   = filterExpr;
        this.locationPath = locationPath;
    }

    public Expr getFilterExpr()
    {
        return this.filterExpr;
    }

    public void setFilterExpr(Expr filterExpr)
    {
        this.filterExpr = filterExpr;
    }

    public LocationPath getLocationPath()
    {
        return this.locationPath;
    }

    public String toString()
    {
        if ( getLocationPath() != null )
        {
            return "[(DefaultPathExpr): " + getFilterExpr() + ", " + getLocationPath() + "]";
        }

        return "[(DefaultPathExpr): " + getFilterExpr() + "]";
    }

    public String getText()
    {
        StringBuffer buf = new StringBuffer();

        if ( getFilterExpr() != null )
        {
            buf.append( getFilterExpr().getText() );
        }

        if ( getLocationPath() != null )
        {
            buf.append( getLocationPath().getText() );
        }

        return buf.toString();
    }

    public Expr simplify()
    {
        if ( getFilterExpr() != null )
        {
            setFilterExpr( getFilterExpr().simplify() );
        }

        if ( getLocationPath() != null )
        {
            getLocationPath().simplify();
        }

        if ( getLocationPath() == null )
        {
            return getFilterExpr();
        }

        if ( getFilterExpr() == null )
        {
            return getLocationPath();
        }

        return this;
    }

    public Object evaluate(Context context) throws JaxenException
    {
        Object results = getFilterExpr().evaluate( context );
        context.setNodeSet( convertToList( results ) );

        
        return getLocationPath().evaluate( context );
    }
}
