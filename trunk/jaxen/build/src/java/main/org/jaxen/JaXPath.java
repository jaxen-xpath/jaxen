
package org.jaxen;

import org.jaxen.expr.XPath;

import org.saxpath.XPathReader;
import org.saxpath.SAXPathException;
import org.saxpath.helpers.XPathReaderFactory;

import java.util.List;

public class JaXPath
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

    public List select(Context context)
    { 
        return this.xpath.asList( context );
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
