package com.github.bloodshura.ignitium.venus.expression;

import com.github.bloodshura.ignitium.util.XApi;
import com.github.bloodshura.ignitium.venus.exception.runtime.InvalidArrayAccessException;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.type.PrimitiveType;
import com.github.bloodshura.ignitium.venus.value.ArrayValue;
import com.github.bloodshura.ignitium.venus.value.IntegerValue;
import com.github.bloodshura.ignitium.venus.value.Value;

public class ArrayGet implements Expression {
	private final Expression index;
	private final String name;

	public ArrayGet(String name, Expression index) {
		XApi.requireNonNull(index, "index");
		XApi.requireNonNull(name, "name");

		this.index = index;
		this.name = name;
	}

	public Expression getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}

	@Override
	public Value resolve(Context context) throws ScriptRuntimeException {
		Value value = context.getVarValue(getName());

		if (value instanceof ArrayValue) {
			ArrayValue array = (ArrayValue) value;
			Value index = getIndex().resolve(context);

			if (index instanceof IntegerValue) {
				IntegerValue intIndex = (IntegerValue) index;

				return array.get(context, intIndex.value().intValue());
			}

			throw new InvalidArrayAccessException(context, "Index \"" + index + "\" is of type " + index.getType() + "; expected to be an " + PrimitiveType.INTEGER);
		}

		throw new InvalidArrayAccessException(context, "Variable \"" + getName() + "\" is of type " + value.getType() + "; expected to be an " + PrimitiveType.ARRAY);
	}

	@Override
	public String toString() {
		return "arr(" + getName() + '[' + getIndex() + "])";
	}
}
