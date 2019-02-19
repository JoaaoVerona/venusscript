package com.github.bloodshura.ignitium.venus.expression;

import com.github.bloodshura.ignitium.collection.view.XArrayView;
import com.github.bloodshura.ignitium.collection.view.XView;
import com.github.bloodshura.ignitium.util.XApi;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.value.ArrayValue;
import com.github.bloodshura.ignitium.venus.value.Value;

public class ArrayLiteral implements Expression {
	private final Expression[] expressions;

	public ArrayLiteral(Expression... expressions) {
		XApi.requireNonNull(expressions, "expressions");

		this.expressions = expressions;
	}

	public XView<Expression> getExpressions() {
		return new XArrayView<>(expressions);
	}

	@Override
	public Value resolve(Context context) throws ScriptRuntimeException {
		ArrayValue value = new ArrayValue(getExpressions().size());
		int i = 0;

		for (Expression expression : getExpressions()) {
			value.set(context, i++, expression.resolve(context));
		}

		return value;
	}

	@Override
	public String toString() {
		return "arrdef(" + getExpressions() + ')';
	}
}