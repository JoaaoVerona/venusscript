package com.github.bloodshura.ignitium.venus.library.math;

import com.github.bloodshura.ignitium.math.random.XRandom;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.ignitium.venus.function.Method;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodArgs;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodName;
import com.github.bloodshura.ignitium.venus.value.IntegerValue;
import com.github.bloodshura.ignitium.venus.value.Value;

@MethodArgs({ IntegerValue.class, IntegerValue.class })
@MethodName("randInt")
public class RandInt extends Method {
	@Override
	public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
		IntegerValue min = (IntegerValue) descriptor.get(0);
		IntegerValue max = (IntegerValue) descriptor.get(1);
		long minValue = min.value();
		long maxValue = max.value();

		if (minValue > maxValue) {
			long temp = maxValue;

			maxValue = minValue;
			minValue = temp;
		}

		return new IntegerValue(XRandom.INSTANCE.nextLong(minValue, maxValue));
	}
}