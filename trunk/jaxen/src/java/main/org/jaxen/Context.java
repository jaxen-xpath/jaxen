
package org.jaxen;

import java.util.List;

public class Context
{
    private ContextSupport contextSupport;

    private List           nodeSet;

    private int size;
    private int position;

    public Context(ContextSupport contextSupport)
    {
        this.contextSupport = contextSupport;
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

}
