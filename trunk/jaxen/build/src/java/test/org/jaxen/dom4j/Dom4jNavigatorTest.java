
package org.jaxen.dom4j;

import junit.framework.TestCase;

import org.jaxen.NavigatorTestBase;

import org.jaxen.Context;
import org.jaxen.Navigator;
import org.jaxen.ContextSupport;
import org.jaxen.XPathFunctionContext;
import org.jaxen.JaXPath;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Attribute;
import org.dom4j.Comment;
import org.dom4j.DocumentFactory;

import org.jaxen.util.SingleObjectIterator;

import org.saxpath.SAXPathException;
import org.saxpath.XPathSyntaxException;

import java.util.Iterator;
import java.util.List;
import java.util.Collections;

public class Dom4jNavigatorTest extends NavigatorTestBase
{
    private Document doc;
    private Element foo;
    private Attribute taco;
    private Element bar;
    private Element baz;
    private Element bazToo;
    private Element bazThree;
    private Element cheese;

    public Dom4jNavigatorTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        super.setUp();

        this.doc = DocumentFactory.getInstance().createDocument();

        this.doc.addComment("I am a document comment");

        this.foo    = this.doc.addElement("foo");
        this.foo.addAttribute("taco", "crunchy");
        this.taco = this.foo.attribute("taco");

        this.bar    = this.foo.addElement("bar");
        this.baz    = this.bar.addElement("baz");

        this.foo.addComment("I am a comment");

        this.cheese = this.bar.addElement("cheese");
        this.bazToo = this.bar.addElement("baz");
        this.bazThree = this.bar.addElement("baz");

        this.bazThree.addAttribute("bazThree", "true");
    }

    public void tearDown()
    {
        this.doc = null;
        this.foo = null;
        this.bar = null;
    }

    public Object getDocument()
    {
        return this.doc;
    }

    public Object getInitialContextNode()
    {
        return this.foo;
    }

    public Navigator getNavigator()
    {
        return new Dom4jNavigator();
    }
}
