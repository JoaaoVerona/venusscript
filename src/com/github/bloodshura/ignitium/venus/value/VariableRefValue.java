package com.github.bloodshura.ignitium.venus.value;

import com.github.bloodshura.ignitium.util.XApi;
import com.github.bloodshura.ignitium.venus.compiler.KeywordDefinitions;
import com.github.bloodshura.ignitium.venus.type.PrimitiveType;

public class VariableRefValue extends Value {
	private final String value;

	public VariableRefValue(String value) {
		super(PrimitiveType.VARIABLE_REFERENCE);
		XApi.requireNonNull(value, "value");

		this.value = value;
	}

	@Override
	public VariableRefValue clone() {
		return new VariableRefValue(value());
	}

	@Override
	public String toString() {
		return KeywordDefinitions.COLON + value();
	}

	@Override
	public String value() {
		return value;
	}
}