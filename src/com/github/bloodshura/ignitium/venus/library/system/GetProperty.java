package com.github.bloodshura.ignitium.venus.library.system;

import com.github.bloodshura.ignitium.sys.XSystem;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.ignitium.venus.function.Method;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodArgs;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodName;
import com.github.bloodshura.ignitium.venus.value.StringValue;
import com.github.bloodshura.ignitium.venus.value.Value;

@MethodArgs({ StringValue.class, Value.class })
@MethodName("getProperty")
public class GetProperty extends Method {
	@Override
	public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
		StringValue key = (StringValue) descriptor.get(0);
		Object value = XSystem.getProperty(key.value());
		Value result = Value.construct(value);

		return result != null ? result : descriptor.get(1);
	}
}