package com.github.bloodshura.ignitium.venus.value;

import com.github.bloodshura.ignitium.venus.type.PrimitiveType;

public class DecimalValue extends NumericValue {
	private final double value;

	public DecimalValue(double value) {
		super(PrimitiveType.DECIMAL);
		this.value = value;
	}

	@Override
	public DecimalValue clone() {
		return new DecimalValue(value());
	}

	@Override
	public Integer compareTo(Value value) {
		if (value instanceof NumericValue) {
			NumericValue numeric = (NumericValue) value;

			return value().compareTo(numeric.value().doubleValue());
		}

		return null;
	}

	@Override
	public Value divide(Value value) {
		if (value instanceof NumericValue) {
			NumericValue numeric = (NumericValue) value;

			return new DecimalValue(value() / numeric.value().doubleValue());
		}

		return super.divide(value);
	}

	@Override
	public Value minus(Value value) {
		if (value instanceof NumericValue) {
			NumericValue numeric = (NumericValue) value;

			return new DecimalValue(value() - numeric.value().doubleValue());
		}

		return super.minus(value);
	}

	@Override
	public Value multiply(Value value) {
		if (value instanceof NumericValue) {
			NumericValue numeric = (NumericValue) value;

			return new DecimalValue(value() * numeric.value().doubleValue());
		}

		return super.multiply(value);
	}

	@Override
	public Value negate() {
		return new DecimalValue(-value());
	}

	@Override
	public Value plus(Value value) {
		if (value instanceof NumericValue) {
			NumericValue numeric = (NumericValue) value;

			return new DecimalValue(value() + numeric.value().doubleValue());
		}

		return super.plus(value);
	}

	@Override
	public Value remainder(Value value) {
		if (value instanceof NumericValue) {
			NumericValue numeric = (NumericValue) value;

			return new DecimalValue(value() % numeric.value().doubleValue());
		}

		return super.remainder(value);
	}

	@Override
	public Double value() {
		return value;
	}

	@Override
	public String toString() {
		return Double.toString(value());
	}
}