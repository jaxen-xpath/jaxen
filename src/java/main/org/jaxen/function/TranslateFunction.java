
package org.jaxen.function;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;
import org.jaxen.function.StringFunction;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * <p><b>4.2</b> <code><i>string</i> translate(<i>string</i>,<i>string</i>,<i>string</i>)</code> 
 * 
 * The translation is done thru a HashMap.
 * Performance tip (for anyone who needs to improve the performance of this particular function):
 *   Cache the HashMaps, once they are constructed.
 * 
 * @author Jan Dvorak ( jan.dvorak @ mathan.cz )
 * 
 */
public class TranslateFunction implements Function
{

    public Object call(Context context,
                       List args) throws FunctionCallException
    {
	if (args.size() == 3)
        {
            return evaluate( args.get(0),
                             args.get(1),
                             args.get(2),
                             context.getNavigator() );
        }

        throw new FunctionCallException( "translate() requires three arguments." );
    }

    public static String evaluate(Object strArg,
                                  Object fromArg,
                                  Object toArg,
                                  Navigator nav)
    {
        String inStr = StringFunction.evaluate( strArg, nav );
        String fromStr = StringFunction.evaluate( fromArg, nav );
        String toStr = StringFunction.evaluate( toArg, nav );
	
	// Initialize the mapping in a HashMap
	Map charMap = new HashMap();
	int fromLen = fromStr.length();
	int toLen = toStr.length();
	for ( int i = 0; i < fromLen; ++i ) {
	    String cFrom = fromStr.substring( i, i+1 ).intern();
	    if ( charMap.containsKey( cFrom ) ) {
		// We've seen the character before, ignore
		continue;
	    }
	    if ( i < toLen ) {
		Character cTo = new Character( toStr.charAt( i ) );
		// Will change
		charMap.put( cFrom, cTo );
	    } else {
		// Will delete
		charMap.put( cFrom, null );
	    }
	}

	// Process the input string thru the map
	StringBuffer outStr = new StringBuffer( inStr.length() );
	int inLen = inStr.length();
	for ( int i = 0; i < inLen; ++i ) {
	    String cIn = inStr.substring( i, i+1 );
	    if ( charMap.containsKey( cIn ) ) {
		Character cTo = (Character) charMap.get( cIn );
		if ( cTo != null ) {
		    outStr.append( cTo.charValue() );
		}
	    } else {
		outStr.append( cIn );
	    }
	}

	return new String( outStr );
    }
}

