
package org.jaxen.jdom;

import org.jaxen.NavigatorTestBase;

import junit.framework.TestCase;

import org.jaxen.Context;
import org.jaxen.Navigator;
import org.jaxen.ContextSupport;
import org.jaxen.XPathFunctionContext;
import org.jaxen.JaXPath;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Attribute;
import org.jdom.Comment;

import org.jaxen.util.SingleObjectIterator;

import org.saxpath.SAXPathException;
import org.saxpath.XPathSyntaxException;

import java.util.Iterator;
import java.util.List;
import java.util.Collections;

public class JdomNavigatorTest extends NavigatorTestBase
{
    private Document doc;
    private Element foo;
    private Attribute taco;
    private Element bar;
    private Element baz;
    private Element bazToo;
    private Element bazThree;
    private Element cheese;

    public JdomNavigatorTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        super.setUp();

        this.foo    = new Element("foo");
        this.foo.setAttribute("taco", "crunchy");

        this.doc = new Document( this.foo );

        this.doc.getContent().add( new Comment("I am a document comment") );

        this.taco = this.foo.getAttribute("taco");

        this.bar    = new Element("bar");
        this.foo.addContent( this.bar );

        this.baz    = new Element("baz");
        this.bar.addContent( this.baz );

        this.foo.getContent().add( new Comment("I am a comment") );

        this.cheese = new Element("cheese");
        this.bar.addContent( this.cheese );

        this.bazToo = new Element("baz");
        this.bar.addContent( this.bazToo );

        this.bazThree = new Element("baz");
        this.bar.addContent( this.bazThree );

        this.bazThree.setAttribute("bazThree", "true");

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
        return new JdomNavigator();
    }
}
