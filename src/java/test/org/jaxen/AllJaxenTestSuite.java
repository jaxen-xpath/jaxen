/*
 $Id$

 Copyright 2003 (C) The Werken Company. All Rights Reserved.
 
 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.
 
 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.
 
 3. The name "jaxen" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Werken Company.  For written permission,
    please contact bob@werken.com.
 
 4. Products derived from this Software may not be called "jaxen"
    nor may "jaxen" appear in their names without prior written
    permission of The Werken Company. "jaxen" is a registered
    trademark of The Werken Company.
 
 5. Due credit should be given to The Werken Company.
    (http://jaxen.werken.com/).
 
 THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.

 */
package org.jaxen;

import org.jaxen.dom.DocumentNavigatorTest;
import org.jaxen.dom.XPathTest;
import org.jaxen.pattern.PatternHandlerTest;
import org.jaxen.pattern.PriorityTest;
import org.jaxen.saxpath.base.XPathLexerTest;
import org.jaxen.saxpath.base.XPathLexerTokenTest;
import org.jaxen.saxpath.base.XPathReaderTest;
import org.jaxen.saxpath.helpers.XPathReaderFactoryTest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Test suite for Jaxen.
 *
 * @author Stephen Colebourne
 * @version $Id$
 */
public class AllJaxenTestSuite extends TestCase {
    
    /**
     * Construct a new instance.
     */
    public AllJaxenTestSuite(String name) {
        super(name);
    }

    /**
     * Command-line interface.
     */
    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    /**
     * Get the suite of tests
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.setName("Jaxen (all) Tests");
        suite.addTest(new TestSuite(AddNamespaceTest.class));
        suite.addTest(new TestSuite(ContextTest.class));
        suite.addTest(new TestSuite(JaxenHandlerTest.class));
        suite.addTest(new TestSuite(DocumentNavigatorTest.class));
        suite.addTest(new TestSuite(XPathTest.class));
        suite.addTest(new TestSuite(org.jaxen.dom4j.DocumentNavigatorTest.class));
        suite.addTest(new TestSuite(org.jaxen.dom4j.XPathTest.class));
        suite.addTest(new TestSuite(org.jaxen.jdom.DocumentNavigatorTest.class));
        suite.addTest(new TestSuite(org.jaxen.jdom.XPathTest.class));
        suite.addTest(new TestSuite(PatternHandlerTest.class));
        suite.addTest(new TestSuite(PriorityTest.class));
        suite.addTest(new TestSuite(XPathLexerTest.class));
        suite.addTest(new TestSuite(XPathLexerTokenTest.class));
        suite.addTest(new TestSuite(XPathReaderTest.class));
        suite.addTest(new TestSuite(XPathReaderFactoryTest.class));
        return suite;
    }
}
