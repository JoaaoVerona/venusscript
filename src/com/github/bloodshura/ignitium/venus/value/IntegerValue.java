package com.github.bloodshura.ignitium.venus.value;

import com.github.bloodshura.ignitium.venus.type.PrimitiveType;

public class IntegerValue extends NumericValue {
	private final long value;

	public IntegerValue(long value) {
		super(PrimitiveType.INTEGER);
		this.value = value;
	}

	@Override
	public Value and(Value value) {
		if (value instanceof IntegerValue) {
			return new IntegerValue(value() & ((IntegerValue) value).value());
		}

		return super.and(value);
	}

	@Override
	public IntegerValue clone() {
		return new IntegerValue(value());
	}

	@Override
	public Integer compareTo(Value value) {
		if (value instanceof NumericValue) {
			NumericValue numeric = (NumericValue) value;

			return value().compareTo(numeric.value().longValue());
		}

		return super.compareTo(value);
	}

	@Override
	public Value divide(Value value) {
		if (value instanceof NumericValue) {
			NumericValue numeric = (NumericValue) value;

			return new IntegerValue(value() / numeric.value().longValue());
		}

		return super.divide(value);
	}

	@Override
	public Value minus(Value value) {
		if (value instanceof NumericValue) {
			NumericValue numeric = (NumericValue) value;

			return new IntegerValue(value() - numeric.value().longValue());
		}

		return super.minus(value);
	}

	@Override
	public Value multiply(Value value) {
		if (value instanceof NumericValue) {
			NumericValue numeric = (NumericValue) value;

			return new IntegerValue(value() * numeric.value().longValue());
		}

		return super.multiply(value);
	}

	@Override
	public Value negate() {
		return new IntegerValue(-value());
	}

	@Override
	public Value or(Value value) {
		if (value instanceof IntegerValue) {
			return new IntegerValue(value() | ((IntegerValue) value).value());
		}

		return super.or(value);
	}

	@Override
	public Value plus(Value value) {
		if (value instanceof NumericValue) {
			NumericValue numeric = (NumericValue) value;

			return new IntegerValue(value() + numeric.value().longValue());
		}

		return super.plus(value);
	}

	@Override
	public Value remainder(Value value) {
		if (value instanceof NumericValue) {
			NumericValue numeric = (NumericValue) value;

			return new IntegerValue(value() % numeric.value().longValue());
		}

		return super.remainder(value);
	}

	@Override
	public Value shiftLeft(Value value) {
		if (value instanceof IntegerValue) {
			IntegerValue integer = (IntegerValue) value;

			return new IntegerValue(value() << integer.value());
		}

		return super.shiftLeft(value);
	}

	@Override
	public Value shiftRight(Value value) {
		if (value instanceof IntegerValue) {
			IntegerValue integer = (IntegerValue) value;

			return new IntegerValue(value() >> integer.value());
		}

		return super.shiftRight(value);
	}

	@Override
	public String toString() {
		return Long.toString(value());
	}

	@Override
	public Long value() {
		return value;
	}
}