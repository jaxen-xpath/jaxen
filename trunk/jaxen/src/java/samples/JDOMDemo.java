
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import org.jaxen.jdom.XPath;

import org.saxpath.SAXPathException;
import org.saxpath.XPathSyntaxException;

import java.util.List;
import java.util.Iterator;

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
            
            Iterator resultIter = results.iterator();

            System.out.println("Document :: " + args[0] );
            System.out.println("   XPath :: " + args[1] );
            System.out.println("");
            System.out.println("Results" );
            System.out.println("----------------------------------");

            while ( resultIter.hasNext() )
            {
                System.out.println( resultIter.next() );
            }
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
