package com.github.bloodshura.ignitium.venus.function;

import com.github.bloodshura.ignitium.collection.list.XList;
import com.github.bloodshura.ignitium.collection.view.XBasicView;
import com.github.bloodshura.ignitium.collection.view.XView;
import com.github.bloodshura.ignitium.venus.component.Container;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.origin.ScriptMode;
import com.github.bloodshura.ignitium.venus.type.Type;
import com.github.bloodshura.ignitium.venus.value.Value;

import javax.annotation.Nonnull;

public final class Definition extends Container implements Function {
	private final XList<Argument> arguments;
	private final boolean global;
	private final String name;

	public Definition(String name, XList<Argument> arguments, boolean global) {
		this.arguments = arguments;
		this.global = global;
		this.name = name;
	}

	@Override
	public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
		int i = 0;

		if (!isGlobal()) {
			this.context = new Context(this, context);
		}

		for (Argument argument : getArguments()) {
			getContext().setVar(argument.getName(), descriptor.get(i++));
		}

		return getApplicationContext().currentExecutor().run(this, ScriptMode.NORMAL);
	}

	@Override
	public int getArgumentCount() { // Default impl. of getArgumentCount() calls getArgumentTypes(), but our impl. is expensive
		return arguments.size();
	}

	@Override
	public XView<Type> getArgumentTypes() {
		return getArguments().map(Argument::getType);
	}

	public XView<Argument> getArguments() {
		return new XBasicView<>(arguments);
	}

	@Nonnull
	@Override
	public String getName() {
		return name;
	}

	public boolean isGlobal() {
		return global;
	}

	@Override
	public boolean isVarArgs() {
		return false;
	}

	@Override
	public void setParent(Container parent) {
		super.setParent(parent);
		this.context = new Context(this, parent.getContext());
	}

	@Override
	public String toString() {
		return "definition(" + getName() + ')';
	}
}