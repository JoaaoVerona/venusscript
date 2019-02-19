package com.github.bloodshura.ignitium.venus.operator;

import com.github.bloodshura.ignitium.collection.view.XArrayView;
import com.github.bloodshura.ignitium.collection.view.XView;
import com.github.bloodshura.ignitium.util.XApi;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.value.Value;

import java.util.function.BiFunction;

public class BinaryOperator implements Operator {
	private final BiFunction<Value, Value, Value> function;
	private final XView<String> identifiers;
	private final String name;

	public BinaryOperator(String name, BiFunction<Value, Value, Value> function, String... identifiers) {
		XApi.requireNonNull(function, "function");
		XApi.requireNonNull(identifiers, "identifiers");
		XApi.requireNonNull(name, "name");

		this.function = function;
		this.identifiers = new XArrayView<>(identifiers);
		this.name = name;
	}

	public BiFunction<Value, Value, Value> getFunction() {
		return function;
	}

	@Override
	public XView<String> getIdentifiers() {
		return identifiers;
	}

	public final Value operate(Context context, Value left, Value right) {
		return getFunction().apply(left, right);
	}

	@Override
	public String toString() {
		return name;
	}
}