package com.github.bloodshura.ignitium.venus.value;

import com.github.bloodshura.ignitium.venus.type.Type;

public abstract class NumericValue extends Value {
	public NumericValue(Type type) {
		super(type);
	}

	@Override
	public abstract Number value();
}