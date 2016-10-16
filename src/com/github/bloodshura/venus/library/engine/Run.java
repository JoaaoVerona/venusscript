//////////////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2016, João Vitor Verona Biazibetti - All Rights Reserved                /
//                                                                                       /
// This program is free software: you can redistribute it and/or modify                  /
// it under the terms of the GNU General Public License as published by                  /
// the Free Software Foundation, either version 3 of the License, or                     /
// (at your option) any later version.                                                   /
//                                                                                       /
// This program is distributed in the hope that it will be useful,                       /
// but WITHOUT ANY WARRANTY; without even the implied warranty of                        /
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the                          /
// GNU General Public License for more details.                                          /
//                                                                                       /
// You should have received a copy of the GNU General Public License                     /
// along with this program. If not, see <http://www.gnu.org/licenses/>.                  /
//                                                                                       /
// https://www.github.com/BloodShura                                                     /
//////////////////////////////////////////////////////////////////////////////////////////

package com.github.bloodshura.venus.library.engine;

import com.github.bloodshura.venus.component.Script;
import com.github.bloodshura.venus.exception.compile.ScriptCompileException;
import com.github.bloodshura.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.venus.executor.Context;
import com.github.bloodshura.venus.executor.VenusExecutor;
import com.github.bloodshura.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.venus.function.Method;
import com.github.bloodshura.venus.function.annotation.MethodArgs;
import com.github.bloodshura.venus.function.annotation.MethodName;
import com.github.bloodshura.venus.origin.ScriptMode;
import com.github.bloodshura.venus.origin.ScriptOrigin;
import com.github.bloodshura.venus.value.BoolValue;
import com.github.bloodshura.venus.value.StringValue;
import com.github.bloodshura.venus.value.Value;

@MethodArgs(StringValue.class)
@MethodName("run")
public class Run extends Method {
  @Override
  public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
    StringValue path = (StringValue) descriptor.get(0);
    Script current = context.getScript();
    ScriptOrigin origin = current.getOrigin().findRelative(path.value());

    if (origin != null) {
      try {
        Script script = origin.compile(current.getApplicationContext());
        VenusExecutor executor = new VenusExecutor();

        executor.run(script, ScriptMode.NORMAL);

        return new BoolValue(true);
      }
      catch (ScriptCompileException exception) {
        throw new ScriptRuntimeException(context, "Failed to compile script", exception);
      }
    }

    return new BoolValue(false);
  }
}