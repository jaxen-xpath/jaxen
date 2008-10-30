/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright 2005 bob mcwhirter & James Strachan.
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
 * Jaxen Project, please see <http://www.jaxen.org/>.
 * 
 * $Id$
 */


package org.jaxen.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 *   Collect the org.jaxen. tests.
 * </p>
 * 
 * @author Elliotte Rusty Harold
 * @version 1.1.2
 *
 */
public class CoreTests extends TestCase {

    
    public CoreTests(String name) {
        super(name);   
    }

    
    public static Test suite() {
        
        TestSuite result = new TestSuite();
        result.addTest(new TestSuite(AddNamespaceTest.class));
        result.addTest(new TestSuite(BaseXPathTest.class));
        result.addTest(new TestSuite(FunctionContextTest.class));
        result.addTest(new TestSuite(SimpleNamespaceContextTest.class));
        result.addTest(new TestSuite(ContextTest.class));
        result.addTest(new TestSuite(JaxenHandlerTest.class));
        result.addTest(new TestSuite(JaxenRuntimeExceptionTest.class));
        result.addTest(new TestSuite(FunctionCallExceptionTest.class));
        result.addTest(new TestSuite(UnresolvableExceptionTest.class));
        result.addTest(new TestSuite(VariableContextTest.class));
        result.addTest(new TestSuite(SimpleNamespaceContextTest.class));
        result.addTest(new TestSuite(XPathSyntaxExceptionTest.class));
        result.addTest(new TestSuite(UnsupportedAxisExceptionTest.class));
        result.addTest(new TestSuite(JaxenExceptionTest.class));
        result.addTest(new TestSuite(ArithmeticTest.class));
        result.addTest(new TestSuite(IterableAxisTest.class));
        result.addTest(new TestSuite(DefaultXPathFactoryTest.class));
        result.addTest(new TestSuite(NodesetEqualityTest.class));
        return result;
        
    }

    
}