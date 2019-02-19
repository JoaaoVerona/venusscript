package com.github.bloodshura.ignitium.venus.library.time;

import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.ignitium.venus.function.Method;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodName;
import com.github.bloodshura.ignitium.venus.value.IntegerValue;
import com.github.bloodshura.ignitium.venus.value.Value;

import java.time.LocalDate;

@MethodName("getYear")
public class GetYear extends Method {
	@Override
	public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
		return new IntegerValue(LocalDate.now().getYear());
	}
}