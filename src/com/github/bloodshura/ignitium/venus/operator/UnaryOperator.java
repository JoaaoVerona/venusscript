package com.github.bloodshura.ignitium.venus.operator;

import com.github.bloodshura.ignitium.collection.view.XArrayView;
import com.github.bloodshura.ignitium.collection.view.XView;
import com.github.bloodshura.ignitium.util.XApi;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.value.Value;

import java.util.function.Function;

public class UnaryOperator implements Operator {
	private final Function<Value, Value> function;
	private final XView<String> identifiers;
	private final String name;

	public UnaryOperator(String name, Function<Value, Value> function, String... identifiers) {
		XApi.requireNonNull(function, "function");
		XApi.requireNonNull(identifiers, "identifiers");
		XApi.requireNonNull(name, "name");

		this.function = function;
		this.identifiers = new XArrayView<>(identifiers);
		this.name = name;
	}

	public Function<Value, Value> getFunction() {
		return function;
	}

	@Override
	public XView<String> getIdentifiers() {
		return identifiers;
	}

	public final Value operate(Context context, Value value) {
		return getFunction().apply(value);
	}

	@Override
	public String toString() {
		return name;
	}
}