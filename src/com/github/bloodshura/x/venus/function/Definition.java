/*
 * Copyright (c) 2013-2017, João Vitor Verona Biazibetti - All Rights Reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * https://www.github.com/BloodShura
 */

package com.github.bloodshura.x.venus.function;

import com.github.bloodshura.x.collection.list.XList;
import com.github.bloodshura.x.collection.view.XBasicView;
import com.github.bloodshura.x.collection.view.XView;
import com.github.bloodshura.x.venus.component.Container;
import com.github.bloodshura.x.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.x.venus.executor.Context;
import com.github.bloodshura.x.venus.origin.ScriptMode;
import com.github.bloodshura.x.venus.type.Type;
import com.github.bloodshura.x.venus.value.Value;

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