/*
 * Copyright 2000-2002 bob mcwhirter & James Strachan.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 * 
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 * 
 *   * Neither the name of the Jaxen Project nor the names of its
 *     contributors may be used to endorse or promote products derived 
 *     from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * ====================================================================
 * This software consists of voluntary contributions made by many 
 * individuals on behalf of the Jaxen Project and was originally 
 * created by bob mcwhirter <bob@werken.com> and 
 * James Strachan <jstrachan@apache.org>.  For more information on the 
 * Jaxen Project, please see <https://github.com/jaxen-xpath/jaxen/>.
 */



package org.jaxen.expr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import org.jaxen.Context;
import org.jaxen.JaxenException;

abstract class DefaultBinaryExpr extends DefaultExpr implements BinaryExpr
{
    private Expr lhs;
    private Expr rhs;

    DefaultBinaryExpr(Expr lhs, Expr rhs)
    {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public Expr getLHS()
    {
        return this.lhs;
    }

    public Expr getRHS()
    {
        return this.rhs;
    }

    public void setLHS(Expr lhs)
    {
        this.lhs = lhs;
    }

    public void setRHS(Expr rhs)
    {
        this.rhs = rhs;
    }

    public abstract String getOperator();

    public String getText()
    {
        return buildText(false);
    }

    public String toString()
    {
        return buildText(true);
    }

    private String buildText(boolean useToString)
    {
        Stack<Expr> stack1 = new Stack<Expr>();
        Stack<Expr> stack2 = new Stack<Expr>();

        stack1.push(this);
        while (!stack1.isEmpty())
        {
            Expr node = stack1.pop();
            stack2.push(node);
            if (node instanceof DefaultBinaryExpr)
            {
                DefaultBinaryExpr bin = (DefaultBinaryExpr) node;
                stack1.push(bin.getLHS());
                stack1.push(bin.getRHS());
            }
        }

        Stack<String> resultStack = new Stack<String>();
        while (!stack2.isEmpty())
        {
            Expr node = stack2.pop();
            if (node instanceof DefaultBinaryExpr)
            {
                DefaultBinaryExpr bin = (DefaultBinaryExpr) node;
                String rhs = resultStack.pop();
                String lhs = resultStack.pop();
                if (useToString)
                {
                    resultStack.push("[" + bin.getClass().getName() + ": " + lhs + ", " + rhs + "]");
                }
                else
                {
                    resultStack.push("(" + lhs + " " + bin.getOperator() + " " + rhs + ")");
                }
            }
            else
            {
                resultStack.push(useToString ? node.toString() : node.getText());
            }
        }

        return resultStack.pop();
    }

    /**
     * Evaluates this binary expression tree iteratively using an explicit
     * work stack, avoiding recursion across distinct operator types that could
     * otherwise overflow the call stack for deeply nested expressions.
     * 
     * <p>Each {@code DefaultBinaryExpr} node expands into its flattened operand
     * list via {@link #flattenChain()}, and the resulting values are combined by
     * {@link #evaluateChain(List, Context)}.  Non-binary sub-expressions are
     * evaluated with their own {@code evaluate} implementations.
     */
    public Object evaluate(Context context) throws JaxenException
    {
        Stack<Object> workStack = new Stack<Object>();
        Stack<Object> resultStack = new Stack<Object>();

        workStack.push(this);

        while (!workStack.isEmpty())
        {
            Object item = workStack.pop();

            if (item instanceof EvalFrame)
            {
                EvalFrame frame = (EvalFrame) item;
                Object[] vals = new Object[frame.count];
                for (int i = frame.count - 1; i >= 0; i--)
                {
                    vals[i] = resultStack.pop();
                }
                resultStack.push(frame.expr.evaluateChain(Arrays.asList(vals), context));
            }
            else if (item instanceof DefaultBinaryExpr)
            {
                DefaultBinaryExpr bin = (DefaultBinaryExpr) item;
                List<Expr> operands = bin.flattenChain();
                // Push the combine frame first so it fires after all operands are evaluated.
                workStack.push(new EvalFrame(bin, operands.size()));
                // Push operands in reverse index order so operands.get(0) is processed first.
                for (int i = operands.size() - 1; i >= 0; i--)
                {
                    workStack.push(operands.get(i));
                }
            }
            else
            {
                Expr expr = (Expr) item;
                resultStack.push(expr.evaluate(context));
            }
        }

        return resultStack.pop();
    }

    /**
     * Combines a list of pre-evaluated operand values according to this
     * operator's semantics.  The {@code values} list mirrors the list
     * returned by {@link #flattenChain()}: {@code values.get(values.size()-1)}
     * is the left-most (highest-precedence) operand.
     *
     * @param values  the already-evaluated operand values, in the same order
     *                as the list produced by {@code flattenChain()}
     * @param context the evaluation context
     * @return the result of applying this operator to the operand values
     */
    abstract Object evaluateChain(List<Object> values, Context context)
            throws JaxenException;

    /** Carry state on the work stack for combining evaluated operands. */
    private static final class EvalFrame
    {
        final DefaultBinaryExpr expr;
        final int count;

        EvalFrame(DefaultBinaryExpr expr, int count)
        {
            this.expr = expr;
            this.count = count;
        }
    }

    protected List<Expr> flattenChain()
    {
        List<Expr> operands = new ArrayList<Expr>();
        Expr current = this;

        while (current instanceof DefaultBinaryExpr
                && current.getClass() == this.getClass())
        {
            DefaultBinaryExpr binaryExpr = (DefaultBinaryExpr) current;
            operands.add(binaryExpr.getRHS());
            current = binaryExpr.getLHS();
        }
        operands.add(current);

        return operands;
    }

    public Expr simplify()
    {
        List<DefaultBinaryExpr> chain = new ArrayList<DefaultBinaryExpr>();
        Expr current = this;
        while (current instanceof DefaultBinaryExpr)
        {
            DefaultBinaryExpr binaryExpr = (DefaultBinaryExpr) current;
            chain.add(binaryExpr);
            current = binaryExpr.getLHS();
        }

        Expr simplified = current.simplify();
        for (int i = chain.size() - 1; i >= 0; i--)
        {
            DefaultBinaryExpr binaryExpr = chain.get(i);
            binaryExpr.setLHS(simplified);
            binaryExpr.setRHS(binaryExpr.getRHS().simplify());
            simplified = binaryExpr;
        }

        return simplified;
    }
}
