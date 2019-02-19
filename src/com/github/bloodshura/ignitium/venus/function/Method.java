package com.github.bloodshura.ignitium.venus.function;

import com.github.bloodshura.ignitium.collection.view.XArrayView;
import com.github.bloodshura.ignitium.collection.view.XBasicView;
import com.github.bloodshura.ignitium.collection.view.XView;
import com.github.bloodshura.ignitium.util.XApi;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodArgs;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodName;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodVarArgs;
import com.github.bloodshura.ignitium.venus.type.PrimitiveType;
import com.github.bloodshura.ignitium.venus.type.Type;
import com.github.bloodshura.ignitium.venus.value.Value;

import javax.annotation.Nonnull;

public abstract class Method implements Function {
	private final XView<Type> arguments;
	private final String name;
	private final boolean varArgs;

	public Method() {
		boolean hasMethodArgs = getClass().isAnnotationPresent(MethodArgs.class);
		boolean hasMethodName = getClass().isAnnotationPresent(MethodName.class);
		boolean isMethodVarArgs = getClass().isAnnotationPresent(MethodVarArgs.class);

		XApi.requireState(!hasMethodArgs || (hasMethodArgs != isMethodVarArgs), "Must contain either @MethodArgs or @MethodVarArgs");
		XApi.requireState(hasMethodName, "No @MethodName found");

		if (hasMethodArgs) {
			Class<? extends Value>[] args = getClass().getAnnotation(MethodArgs.class).value();

			this.arguments = new XArrayView<>(args).map(PrimitiveType::forType);
		}
		else {
			this.arguments = new XBasicView<>();
		}

		this.name = getClass().getAnnotation(MethodName.class).value();
		this.varArgs = isMethodVarArgs;
	}

	@Override
	public final XView<Type> getArgumentTypes() {
		return arguments;
	}

	@Nonnull
	@Override
	public final String getName() {
		return name;
	}

	@Override
	public final boolean isVarArgs() {
		return varArgs;
	}

	@Override
	public final String toString() {
		return "method(" + getName() + ')';
	}
}