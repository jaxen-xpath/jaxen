
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import org.jaxen.dom.XPath;

import org.saxpath.SAXPathException;
import org.saxpath.XPathSyntaxException;

import java.util.List;
import java.util.Iterator;

public class DOMDemo 
{
    public static void main(String[] args)
    {
        if ( args.length != 2 )
        {
            System.err.println("usage: DOMDemo <document url> <xpath expr>");
            System.exit( 1 );
        }

        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
        
            Document doc = builder.parse( args[0] );
            
            XPath xpath = new XPath( args[1] );

            System.out.println( "XPah:h " + xpath );
            
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
        catch (XPathSyntaxException e)
        {
            System.err.println( e.getMultilineMessage() );
        }
        catch (SAXPathException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
