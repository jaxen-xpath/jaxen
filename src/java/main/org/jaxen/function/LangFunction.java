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


package org.jaxen.function;

import java.util.Iterator;
import java.util.List;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;
import org.jaxen.UnsupportedAxisException;

/**
 * <p><b>4.3</b> <code><i>boolean</i> lang(<i>string</i>)</code> 
 * 
 * @author Attila Szegedi (szegedia @ freemail.hu)
 */
public class LangFunction implements Function
{
    private static final String LANG_LOCALNAME = "lang";
    private static final String XMLNS_URI = 
        "http://www.w3.org/XML/1998/namespace";
    

    public Object call(Context context,
                       List args) throws FunctionCallException
    {
        if (args.size() == 1)
        {
            if(args.get(0) instanceof String)
            {
                try
                {
                    return evaluate(context.getNodeSet(), 
                                     (String)args.get(0),
                                      context.getNavigator() );
                }
                catch(UnsupportedAxisException e)
                {
                    throw new FunctionCallException("Can't evaluate lang()", 
                                                     e);
                }
            }
            throw 
                new FunctionCallException("lang() requires a string argument.");
        }

        throw 
            new FunctionCallException("lang() requires exactly one argument.");
    }

    public static Boolean evaluate(List contextNodes, String lang,
                                  Navigator nav)
      throws UnsupportedAxisException
    {
        // The XPath spec isn't clear what to do when there's more than one
        // node in the context. I assume that in this case, lang should
        // return true iff it would return true for every node individually.
        // FIXME There can only be one context node
        for(Iterator nodes = contextNodes.iterator(); nodes.hasNext();)
        {
            if(!evaluate(nodes.next(), lang, nav))
            {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    private static boolean evaluate(Object node, String lang, 
                                     Navigator nav)
      throws UnsupportedAxisException
    {
        
        Object element = node;
        if (! nav.isElement(element)) {
            element = nav.getParentNode(node);
        }
        while (element != null && nav.isElement(element)) 
        {
            Iterator attrs = nav.getAttributeAxisIterator(element);
            while(attrs.hasNext())
            {
                Object attr = attrs.next();
                if(LANG_LOCALNAME.equals(nav.getAttributeName(attr)) && 
                   XMLNS_URI.equals(nav.getAttributeNamespaceUri(attr)))
                {
                    return 
                        isSublang(nav.getAttributeStringValue(attr), lang);
                }
            }
            element = nav.getParentNode(node);
        }
        return false;
    }
    
    private static boolean isSublang(String sublang, String lang)
    {
        if(sublang.equalsIgnoreCase(lang))
        {
            return true;
        }
        int ll = lang.length();
        return 
            sublang.length() > ll && 
            sublang.charAt(ll) == '-' && 
            sublang.substring(0, ll).equalsIgnoreCase(lang);
    }
}

