// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen;

import org.jaxen.expr.XPathFactory;
import org.jaxen.expr.DefaultXPathFactory;
import org.jaxen.expr.XPath;
import org.jaxen.expr.LocationPath;
import org.jaxen.expr.FilterExpr;
import org.jaxen.expr.Expr;
import org.jaxen.expr.Step;
import org.jaxen.expr.Predicate;
import org.jaxen.expr.Predicated;
import org.jaxen.expr.FunctionCallExpr;

import org.saxpath.XPathHandler;
import org.saxpath.Axis;
import org.saxpath.Operator;

import java.util.LinkedList;
import java.util.Iterator;

/** SAXPath <code>XPathHandler</code> implementation capable
 *  of building Jaxen expression trees which can walk various
 *  different object models.
 *
 *  @author bob mcwhirter (bob@werken.com)
 */
public class JaxenHandler implements XPathHandler
{
    private XPathFactory xpathFactory;
    private XPath        xpath;
    protected boolean    simplified;

    protected LinkedList stack;

    /** Construct.
     */
    public JaxenHandler()
    {
        this.stack        = new LinkedList();
        this.xpathFactory = new DefaultXPathFactory();
    }
    
    /** Set the Jaxen <code>XPathFactory</code> to use
     *  during the parse to construct the XPath expression tree.
     *
     *  @param xpathFactory The factory to use during the parse.
     */
    public void setXPathFactory(XPathFactory xpathFactory)
    {
        this.xpathFactory = xpathFactory;
    }

    /** Retrieve the Jaxen <code>XPathFactory</code> used
     *  during the parse to construct the XPath expression tree.
     *
     *  @return The <code>XPathFactory</code> used during the parse.
     */
    public XPathFactory getXPathFactory()
    {
        return this.xpathFactory;
    }

    /** Retrieve the simplified Jaxen XPath expression tree.
     *
     *  <p>
     *  This method is only valid once <code>XPathReader.parse(...)</code>
     *  successfully returned.
     *  </p>
     *
     *  @return The XPath expression tree.
     */
    public XPath getXPath()
    {
        return getXPath( true );
    }

    /** Retrieve the Jaxen XPath expression tree, optionally
     *  simplified.
     *
     *  <p>
     *  This method is only valid once <code>XPathReader.parse(...)</code>
     *  successfully returned.
     *  </p>
     *
     *  @return The XPath expression tree.
     */
    public XPath getXPath(boolean shouldSimplify)
    {
        if ( shouldSimplify && ! this.simplified )
        {
            //System.err.println("simplifyin....");
            this.xpath.simplify();
            this.simplified = true;
        }

        return this.xpath;
    }

    public void startXPath() throws JaxenException
    {
        //System.err.println("startXPath()");
        this.simplified = false;
        pushFrame();
    }
    
    public void endXPath() throws JaxenException
    {
        //System.err.println("endXPath()");
        this.xpath = getXPathFactory().createXPath( (Expr) pop() );

        popFrame();
    }

    public void startPathExpr() throws JaxenException
    {
        //System.err.println("startPathExpr()");
        pushFrame();
    }

    public void endPathExpr() throws JaxenException
    {
        //System.err.println("endPathExpr()");

        // PathExpr ::=   LocationPath
        //              | FilterExpr
        //              | FilterExpr / RelativeLocationPath
        //              | FilterExpr // RelativeLocationPath
        //
        // If the current stack-frame has two items, it's a
        // FilterExpr and a LocationPath (of some flavor).
        //
        // If the current stack-frame has one item, it's simply
        // a FilterExpr, and more than like boils down to a
        // primary expr of some flavor.  But that's for another
        // method...

        FilterExpr   filterExpr;
        LocationPath locationPath;

        Object       popped;

        //System.err.println("stackSize() == " + stackSize() );

        if ( stackSize() == 2 )
        {
            locationPath = (LocationPath) pop();
            filterExpr   = (FilterExpr) pop();
        }
        else
        {
            popped = pop();

            if ( popped instanceof LocationPath )
            {
                locationPath = (LocationPath) popped;
                filterExpr   = null;
            }
            else
            {
                locationPath = null;
                filterExpr   = (FilterExpr) popped;
            }
        }
        popFrame();

        push( getXPathFactory().createPathExpr( filterExpr,
                                               locationPath ) );
    }

    public void startAbsoluteLocationPath() throws JaxenException
    {
        //System.err.println("startAbsoluteLocationPath()");
        pushFrame();

        push( getXPathFactory().createAbsoluteLocationPath() );
    }

    public void endAbsoluteLocationPath() throws JaxenException
    {
        //System.err.println("endAbsoluteLocationPath()");
        endLocationPath();
    }

    public void startRelativeLocationPath() throws JaxenException
    {
        //System.err.println("startRelativeLocationPath()");
        pushFrame();

        push( getXPathFactory().createRelativeLocationPath() );
    }

    public void endRelativeLocationPath() throws JaxenException
    {
        //System.err.println("endRelativeLocationPath()");
        endLocationPath();
    }

    protected void endLocationPath() throws JaxenException
    {
        LocationPath path = (LocationPath) peekFrame().removeFirst();

        addSteps( path,
                  popFrame().iterator() );

        push( path );
    }

    protected void addSteps(LocationPath locationPath,
                          Iterator stepIter)
    {
        while ( stepIter.hasNext() )
        {
            locationPath.addStep( (Step) stepIter.next() );
        }
    }

    public void startNameStep(int axis,
                              String prefix,
                              String localName) throws JaxenException
    {
        //System.err.println("startNameStep(" + axis + ", " + prefix + ", " + localName + ")");
        pushFrame();

        push( getXPathFactory().createNameStep( axis,
                                               prefix,
                                               localName ) );
    }

    public void endNameStep() throws JaxenException
    {
        //System.err.println("endNameStep()");
        endStep();
    }
    
    public void startTextNodeStep(int axis) throws JaxenException
    {
        //System.err.println("startTextNodeStep()");
        pushFrame();
        
        push( getXPathFactory().createTextNodeStep( axis ) );
    }
    
    public void endTextNodeStep() throws JaxenException
    {
        //System.err.println("endTextNodeStep()");
        endStep();
    }

    public void startCommentNodeStep(int axis) throws JaxenException
    {
        //System.err.println("startCommentNodeStep()");
        pushFrame();

        push( getXPathFactory().createCommentNodeStep( axis ) );
    }

    public void endCommentNodeStep() throws JaxenException
    {
        //System.err.println("endCommentNodeStep()");
        endStep();
    }
        
    public void startAllNodeStep(int axis) throws JaxenException
    {
        //System.err.println("startAllNodeStep()");
        pushFrame();

        push( getXPathFactory().createAllNodeStep( axis ) );
    }

    public void endAllNodeStep() throws JaxenException
    {
        //System.err.println("endAllNodeStep()");
        endStep();
    }

    public void startProcessingInstructionNodeStep(int axis,
                                                   String name) throws JaxenException
    {
        //System.err.println("startProcessingInstructionStep()");
        pushFrame();

        push( getXPathFactory().createProcessingInstructionNodeStep( axis,
                                                                    name ) );
    }
    
    public void endProcessingInstructionNodeStep() throws JaxenException
    {
        //System.err.println("endProcessingInstructionStep()");
        endStep();
    }

    protected void endStep()
    {
        Step step = (Step) peekFrame().removeFirst();

        addPredicates( step,
                       popFrame().iterator() );

        push( step );
    }
    
    public void startPredicate() throws JaxenException
    {
        //System.err.println("startPredicate()");
        pushFrame();
    }
    
    public void endPredicate() throws JaxenException
    {
        //System.err.println("endPredicate()");
        Predicate predicate = getXPathFactory().createPredicate( (Expr) pop() );

        popFrame();

        push( predicate );
    }

    public void startFilterExpr() throws JaxenException
    {
        //System.err.println("startFilterExpr()");
        pushFrame();
    }

    public void endFilterExpr() throws JaxenException
    {
        //System.err.println("endFilterExpr()");
        Expr expr = (Expr) peekFrame().removeFirst();
        
        FilterExpr filter = getXPathFactory().createFilterExpr( expr );

        Iterator predIter = popFrame().iterator();

        addPredicates( filter,
                       predIter );

        push( filter );
    }

    protected void addPredicates(Predicated obj,
                               Iterator predIter)
    {
        while ( predIter.hasNext() )
        {
            obj.addPredicate( (Predicate) predIter.next() );
        }
    }

    protected void returnExpr()
    {
        Expr expr = (Expr) pop();
        popFrame();
        push( expr );
    }

    public void startOrExpr() throws JaxenException
    {
        //System.err.println("startOrExpr()");
    }

    public void endOrExpr(boolean create) throws JaxenException
    {
        //System.err.println("endOrExpr()");

        if ( create )
        {
            //System.err.println("makeOrExpr");
            Expr rhs = (Expr) pop();
            Expr lhs = (Expr) pop();

            push( getXPathFactory().createOrExpr( lhs,
                                                 rhs ) );
        }
    }

    public void startAndExpr() throws JaxenException
    {
        //System.err.println("startAndExpr()");
    }

    public void endAndExpr(boolean create) throws JaxenException
    {
        //System.err.println("endAndExpr()");

        if ( create )
        {
            //System.err.println("makeAndExpr");

            Expr rhs = (Expr) pop();
            Expr lhs = (Expr) pop();

            push( getXPathFactory().createAndExpr( lhs,
                                                  rhs ) );
        }
    }

    public void startEqualityExpr() throws JaxenException
    {
        //System.err.println("startEqualityExpr()");
    }

    public void endEqualityExpr(int operator) throws JaxenException
    {
        //System.err.println("endEqualityExpr(" + operator + ")");

        if ( operator != Operator.NO_OP )
        {
            //System.err.println("makeEqualityExpr");
            
            Expr rhs = (Expr) pop();
            Expr lhs = (Expr) pop();
            
            push( getXPathFactory().createEqualityExpr( lhs,
                                                        rhs,
                                                        operator ) );
        }
    }

    public void startRelationalExpr() throws JaxenException
    {
        //System.err.println("startRelationalExpr()");
    }

    public void endRelationalExpr(int operator) throws JaxenException
    {
        //System.err.println("endRelationalExpr(" + operator + ")");

        if ( operator != Operator.NO_OP )
        {
            //System.err.println("makeRelationalExpr");

            Expr rhs = (Expr) pop();
            Expr lhs = (Expr) pop();

            push( getXPathFactory().createRelationalExpr( lhs,
                                                         rhs,
                                                         operator ) );
        }
    }

    public void startAdditiveExpr() throws JaxenException
    {
        //System.err.println("startAdditiveExpr()");
    }

    public void endAdditiveExpr(int operator) throws JaxenException
    {
        //System.err.println("endAdditiveExpr(" + operator + ")");

        if ( operator != Operator.NO_OP )
        {
            //System.err.println("makeAdditiveExpr");
            
            Expr rhs = (Expr) pop();
            Expr lhs = (Expr) pop();
            
            push( getXPathFactory().createAdditiveExpr( lhs,
                                                        rhs,
                                                        operator ) );
        }
    }

    public void startMultiplicativeExpr() throws JaxenException
    {
        //System.err.println("startMultiplicativeExpr()");
    }

    public void endMultiplicativeExpr(int operator) throws JaxenException
    {
        //System.err.println("endMultiplicativeExpr(" + operator + ")");

        if ( operator != Operator.NO_OP )
        {
            //System.err.println("makeMulitiplicativeExpr");

            Expr rhs = (Expr) pop();
            Expr lhs = (Expr) pop();
            
            push( getXPathFactory().createMultiplicativeExpr( lhs,
                                                             rhs,
                                                             operator ) );
        }
    }

    public void startUnaryExpr() throws JaxenException
    {
        //System.err.println("startUnaryExpr()");
    }

    public void endUnaryExpr(int operator) throws JaxenException
    {
        //System.err.println("endUnaryExpr(" + operator + ")");

        if ( operator != Operator.NO_OP )
        {
            push( getXPathFactory().createUnaryExpr( (Expr) pop(),
                                                    operator ) );
        }
    }

    public void startUnionExpr() throws JaxenException
    {
        //System.err.println("startUnionExpr()");
    }

    public void endUnionExpr(boolean create) throws JaxenException
    {
        //System.err.println("endUnionExpr()");

        if ( create )
        {
            //System.err.println("makeUnionExpr");

            Expr rhs = (Expr) pop();
            Expr lhs = (Expr) pop();

            push( getXPathFactory().createUnionExpr( lhs,
                                                    rhs ) );
        }
    }

    public void number(int number) throws JaxenException
    {
        //System.err.println("number(" + number + ")");
        push( getXPathFactory().createNumberExpr( number ) );
    }

    public void number(double number) throws JaxenException
    {
        //System.err.println("number(" + number + ")");
        push( getXPathFactory().createNumberExpr( number ) );
    }

    public void literal(String literal) throws JaxenException
    {
        push( getXPathFactory().createLiteralExpr( literal ) );
    }

    public void variableReference(String prefix,
                                  String variableName) throws JaxenException
    {
        push( getXPathFactory().createVariableReferenceExpr( prefix,
                                                             variableName ) );
    }

    public void startFunction(String prefix,
                              String functionName) throws JaxenException
    {
        pushFrame();
        push( getXPathFactory().createFunctionCallExpr( prefix,
                                                        functionName ) );
    }

    public void endFunction() throws JaxenException
    {
        FunctionCallExpr function = (FunctionCallExpr) peekFrame().removeFirst();

        addParameters( function,
                       popFrame().iterator() );

        push( function );
    }

    protected void addParameters(FunctionCallExpr function,
                               Iterator paramIter)
    {
        while ( paramIter.hasNext() )
        {
            function.addParameter( (Expr) paramIter.next() );
        }
    }

    protected int stackSize()
    {
        return peekFrame().size();
    }

    protected void push(Object obj)
    {
        peekFrame().addLast( obj );

        //System.err.println("push(" + this.stack.size() + "/" + peekFrame().size() + ") == " + obj );
    }

    protected Object pop()
    {
        //System.err.println("pop(" + this.stack.size() + "/" + peekFrame().size() + ")");
        return peekFrame().removeLast();
    }

    protected boolean canPop()
    {
        return ( peekFrame().size() > 0 );
    }

    protected void pushFrame()
    {
        this.stack.addLast( new LinkedList() );
        //System.err.println("pushFrame(" + this.stack.size() + ")");
    }

    protected LinkedList popFrame()
    {
        //System.err.println("popFrame(" + this.stack.size() + ")");
        return (LinkedList) this.stack.removeLast();
    }

    protected LinkedList peekFrame()
    {
        return (LinkedList) this.stack.getLast();
    }
}
