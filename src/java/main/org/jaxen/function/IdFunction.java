package org.jaxen.function;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.Navigator;
import org.jaxen.FunctionCallException;

/**
 * <p><b>4.1</b> <code><i>node-set</i> id(<i>object</i>)</code> 
 *  
 * @author Erwin Bolwidt (ejb @ klomp.org)
 */
public class IdFunction implements Function
{

    public Object call (Context context, List args) throws FunctionCallException
    {
        if ( args.size() == 1 ) {
            return evaluate( context.getNodeSet(),
                             args.get(0), context.getNavigator() );
        }

        throw new FunctionCallException( "id() requires one argument" );
    }

    public static List evaluate (List contextNodes, Object arg, Navigator nav)
    {
        List nodes = new ArrayList();

        if (contextNodes.size() == 0)
            return nodes;
      
        Object contextNode = contextNodes.get(0);

        if (arg instanceof List) {
            Iterator iter = ((List)arg).iterator();
            while (iter.hasNext()) {
                String id = StringFunction.evaluate(iter.next(), nav);
                Object node = nav.getElementById(contextNode, id);
                if (node != null) {
                    nodes.add(node);
                }
            }
        } else {
            String ids = StringFunction.evaluate(arg, nav);
            StringTokenizer tok = new StringTokenizer(ids);
            while (tok.hasMoreTokens()) {
                String id = tok.nextToken();
                Object node = nav.getElementById(contextNode, id);
                if (node != null) {
                    nodes.add(node);
                }
            }
        }
        return nodes;
    }
}

