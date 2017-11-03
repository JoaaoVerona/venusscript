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

package com.github.bloodshura.x.venus.library.dynamic;

import com.github.bloodshura.x.venus.component.Script;
import com.github.bloodshura.x.venus.exception.compile.ScriptCompileException;
import com.github.bloodshura.x.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.x.venus.executor.Context;
import com.github.bloodshura.x.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.x.venus.function.VoidMethod;
import com.github.bloodshura.x.venus.function.annotation.MethodArgs;
import com.github.bloodshura.x.venus.function.annotation.MethodName;
import com.github.bloodshura.x.venus.value.BoolValue;
import com.github.bloodshura.x.venus.value.StringValue;

@MethodArgs({ StringValue.class, BoolValue.class })
@MethodName("dynamicInclude")
public class DynamicInclude extends VoidMethod {
  @Override
  public void callVoid(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
    StringValue includeName = (StringValue) descriptor.get(0);
    BoolValue maybe = (BoolValue) descriptor.get(1);
    Script script = context.getScript();

    try {
      script.include(includeName.value(), maybe.value());
    }
    catch (ScriptCompileException exception) {
      throw new ScriptRuntimeException(context, "Could not include script: " + exception.getMessage(), exception.getCause());
    }
  }
}