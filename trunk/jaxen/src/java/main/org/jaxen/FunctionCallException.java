
package org.jaxen;

import java.io.PrintStream;
import java.io.PrintWriter;

/** <code>FunctionCallException</code> is thrown if an exception
 * occurs during the evaluation of a function.
 * This exception may include a root exception, such as if the 
 * real exception was failure to load an XML document via the
 * document() function call.
 *
 * @author bob mcwhirter (bob @ werken.com)
 * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 */
public class FunctionCallException extends JaxenException
{
    private Throwable nestedException;

    public FunctionCallException(String message) {
        super( message );
    }

    public FunctionCallException(Throwable nestedException) {
        super( nestedException.getMessage() );
        this.nestedException = nestedException;
    }

    public FunctionCallException(String message, Exception nestedException) {
        super( message );
        this.nestedException = nestedException;
    }

    public void printStackTrace( PrintStream s ) {
        super.printStackTrace();
        if ( nestedException != null ) 
        {
            s.println( "Root cause:" );
            nestedException.printStackTrace( s );
        }
    }
    
    public void printStackTrace( PrintWriter w ) {
        super.printStackTrace();
        if ( nestedException != null ) 
        {
            w.println( "Root cause:" );
            nestedException.printStackTrace( w );
        }
    }
    
    public void printStackTrace() {
        super.printStackTrace();
        if ( nestedException != null ) 
        {
            System.out.println( "Root cause:" );
            nestedException.printStackTrace();
        }
    }
    
    public Throwable fillInStackTrace() {
        if ( nestedException == null ) {
            return super.fillInStackTrace(); 
        } else {
            return nestedException.fillInStackTrace();
        }
    }
    
    // Properties
    //-------------------------------------------------------------------------    
    public Throwable getNestedException() {
        return nestedException;
    }
}
