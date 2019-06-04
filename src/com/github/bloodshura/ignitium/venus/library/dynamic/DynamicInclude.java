package com.github.bloodshura.ignitium.venus.library.dynamic;

import com.github.bloodshura.ignitium.venus.component.Script;
import com.github.bloodshura.ignitium.venus.exception.compile.ScriptCompileException;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.ignitium.venus.function.VoidMethod;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodArgs;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodName;
import com.github.bloodshura.ignitium.venus.value.BoolValue;
import com.github.bloodshura.ignitium.venus.value.StringValue;

@MethodArgs({ StringValue.class, BoolValue.class })
@MethodName("dynamicInclude")
public class DynamicInclude extends VoidMethod {
	@Override
	public void callVoid(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
		StringValue includeName = (StringValue) descriptor.get(0);
		BoolValue maybe = (BoolValue) descriptor.get(1);
		Script script = context.getScript();

		try {
			script.include(includeName.value(), maybe.value());
		} catch (ScriptCompileException exception) {
			throw new ScriptRuntimeException(context, "Could not include script: " + exception.getMessage(), exception.getCause());
		}
	}
}
