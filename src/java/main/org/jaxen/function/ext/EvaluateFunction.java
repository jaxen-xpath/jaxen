package org.jaxen.function.ext;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jaxen.BaseXPath;
import org.jaxen.Context;
import org.jaxen.ContextSupport;
import org.jaxen.Function;
import org.jaxen.Navigator;
import org.jaxen.FunctionCallException;
import org.jaxen.function.StringFunction;

import org.saxpath.SAXPathException;

/**
 * <code><i>node-set</i> evaluate(<i>string</i>)</code> 
 *  
 * @author Erwin Bolwidt (ejb @ klomp.org)
 */
public class EvaluateFunction implements Function
{
    public Object call( Context context, List args )
        throws FunctionCallException
    {
        if ( args.size() == 1 ) {
            return evaluate( context, args.get(0));
        }

        throw new FunctionCallException( "id() requires one argument" );
    }

    public static List evaluate (Context context, Object arg)
        throws FunctionCallException
    {
        List contextNodes = context.getNodeSet();
        
        if (contextNodes.size() == 0)
            return Collections.EMPTY_LIST;
      
        Object contextNode = contextNodes.get(0);
        Navigator nav = context.getNavigator();

        String xpathString;
        if ( arg instanceof String )
            xpathString = (String)arg;
        else
            xpathString = StringFunction.evaluate(arg, nav);

        try {
            BaseXPath xpath = nav.parseXPath(xpathString);
            ContextSupport support = context.getContextSupport();
            xpath.setVariableContext( support.getVariableContext() );
            xpath.setFunctionContext( support.getFunctionContext() );
            xpath.setNamespaceContext( support.getNamespaceContext() );
            return xpath.selectNodes( context.duplicate() );
        }
        catch ( SAXPathException e ) {
            throw new FunctionCallException(e.toString());
        }
    }
}

