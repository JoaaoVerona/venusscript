package com.github.bloodshura.ignitium.venus.library.math;

import com.github.bloodshura.ignitium.collection.list.XList;
import com.github.bloodshura.ignitium.collection.list.impl.XArrayList;
import com.github.bloodshura.ignitium.collection.view.XBasicView;
import com.github.bloodshura.ignitium.collection.view.XView;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.function.Function;
import com.github.bloodshura.ignitium.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.ignitium.venus.type.PrimitiveType;
import com.github.bloodshura.ignitium.venus.type.Type;
import com.github.bloodshura.ignitium.venus.value.IntegerValue;
import com.github.bloodshura.ignitium.venus.value.Value;
import com.github.bloodshura.ignitium.worker.UtilWorker;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MathFunction implements Function {
	private final XList<Type> arguments;
	private final Method method;
	private final String name;

	public MathFunction(Method method) {
		this.arguments = new XArrayList<>();
		this.method = method;
		this.name = method.getName();

		for (Class<?> arg : method.getParameterTypes()) {
			arguments.add(PrimitiveType.forObjectType(arg));
		}
	}

	@Override
	public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
		XList<Object> values = new XArrayList<>();
		int i = 0;

		for (Value argument : descriptor.getValues()) {
			if (argument instanceof IntegerValue && UtilWorker.fixPrimitiveClass(method.getParameterTypes()[i]) == Integer.class) {
				values.add(((Long) argument.value()).intValue());
			}
			else {
				values.add(argument.value());
			}

			i++;
		}

		try {
			Object result = getMethod().invoke(null, values.toArray());

			if (getMethod().getReturnType() == void.class && result == null) {
				return null;
			}

			Value value = Value.construct(result);

			if (value == null) {
				throw new ScriptRuntimeException(context, "Math method \"" + method.getName() + "\" returned untranslatable value of type " + result.getClass().getSimpleName() + "?!");
			}

			return value;
		}
		catch (IllegalAccessException | InvocationTargetException exception) {
			throw new ScriptRuntimeException(context, "Could not call math function \"" + getName() + "\"", exception);
		}
	}

	@Override
	public XView<Type> getArgumentTypes() {
		return new XBasicView<>(arguments);
	}

	public Method getMethod() {
		return method;
	}

	@Nonnull
	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isVarArgs() {
		return false;
	}

	public static boolean validate(Method method) {
		if (PrimitiveType.forObjectType(method.getReturnType()) == null) {
			return false;
		}

		for (Class<?> arg : method.getParameterTypes()) {
			if (PrimitiveType.forObjectType(arg) == null) {
				return false;
			}
		}

		return true;
	}
}