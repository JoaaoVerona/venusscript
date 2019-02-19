package com.github.bloodshura.ignitium.venus.expression;

import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.type.Type;
import com.github.bloodshura.ignitium.venus.value.Value;

public interface Expression {
	Value resolve(Context context) throws ScriptRuntimeException;

	default Type resolveType(Context context) throws ScriptRuntimeException {
		return resolve(context).getType();
	}
}