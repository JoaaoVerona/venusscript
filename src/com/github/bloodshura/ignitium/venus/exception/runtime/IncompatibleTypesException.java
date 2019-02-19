package com.github.bloodshura.ignitium.venus.exception.runtime;

import com.github.bloodshura.ignitium.venus.executor.Context;

public class IncompatibleTypesException extends ScriptRuntimeException {
	public IncompatibleTypesException(Context context, CharSequence message) {
		super(context, message);
	}
}