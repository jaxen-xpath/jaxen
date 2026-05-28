/*
 * Copyright 2026 the Jaxen Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 * 
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 * 
 *   * Neither the name of the Jaxen Project nor the names of its
 *     contributors may be used to endorse or promote products derived 
 *     from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * ====================================================================
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Jaxen Project.
 * For more information on the Jaxen Project, please see
 * <https://github.com/jaxen-xpath/jaxen/>.
 */
package org.jaxen.test;

import junit.framework.TestCase;
import org.jaxen.JaxenException;
import org.jaxen.dom.DOMXPath;

public class OverflowTest extends TestCase
{

    private static final int TERMS = 10000;

    public void testManyAdditions() throws JaxenException
    {
        StringBuilder expression = new StringBuilder(TERMS * 3);
        for (int i = 1; i <= TERMS; i++)
        {
            if (i > 1)
            {
                expression.append('+');
            }
            expression.append(i);
        }

        Double result = (Double) new DOMXPath(expression.toString()).evaluate(null);
        assertEquals(50005000.0, result.doubleValue(), 0.0);
    }

    public void testManySubtractions() throws JaxenException
    {
        StringBuilder expression = new StringBuilder(TERMS * 3);
        expression.append(TERMS);
        for (int i = 1; i < TERMS; i++)
        {
            expression.append('-');
            expression.append(i);
        }

        Double result = (Double) new DOMXPath(expression.toString()).evaluate(null);
        assertEquals(-49985000.0, result.doubleValue(), 0.0);
    }

    public void testManyOrs() throws JaxenException
    {
        StringBuilder expression = new StringBuilder(TERMS * 10);
        for (int i = 1; i <= TERMS; i++)
        {
            if (i > 1)
            {
                expression.append(" or ");
            }
            expression.append(i == TERMS ? "true()" : "false()");
        }

        Boolean result = (Boolean) new DOMXPath(expression.toString()).evaluate(null);
        assertEquals(Boolean.TRUE, result);
    }

    public void testManyAnds() throws JaxenException
    {
        StringBuilder expression = new StringBuilder(TERMS * 10);
        for (int i = 1; i <= TERMS; i++)
        {
            if (i > 1)
            {
                expression.append(" and ");
            }
            expression.append(i == TERMS ? "false()" : "true()");
        }

        Boolean result = (Boolean) new DOMXPath(expression.toString()).evaluate(null);
        assertEquals(Boolean.FALSE, result);
    }

    public void testDeepGetText() throws JaxenException
    {
        StringBuilder expression = new StringBuilder(TERMS * 3);
        for (int i = 1; i <= TERMS; i++)
        {
            if (i > 1)
            {
                expression.append('+');
            }
            expression.append(i);
        }

        DOMXPath xpath = new DOMXPath(expression.toString());
        String text = xpath.getRootExpr().getText();

        assertNotNull(text);
        assertTrue(text.startsWith("("));
        assertTrue(text.endsWith(")"));
    }

    public void testDeepToString() throws JaxenException
    {
        StringBuilder expression = new StringBuilder(TERMS * 3);
        for (int i = 1; i <= TERMS; i++)
        {
            if (i > 1)
            {
                expression.append('+');
            }
            expression.append(i);
        }

        DOMXPath xpath = new DOMXPath(expression.toString());
        String text = xpath.getRootExpr().toString();

        assertNotNull(text);
        assertTrue(text.startsWith("["));
        assertTrue(text.endsWith("]"));
    }

    public void testManyEqualities() throws JaxenException
    {
        StringBuilder expression = new StringBuilder(TERMS * 4);
        for (int i = 1; i <= TERMS; i++)
        {
            if (i > 1)
            {
                expression.append(" = ");
            }
            expression.append('1');
        }

        Boolean result = (Boolean) new DOMXPath(expression.toString()).evaluate(null);
        assertEquals(Boolean.TRUE, result);
    }

    public void testManyRelationalComparisons() throws JaxenException
    {
        StringBuilder expression = new StringBuilder(TERMS * 4);
        expression.append('1');
        for (int i = 1; i < TERMS; i++)
        {
            expression.append(" < 2");
        }

        Boolean result = (Boolean) new DOMXPath(expression.toString()).evaluate(null);
        assertEquals(Boolean.TRUE, result);
    }
}
