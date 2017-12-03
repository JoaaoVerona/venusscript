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

package com.github.bloodshura.x.venus.library.std;

import com.github.bloodshura.x.venus.exception.runtime.AssertionException;
import com.github.bloodshura.x.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.x.venus.executor.Context;
import com.github.bloodshura.x.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.x.venus.function.Method;
import com.github.bloodshura.x.venus.function.annotation.MethodArgs;
import com.github.bloodshura.x.venus.function.annotation.MethodName;
import com.github.bloodshura.x.venus.type.PrimitiveType;
import com.github.bloodshura.x.venus.value.BoolValue;
import com.github.bloodshura.x.venus.value.Value;

@MethodArgs(Value.class)
@MethodName("assert")
public class Assert extends Method {
	@Override
	public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
		Value value = descriptor.get(0);

		if (value instanceof BoolValue) {
			BoolValue boolValue = (BoolValue) value;

			if (boolValue.value()) {
				return null;
			}

			throw new AssertionException(context, "Assertion failed");
		}

		throw new AssertionException(context, "Assertion expected a value of type " + PrimitiveType.BOOLEAN + "; received " + value.getType());
	}
}