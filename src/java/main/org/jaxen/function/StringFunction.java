/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright (C) 2000-2002 bob mcwhirter & James Strachan.
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
 *      Jaxen Project <http://www.jaxen.org/>."
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


package org.jaxen.function;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;
import org.jaxen.UnsupportedAxisException;
import org.jaxen.JaxenRuntimeException;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Iterator;
import java.util.Locale;

/**
 * <p>
 * <b>4.2</b> <code><i>string</i> string(<i>object</i>)</code>
 * </p>
 * 
 * 
 * <blockquote src="http://www.w3.org/TR/xpath">
 * <p>
 * The <b>string</b> function converts
 * an object to a string as follows:
 * </p>
 * 
 * <ul>
 * 
 * <li>
 * <p>
 * A node-set is converted to a string by returning the <a
 * href="http://www.w3.org/TR/xpath#dt-string-value" target="_top">string-value</a> of the node in the node-set
 * that is first in <a href="http://www.w3.org/TR/xpath#dt-document-order" target="_top">document order</a>. If
 * the node-set is empty, an empty string is returned.
 * </p>
 * </li>
 * 
 * <li>
 * <p>
 * A number is converted to a string as follows
 * </p>
 * 
 * <ul>
 * 
 * <li>
 * <p>
 * NaN is converted to the string <code>NaN</code>
 * </p>
 * </li>
 * 
 * <li>
 * <p>
 * positive zero is converted to the string <code>0</code>
 * </p>
 * </li>
 * 
 * <li>
 * 
 * <p>
 * negative zero is converted to the string <code>0</code>
 * </p>
 * </li>
 * 
 * <li>
 * <p>
 * positive infinity is converted to the string <code>Infinity</code>
 * </p>
 * </li>
 * 
 * <li>
 * <p>
 * negative infinity is converted to the string <code>-Infinity</code>
 * 
 * </p>
 * </li>
 * 
 * <li>
 * <p>
 * if the number is an integer, the number is represented in decimal
 * form as a <a href="http://www.w3.org/TR/xpath#NT-Number" target="_top">Number</a> with no decimal point and
 * no leading zeros, preceded by a minus sign (<code>-</code>) if
 * the number is negative
 * </p>
 * </li>
 * 
 * <li>
 * <p>
 * otherwise, the number is represented in decimal form as a Number including a decimal point with at least
 * one digit before the decimal point and at least one digit after the
 * decimal point, preceded by a minus sign (<code>-</code>) if the
 * number is negative; there must be no leading zeros before the decimal
 * point apart possibly from the one required digit immediately before
 * the decimal point; beyond the one required digit after the decimal
 * point there must be as many, but only as many, more digits as are
 * needed to uniquely distinguish the number from all other IEEE 754
 * numeric values.
 * </p>
 * 
 * </li>
 * 
 * </ul>
 * 
 * </li>
 * 
 * <li>
 * <p>
 * The boolean false value is converted to the string <code>false</code>.
 * The boolean true value is converted to the string <code>true</code>.
 * </p>
 * </li>
 * 
 * <li>
 * <p>
 * An object of a type other than the four basic types is converted to a
 * string in a way that is dependent on that type.
 * </p>
 * 
 * </li>
 * 
 * </ul>
 * 
 * <p>
 * If the argument is omitted, it defaults to a node-set with the
 * context node as its only member.
 * </p>
 * 
 * </blockquote>
 * 
 * @author bob mcwhirter (bob @ werken.com)
 * @see <a href="http://www.w3.org/TR/xpath#function-string"
 *      target="_top">Section 4.2 of the XPath Specification</a>
 */
public class StringFunction implements Function
{

    private static DecimalFormat format = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
    
    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        symbols.setNaN("NaN");
        symbols.setInfinity("Infinity");
        format.setGroupingUsed(false);
        format.setMaximumFractionDigits(32);
        format.setDecimalFormatSymbols(symbols);
    }

    /**
     * Create a new <code>StringFunction</code> object.
     */
    public StringFunction() {}
    
    /**
     * Returns the string-value of  
     * <code>args.get(0)</code> or of the context node if ,code>args</code> is empty.
     * 
     * @param context the context at the point in the
     *         expression where the function is called
     * @param args list with zero or one element
     * 
     * @return a <code>String</code> 
     * 
     * @throws FunctionCallException if <code>args</code> has more than one item
     */    
    public Object call(Context context,
                       List args) throws FunctionCallException
    {
        int size = args.size();

        if ( size == 0 )
        {
            return evaluate( context.getNodeSet(),
                             context.getNavigator() );
        }
        else if ( size == 1 )
        {
            return evaluate( args.get(0),
                             context.getNavigator() );
        }

        throw new FunctionCallException( "string() takes at most argument." );
    }
    
    /**
     * Returns the string-value of <code>obj</code>.
     * 
     * @param obj the object whose string-value is calculated
     * @param nav the navigator used to calculate the string-value
     * 
     * @return a <code>String</code>. May be empty but is never null.
     */    
    public static String evaluate(Object obj,
                                  Navigator nav)
    {
        try
        {
            if (obj == null) {
                return "";
            }
            
            // Workaround because XOM uses lists for Text nodes
            // so we need to check for that first
            if (nav != null && nav.isText(obj))
            {
                return nav.getTextStringValue(obj);
            }
            
            if (obj instanceof List)
            {
                List list = (List) obj;
                if (list.isEmpty())
                {
                    return "";
                }
                // do not recurse: only first list should unwrap
                obj = list.get(0);
            }
            
            if (nav != null) {
                // This stack of instanceof really suggests there's 
                // a failure to take adavantage of polymorphism here
                if (nav.isElement(obj))
                {
                    return nav.getElementStringValue(obj);
                }
                else if (nav.isAttribute(obj))
                {
                    return nav.getAttributeStringValue(obj);
                }
    
                else if (nav.isDocument(obj))
                {
                    Iterator childAxisIterator = nav.getChildAxisIterator(obj);
                    while (childAxisIterator.hasNext())
                    {
                        Object descendant = childAxisIterator.next();
                        if (nav.isElement(descendant))
                        {
                            return nav.getElementStringValue(descendant);
                        }
                    }
                }
                else if (nav.isProcessingInstruction(obj))
                {
                    return nav.getProcessingInstructionData(obj);
                }
                else if (nav.isComment(obj))
                {
                    return nav.getCommentStringValue(obj);
                }
                else if (nav.isText(obj))
                {
                    return nav.getTextStringValue(obj);
                }
                else if (nav.isNamespace(obj))
                {
                    return nav.getNamespaceStringValue(obj);
                }
            }
            
            if (obj instanceof String)
            {
                return (String) obj;
            }
            else if (obj instanceof Boolean)
            {
                return stringValue(((Boolean) obj).booleanValue());
            }
            else if (obj instanceof Number)
            {
                return stringValue(((Number) obj).doubleValue());
            }
            
        }
        catch (UnsupportedAxisException e)
        {
            throw new JaxenRuntimeException(e);
        }
        
        return "";

    }

    private static String stringValue(double value)
    {
        
        // DecimalFormat formats negative zero as "-0".
        // Therefore we need to test for zero explicitly here.
        if (value == 0) return "0";
        
        // need to synchronize object for thread-safety
        String result = null;
        synchronized (format) {
            result = format.format(value);
        }
        return result;
        
    }

    private static String stringValue(boolean value)
    {
        return value ? "true" : "false";
    }

}
