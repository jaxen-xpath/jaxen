
package org.jaxen;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/** Wrapper around implementation-specific objects used
 *  as the context of an expression evaluation.
 *
 *  <p>
 *  <b>NOTE:</b> This class is not typically used directly,
 *  but is exposed for writers of implementation-specific
 *  XPath packages.
 *  </p>
 *
 *  <p>
 *  The <code>Context</code> bundles utilities together
 *  for evaluation of the expression.  It wraps the provided
 *  objects for ease-of-passage through the expression AST.
 *  </p>
 *
 *  @see ContextSupport
 *  @see BaseXPath
 *  @see org.jaxen.dom4j.Dom4jXPath XPath for dom4j
 *  @see org.jaxen.jdom.JDOMXPath  XPath for JDOM
 *  @see org.jaxen.dom.DOMXPath   XPath for W3C DOM
 *  @see org.jaxen.exml.ElectricXPath  XPath for Electric XML
 *
 *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 */
public class Context implements Serializable
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

    public Object getVariableValue( String namespaceURI,
                                    String prefix,
                                    String localName )
        throws UnresolvableException
    {
        return getContextSupport().getVariableValue( namespaceURI,
                                                     prefix,
                                                     localName );
    }

    public Function getFunction( String namespaceURI,
                                 String prefix,
                                 String localName )
        throws UnresolvableException
    {
        return getContextSupport().getFunction( namespaceURI,
                                                prefix,
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
