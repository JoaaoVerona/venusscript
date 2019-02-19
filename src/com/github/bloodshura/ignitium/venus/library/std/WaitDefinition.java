package com.github.bloodshura.ignitium.venus.library.std;

import com.github.bloodshura.ignitium.threading.IgThread;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.ignitium.venus.function.VoidMethod;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodArgs;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodName;
import com.github.bloodshura.ignitium.venus.value.VariableRefValue;

@MethodArgs(VariableRefValue.class)
@MethodName("wait")
public class WaitDefinition extends VoidMethod {
	@Override
	public void callVoid(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
		VariableRefValue reference = (VariableRefValue) descriptor.get(0);

		while (!context.hasVar(reference.value())) {
			IgThread.stay(50L);
		}
	}
}