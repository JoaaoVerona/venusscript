package com.github.bloodshura.ignitium.venus.exception.runtime;

import com.github.bloodshura.ignitium.venus.executor.Context;

public class InvalidArrayAccessException extends ScriptRuntimeException {
	public InvalidArrayAccessException(Context context, CharSequence message) {
		super(context, message);
	}
}