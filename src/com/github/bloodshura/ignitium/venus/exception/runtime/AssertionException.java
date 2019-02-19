package com.github.bloodshura.ignitium.venus.exception.runtime;

import com.github.bloodshura.ignitium.venus.executor.Context;

public class AssertionException extends ScriptRuntimeException {
	public AssertionException(Context context, CharSequence message) {
		super(context, message);
	}
}