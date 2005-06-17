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


package org.jaxen;

import junit.framework.TestCase;
import org.jaxen.function.StringFunction;
import org.jaxen.saxpath.helpers.XPathReaderFactory;
import org.jaxen.pattern.Pattern;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class XPathTestBase extends TestCase
{
    protected static String VAR_URI   = "http://jaxen.org/test-harness/var";
    protected static String TESTS_XML = "xml/test/tests.xml";

    protected static boolean verbose = false;
    protected static boolean debug = false;
    private ContextSupport contextSupport;

    public XPathTestBase(String name)
    {
        super(name);
    }

    public void setUp()
    {
        this.contextSupport = null;
        System.setProperty(XPathReaderFactory.DRIVER_PROPERTY,
                "");
        log("-----------------------------");
    }

    public void log(String text)
    {
        log(verbose,
                text);
    }

    public void log(boolean actualVerbose,
                    String text)
    {
        if (actualVerbose) System.out.println(text);
    }

    protected void assertCountXPath(int expectedSize, Object context, String xpathStr) throws JaxenException
    {
        try
        {
            assertCountXPath2(expectedSize, context, xpathStr);
        }
        catch (UnsupportedAxisException e)
        {
            log(debug,
                    "      ## SKIPPED -- Unsupported Axis");
        }
    }

    protected Object assertCountXPath2(int expectedSize, Object context, String xpathStr) throws JaxenException
    {
        log(debug,
                "  Select :: " + xpathStr);
        BaseXPath xpath = new BaseXPath(xpathStr);
        List results = xpath.selectNodes(getContext(context));
        log(debug,
                "    Expected Size :: " + expectedSize);
        log(debug,
                "    Result Size   :: " + results.size());
        if (expectedSize != results.size())
        {
            log(debug,
                    "      ## FAILED");
            log(debug,
                    "      ## xpath: " + xpath + " = " + xpath.debug());
            Iterator resultIter = results.iterator();
            while (resultIter.hasNext())
            {
                log(debug,
                        "      --> " + resultIter.next());
            }
        }
        assertEquals(xpathStr,
                expectedSize,
                results.size());
        if (expectedSize > 0)
        {
            return results.get(0);
        }
        return null;
    }

    protected void assertInvalidXPath(Object context, String xpathStr)
    {
        try
        {
            log(debug,
                    "  Select :: " + xpathStr);
            BaseXPath xpath = new BaseXPath(xpathStr);
            List results = xpath.selectNodes(getContext(context));
            log(debug,
                    "    Result Size   :: " + results.size());
            fail("An exception was expected.");
        }
        catch (UnsupportedAxisException e)
        {
            log(debug,
                    "      ## SKIPPED -- Unsupported Axis");
        }
        catch (JaxenException e)
        {
            log(debug, "    Caught expected exception " + e.getMessage());
        }
    }

    protected void assertValueOfXPath(String expected, Object context, String xpathStr) throws JaxenException
    {
        try
        {
            BaseXPath xpath = new BaseXPath(xpathStr);
            Object node = xpath.evaluate(getContext(context));
            String result = StringFunction.evaluate(node,
                    getNavigator());
            log(debug,
                    "  Select :: " + xpathStr);
            log(debug,
                    "    Expected :: " + expected);
            log(debug,
                    "    Result   :: " + result);
            if (!expected.equals(result))
            {
                log(debug,
                        "      ## FAILED");
                log(debug,
                        "      ## xpath: " + xpath + " = " + xpath.debug());
            }
            assertEquals(xpathStr,
                    expected,
                    result);
        }
        catch (UnsupportedAxisException e)
        {
            log(debug,
                    "      ## SKIPPED -- Unsupported Axis ");
        }
    }

    protected Context getContext(Object contextNode)
    {
        Context context = new Context(getContextSupport());
        List list = new ArrayList(1);
        list.add(contextNode);
        context.setNodeSet(list);
        return context;
    }

    public ContextSupport getContextSupport()
    {
        if (this.contextSupport == null)
        {
            this.contextSupport = new ContextSupport(new SimpleNamespaceContext(),
                    XPathFunctionContext.getInstance(),
                    new SimpleVariableContext(),
                    getNavigator());
        }
        return this.contextSupport;
    }

    public abstract Navigator getNavigator();

    public abstract Object getDocument(String url) throws Exception;

    public void testGetNodeType() throws FunctionCallException, UnsupportedAxisException
    {
        Navigator nav = getNavigator();
        Object document = nav.getDocument("xml/testNamespaces.xml");
        int count = 0;
        Iterator descendantOrSelfAxisIterator = nav.getDescendantOrSelfAxisIterator(document);
        while (descendantOrSelfAxisIterator.hasNext())
        {
            Object node = descendantOrSelfAxisIterator.next();
            Iterator namespaceAxisIterator = nav.getNamespaceAxisIterator(node);
            while (namespaceAxisIterator.hasNext())
            {
                count++;
                assertEquals("Node type mismatch", Pattern.NAMESPACE_NODE, nav.getNodeType(namespaceAxisIterator.next()));
            }
        }
        assertEquals(25, count);
    }


    /* test for jaxen-24
    */
    public void testid53371() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/jaxen24.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/body/div", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertCountXPath(1, context, "preceding::*[1]");
            assertValueOfXPath("span", context, "local-name(preceding::*[1])");
        }
    }

    /* jaxen-58
    */
    public void testid53391() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/jaxen24.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertCountXPath(0, context, "//preceding::x");
            assertCountXPath(0, context, "//following::x");
            assertCountXPath(0, context, "/descendant::*/preceding::x");
            assertCountXPath(0, context, "/descendant::node()/preceding::x");
        }
    }

    /* test for jaxen-3
    */
    public void testid53430() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/simple.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertValueOfXPath("abd", context, "string()");
        }
    }

    public void testid53441() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/simple.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/root", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertValueOfXPath("abd", context, "string()");
        }
    }

    public void testid53452() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/simple.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/root/a", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertValueOfXPath("a", context, "string()");
        }
    }

    public void testid53463() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/simple.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/root/c", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertValueOfXPath("d", context, "string()");
        }
    }

    /* test for jaxen-3
    */
    public void testid53482() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/jaxen3.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertCountXPath(1, context, "/Configuration/hostname/attrlist/hostname[. = 'CE-A'] ");
        }
    }

    /* parser test cases all of which should fail
    */
    public void testid53502() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/numbers.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            /* repeated xpaths, jaxen-35
            */
            assertInvalidXPath(context, "/numbers numbers");
            /* invalid xpath, jaxen-34
            */
            assertInvalidXPath(context, "/a/b[c > d]efg");
            /* invalid xpath, jaxen-27
            */
            assertInvalidXPath(context, "/inv/child::");
            /* invalid xpath, jaxen-26
            */
            assertInvalidXPath(context, "/invoice/@test[abcd");
            assertInvalidXPath(context, "/invoice/@test[abcd > x");
            /* unterminated string
            */
            assertInvalidXPath(context, "string-length('a");
            /* various edge cases where code threw no exception
            */
            assertInvalidXPath(context, "/descendant::()");
            assertInvalidXPath(context, "(1 + 1");
        }
    }

    /* test cases for the use of underscores in names
    */
    public void testid53602() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/underscore.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertCountXPath(1, context, "/root/@a");
            assertCountXPath(1, context, "/root/@_a");
            assertCountXPath(1, context, "/root/b");
            assertCountXPath(1, context, "/root/_b");
            assertValueOfXPath("1", context, "/root/@a");
            assertValueOfXPath("2", context, "/root/@_a");
            assertValueOfXPath("1", context, "/root/b");
            assertValueOfXPath("2", context, "/root/_b");
        }
    }

    /* test cases for the use of = with node-sets
    */
    public void testid53662() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/web.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertValueOfXPath("true", context, "/web-app/servlet/servlet-name = 'file'");
            assertValueOfXPath("true", context, "/web-app/servlet/servlet-name = 'snoop'");
        }
    }

    public void testid53685() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/numbers.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertValueOfXPath("true", context, "/numbers/set/nr = '-3'");
            assertValueOfXPath("true", context, "/numbers/set/nr = -3");
            assertValueOfXPath("true", context, "/numbers/set/nr = 24");
            assertValueOfXPath("true", context, "/numbers/set/nr/@value = '9999'");
            assertValueOfXPath("true", context, "/numbers/set/nr/@value = 9999.0");
            assertValueOfXPath("true", context, "/numbers/set/nr/@value = 66");
        }
    }

    /* test basic math...
    */
    public void testid53733() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/numbers.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertValueOfXPath("true", context, "(8 * 2 + 1) = 17");
            assertValueOfXPath("true", context, "(1 + 8 * 2) = 17");
            assertValueOfXPath("true", context, "(7 - 3 + 1) = 5");
            assertValueOfXPath("true", context, "(8 - 4 + 5 - 6) = 3");
            /* left-assoc tests, comments show WRONG evaluation
            */
            /* 3 - 2 - 1 != 2
            */
            assertValueOfXPath("0", context, "3 - 2 - 1");
            /* 8 div 4 div 2 != 4
            */
            assertValueOfXPath("1", context, "8 div 4 div 2");
            /* 3 mod 5 mod 7 != 1
            */
            assertValueOfXPath("3", context, "3 mod 7 mod 5");
            /* 1=(2=2) is true
            */
            assertValueOfXPath("false", context, "1 = 2 = 2");
            /*  2!=(3!=1) => 2!=1 => true, (2!=3)!=1 => 1!=1 => false
            */
            assertValueOfXPath("false", context, "2 != 3 != 1");
            /* 3 > (2 > 1) is true
            */
            assertValueOfXPath("false", context, "3 > 2 > 1");
            /* 3 >= (2 >= 2) is true
            */
            assertValueOfXPath("false", context, "3 >= 2 >= 2");
            /* 1 < (2 < 3) is false
            */
            assertValueOfXPath("true", context, "1 < 2 < 3");
            /* 0 <= (2 <= 3) is true
            */
            assertValueOfXPath("true", context, "2 <= 2 <= 3");
        }
    }

    /* test cases for preceding axis with different node types
    */
    public void testid53850() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/pi2.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/a/c", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertCountXPath(1, context, "//processing-instruction()");
            assertCountXPath(1, context, "preceding-sibling::*");
            assertCountXPath(5, context, "preceding-sibling::node()");
            assertCountXPath(1, context, "preceding-sibling::*[1]");
            assertCountXPath(1, context, "preceding-sibling::processing-instruction()");
            assertValueOfXPath("order-by=\"x\"", context, "preceding-sibling::processing-instruction()");
            assertValueOfXPath("foo", context, "preceding-sibling::*[1]");
            assertValueOfXPath("order-by=\"x\"", context, "preceding-sibling::node()[2]");
        }
    }

    public void testid53911() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/id.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        SimpleVariableContext varContext = new SimpleVariableContext();
        varContext.setVariableValue(null, "foobar", "foobar");
        varContext.setVariableValue(null, "foo", "foo");
        getContextSupport().setVariableContext(varContext);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertValueOfXPath("foobar", context, "$foobar");
            assertCountXPath(1, context, "/foo[@id=$foobar]");
            assertCountXPath(0, context, "/foo[@id='$foobar']");
            assertCountXPath(1, context, "/foo[concat($foo, 'bar')=@id]");
            assertCountXPath(0, context, "CD_Library/artist[@name=$artist]");
        }
    }

    public void testid53957() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/id.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            /* attributes have a parent: their element
            */
            assertCountXPath(1, context, "/foo/@id/parent::foo");
        }
    }

    /* attributes can also be used as context nodes
    */
    public void testid53975() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/id.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/foo/@id", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertCountXPath(1, context, "parent::foo");
        }
    }

    public void testid53992() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/pi.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertCountXPath(3, context, "//processing-instruction()");
            assertCountXPath(2, context, "//processing-instruction('cheese')");
            try
            {
                Object result = assertCountXPath2(1, context, "//processing-instruction('toast')");
                assertValueOfXPath("is tasty", result, "string()");
            }
            catch (UnsupportedAxisException e)
            {
                log(debug, "      ## SKIPPED -- Unsupported Axis");
            }
        }
    }

    /* test evaluate() extension function
    */
    public void testid54032() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/evaluate.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertCountXPath(3, context, "evaluate('//jumps/*')");
            assertCountXPath(1, context, "evaluate('//jumps/object/dog')");
            assertCountXPath(0, context, "evaluate('//jumps/object')/evaluate");
            assertCountXPath(1, context, "evaluate('//jumps/object')/dog");
            assertCountXPath(1, context, "evaluate('//jumps/*')/dog");
            assertCountXPath(1, context, "//metatest[ evaluate(@select) = . ]");
        }
    }

    public void testid54082() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/numbers.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/numbers/set[1]", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertCountXPath(1, context, "*[-3 = .]");
            assertValueOfXPath("true", context, "54 < *");
            assertValueOfXPath("true", context, "55 <= *");
            assertValueOfXPath("false", context, "69 < *");
            assertValueOfXPath("true", context, "-2 > *");
            assertValueOfXPath("true", context, "-3 >= *");
            assertValueOfXPath("false", context, "-4 >= *");
        }
    }

    /* TODO
    This context should work, but needs a fixed version of saxpath to parse the right-hand side
    of the greater-than expression.
    <context select="/numbers/set[2]">
      <valueOf select="1 &gt; nr/@value">false</valueOf>
      <valueOf select="55 &gt; nr/@value">false</valueOf>
      <valueOf select="55 &gt;= nr/@value">true</valueOf>
      <valueOf select="1000000 &gt; nr/@value">true</valueOf>
    </context>
    
    */
    /* test sibling axes 
    */
    public void testid54145() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/axis.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/root", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertCountXPath(0, context, "preceding-sibling::*");
        }
    }

    public void testid54156() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/axis.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/root/a/a.3", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertCountXPath(2, context, "preceding::*");
        }
    }

    public void testid54168() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/axis.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/root/a/a.3", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertCountXPath(2, context, "preceding-sibling::*");
        }
    }

    public void testid54180() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/axis.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertValueOfXPath("a.2", context, "name(/root/a/a.3/preceding-sibling::*[1])");
            assertValueOfXPath("a.1", context, "name(/root/a/a.3/preceding-sibling::*[2])");
        }
    }

    public void testid54197() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/axis.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertValueOfXPath("a.4", context, "name(/root/a/a.3/following-sibling::*[1])");
            assertValueOfXPath("a.5", context, "name(/root/a/a.3/following-sibling::*[2])");
        }
    }

    public void testid54219() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/web.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertValueOfXPath("snoop", context, "/web-app/servlet[1]/servlet-name");
            assertValueOfXPath("snoop", context, "/web-app/servlet[1]/servlet-name/text()");
            assertValueOfXPath("file", context, "/web-app/servlet[2]/servlet-name");
            assertValueOfXPath("file", context, "/web-app/servlet[2]/servlet-name/text()");
        }
    }

    public void testid54249() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/web.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/web-app/servlet[1]", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertValueOfXPath("snoop", context, "servlet-name");
            assertValueOfXPath("snoop", context, "servlet-name/text()");
        }
    }

    public void testid54266() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/web.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/web-app/servlet[2]/servlet-name", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertCountXPath(3, context, "preceding::*");
        }
    }

    public void testid54278() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/web.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/web-app/servlet[2]/servlet-name", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertCountXPath(13, context, "following::*");
        }
    }

    /* test name
    */
    public void testid54298() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/web.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            try
            {
                Object result = assertCountXPath2(1, context, "*");
                assertValueOfXPath("web-app", result, "name()");
            }
            catch (UnsupportedAxisException e)
            {
                log(debug, "      ## SKIPPED -- Unsupported Axis");
            }
            /* NOTE that the child::node() tests only work if the
              XML document does not comments or PIs

            */
            try
            {
                Object result = assertCountXPath2(1, context, "./*");
                assertValueOfXPath("web-app", result, "name()");
            }
            catch (UnsupportedAxisException e)
            {
                log(debug, "      ## SKIPPED -- Unsupported Axis");
            }
            try
            {
                Object result = assertCountXPath2(1, context, "child::*");
                assertValueOfXPath("web-app", result, "name()");
            }
            catch (UnsupportedAxisException e)
            {
                log(debug, "      ## SKIPPED -- Unsupported Axis");
            }
            try
            {
                Object result = assertCountXPath2(1, context, "/*");
                assertValueOfXPath("web-app", result, "name()");
            }
            catch (UnsupportedAxisException e)
            {
                log(debug, "      ## SKIPPED -- Unsupported Axis");
            }
            try
            {
                Object result = assertCountXPath2(1, context, "/child::node()");
                assertValueOfXPath("web-app", result, "name(.)");
            }
            catch (UnsupportedAxisException e)
            {
                log(debug, "      ## SKIPPED -- Unsupported Axis");
            }
            try
            {
                Object result = assertCountXPath2(1, context, "child::node()");
                assertValueOfXPath("web-app", result, "name(.)");
            }
            catch (UnsupportedAxisException e)
            {
                log(debug, "      ## SKIPPED -- Unsupported Axis");
            }
            /* empty names
            */
            assertValueOfXPath("", context, "name()");
            assertValueOfXPath("", context, "name(.)");
            assertValueOfXPath("", context, "name(parent::*)");
            assertValueOfXPath("", context, "name(/)");
            assertValueOfXPath("", context, "name(/.)");
            assertValueOfXPath("", context, "name(/self::node())");
            /* name of root elemet
            */
            assertValueOfXPath("web-app", context, "name(node())");
            assertValueOfXPath("web-app", context, "name(/node())");
            assertValueOfXPath("web-app", context, "name(/*)");
            assertValueOfXPath("web-app", context, "name(/child::*)");
            assertValueOfXPath("web-app", context, "name(/child::node())");
            assertValueOfXPath("web-app", context, "name(/child::node())");
            assertValueOfXPath("web-app", context, "name(child::node())");
            assertValueOfXPath("web-app", context, "name(./*)");
            assertValueOfXPath("web-app", context, "name(*)");
        }
    }

    public void testid54467() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/web.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/*", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            /* empty names
            */
            assertValueOfXPath("", context, "name(..)");
            assertValueOfXPath("", context, "name(parent::node())");
            assertValueOfXPath("", context, "name(parent::*)");
            /* name of root elemet
            */
            assertValueOfXPath("web-app", context, "name()");
            assertValueOfXPath("web-app", context, "name(.)");
            assertValueOfXPath("web-app", context, "name(../*)");
            assertValueOfXPath("web-app", context, "name(../child::node())");
        }
    }

    /* test predicates
    */
    public void testid54522() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/nitf.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/nitf/head/docdata", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertCountXPath(1, context, "doc-id[@regsrc='AP' and @id-string='D76UIMO80']");
        }
    }

    public void testid54534() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/nitf.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/nitf/head", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertCountXPath(1, context, "meta[@name='ap-cycle']");
            assertCountXPath(1, context, "meta[@content='AP']");
            assertCountXPath(8, context, "meta[@name and @content]");
            assertCountXPath(1, context, "meta[@name='ap-cycle' and @content='AP']");
            assertCountXPath(7, context, "meta[@name != 'ap-cycle']");
        }
    }

    public void testid54570() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/nitf.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertCountXPath(1, context, "/nitf/head/meta[@name='ap-cycle']");
            assertCountXPath(1, context, "/nitf/head/meta[@content='AP']");
            assertCountXPath(8, context, "/nitf/head/meta[@name and @content]");
            assertCountXPath(1, context, "/nitf/head/meta[@name='ap-cycle' and @content='AP']");
            assertCountXPath(7, context, "/nitf/head/meta[@name != 'ap-cycle']");
        }
    }

    public void testid54614() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/moreover.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertCountXPath(1, context, "/child::node()");
            assertCountXPath(1, context, "/*");
            assertCountXPath(20, context, "/*/article");
            assertCountXPath(221, context, "//*");
            assertCountXPath(20, context, "//*[local-name()='article']");
            assertCountXPath(20, context, "//article");
            assertCountXPath(20, context, "/*/*[@code]");
            assertCountXPath(1, context, "/moreovernews/article[@code='13563275']");
            try
            {
                BaseXPath xpath = new BaseXPath("/moreovernews/article[@code='13563275']");
                List results = xpath.selectNodes(getContext(context));
                Object result = results.get(0);
                assertValueOfXPath("http://c.moreover.com/click/here.pl?x13563273", result, "url");
            }
            catch (UnsupportedAxisException e)
            {
                log(debug, "      ## SKIPPED -- Unsupported Axis");
            }
            try
            {
                BaseXPath xpath = new BaseXPath("/*/article[@code='13563275']");
                List results = xpath.selectNodes(getContext(context));
                Object result = results.get(0);
                assertValueOfXPath("http://c.moreover.com/click/here.pl?x13563273", result, "url");
            }
            catch (UnsupportedAxisException e)
            {
                log(debug, "      ## SKIPPED -- Unsupported Axis");
            }
            try
            {
                BaseXPath xpath = new BaseXPath("//article[@code='13563275']");
                List results = xpath.selectNodes(getContext(context));
                Object result = results.get(0);
                assertValueOfXPath("http://c.moreover.com/click/here.pl?x13563273", result, "url");
            }
            catch (UnsupportedAxisException e)
            {
                log(debug, "      ## SKIPPED -- Unsupported Axis");
            }
            try
            {
                BaseXPath xpath = new BaseXPath("//*[@code='13563275']");
                List results = xpath.selectNodes(getContext(context));
                Object result = results.get(0);
                assertValueOfXPath("http://c.moreover.com/click/here.pl?x13563273", result, "url");
            }
            catch (UnsupportedAxisException e)
            {
                log(debug, "      ## SKIPPED -- Unsupported Axis");
            }
            try
            {
                BaseXPath xpath = new BaseXPath("/child::node()/child::node()[@code='13563275']");
                List results = xpath.selectNodes(getContext(context));
                Object result = results.get(0);
                assertValueOfXPath("http://c.moreover.com/click/here.pl?x13563273", result, "url");
            }
            catch (UnsupportedAxisException e)
            {
                log(debug, "      ## SKIPPED -- Unsupported Axis");
            }
            try
            {
                BaseXPath xpath = new BaseXPath("/*/*[@code='13563275']");
                List results = xpath.selectNodes(getContext(context));
                Object result = results.get(0);
                assertValueOfXPath("http://c.moreover.com/click/here.pl?x13563273", result, "url");
            }
            catch (UnsupportedAxisException e)
            {
                log(debug, "      ## SKIPPED -- Unsupported Axis");
            }
        }
    }

    /* test other node types
    */
    public void testid54747() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/contents.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertCountXPath(3, context, "processing-instruction()");
            assertCountXPath(3, context, "/processing-instruction()");
            assertCountXPath(1, context, "/comment()");
            assertCountXPath(1, context, "comment()");
            assertCountXPath(2, context, "/child::node()/comment()");
            assertCountXPath(2, context, "/*/comment()");
            assertCountXPath(3, context, "//comment()");
        }
    }

    /* test positioning
    */
    public void testid54802() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/fibo.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertCountXPath(9, context, "/*/fibonacci[position() < 10]");
            assertValueOfXPath("196417", context, "sum(//fibonacci)");
            assertValueOfXPath("325", context, "sum(//fibonacci/@index)");
            assertValueOfXPath("1", context, "/*/fibonacci[2]");
            assertValueOfXPath("75025", context, "/*/fibonacci[ count(/*/fibonacci) ]");
            assertValueOfXPath("46368", context, "/*/fibonacci[ count(/*/fibonacci) - 1 ]");
        }
    }

    /* test number functions
    */
    /* test Axes 
    */
    public void testid54853() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/web.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertCountXPath(19, context, "descendant-or-self::*");
            assertCountXPath(19, context, "descendant::*");
            assertCountXPath(19, context, "/descendant::*");
            assertCountXPath(19, context, "/descendant-or-self::*");
            assertCountXPath(2, context, "/descendant::servlet");
            assertCountXPath(2, context, "/descendant-or-self::servlet");
            assertCountXPath(2, context, "descendant-or-self::servlet");
            assertCountXPath(2, context, "descendant::servlet");
            assertCountXPath(2, context, "/*/servlet");
            assertValueOfXPath("2", context, "count(/*/servlet)");
            assertCountXPath(2, context, "//servlet");
            assertValueOfXPath("2", context, "count(//servlet)");
        }
    }

    public void testid54932() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/web.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/web-app", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertCountXPath(2, context, "/descendant::servlet");
            assertCountXPath(2, context, "/descendant-or-self::servlet");
            assertCountXPath(2, context, "descendant-or-self::servlet");
            assertCountXPath(2, context, "descendant::servlet");
        }
    }

    public void testid54968() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/much_ado.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertCountXPath(5, context, "/descendant::ACT");
            assertCountXPath(5, context, "descendant::ACT");
            assertValueOfXPath("Much Ado about Nothing", context, "/PLAY/TITLE");
            assertValueOfXPath("4", context, "2+2");
            assertValueOfXPath("21", context, "5 * 4 + 1");
            assertValueOfXPath("5", context, "count(descendant::ACT)");
            assertValueOfXPath("35", context, "10 + count(descendant::ACT) * 5");
            assertValueOfXPath("75", context, "(10 + count(descendant::ACT)) * 5");
        }
    }

    public void testid55020() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/much_ado.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/PLAY/ACT[2]/SCENE[1]", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertCountXPath(5, context, "/descendant::ACT");
            assertCountXPath(5, context, "../../descendant::ACT");
            assertCountXPath(141, context, "/PLAY/ACT[2]/SCENE[1]/descendant::SPEAKER");
            assertCountXPath(141, context, "descendant::SPEAKER");
            assertValueOfXPath("646", context, "count(descendant::*)+1");
            assertValueOfXPath("142", context, "count(descendant::SPEAKER)+1");
            assertValueOfXPath("2", context, "count(ancestor::*)");
            assertValueOfXPath("1", context, "count(ancestor::PLAY)");
            assertValueOfXPath("3", context, "count(ancestor-or-self::*)");
            assertValueOfXPath("1", context, "count(ancestor-or-self::PLAY)");
            assertValueOfXPath("6", context, "5+count(ancestor::*)-1");
        }
    }

    public void testid55090() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/much_ado.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            /* Test correct predicate application
            */
            assertValueOfXPath("5", context, "count(/PLAY/ACT/SCENE[1])");
        }
    }

    /* test axis node ordering
    */
    public void testid55112() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/web.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            /* Reported as Jira issue JAXEN-24
            */
            assertCountXPath(1, context, "//servlet-mapping/preceding::*[1][name()='description']");
            assertCountXPath(1, context, "/web-app/servlet//description/following::*[1][name()='servlet-mapping']");
            assertCountXPath(1, context, "/web-app/servlet//description/following::*[2][name()='servlet-name']");
        }
    }

    /* test document function
    */
    public void testid55150() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/text.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            try
            {
                Object result = assertCountXPath2(1, context, "document('xml/web.xml')");
                assertValueOfXPath("snoop", result, "/web-app/servlet[1]/servlet-name");
                assertValueOfXPath("snoop", result, "/web-app/servlet[1]/servlet-name/text()");
            }
            catch (UnsupportedAxisException e)
            {
                log(debug, "      ## SKIPPED -- Unsupported Axis");
            }
            assertValueOfXPath("snoop", context, "document('xml/web.xml')/web-app/servlet[1]/servlet-name");
        }
    }

    /* Test to check if the context changes when an extension function is used.
    First test is an example, second is the actual test.
    
    */
    public void testid55189() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/text.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/foo/bar/cheese[1]", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertValueOfXPath("3foo3", context, "concat(./@id,'foo',@id)");
            assertValueOfXPath("3snoop3", context, "concat(./@id,document('xml/web.xml')/web-app/servlet[1]/servlet-name,./@id)");
        }
    }

    public void testid55211() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/message.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertValueOfXPath("Pruefgebiete", context, "/message/body/data/items/item[name/text()='parentinfo']/value");
            assertValueOfXPath("Pruefgebiete", context, "document('xml/message.xml')/message/body/data/items/item[name/text()='parentinfo']/value");
        }
    }

    /* test behaviour of AbsoluteLocationPath
    */
    public void testid55183() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/simple.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/root/a", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertValueOfXPath("ab", context, "concat( ., /root/b )");
            assertValueOfXPath("ba", context, "concat( ../b, . )");
            assertValueOfXPath("ba", context, "concat( /root/b, . )");
            assertValueOfXPath("db", context, "concat( /root/c/d, ../b )");
        }
    }

    /* test the translate() function
    */
    public void testid55268() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/simple.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertValueOfXPath("", context, "translate( '', '', '' )");
            assertValueOfXPath("abcd", context, "translate( 'abcd', '', '' )");
            assertValueOfXPath("abcd", context, "translate( 'abcd', 'abcd', 'abcd' )");
            assertValueOfXPath("abcd", context, "translate( 'abcd', 'dcba', 'dcba' )");
            assertValueOfXPath("dcba", context, "translate( 'abcd', 'abcd', 'dcba' )");
            assertValueOfXPath("ab", context, "translate( 'abcd', 'abcd', 'ab' )");
            assertValueOfXPath("cd", context, "translate( 'abcd', 'cdab', 'cd' )");
            assertValueOfXPath("xy", context, "translate( 'abcd', 'acbd', 'xy' )");
            assertValueOfXPath("abcd", context, "translate( 'abcd', 'abcdb', 'abcdb' )");
            assertValueOfXPath("abcd", context, "translate( 'abcd', 'abcd', 'abcdb' )");
        }
    }

    public void testid55331() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/simple.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertValueOfXPath("234", context, "substring('12345', 1.5, 2.6)");
            assertValueOfXPath("12", context, "substring('12345', 0, 3)");
            assertValueOfXPath("", context, "substring('12345', 0 div 0, 3)");
            assertValueOfXPath("", context, "substring('12345', 1, 0 div 0)");
            assertValueOfXPath("12345", context, "substring('12345', -42, 1 div 0)");
            assertValueOfXPath("", context, "substring('12345', -1 div 0, 1 div 0)");
            assertValueOfXPath("345", context, "substring('12345', 3)");
            assertValueOfXPath("12345", context, "substring('12345',1,15)");
        }
    }

    /* Some tests for the normalize-space() function
    */
    public void testid55382() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/simple.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertValueOfXPath("abc", context, "normalize-space('    abc    ')");
            assertValueOfXPath("a b c", context, "normalize-space(' a  b  c  ')");
            assertValueOfXPath("a b c", context, "normalize-space(' a \n b \n  c')");
            /* Next test case addresses issue JAXEN-22
            */
            assertValueOfXPath("", context, "normalize-space(' ')");
            /* Next test case addresses issue JAXEN-29
            */
            assertValueOfXPath("", context, "normalize-space('')");
        }
    }

    /* test cases for String extension functions
    */
    public void testid55429() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/web.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/web-app/servlet[1]", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertValueOfXPath("SNOOPSERVLET", context, "upper-case( servlet-class )");
            assertValueOfXPath("snoopservlet", context, "lower-case( servlet-class )");
            assertValueOfXPath("SNOOPSERVLET", context, "upper-case( servlet-class, 'fr' )");
            assertValueOfXPath("SNOOPSERVLET", context, "upper-case( servlet-class, 'fr-CA' )");
            assertValueOfXPath("SNOOPSERVLET", context, "upper-case( servlet-class, 'es-ES-Traditional_WIN' )");
            assertValueOfXPath("true", context, "ends-with( servlet-class, 'Servlet' )");
            assertValueOfXPath("false", context, "ends-with( servlet-class, 'S' )");
        }
    }

    /* test cases for the lang() function
    */
    public void testid55485() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/lang.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertCountXPath(0, context, "/e1/e2[lang('hr')]");
            assertCountXPath(1, context, "/e1/e2/e3[lang('en')]");
            assertCountXPath(1, context, "/e1/e2/e3[lang('en-US')]");
            assertCountXPath(0, context, "/e1/e2/e3[lang('en-GB')]");
            assertCountXPath(2, context, "/e1/e2/e3[lang('hu')]");
            assertCountXPath(0, context, "/e1/e2/e3[lang('hu-HU')]");
            assertCountXPath(1, context, "/e1/e2/e3[lang('es')]");
            assertCountXPath(0, context, "/e1/e2/e3[lang('es-BR')]");
        }
    }

    /* test namespace
    */
    public void testid55235() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/namespaces.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        SimpleNamespaceContext nsContext = new SimpleNamespaceContext();
        nsContext.addNamespace("alias", "http://fooNamespace/");
        nsContext.addNamespace("bar", "http://barNamespace/");
        nsContext.addNamespace("voo", "http://fooNamespace/");
        nsContext.addNamespace("foo", "http://fooNamespace/");
        getContextSupport().setNamespaceContext(nsContext);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertCountXPath(1, context, "/*");
            assertCountXPath(1, context, "/foo:a");
            assertCountXPath(1, context, "/foo:a/b");
            assertCountXPath(1, context, "/voo:a/b/c");
            assertCountXPath(1, context, "/voo:a/bar:f");
            assertCountXPath(1, context, "/*[namespace-uri()='http://fooNamespace/' and local-name()='a']");
            assertCountXPath(1, context, "/*[local-name()='a' and namespace-uri()='http://fooNamespace/']/*[local-name()='x' and namespace-uri()='http://fooNamespace/']");
            assertCountXPath(1, context, "/*[local-name()='a' and namespace-uri()='http://fooNamespace/']/*[local-name()='x' and namespace-uri()='http://fooNamespace/']/*[local-name()='y' and namespace-uri()='http://fooNamespace/']");
        }
    }

    /* the prefix here and in the document have no relation; it's their
    namespace-uri binding that counts 
    */
    public void testid55615() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/namespaces.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        SimpleNamespaceContext nsContext = new SimpleNamespaceContext();
        nsContext.addNamespace("foo", "http://somethingElse/");
        getContextSupport().setNamespaceContext(nsContext);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertCountXPath(0, context, "/foo:a/b/c");
        }
    }

    public void testid55632() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/namespaces.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        SimpleNamespaceContext nsContext = new SimpleNamespaceContext();
        nsContext.addNamespace("alias", "http://fooNamespace/");
        nsContext.addNamespace("bar", "http://barNamespace/");
        nsContext.addNamespace("foo", "http://fooNamespace/");
        getContextSupport().setNamespaceContext(nsContext);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertValueOfXPath("Hello", context, "/foo:a/b/c");
            assertValueOfXPath("Hey", context, "/foo:a/foo:d/foo:e");
            assertValueOfXPath("Hey3", context, "/foo:a/alias:x/alias:y");
            assertValueOfXPath("Hey3", context, "/foo:a/foo:x/foo:y");
            assertValueOfXPath("Hey3", context, "/*[local-name()='a' and namespace-uri()='http://fooNamespace/']/*[local-name()='x' and namespace-uri()='http://fooNamespace/']/*[local-name()='y' and namespace-uri()='http://fooNamespace/']");
        }
    }

    public void testid55676() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/defaultNamespace.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            /* NOTE: /a/b/c selects elements in no namespace only!
            */
            assertCountXPath(0, context, "/a/b/c");
            /*
                The following test uses an unbound prefix 'x' and should throw an exception.
                Addresses issue JAXEN-18.
                Turns out this isn't really tested as the test didn't fail when the exception wasn't thrown.
              <test select="/x:a/x:b/x:c" count="0" exception="true"/>

            */
        }
    }

    public void testid55692() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/defaultNamespace.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        SimpleNamespaceContext nsContext = new SimpleNamespaceContext();
        nsContext.addNamespace("dummy", "http://dummyNamespace/");
        getContextSupport().setNamespaceContext(nsContext);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertCountXPath(1, context, "/dummy:a/dummy:b/dummy:c");
        }
    }

    public void testid55716() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/text.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertCountXPath(3, context, "/foo/bar/text()");
            assertValueOfXPath("baz", context, "normalize-space(/foo/bar/text())");
        }
    }

    public void testid55739() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/testNamespaces.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            /* the root is not an element, so no namespaces
            */
            assertCountXPath(0, context, "namespace::*");
            assertCountXPath(0, context, "/namespace::*");
            /* must count the default xml: prefix as well
            */
            assertCountXPath(3, context, "/Template/Application1/namespace::*");
            assertCountXPath(3, context, "/Template/Application2/namespace::*");
            /* every element has separate copies
            */
            assertCountXPath(25, context, "//namespace::*");
        }
    }

    public void testid55797() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/testNamespaces.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/Template/Application1", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            /* must count the default xml: prefix as well
            */
            assertCountXPath(3, context, "namespace::*");
            assertCountXPath(0, context, "/namespace::*");
            assertCountXPath(3, context, "/Template/Application1/namespace::*");
            assertCountXPath(3, context, "/Template/Application2/namespace::*");
            assertCountXPath(25, context, "//namespace::*");
            assertCountXPath(8, context, "//namespace::xplt");
            /* the name test literally matches the prefix as given in the
              document, and does not use the uri
            */
            assertCountXPath(0, context, "//namespace::somethingelse");
        }
    }

    public void testid55873() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/testNamespaces.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            /* namespace nodes have their element as their parent
            */
            assertCountXPath(1, context, "/Template/namespace::xml/parent::Template");
        }
    }

    /* namespace nodes can also be used as context nodes
    */
    public void testid55893() throws JaxenException
    {
        Navigator nav = getNavigator();
        String url = "xml/testNamespaces.xml";
        log("Document [" + url + "]");
        Object document = nav.getDocument(url);
        XPath contextpath = new BaseXPath("/Template/namespace::xml", nav);
        log("Initial Context :: " + contextpath);
        List list = contextpath.selectNodes(document);
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Object context = iter.next();
            assertCountXPath(1, context, "parent::Template");
        }
    }
}            
        