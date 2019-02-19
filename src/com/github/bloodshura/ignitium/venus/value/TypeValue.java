package com.github.bloodshura.ignitium.venus.value;

import com.github.bloodshura.ignitium.util.XApi;
import com.github.bloodshura.ignitium.venus.type.PrimitiveType;
import com.github.bloodshura.ignitium.venus.type.Type;

public class TypeValue extends Value {
	private final Type value;

	public TypeValue(Type value) {
		super(PrimitiveType.TYPE);
		XApi.requireNonNull(value, "value");

		this.value = value;
	}

	@Override
	public TypeValue clone() {
		return new TypeValue(value());
	}

	@Override
	public String toString() {
		return value().toString();
	}

	@Override
	public Type value() {
		return value;
	}
}