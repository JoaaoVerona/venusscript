package com.github.bloodshura.ignitium.venus.library.std;

import com.github.bloodshura.ignitium.venus.exception.runtime.AssertionException;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.ignitium.venus.function.Method;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodArgs;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodName;
import com.github.bloodshura.ignitium.venus.type.PrimitiveType;
import com.github.bloodshura.ignitium.venus.value.BoolValue;
import com.github.bloodshura.ignitium.venus.value.Value;

@MethodArgs(Value.class)
@MethodName("assert")
public class Assert extends Method {
	@Override
	public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
		Value value = descriptor.get(0);

		if (value instanceof BoolValue) {
			BoolValue boolValue = (BoolValue) value;

			if (boolValue.value()) {
				return null;
			}

			throw new AssertionException(context, "Assertion failed");
		}

		throw new AssertionException(context, "Assertion expected a value of type " + PrimitiveType.BOOLEAN + "; received " + value.getType());
	}
}