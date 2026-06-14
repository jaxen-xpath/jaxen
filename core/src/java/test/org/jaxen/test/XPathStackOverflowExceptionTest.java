/*
 * Copyright 2026 the Jaxen project.
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
 * individuals on behalf of the Jaxen Project and was originally 
 * created by bob mcwhirter <bob@werken.com> and 
 * James Strachan <jstrachan@apache.org>.  For more information on the 
 * Jaxen Project, please see <https://github.com/jaxen-xpath/jaxen/>.
 */

package org.jaxen.test;

import junit.framework.TestCase;
import org.jaxen.XPathStackOverflowException;

public class XPathStackOverflowExceptionTest extends TestCase {

    public XPathStackOverflowExceptionTest(String name) {
        super(name);
    }

    public void testStoresMessageAndCause() {
        StackOverflowError rootCause = new StackOverflowError();
        XPathStackOverflowException ex = new XPathStackOverflowException("Too deep", rootCause);
        assertEquals("Too deep", ex.getMessage());
        assertSame(rootCause, ex.getCause());
    }

    public void testNullMessageIsRejected() {
        try {
            new XPathStackOverflowException(null, new StackOverflowError());
            fail("Expected NullPointerException");
        }
        catch (NullPointerException ex) {
            assertEquals("message", ex.getMessage());
        }
    }

    public void testNullRootCauseIsRejected() {
        try {
            new XPathStackOverflowException("Too deep", null);
            fail("Expected NullPointerException");
        }
        catch (NullPointerException ex) {
            assertEquals("rootCause", ex.getMessage());
        }
    }
}
