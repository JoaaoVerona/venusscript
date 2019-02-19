package com.github.bloodshura.ignitium.venus.exception.runtime;

import com.github.bloodshura.ignitium.venus.executor.Context;

public class UndefinedValueTypeException extends ScriptRuntimeException {
	public UndefinedValueTypeException(Context context, String name) {
		super(context, "No definition found for a type named \"" + name + "\"");
	}
}