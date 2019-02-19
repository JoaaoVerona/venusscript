package com.github.bloodshura.ignitium.venus.expression;

import com.github.bloodshura.ignitium.util.XApi;
import com.github.bloodshura.ignitium.venus.exception.runtime.IncompatibleTypesException;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.operator.UnaryOperator;
import com.github.bloodshura.ignitium.venus.value.Value;

public class UnaryOperation implements Expression {
	private final UnaryOperator operator;
	private final Expression expression;

	public UnaryOperation(UnaryOperator operator, Expression expression) {
		XApi.requireNonNull(expression, "expression");
		XApi.requireNonNull(operator, "operator");

		this.operator = operator;
		this.expression = expression;
	}

	public UnaryOperator getOperator() {
		return operator;
	}

	public Expression getExpression() {
		return expression;
	}

	@Override
	public Value resolve(Context context) throws ScriptRuntimeException {
		Value value = getExpression().resolve(context);
		Value result = getOperator().operate(context, value);

		if (result == null) {
			throw new IncompatibleTypesException(context, "Operator " + getOperator() + " cannot be applied with " + value.getType());
		}

		return result;
	}

	@Override
	public String toString() {
		return "unioperation(" + getOperator() + " [" + getExpression() + "])";
	}
}