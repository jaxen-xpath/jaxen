
package org.jaxen;

/** Indicates an error during parsing of an XPath
 *  expression.
 *
 *  @see org.jaxen.dom4j.XPath XPath for dom4j
 *  @see org.jaxen.jdom.XPath  XPath for JDOM
 *  @see org.jaxen.dom.XPath   XPath for W3C DOM
 *  @see org.jaxen.exml.XPath  XPath for EXML
 *
 *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 */
public class XPathSyntaxException extends JaxenException
{
    /** The textual xpath expression. */
    private String xpath;

    /** The position of the error. */
    private int    position;

    /** Construct.
     *
     *  @param xpath The erroneous xpath.
     *  @param position The position of the error.
     *  @param message The error message.
     */
    public XPathSyntaxException(String xpath,
                                int position,
                                String message)
    {
        super( message );

        this.xpath    = xpath;
        this.position = position;
    }

    /** Retrieve the position of the error.
     *
     *  @return The position of the error.
     */
    public int getPosition()
    {
        return this.position;
    }

    /** Retrieve the expression containing the error.
     *
     *  @return The erroneous expression.
     */
    public String getXPath()
    {
        return this.xpath;
    }

    /** Retrieve a string useful for denoting where
     *  the error occured.
     *
     *  <p>
     *  This is a string composed of whitespace and
     *  a marker at the position (see {@link #getPosition})
     *  of the error.  This is useful for creating
     *  friendly multi-line error displays.
     *  </p>
     *
     *  @return The error position marker.
     */
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

    /** Retrieve the friendly multi-line error message.
     *
     *  <p>
     *  This returns a multi-line string that contains
     *  the original erroneous xpath expression with a
     *  marker underneath indicating exactly where the
     *  error occurred.
     *  </p>
     *
     *  @return The multi-line error message.
     */
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
