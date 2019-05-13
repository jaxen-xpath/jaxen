/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 *
 * Copyright 2006 Elliotte Rusty Harold
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


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.jaxen.SimpleVariableContext;
import org.jaxen.UnresolvableException;

import junit.framework.TestCase;

/**
 * <p>
 *  Test for namespace context.
 * </p>
 * 
 * @author Elliotte Rusty Harold
 * @version 1.1b12
 *
 */
public class SimpleVariableContextTest extends TestCase
{

    public void testRoundTripSerialization() 
      throws IOException, ClassNotFoundException, UnresolvableException {
        
        // construct test object
        SimpleVariableContext original = new SimpleVariableContext();
        original.setVariableValue("s", "String Value");
        original.setVariableValue("x", new Double(3.1415292));
        original.setVariableValue("b", Boolean.TRUE);
        
        // serialize
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(original);
        oos.close();
        
        //deserialize
        byte[] pickled = out.toByteArray();
        InputStream in = new ByteArrayInputStream(pickled);
        ObjectInputStream ois = new ObjectInputStream(in);
        Object o = ois.readObject();
        SimpleVariableContext copy = (SimpleVariableContext) o;
        
        // test the result
        assertEquals("String Value", copy.getVariableValue("", "", "s"));
        assertEquals(new Double(3.1415292), copy.getVariableValue("", "", "x"));
        assertEquals(Boolean.TRUE, copy.getVariableValue("", "", "b"));
        assertEquals("", "");
        
    }
    
    public void testSerializationFormatHasNotChanged() 
      throws IOException, ClassNotFoundException, UnresolvableException {
        
        //deserialize
        InputStream in = new FileInputStream("xml/simplevariablecontext.ser");
        ObjectInputStream ois = new ObjectInputStream(in);
        Object o = ois.readObject();
        SimpleVariableContext context = (SimpleVariableContext) o;
        
        // test the result
        assertEquals("String Value", context.getVariableValue("", "", "s"));
        assertEquals(new Double(3.1415292), context.getVariableValue("", "", "x"));
        assertEquals(Boolean.TRUE, context.getVariableValue("", "", "b"));
        assertEquals("", "");
        
    }
    
}
