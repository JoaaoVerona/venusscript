package com.github.bloodshura.ignitium.venus.value;

import com.github.bloodshura.ignitium.venus.compiler.KeywordDefinitions;
import com.github.bloodshura.ignitium.venus.type.PrimitiveType;

public class BoolValue extends Value {
	private final boolean value;

	public BoolValue(boolean value) {
		super(PrimitiveType.BOOLEAN);
		this.value = value;
	}

	@Override
	public Value and(Value value) {
		if (value instanceof BoolValue) {
			BoolValue bool = (BoolValue) value;

			return new BoolValue(value() && bool.value());
		}

		return super.and(value);
	}

	@Override
	public BoolValue clone() {
		return new BoolValue(value());
	}

	@Override
	public Value not() {
		return new BoolValue(!value());
	}

	@Override
	public Value or(Value value) {
		if (value instanceof BoolValue) {
			BoolValue bool = (BoolValue) value;

			return new BoolValue(value() || bool.value());
		}

		return super.or(value);
	}

	@Override
	public String toString() {
		return value() ? KeywordDefinitions.TRUE : KeywordDefinitions.FALSE;
	}

	@Override
	public Boolean value() {
		return value;
	}
}