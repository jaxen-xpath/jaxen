/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright (C) 2000-2003 bob mcwhirter & James Strachan.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions, and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions, and the disclaimer that follows
 *    these conditions in the documentation and/or other materials
 *    provided with the distribution.
 *
 * 3. The name "Jaxen" must not be used to endorse or promote products
 *    derived from this software without prior written permission.  For
 *    written permission, please contact license@jaxen.org.
 *
 * 4. Products derived from this software may not be called "Jaxen", nor
 *    may "Jaxen" appear in their name, without prior written permission
 *    from the Jaxen Project Management (pm@jaxen.org).
 *
 * In addition, we request (but do not require) that you include in the
 * end-user documentation provided with the redistribution and/or in the
 * software itself an acknowledgement equivalent to the following:
 *     "This product includes software developed by the
 *      Jaxen Project (http://www.jaxen.org/)."
 * Alternatively, the acknowledgment may be graphical using the logos
 * available at http://www.jaxen.org/
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE Jaxen AUTHORS OR THE PROJECT
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 * ====================================================================
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Jaxen Project and was originally
 * created by bob mcwhirter <bob@werken.com> and
 * James Strachan <jstrachan@apache.org>.  For more information on the
 * Jaxen Project, please see <http://www.jaxen.org/>.
 *
 * $Id$
 */

package org.jaxen.dom.html;

import java.util.Locale;

import org.jaxen.XPath;
import org.jaxen.Navigator;
import org.jaxen.saxpath.SAXPathException;
import org.w3c.dom.Node;

/**
 * Interface for navigating around the W3C HTML DOM object model.
 *
 * <p>
 * This class is not intended for direct usage, but is
 * used by the Jaxen engine during evaluation.
 * </p>
 *
 * <p>XML is case-sensitive. HTML is not. This navigator is an extension
 * of the DOM DocumentNavigator that automatically
 * changes all element, but not attribute, names to lowercase or uppercase
 * to aid in navigating through HTML documents.  Note that case modification
 * are bypassed for XHTML documents.  XHTML is case sensitive and can be
 * expected to store all elements and atributes in lower case. Also Note that
 * HTML attribute names are stored as lower case in the HTML (and XHTML) DOM
 * already which is why the case of attribute names are not modified.
 *
 *  @author David Peterson
 *
 *  @see XPath
 *  @see org.jaxen.dom.DOMXPath
 *  @see org.jaxen.dom.NamespaceNode
 */
public class DocumentNavigator extends org.jaxen.dom.DocumentNavigator
{

    ////////////////////////////////////////////////////////////////////
    // Constants.
    ////////////////////////////////////////////////////////////////////

    /**
     * Constant: lowercase navigator.
     */
    private final static DocumentNavigator LOWERCASE =
        new DocumentNavigator(true);

    private final static DocumentNavigator UPPERCASE =
        new DocumentNavigator(false);

    private final boolean toLowerCase;

    ////////////////////////////////////////////////////////////////////
    // Constructor.
    ////////////////////////////////////////////////////////////////////

    /**
     * Constructs a new DocumentNavigator that will convert to lowercase.
     */
    public DocumentNavigator ()
    {
        this(true);
    }

    /**
     * Constructs a new DocumentNavigator that will convert to lowercase.
     * 
     * @param toLowerCase whether to convert all names to lowercase
     */
    public DocumentNavigator(boolean toLowerCase)
    {
        this.toLowerCase = toLowerCase;
    }

    /**
     * Returns <code>true</code> if the navigator is converting to lowercase.
     * 
     * @return true if the navigator is converting to lowercase; false otherwise
     */
    public boolean isToLowerCase()
    {
      return toLowerCase;
    }

    /**
     * Get a singleton DocumentNavigator for efficiency.
     *
     * @return a singleton instance of a DocumentNavigator
     */
    public static Navigator getInstance (boolean toLowerCase)
    {
        if (toLowerCase)
          return LOWERCASE;
        return UPPERCASE;
    }

    ////////////////////////////////////////////////////////////////////
    // Implementation of the navigator.
    ////////////////////////////////////////////////////////////////////

    /** Returns a parsed form of the given xpath string, which will be suitable
     *  for queries on HTML DOM documents.
     */
    public XPath parseXPath (String xpath) throws SAXPathException
    {
        return new HTMLXPath(xpath);
    }

  /**
   * Get the name of the node in the case specified for the current object
   *
   * @param node the target node. Used to avoid case modification of node
   *        names in XML documents.
   * @param name the name of the node, presumably in the case natively
   *        stored by the DOM
   * @return the name of the node, case-modified as desired, if the current
   *         document is HTML and not XML
   */
  protected String getHTMLNodeName(Node node, String name)
  {
      //modify case if, and only if, the
      //name is not null and the document
      //is not an XML document.
      if (name != null && !isXMLNode(node))
      {
          if (toLowerCase)
              name = name.toLowerCase(Locale.ENGLISH);
          else
              name = name.toUpperCase(Locale.ENGLISH);
      }
      return name;
  }

  public String getElementName(Object object)
  {
      return getHTMLNodeName((Node)object, super.getElementName(object));
  }

  public String getElementQName(Object object)
  {
      return getHTMLNodeName((Node)object, super.getElementQName(object));
  }

  /**
   * Determine if a node is definitely an XML node.
   * Note: This will not work if the XML node has not been namespaced.
   * However, since we are dealing with HTML documents, this is a minimal
   * risk.
   */
  private boolean isXMLNode(Node node) {
      return (node != null) && (node.getNamespaceURI() != null);
  }

}
