
package org.jaxen;

import junit.framework.TestCase;

import java.util.List;
import java.util.ArrayList;

public class ContextTest extends TestCase
{
    private List           nodeSet;
    private ContextSupport support;

    public ContextTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.nodeSet = new ArrayList();

        this.nodeSet.add( "one" );
        this.nodeSet.add( "two" );
        this.nodeSet.add( "three" );
        this.nodeSet.add( "four" );

        this.support = new ContextSupport();
    }

    public void tearDown()
    {
        this.nodeSet = null;
    }

    public void testDuplicate()
    {
        Context original = new Context( this.support );

        original.setNodeSet( this.nodeSet );

        original.setSize( 4 );
        original.setPosition( 2 );

        Context dupe = original.duplicate();

        assertTrue( original != dupe );

        List dupeNodeSet = dupe.getNodeSet();

        assertTrue( original.getNodeSet() != dupe.getNodeSet() );

        dupeNodeSet.clear();

        assertSame( dupeNodeSet,
                    dupe.getNodeSet() );

        assertEquals( 0,
                      dupe.getNodeSet().size() );


        assertEquals( 4,
                      original.getNodeSet().size() );

        dupe.setSize( 0 );
        dupe.setPosition( 0 );

        assertEquals( 0,
                      dupe.getSize() );

        assertEquals( 0,
                      dupe.getPosition() );

        assertEquals( 4,
                      original.getSize() );

        assertEquals( 2,
                      original.getPosition() );
    }
}

