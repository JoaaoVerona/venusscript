package com.github.bloodshura.ignitium.venus.library.engine;

import com.github.bloodshura.ignitium.collection.list.XList;
import com.github.bloodshura.ignitium.collection.list.impl.XArrayList;
import com.github.bloodshura.ignitium.collection.view.XBasicView;
import com.github.bloodshura.ignitium.venus.exception.runtime.InvalidFunctionParameterException;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.exception.runtime.UndefinedFunctionException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.ignitium.venus.function.Method;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodName;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodVarArgs;
import com.github.bloodshura.ignitium.venus.type.Type;
import com.github.bloodshura.ignitium.venus.value.BoolValue;
import com.github.bloodshura.ignitium.venus.value.TypeValue;
import com.github.bloodshura.ignitium.venus.value.Value;

@MethodName("hasFunction")
@MethodVarArgs
public class HasFunction extends Method {
	@Override
	public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
		if (descriptor.isEmpty()) {
			throw new InvalidFunctionParameterException(context, "Expected at least function name");
		}

		String name = descriptor.get(0).toString();
		XList<Type> types = new XArrayList<>();

		for (int i = 1; i < descriptor.count(); i++) {
			Value value = descriptor.get(i);

			if (value instanceof TypeValue) {
				TypeValue typeValue = (TypeValue) value;

				types.add(typeValue.value());
			} else {
				throw new InvalidFunctionParameterException(context, "Expected value type, received " + value.getType());
			}
		}

		try {
			context.getOwner().findFunction(context, name, new XBasicView<>(types));

			return new BoolValue(true);
		} catch (UndefinedFunctionException exception) {
			return new BoolValue(false);
		}
	}
}
