/*
 * Copyright (c) 2013-2018, João Vitor Verona Biazibetti - All Rights Reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * https://www.github.com/BloodShura
 */

package com.github.bloodshura.ignitium.venus.function;

import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.type.PrimitiveType;
import com.github.bloodshura.ignitium.venus.type.Type;
import com.github.bloodshura.ignitium.venus.value.Value;
import com.github.bloodshura.ignitium.collection.view.XView;
import com.github.bloodshura.ignitium.lang.Nameable;
import com.github.bloodshura.ignitium.util.XApi;
import com.github.bloodshura.ignitium.venus.executor.Context;

public interface Function extends Nameable {
	default boolean accepts(String name, XView<Type> argumentTypes) {
		XApi.requireNonNull(name, "name");

		if (getName().equals(name)) {
			if (argumentTypes == null) {
				return true;
			}

			if (getArgumentCount() == argumentTypes.size()) {
				for (int i = 0; i < getArgumentCount(); i++) {
					Type required = getArgumentTypes().get(i);
					Type found = argumentTypes.get(i);

					if (!required.accepts(found) && (required != PrimitiveType.DECIMAL || found != PrimitiveType.INTEGER)) {
						return false;
					}
				}

				return true;
			}
			else if (isVarArgs()) {
				return true;
			}
		}

		return false;
	}

	Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException;

	default int getArgumentCount() {
		return getArgumentTypes().size();
	}

	XView<Type> getArgumentTypes();

	boolean isVarArgs();
}