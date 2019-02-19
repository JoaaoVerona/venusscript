package com.github.bloodshura.ignitium.venus.value;

import com.github.bloodshura.ignitium.venus.type.Type;

public abstract class IterableValue extends Value implements Iterable<Value> {
	public IterableValue(Type type) {
		super(type);
	}
}