
package org.jaxen;

import org.jaxen.expr.XPath;
import org.jaxen.function.StringFunction;
import org.jaxen.function.NumberFunction;

import org.saxpath.XPathReader;
import org.saxpath.SAXPathException;
import org.saxpath.helpers.XPathReaderFactory;

import java.util.List;

class JaXPath
{
    private XPath xpath;

    public JaXPath(String xpathExpr) throws SAXPathException
    {
        XPathReader reader = XPathReaderFactory.createReader();

        JaxenHandler handler = new JaxenHandler();

        reader.setXPathHandler( handler );

        reader.parse( xpathExpr );

        this.xpath = handler.getXPath();
    }

    protected List jaSelectNodes(Context context) throws JaxenException
    { 
        return this.xpath.asList( context );
    }

    protected Object jaSelectSingleNode(Context context) throws JaxenException
    {
        List results = jaSelectNodes( context );

        if ( results.isEmpty() )
        {
            return null;
        }

        return results.get( 0 );
    }

    protected String jaValueOf(Context context) throws JaxenException
    {
        Object result = jaSelectSingleNode( context );

        if ( result == null )
        {
            return "";
        }

        return StringFunction.evaluate( result,
                                        context.getNavigator() );
    }

    protected Number jaNumberValueOf(Context context) throws JaxenException
    {
        Object result = jaSelectSingleNode( context );

        if ( result == null )
        {
            return null;
        }

        return NumberFunction.evaluate( result,
                                        context.getNavigator() );
    }

    public String toString()
    {
        return this.xpath.getText();
    }

    public String debug()
    {
        return this.xpath.toString();
    }
}
