// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen;

import java.util.Iterator;

/** Interface for navigating around an arbitrary object
 *  model, using xpath semantics.
 *
 *  <p>
 *  There is a method to obtain a <code>java.util.Iterator</code>,
 *  for each axis specified by XPath.  If the target object model
 *  does not support the semantics of a particular axis, an
 *  {@link UnsupportedAxisException} is to be thrown.
 *  </p>
 *
 *  @author bob mcwhirter (bob@werken.com)
 */
public interface Navigator
{
    /** Retrieve an <code>Iterator</code> matching the <code>child</code> xpath axis.
     *
     *  @param contextNode The origin context node.
     *
     *  @return An Iterator capable of traversing the axis.
     *
     *  @throws UnsupportedAxisException is the semantics of this axis are
     *          not supported by this object model.
     */
    Iterator getChildAxisIterator(Object contextNode) throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the <code>descendant</code> xpath axis.
     *
     *  @param contextNode The origin context node.
     *
     *  @return An Iterator capable of traversing the axis.
     *
     *  @throws UnsupportedAxisException is the semantics of this axis are
     *          not supported by this object model.
     */
    Iterator getDescendantAxisIterator(Object contextNode) throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the <code>parent</code> xpath axis.
     *
     *  @param contextNode The origin context node.
     *
     *  @return An Iterator capable of traversing the axis.
     *
     *  @throws UnsupportedAxisException is the semantics of this axis are
     *          not supported by this object model.
     */
    Iterator getParentAxisIterator(Object contextNode) throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the <code>ancestor</code> xpath axis.
     *
     *  @param contextNode The origin context node.
     *
     *  @return An Iterator capable of traversing the axis.
     *
     *  @throws UnsupportedAxisException is the semantics of this axis are
     *          not supported by this object model.
     */
    Iterator getAncestorAxisIterator(Object contextNode) throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the <code>following-sibling</code> xpath axis.
     *
     *  @param contextNode The origin context node.
     *
     *  @return An Iterator capable of traversing the axis.
     *
     *  @throws UnsupportedAxisException is the semantics of this axis are
     *          not supported by this object model.
     */
    Iterator getFollowingSiblingAxisIterator(Object contextNode) throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the <code>preceding-sibling</code> xpath axis.
     *
     *  @param contextNode The origin context node.
     *
     *  @return An Iterator capable of traversing the axis.
     *
     *  @throws UnsupportedAxisException is the semantics of this axis are
     *          not supported by this object model.
     */
    Iterator getPrecedingSiblingAxisIterator(Object contextNode) throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the <code>following</code> xpath axis.
     *
     *  @param contextNode The origin context node.
     *
     *  @return An Iterator capable of traversing the axis.
     *
     *  @throws UnsupportedAxisException is the semantics of this axis are
     *          not supported by this object model.
     */
    Iterator getFollowingAxisIterator(Object contextNode) throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the <code>preceding</code> xpath axis.
     *
     *  @param contextNode The origin context node.
     *
     *  @return An Iterator capable of traversing the axis.
     *
     *  @throws UnsupportedAxisException is the semantics of this axis are
     *          not supported by this object model.
     */
    Iterator getPrecedingAxisIterator(Object contextNode) throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the <code>attribute</code> xpath axis.
     *
     *  @param contextNode The origin context node.
     *
     *  @return An Iterator capable of traversing the axis.
     *
     *  @throws UnsupportedAxisException is the semantics of this axis are
     *          not supported by this object model.
     */
    Iterator getAttributeAxisIterator(Object contextNode) throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the <code>namespace</code> xpath axis.
     *
     *  @param contextNode The origin context node.
     *
     *  @return An Iterator capable of traversing the axis.
     *
     *  @throws UnsupportedAxisException is the semantics of this axis are
     *          not supported by this object model.
     */
    Iterator getNamespaceAxisIterator(Object contextNode) throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the <code>self</code> xpath axis.
     *
     *  @param contextNode The origin context node.
     *
     *  @return An Iterator capable of traversing the axis.
     *
     *  @throws UnsupportedAxisException is the semantics of this axis are
     *          not supported by this object model.
     */
    Iterator getSelfAxisIterator(Object contextNode) throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the <code>descendant-or-self</code> xpath axis.
     *
     *  @param contextNode The origin context node.
     *
     *  @return An Iterator capable of traversing the axis.
     *
     *  @throws UnsupportedAxisException is the semantics of this axis are
     *          not supported by this object model.
     */
    Iterator getDescendantOrSelfAxisIterator(Object contextNode) throws UnsupportedAxisException;

    /** Retrieve an <code>Iterator</code> matching the <code>ancestor-or-self</code> xpath axis.
     *
     *  @param contextNode The origin context node.
     *
     *  @return An Iterator capable of traversing the axis.
     *
     *  @throws UnsupportedAxisException is the semantics of this axis are
     *          not supported by this object model.
     */
    Iterator getAncestorOrSelfAxisIterator(Object contextNode) throws UnsupportedAxisException;

    /** Loads a document from the given URI
     *
     * @param uri is the URI of the document to load
     *
     * @throws FunctionCallException if the document could not be loaded
     */
    Object getDocument(String url) throws FunctionCallException;

    Object getDocumentNode(Object contextNode);
    
    Object getParentNode(Object contextNode) throws UnsupportedAxisException;
    
    String getElementNamespaceUri(Object element);
    String getElementName(Object element);
    String getElementQName(Object element);

    String getAttributeNamespaceUri(Object attr);
    String getAttributeName(Object attr);
    String getAttributeQName(Object attr);

    String getProcessingInstructionTarget(Object pi);
    String getProcessingInstructionData(Object pi);

    boolean isDocument(Object object);
    boolean isElement(Object object);
    boolean isAttribute(Object object);
    boolean isNamespace(Object object);

    boolean isComment(Object object);
    boolean isText(Object object);
    boolean isProcessingInstruction(Object object);

    String getCommentStringValue(Object comment);
    String getElementStringValue(Object element);
    String getAttributeStringValue(Object attr);
    String getNamespaceStringValue(Object ns);
    String getTextStringValue(Object txt);
    
    String translateNamespacePrefixToUri(String prefix, Object element);

    /**
     *  Returns the element whose ID is given by elementId.
     *  If no such element exists, returns null.
     *  Attributes with the name "ID" are not of type ID unless so defined.
     *  Implementations that do not know whether attributes are of type ID or
     *  not are expected to return null.
     *
     *  @param contextNode   a node from the document in which to look for the
     *                       id
     *  @param elementId   id to look for
     *
     *  @return   element whose ID is given by elementId, or null if no such
     *            element exists in the document or if the implementation
     *            does not know about attribute types
     */
    Object getElementById(Object contextNode, String elementId);

    short getNodeType(Object node);
}
