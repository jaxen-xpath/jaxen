
package org.jaxen;

public class XPathSyntaxException extends JaxenException
{
    private String xpath;
    private int    position;

    public XPathSyntaxException(String xpath,
                                int position,
                                String message)
    {
        super( message );

        this.xpath    = xpath;
        this.position = position;
    }

    public int getPosition()
    {
        return this.position;
    }

    public String getXPath()
    {
        return this.xpath;
    }

    public String getPositionMarker()
    {
        StringBuffer buf = new StringBuffer();

        int pos = getPosition();

        for ( int i = 0 ; i < pos ; ++i )
        {
            buf.append(" ");
        }

        buf.append("^");

        return buf.toString();
        
    }

    public String getMultilineMessage()
    {
        StringBuffer buf = new StringBuffer();

        buf.append( getMessage() );
        buf.append( "\n" );
        buf.append( getXPath() );
        buf.append( "\n" );

        buf.append( getPositionMarker() );

        return buf.toString();
    }
}
