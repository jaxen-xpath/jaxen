// Copyright 2001 werken digital. All rights reserved.

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

    Object getDocumentNode(Object contextNode);
    
    String getElementNamespaceUri(Object element);
    String getElementName(Object element);
    String getElementQName(Object element);

    String getAttributeNamespaceUri(Object attr);
    String getAttributeName(Object attr);
    String getAttributeQName(Object attr);

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
    
    String translateNamespacePrefixToUri(String prefix, Object element);
}
