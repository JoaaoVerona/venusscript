package com.github.bloodshura.ignitium.venus.exception.runtime;

import com.github.bloodshura.ignitium.venus.executor.Context;

public class UndefinedVariableException extends ScriptRuntimeException {
	private final String variableName;

	public UndefinedVariableException(Context context, String variableName) {
		super(context, "Undefined variable \"" + variableName + "\"");
		this.variableName = variableName;
	}

	public String getVariableName() {
		return variableName;
	}
}