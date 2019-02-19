package com.github.bloodshura.ignitium.venus.library.std;

import com.github.bloodshura.ignitium.sys.XSystem;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.ignitium.venus.function.VoidMethod;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodArgs;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodName;
import com.github.bloodshura.ignitium.venus.value.DecimalValue;
import com.github.bloodshura.ignitium.venus.value.IntegerValue;

@MethodArgs({ DecimalValue.class, IntegerValue.class })
@MethodName("beep")
public class Beep extends VoidMethod {
	@Override
	public void callVoid(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
		DecimalValue frequency = (DecimalValue) descriptor.get(0);
		IntegerValue duration = (IntegerValue) descriptor.get(1);

		XSystem.getDesktop().beep(frequency.value(), duration.value());
	}
}