// Copyright 2001 werken digital. All rights reserved.

package org.jaxen.expr;

import org.jaxen.JaxenException;

import org.saxpath.Operator;

public interface XPathFactory extends Operator
{
    XPath createXPath(Expr rootExpr) throws JaxenException;

    PathExpr createPathExpr(FilterExpr filterExpr,
                            LocationPath locationPath) throws JaxenException;

    LocationPath createRelativeLocationPath() throws JaxenException;
    LocationPath createAbsoluteLocationPath() throws JaxenException;

    BinaryExpr createOrExpr(Expr lhs,
                            Expr rhs) throws JaxenException;

    BinaryExpr createAndExpr(Expr lhs,
                             Expr rhs) throws JaxenException;

    BinaryExpr createEqualityExpr(Expr lhs,
                                  Expr rhs,
                                  int equalityOperator) throws JaxenException;

    BinaryExpr createRelationalExpr(Expr lhs,
                                    Expr rhs,
                                    int relationalOperator) throws JaxenException;

    BinaryExpr createAdditiveExpr(Expr lhs,
                                  Expr rhs,
                                  int additiveOperator) throws JaxenException;

    BinaryExpr createMultiplicativeExpr(Expr lhs,
                                        Expr rhs,
                                        int multiplicativeOperator) throws JaxenException;

    Expr createUnaryExpr(Expr expr,
                         int unaryOperator) throws JaxenException;

    UnionExpr createUnionExpr(Expr lhs,
                              Expr rhs) throws JaxenException;

    FilterExpr createFilterExpr(Expr expr) throws JaxenException;

    FunctionCallExpr createFunctionCallExpr(String prefix,
                                            String functionName) throws JaxenException;

    NumberExpr createNumberExpr(int number) throws JaxenException;
    NumberExpr createNumberExpr(double number) throws JaxenException;
    LiteralExpr createLiteralExpr(String literal) throws JaxenException;
    VariableReferenceExpr createVariableReferenceExpr(String prefix,
                                                      String variableName) throws JaxenException;

    Step createNameStep(int axis,
                        String prefix,
                        String localName) throws JaxenException;

    Step createAllNodeStep(int axis) throws JaxenException;
    Step createCommentNodeStep(int axis) throws JaxenException;
    Step createTextNodeStep(int axis) throws JaxenException;
    Step createProcessingInstructionNodeStep(int axis,
                                             String name) throws JaxenException;

    Predicate createPredicate(Expr predicateExpr) throws JaxenException;
}
