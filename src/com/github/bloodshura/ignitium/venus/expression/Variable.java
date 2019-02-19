package com.github.bloodshura.ignitium.venus.expression;

import com.github.bloodshura.ignitium.util.XApi;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.value.Value;

public class Variable implements Expression {
	private final String name;

	public Variable(String name) {
		XApi.requireNonNull(name, "name");

		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public Value resolve(Context context) throws ScriptRuntimeException {
		return context.getVarValue(getName());
	}

	@Override
	public String toString() {
		return "var(" + getName() + ')';
	}
}