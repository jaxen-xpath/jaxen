
package org.jaxen;

import java.util.List;
import java.util.ArrayList;

public class Context
{
    private ContextSupport contextSupport;

    private List           nodeSet;

    private int size;
    private int position;

    public Context(ContextSupport contextSupport)
    {
        this.contextSupport = contextSupport;
        this.nodeSet        = new ArrayList(0);
    }

    public List getNodeSet()
    {
        return this.nodeSet;
    }

    public void setNodeSet(List nodeSet)
    {
        this.nodeSet = nodeSet;
    }

    public ContextSupport getContextSupport()
    {
        return this.contextSupport;
    }

    public void setContextSupport(ContextSupport contextSupport)
    {
        this.contextSupport = contextSupport;
    }

    public Navigator getNavigator()
    {
        return getContextSupport().getNavigator();
    }

    public String translateNamespacePrefixToUri(String prefix)
    {
        return getContextSupport().translateNamespacePrefixToUri( prefix );
    }

    public Object getVariableValue(String prefix,
                                   String localName)
    {
        return getContextSupport().getVariableValue( prefix,
                                                     localName );
    }

    public Function getFunction(String prefix,
                                String localName)
    {
        return getContextSupport().getFunction( prefix,
                                                localName );
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }

    public int getSize()
    {
        return this.size;
    }

    public int getPosition()
    {
        return this.position;
    }

    public Context duplicate()
    {
        Context dupe = new Context( getContextSupport() );

        List thisNodeSet = getNodeSet();

        if ( thisNodeSet != null )
        {
            List dupeNodeSet = new ArrayList( thisNodeSet.size() );
            dupeNodeSet.addAll( thisNodeSet );
            dupe.setNodeSet( dupeNodeSet );
        }

        return dupe;
    }
}
