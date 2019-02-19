package com.github.bloodshura.ignitium.venus.function;

import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.value.Value;

public abstract class VoidMethod extends Method {
	@Override
	public final Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
		callVoid(context, descriptor);

		return null;
	}

	public abstract void callVoid(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException;
}