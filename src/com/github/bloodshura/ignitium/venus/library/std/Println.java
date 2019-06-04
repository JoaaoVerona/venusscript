package com.github.bloodshura.ignitium.venus.library.std;

import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.executor.OutputReference;
import com.github.bloodshura.ignitium.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.ignitium.venus.function.VoidMethod;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodName;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodVarArgs;
import com.github.bloodshura.ignitium.venus.value.Value;

@MethodName("println")
@MethodVarArgs
public class Println extends VoidMethod {
	@Override
	public void callVoid(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
		OutputReference reference = context.getApplicationContext().getUserData("out", OutputReference.class);

		if (reference != null) {
			if (!descriptor.isEmpty()) {
				for (Value argument : descriptor.getValues()) {
					reference.output(argument.toString() + '\n');
				}
			} else {
				reference.output("\n");
			}
		}
	}
}
