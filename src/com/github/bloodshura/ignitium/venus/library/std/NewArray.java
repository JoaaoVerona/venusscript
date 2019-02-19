package com.github.bloodshura.ignitium.venus.library.std;

import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.ignitium.venus.function.Method;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodArgs;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodName;
import com.github.bloodshura.ignitium.venus.value.ArrayValue;
import com.github.bloodshura.ignitium.venus.value.IntegerValue;
import com.github.bloodshura.ignitium.venus.value.Value;
import com.github.bloodshura.ignitium.worker.ArrayWorker;

@MethodArgs({ IntegerValue.class, Value.class })
@MethodName("newArray")
public class NewArray extends Method {
	@Override
	public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
		IntegerValue size = (IntegerValue) descriptor.get(0);
		Value[] content = new Value[size.value().intValue()];

		ArrayWorker.fill(content, descriptor.get(1));

		return new ArrayValue(content);
	}
}