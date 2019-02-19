package com.github.bloodshura.ignitium.venus.exception.runtime;

import com.github.bloodshura.ignitium.venus.executor.Context;

public class InvalidFunctionParameterException extends ScriptRuntimeException {
	public InvalidFunctionParameterException(Context context, CharSequence message) {
		super(context, message);
	}
}