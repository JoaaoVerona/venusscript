package com.github.bloodshura.ignitium.venus.exception.runtime;

import com.github.bloodshura.ignitium.collection.view.XView;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.type.Type;

public class UndefinedFunctionException extends ScriptRuntimeException {
	public UndefinedFunctionException(Context context, String functionName, XView<Type> argumentTypes) {
		super(context, "No definition found for a function named \"" + functionName + "\" taking argument types: " + argumentTypes);
	}
}