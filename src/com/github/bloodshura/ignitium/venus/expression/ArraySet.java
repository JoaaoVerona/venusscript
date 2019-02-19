package com.github.bloodshura.ignitium.venus.expression;

import com.github.bloodshura.ignitium.venus.exception.runtime.InvalidArrayAccessException;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.type.PrimitiveType;
import com.github.bloodshura.ignitium.venus.value.ArrayValue;
import com.github.bloodshura.ignitium.venus.value.IntegerValue;
import com.github.bloodshura.ignitium.venus.value.Value;

public class ArraySet implements Expression {
	private final String name;
	private final Expression index;
	private final Expression expression;

	public ArraySet(String name, Expression index, Expression expression) {
		this.index = index;
		this.name = name;
		this.expression = expression;
	}

	public Expression getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}

	public Expression getExpression() {
		return expression;
	}

	@Override
	public Value resolve(Context context) throws ScriptRuntimeException {
		Value value = context.getVarValue(getName());

		if (value instanceof ArrayValue) {
			ArrayValue array = (ArrayValue) value;
			Value index = getIndex().resolve(context);

			if (index instanceof IntegerValue) {
				IntegerValue intIndex = (IntegerValue) index;
				Value result = getExpression().resolve(context);

				array.set(context, intIndex.value().intValue(), result);

				return result;
			}

			throw new InvalidArrayAccessException(context, "Index \"" + index + "\" is of type " + index.getType() + "; expected to be an " + PrimitiveType.INTEGER);
		}

		throw new InvalidArrayAccessException(context, "Variable \"" + getName() + "\" is of type " + value.getType() + "; expected to be an " + PrimitiveType.ARRAY);
	}

	@Override
	public String toString() {
		return "arrayAttribution(" + getName() + '[' + getIndex() + "]=" + getExpression() + ')';
	}
}