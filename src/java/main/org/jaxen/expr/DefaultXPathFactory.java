// Copyright 2001 bob mcwhirter and James Strachan. All rights reserved.

package org.jaxen.expr;

import org.jaxen.JaxenException;

import org.jaxen.expr.iter.*;

import org.saxpath.Axis;

public class DefaultXPathFactory implements XPathFactory
{
    public XPath createXPath(Expr rootExpr) throws JaxenException
    {
        return new DefaultXPath( rootExpr );
    }

    public PathExpr createPathExpr(FilterExpr filterExpr,
                                   LocationPath locationPath) throws JaxenException
    {
        return new DefaultPathExpr( filterExpr,
                                    locationPath );
    }

    public LocationPath createRelativeLocationPath() throws JaxenException
    {
        return new DefaultRelativeLocationPath();
    }

    public LocationPath createAbsoluteLocationPath() throws JaxenException
    {
        return new DefaultAbsoluteLocationPath();
    }

    public BinaryExpr createOrExpr(Expr lhs,
                                   Expr rhs) throws JaxenException
    {
        return new DefaultOrExpr( lhs,
                                  rhs );
    }

    public BinaryExpr createAndExpr(Expr lhs,
                                    Expr rhs) throws JaxenException
    {
        return new DefaultAndExpr( lhs,
                                   rhs );
    }

    public BinaryExpr createEqualityExpr(Expr lhs,
                                         Expr rhs,
                                         int equalityOperator) throws JaxenException
    {
        switch ( equalityOperator )
        {
            case EQUALS:
            {
                return new DefaultEqualsExpr( lhs,
                                              rhs );
            }
            case NOT_EQUALS:
            {
                return new DefaultNotEqualsExpr( lhs,
                                                 rhs );
            }
        }

        throw new JaxenException( "Unhandled operator in createEqualityExpr(): " + equalityOperator );
    }

    public BinaryExpr createRelationalExpr(Expr lhs,
                                           Expr rhs,
                                           int relationalOperator) throws JaxenException
    {
        switch ( relationalOperator )
        {
            case LESS_THAN:
            {
                return new DefaultLessThanExpr( lhs,
                                                rhs );
            }
            case GREATER_THAN:
            {
                return new DefaultGreaterThanExpr( lhs,
                                                   rhs );
            }
            case LESS_THAN_EQUALS:
            {
                return new DefaultLessThanEqualExpr( lhs,
                                                     rhs );
            }
            case GREATER_THAN_EQUALS:
            {
                return new DefaultGreaterThanEqualExpr( lhs,
                                                        rhs );
            }
        }

        throw new JaxenException( "Unhandled operator in createRelationalExpr(): " + relationalOperator );
    }

    public BinaryExpr createAdditiveExpr(Expr lhs,
                                         Expr rhs,
                                         int additiveOperator) throws JaxenException
    {
        switch ( additiveOperator )
        {
            case ADD:
            {
                return new DefaultPlusExpr( lhs,
                                            rhs );
            }
            case SUBTRACT:
            {
                return new DefaultMinusExpr( lhs,
                                             rhs );
            }
        }

        throw new JaxenException( "Unhandled operator in createAdditiveExpr(): " + additiveOperator );
    }

    public BinaryExpr createMultiplicativeExpr(Expr lhs,
                                               Expr rhs,
                                               int multiplicativeOperator) throws JaxenException
    {
        switch ( multiplicativeOperator )
        {
            case MULTIPLY:
            {
                return new DefaultMultiplyExpr( lhs,
                                                rhs );
            }
            case DIV:
            {
                return new DefaultDivExpr( lhs,
                                           rhs );
            }
            case MOD:
            {
                return new DefaultModExpr( lhs,
                                           rhs );
            }
        }

        throw new JaxenException( "Unhandled operator in createMultiplicativeExpr(): " + multiplicativeOperator );
    }

    public Expr createUnaryExpr(Expr expr,
                                int unaryOperator) throws JaxenException
    {
        switch ( unaryOperator )
        {
            case NEGATIVE:
            {
                return new DefaultUnaryExpr( expr );
            }
        }

        return expr;
    }

    public UnionExpr createUnionExpr(Expr lhs,
                                     Expr rhs) throws JaxenException
    {
        return new DefaultUnionExpr( lhs,
                                     rhs );
    }

    public FilterExpr createFilterExpr(Expr expr) throws JaxenException
    {
        return new DefaultFilterExpr( expr );
    }

    public FunctionCallExpr createFunctionCallExpr(String prefix,
                                                   String functionName) throws JaxenException
    {
        return new DefaultFunctionCallExpr( prefix,
                                            functionName );
    }

    public NumberExpr createNumberExpr(int number) throws JaxenException
    {
        // return new DefaultNumberExpr( new Integer(number) );
      return new DefaultNumberExpr( new Double(number) );
    }

    public NumberExpr createNumberExpr(double number) throws JaxenException
    {
        return new DefaultNumberExpr( new Double(number) );
    }

    public LiteralExpr createLiteralExpr(String literal) throws JaxenException
    {
        return new DefaultLiteralExpr( literal );
    }

    public VariableReferenceExpr createVariableReferenceExpr(String prefix,
                                                             String variable) throws JaxenException
    {
        return new DefaultVariableReferenceExpr( prefix,
                                                 variable );
    }

    public Step createNameStep(int axis,
                               String prefix,
                               String localName) throws JaxenException
    {
        IterableAxis iter = getIterableAxis( axis );

        return new DefaultNameStep( iter,
                                    prefix,
                                    localName );
    }

    public Step createTextNodeStep(int axis) throws JaxenException
    {
        IterableAxis iter = getIterableAxis( axis );

        return new DefaultTextNodeStep( iter );
    }

    public Step createCommentNodeStep(int axis) throws JaxenException
    {
        IterableAxis iter = getIterableAxis( axis );

        return new DefaultCommentNodeStep( iter );
    }

    public Step createAllNodeStep(int axis) throws JaxenException
    {
        IterableAxis iter = getIterableAxis( axis );

        return new DefaultAllNodeStep( iter );
    }

    public Step createProcessingInstructionNodeStep(int axis,
                                                    String piName) throws JaxenException
    {
        IterableAxis iter = getIterableAxis( axis );

        return new DefaultProcessingInstructionNodeStep( iter,
                                                         piName );
    }

    public Predicate createPredicate(Expr predicateExpr) throws JaxenException
    {
        return new DefaultPredicate( predicateExpr );
    }

    protected IterableAxis getIterableAxis(int axis)
    {
        IterableAxis iter = null;

        switch ( axis )
        {
            case Axis.CHILD:
            {
                iter = new IterableChildAxis( axis );
                break;
            }
            case Axis.DESCENDANT:
            {
                iter = new IterableDescendantAxis( axis );
                break;
            }
            case Axis.PARENT:
            {
                iter = new IterableParentAxis( axis );
                break;
            }
            case Axis.FOLLOWING_SIBLING:
            {
                iter = new IterableFollowingSiblingAxis( axis );
                break;
            }
            case Axis.PRECEDING_SIBLING:
            {
                iter = new IterablePrecedingSiblingAxis( axis );
                break;
            }
            case Axis.FOLLOWING:
            {
                iter = new IterableFollowingAxis( axis );
                break;
            }
            case Axis.PRECEDING:
            {
                iter = new IterablePrecedingAxis( axis );
                break;
            }
            case Axis.ATTRIBUTE:
            {
                iter = new IterableAttributeAxis( axis );
                break;
            }
            case Axis.NAMESPACE:
            {
                iter = new IterableNamespaceAxis( axis );
                break;
            }
            case Axis.SELF:
            {
                iter = new IterableSelfAxis( axis );
                break;
            }
            case Axis.DESCENDANT_OR_SELF:
            {
                iter = new IterableDescendantOrSelfAxis( axis );
                break;
            }
            case Axis.ANCESTOR_OR_SELF:
            {
                iter = new IterableAncestorOrSelfAxis( axis );
                break;
            }
            case Axis.ANCESTOR:
            {
                iter = new IterableAncestorAxis( axis );
                break;
            }
        }

        return iter;
    }
                                               
}
