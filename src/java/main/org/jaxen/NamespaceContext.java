
package org.jaxen;

/** Resolves namespace prefixes to namespace URIs.
 *
 *  <p>
 *  The prefixes used within an XPath expression are
 *  independant of those used within any target document.
 *  When evaluating an XPath against a document, only
 *  the resolved namespace URIs are compared, not their
 *  prefixes.
 *  </p>
 *
 *  <p>
 *  A <code>NamespaceContext</code> is responsible for
 *  translating prefixes as they appear in XPath expressions
 *  into URIs for comparison.  A document's prefixes are
 *  resolved internal to the document based upon its own
 *  namespace nodes.
 *  </p>
 *
 *  @see BaseXPath
 *  @see Navigator#getElementNamespaceUri
 *  @see Navigator#getAttributeNamespaceUri
 *
 *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 */
public interface NamespaceContext
{
    /** Translate the provided namespace prefix into 
     *  the matching bound namespace URI.
     *
     *  <p>
     *  In XPath, there is no such thing as a 'default namespace'.
     *  The empty prefix <b>always</b> resolves to the empty
     *  namespace URI.
     *  </p>
     *
     *  @param prefix The namespace prefix to resolve.
     *
     *  @return The namespace URI matching the prefix.
     */
    String translateNamespacePrefixToUri(String prefix);
}
