
import electric.xml.Document;
import electric.xml.ParseException;

import org.jaxen.exml.XPath;

import org.saxpath.SAXPathException;
import org.saxpath.XPathSyntaxException;

import java.util.List;
import java.util.Iterator;

import java.io.File;

public class EXMLDemo 
{
    public static void main(String[] args)
    {
        if ( args.length != 2 )
        {
            System.err.println("usage: EXMLDemo <document url> <xpath expr>");
            System.exit( 1 );
        }

        try
        {
            Document doc = new Document( new File( args[0] ) );
            
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
        catch (ParseException e)
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
