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

package org.jaxen.function.ext;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.Navigator;
import org.jaxen.function.StringFunction;

/** <p><b>Extension Function</b> <code><i>boolean</i> matrix-concat(<i>nodeSet</i>,<i>nodeSet</i>,<i>nodeSet*</i>)</code> 
  * 
  * @author James Pereira (JPereira@CT.BBD.CO.ZA)
  */

public class MatrixConcatFunction implements Function {
    
    public Object call(Context context, List args) {
        if ( args.size() >= 2 ) {
            return evaluate(args, context.getNavigator());
        }
        return null;
    }
    
    public static Object evaluate(List list, Navigator nav) {        
        ArrayList matrix = new ArrayList();       
        
        Iterator argIter = list.iterator();

        while (argIter.hasNext()) {
            ArrayList v = new ArrayList();
            Object obj = argIter.next();
            if (obj instanceof List) {
                List args = (List) obj;
                for ( int i = 0, size = args.size(); i < size; i++ ) {
                    v.add( StringFunction.evaluate( args.get(i), nav ) );
                }
            }
            else {
                v.add( StringFunction.evaluate( obj, nav ) );
            }
            matrix.add(v);
        }
        
        ArrayList result = new ArrayList();
        Enumeration elemList = new MatrixEnum( matrix );
        while (elemList.hasMoreElements()) {
            Object obj = elemList.nextElement();
            if (obj instanceof List) {
                StringBuffer text = new StringBuffer(127);
                List args = (List) obj;
                for (Iterator it = args.iterator(); it.hasNext(); ) {
                    text.append(it.next());
                }
                result.add( text.toString() );
            }
            else {
                result.add( obj );
            }
        }
        return result;
    }
    
    public static class MatrixEnum implements Enumeration {
        private ArrayList m_source;
        private int m_maxSize = 0;
        private int m_currIdx = -1;
        
        public MatrixEnum (ArrayList _source) {
            m_source = _source;
            
            for ( Iterator iter = m_source.iterator(); iter.hasNext(); ) {
                ArrayList element = (ArrayList) iter.next();
                int size = element.size();
                if (size > m_maxSize) {
                    m_maxSize = size;
                }
            }
        }
        
        public MatrixEnum (ArrayList _source, int _maxSize) {
            m_source = _source;
            m_maxSize = _maxSize;
        }
        
        public boolean hasMoreElements() {
            if ((m_maxSize != 0) && (++m_currIdx < m_maxSize)) {
                return true;
            }
            else {
                return false;
            }
        }
        
        public Object nextElement() {
            ArrayList result = new ArrayList();
            for ( Iterator iter = m_source.iterator(); iter.hasNext(); ) {
                ArrayList element = (ArrayList) iter.next();
                int size = element.size();
                if ( m_currIdx < size ) {
                    result.add( element.get( m_currIdx ) );
                }
                else {
                    if ( size > 0 ) {
                        result.add( element.get( size - 1 ) );
                    }
                    else {
                        // XXXX: what to do now?
                        result.add( "" );
                    }
                }
            }
            return result;
        }
    }
}
