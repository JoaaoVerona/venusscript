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

package com.github.bloodshura.ignitium.venus.library.std;

import com.github.bloodshura.ignitium.activity.logging.XLogger;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.executor.VariableStructure;
import com.github.bloodshura.ignitium.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.ignitium.venus.function.VoidMethod;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodArgs;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodName;
import com.github.bloodshura.ignitium.venus.value.IntegerValue;
import com.github.bloodshura.ignitium.venus.value.Value;
import com.github.bloodshura.ignitium.venus.value.VariableRefValue;

@MethodArgs(VariableRefValue.class)
@MethodName("consume")
public class Consume extends VoidMethod {
	@Override
	public void callVoid(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
		VariableRefValue reference = (VariableRefValue) descriptor.get(0);
		VariableStructure variable = context.getVar(reference.value());
		Object monitor;

		synchronized ((monitor = variable.getLockMonitor())) {
			Value value = variable.getValue();
			long val = 0;

			if (value instanceof IntegerValue) {
				IntegerValue intValue = (IntegerValue) value;

				val = intValue.value();
			}

			if (val <= 0) {
				try {
					monitor.wait();
					context.setVar(reference.value(), value.minus(new IntegerValue(1)));
				}
				catch (InterruptedException exception) {
					XLogger.warnln("Thread " + Thread.currentThread() + " interrupted while 'consume' was locking.");
				}
			}
		}
	}
}