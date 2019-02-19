package com.github.bloodshura.ignitium.venus.expression;

import com.github.bloodshura.ignitium.venus.exception.runtime.InvalidValueTypeException;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.value.ObjectValue;
import com.github.bloodshura.ignitium.venus.value.Value;

public class InContext implements Expression {
	private final Expression expression;
	private final String name;

	public InContext(String name, Expression expression) {
		this.name = name;
		this.expression = expression;
	}

	public Expression getExpression() {
		return expression;
	}

	public String getName() {
		return name;
	}

	@Override
	public Value resolve(Context context) throws ScriptRuntimeException {
		Value value = context.getVarValue(getName());

		if (value instanceof ObjectValue) {
			ObjectValue object = (ObjectValue) value;

			return getExpression().resolve(object.getContext()); // See issue #24
		}
		else {
			throw new InvalidValueTypeException(context, getName() + " has type " + value.getType() + "; expected to be an object");
		}
	}

	@Override
	public String toString() {
		return "incontext(" + getName() + " << " + getExpression() + ')';
	}
}