
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import org.jaxen.jdom.XPath;

import org.saxpath.SAXPathException;
import org.saxpath.XPathSyntaxException;

import java.util.List;

public class JDOMDemo 
{
    public static void main(String[] args)
    {
        if ( args.length != 2 )
        {
            System.err.println("usage: JDOMDemo <document url> <xpath expr>");
            System.exit( 1 );
        }

        try
        {
            SAXBuilder builder = new SAXBuilder();
            
            Document doc = builder.build( args[0] );
            
            XPath xpath = new XPath( args[1] );
            
            List results = xpath.selectNodes( doc );
            
            System.out.println("selectSingleNodes() -> " + results );
        }
        catch (JDOMException e)
        {
            e.printStackTrace();
        }
        catch (XPathSyntaxException e)
        {
            System.err.println( e.getMultilineMessage() );
        }
        catch (SAXPathException e)
        {
            e.printStackTrace();
        }
    }
}
