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
import junit.framework.TestSuite;

/**
 * <p>
 *   Suite for Jaxen's function tests.
 * </p>
 * 
 * @author Elliotte Rusty Harold
 * @version 1.1b9
 *
 */
public class FunctionTests {
    
    public static Test suite() {
        
        TestSuite result = new TestSuite();
        result.addTest(new TestSuite(TranslateFunctionTest.class));
        result.addTest(new TestSuite(SubstringTest.class));
        result.addTest(new TestSuite(SubstringBeforeTest.class));
        result.addTest(new TestSuite(SubstringAfterTest.class));
        result.addTest(new TestSuite(LangTest.class));
        result.addTest(new TestSuite(LastTest.class));
        result.addTest(new TestSuite(ConcatTest.class));
        result.addTest(new TestSuite(ContainsTest.class));
        result.addTest(new TestSuite(StringLengthTest.class));
        result.addTest(new TestSuite(StartsWithTest.class));
        result.addTest(new TestSuite(CountTest.class));
        result.addTest(new TestSuite(LocalNameTest.class));
        result.addTest(new TestSuite(NameTest.class));
        result.addTest(new TestSuite(NamespaceURITest.class));
        result.addTest(new TestSuite(SumTest.class));
        result.addTest(new TestSuite(NumberTest.class));
        result.addTest(new TestSuite(RoundTest.class));
        result.addTest(new TestSuite(StringTest.class));
        result.addTest(new TestSuite(BooleanTest.class));
        result.addTest(new TestSuite(CeilingTest.class));
        result.addTest(new TestSuite(FloorTest.class));
        result.addTest(new TestSuite(IdTest.class));
        result.addTest(new TestSuite(TrueTest.class));
        result.addTest(new TestSuite(FalseTest.class));
        result.addTest(new TestSuite(NotTest.class));
        result.addTest(new TestSuite(NormalizeSpaceTest.class));
        result.addTest(new TestSuite(PositionTest.class));
        result.addTest(new TestSuite(ExtensionFunctionTest.class));
        return result;
        
    }

}