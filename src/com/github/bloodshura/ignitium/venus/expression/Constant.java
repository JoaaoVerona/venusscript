package com.github.bloodshura.ignitium.venus.expression;

import com.github.bloodshura.ignitium.util.XApi;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.value.Value;

public class Constant implements Expression {
	private final Value value;

	public Constant(Value value) {
		XApi.requireNonNull(value, "value");

		this.value = value;
	}

	public Value getValue() {
		return value;
	}

	@Override
	public Value resolve(Context context) {
		return value;
	}

	@Override
	public String toString() {
		return "const(" + getValue() + ')';
	}
}