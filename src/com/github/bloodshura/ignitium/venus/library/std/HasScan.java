package com.github.bloodshura.ignitium.venus.library.std;

import com.github.bloodshura.ignitium.activity.logging.Logger;
import com.github.bloodshura.ignitium.activity.scanning.XScanner;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.ignitium.venus.function.Method;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodName;
import com.github.bloodshura.ignitium.venus.value.BoolValue;
import com.github.bloodshura.ignitium.venus.value.Value;

@MethodName("hasScan")
public class HasScan extends Method {
	@Override
	public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
		Logger logger = context.getApplicationContext().getUserData("in", Logger.class);

		if (logger != null) {
			return new BoolValue(XScanner.has());
		}

		return new BoolValue(false);
	}
}