package com.github.bloodshura.ignitium.venus.expression;

import com.github.bloodshura.ignitium.util.XApi;
import com.github.bloodshura.ignitium.venus.exception.runtime.IncompatibleTypesException;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.operator.BinaryOperator;
import com.github.bloodshura.ignitium.venus.value.Value;

public class BinaryOperation implements Expression {
	private final Expression left;
	private final BinaryOperator operator;
	private final Expression right;

	public BinaryOperation(BinaryOperator operator, Expression left, Expression right) {
		XApi.requireNonNull(left, "left");
		XApi.requireNonNull(operator, "operator");
		XApi.requireNonNull(right, "right");

		this.left = left;
		this.operator = operator;
		this.right = right;
	}

	public Expression getLeft() {
		return left;
	}

	public BinaryOperator getOperator() {
		return operator;
	}

	public Expression getRight() {
		return right;
	}

	@Override
	public Value resolve(Context context) throws ScriptRuntimeException {
		Value left = getLeft().resolve(context);
		Value right = getRight().resolve(context);
		Value result = getOperator().operate(context, left, right);

		if (result == null) {
			throw new IncompatibleTypesException(context, "Operator " + getOperator() + " cannot be applied with " + left.getType() + " and " + right.getType());
		}

		return result;
	}

	@Override
	public String toString() {
		return "bioperation([" + getLeft() + "] " + getOperator() + " [" + getRight() + "])";
	}
}