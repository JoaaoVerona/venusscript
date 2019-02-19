package com.github.bloodshura.ignitium.venus.expression;

import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.value.Value;

public class Attribution implements Expression {
	private final String name;
	private final Expression expression;

	public Attribution(String name, Expression expression) {
		this.name = name;
		this.expression = expression;
	}

	public String getName() {
		return name;
	}

	public Expression getExpression() {
		return expression;
	}

	@Override
	public Value resolve(Context context) throws ScriptRuntimeException {
		Value value = getExpression().resolve(context);

		context.setVar(getName(), value);

		return value;
	}

	@Override
	public String toString() {
		return "attribution(" + getName() + "=" + getExpression() + ')';
	}
}