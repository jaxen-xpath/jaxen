
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import org.jaxen.XPath;
import org.jaxen.XPathSyntaxException;
import org.jaxen.JaxenException;

import org.jaxen.dom4j.Dom4jXPath;

import java.util.List;
import java.util.Iterator;

public class Dom4jDemo 
{
    public static void main(String[] args)
    {
        if ( args.length != 2 )
        {
            System.err.println("usage: Dom4jDemo <document url> <xpath expr>");
            System.exit( 1 );
        }

        try
        {
            SAXReader reader = new SAXReader();
            
            Document doc = reader.read( args[0] );
            
            XPath xpath = new Dom4jXPath( args[1] );
            
            List results = xpath.selectNodes( doc );

            Iterator resultIter = results.iterator();
            
            System.out.println("Document :: " + args[0] );
            System.out.println("   XPath :: " + args[1] );
            System.out.println("");
            System.out.println("Results" );
            System.out.println("----------------------------------");
            
            while ( resultIter.hasNext() )
            {
                Object object = resultIter.next();
                if ( object instanceof Node ) 
                {
                    Node node = (Node) object;
                    System.out.println( node.asXML() );
                }
                else 
                {
                    System.out.println( object );
                }
            }
        }
        catch (XPathSyntaxException e)
        {
            System.err.println( e.getMultilineMessage() );
        }
        catch (JaxenException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
