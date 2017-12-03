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

import com.github.bloodshura.x.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.x.venus.executor.Context;
import com.github.bloodshura.x.venus.executor.OutputReference;
import com.github.bloodshura.x.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.x.venus.function.VoidMethod;
import com.github.bloodshura.x.venus.function.annotation.MethodName;
import com.github.bloodshura.x.venus.function.annotation.MethodVarArgs;
import com.github.bloodshura.x.venus.value.Value;

@MethodName("println")
@MethodVarArgs
public class Println extends VoidMethod {
	@Override
	public void callVoid(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
		OutputReference reference = context.getApplicationContext().getUserData("out", OutputReference.class);

		if (reference != null) {
			if (!descriptor.isEmpty()) {
				for (Value argument : descriptor.getValues()) {
					reference.output(argument.toString() + '\n');
				}
			}
			else {
				reference.output("\n");
			}
		}
	}
}