/** This is a W3C interface we include here so that NamespaceNode can compile in both
 *  Java 1.4 and 1.5. It's owned by the W3C, and available under their usual 
 *  extremely liberal license so this shoudldn't bother anyone. (XPath itself
 *  is under the same license after all.)
 */

package org.w3c.dom;

public interface UserDataHandler {
    // OperationType
    public static final short NODE_CLONED               = 1;
    public static final short NODE_IMPORTED             = 2;
    public static final short NODE_DELETED              = 3;
    public static final short NODE_RENAMED              = 4;
    public static final short NODE_ADOPTED              = 5;

    public void handle(short operation, 
                       String key, 
                       Object data, 
                       Node src, 
                       Node dst);

}