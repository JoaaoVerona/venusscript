package com.github.bloodshura.ignitium.venus.library.math;

import com.github.bloodshura.ignitium.math.random.XRandom;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.ignitium.venus.function.Method;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodArgs;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodName;
import com.github.bloodshura.ignitium.venus.value.DecimalValue;
import com.github.bloodshura.ignitium.venus.value.Value;

@MethodArgs({ DecimalValue.class, DecimalValue.class })
@MethodName("randDecimal")
public class RandDecimal extends Method {
	@Override
	public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
		DecimalValue min = (DecimalValue) descriptor.get(0);
		DecimalValue max = (DecimalValue) descriptor.get(1);
		double minValue = min.value();
		double maxValue = max.value();

		if (minValue > maxValue) {
			double temp = maxValue;

			maxValue = minValue;
			minValue = temp;
		}

		return new DecimalValue(XRandom.INSTANCE.nextDouble(minValue, maxValue));
	}
}