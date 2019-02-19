package com.github.bloodshura.ignitium.venus.type;

import com.github.bloodshura.ignitium.venus.value.Value;

public abstract class Type {
	private final String identifier;

	public Type(String identifier) {
		this.identifier = identifier;
	}

	public abstract boolean accepts(Class<? extends Value> valueClass);

	public abstract boolean accepts(Type type);

	public final String getIdentifier() {
		return identifier;
	}

	public abstract boolean objectAccepts(Class<?> type);

	@Override
	public String toString() {
		return getIdentifier();
	}
}