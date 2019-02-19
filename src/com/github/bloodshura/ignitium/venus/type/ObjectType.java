package com.github.bloodshura.ignitium.venus.type;

import com.github.bloodshura.ignitium.venus.value.ObjectValue;
import com.github.bloodshura.ignitium.venus.value.Value;

public class ObjectType extends Type {
	public ObjectType(String identifier) {
		super(identifier);
	}

	@Override
	public boolean accepts(Class<? extends Value> valueClass) {
		return ObjectValue.class.isAssignableFrom(valueClass);
	}

	@Override
	public boolean accepts(Type type) {
		return this == type;
	}

	@Override
	public boolean objectAccepts(Class<?> type) {
		return false;
	}
}