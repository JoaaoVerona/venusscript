package com.github.bloodshura.ignitium.venus.value;

import com.github.bloodshura.ignitium.util.XApi;
import com.github.bloodshura.ignitium.venus.type.PrimitiveType;

public class StringValue extends Value {
	private final String value;

	public StringValue(String value) {
		super(PrimitiveType.STRING);
		XApi.requireNonNull(value, "value");

		this.value = value;
	}

	@Override
	public StringValue clone() {
		return new StringValue(value());
	}

	@Override
	public Integer compareTo(Value value) {
		if (value instanceof StringValue) {
			StringValue string = (StringValue) value;

			return value().compareToIgnoreCase(string.value());
		}

		return null;
	}

	public boolean isCharacter() {
		return value().length() == 1;
	}

	@Override
	public StringValue plus(Value value) {
		return new StringValue(value() + value);
	}

	public char toCharacter() {
		XApi.requireState(isCharacter(), "Cannot convert multi character StringValue to single character");

		return value().charAt(0);
	}

	@Override
	public String toString() {
		return value();
	}

	@Override
	public String value() {
		return value;
	}
}