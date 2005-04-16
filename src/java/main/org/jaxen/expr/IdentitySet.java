/*
 $Id$

 Copyright 2005 Elliotte Rusty Harold. All Rights Reserved.
 
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
package org.jaxen.expr;

import java.util.HashSet;


/**
 * <p>
 *  This is a set that uses identity rather than equality semantics.
 * </p>
 * 
 * @author Elliotte Rusty Harold
 *
 */
final class IdentitySet {

    private HashSet contents = new HashSet();
    
    IdentitySet() {
        super();
    }

    void add(Object object) {
        IdentityWrapper wrapper = new IdentityWrapper(object);
        contents.add(wrapper);
    }

    public boolean contains(Object object) {
        IdentityWrapper wrapper = new IdentityWrapper(object);
        return contents.contains(wrapper);
    }
    
    private static class IdentityWrapper {

        private Object object;
        
        IdentityWrapper(Object object) {
            this.object = object;
        }
        
        public boolean equals(Object o) {
            IdentityWrapper w = (IdentityWrapper) o;
            return object == w.object;
        }

        public int hashCode() {
            return System.identityHashCode(object);
        }

    }


}
