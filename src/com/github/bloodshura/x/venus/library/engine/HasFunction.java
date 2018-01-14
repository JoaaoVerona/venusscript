/*
 * Copyright (c) 2013-2018, João Vitor Verona Biazibetti - All Rights Reserved
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

package com.github.bloodshura.x.venus.library.engine;

import com.github.bloodshura.x.collection.list.XList;
import com.github.bloodshura.x.collection.list.impl.XArrayList;
import com.github.bloodshura.x.collection.view.XBasicView;
import com.github.bloodshura.x.venus.exception.runtime.InvalidFunctionParameterException;
import com.github.bloodshura.x.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.x.venus.exception.runtime.UndefinedFunctionException;
import com.github.bloodshura.x.venus.executor.Context;
import com.github.bloodshura.x.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.x.venus.function.Method;
import com.github.bloodshura.x.venus.function.annotation.MethodName;
import com.github.bloodshura.x.venus.function.annotation.MethodVarArgs;
import com.github.bloodshura.x.venus.type.Type;
import com.github.bloodshura.x.venus.value.BoolValue;
import com.github.bloodshura.x.venus.value.TypeValue;
import com.github.bloodshura.x.venus.value.Value;

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
			}
			else {
				throw new InvalidFunctionParameterException(context, "Expected value type, received " + value.getType());
			}
		}

		try {
			context.getOwner().findFunction(context, name, new XBasicView<>(types));

			return new BoolValue(true);
		}
		catch (UndefinedFunctionException exception) {
			return new BoolValue(false);
		}
	}
}