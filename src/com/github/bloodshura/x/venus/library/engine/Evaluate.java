//////////////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2013-2017, João Vitor Verona Biazibetti - All Rights Reserved           /
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

package com.github.bloodshura.x.venus.library.engine;

import com.github.bloodshura.x.charset.TextBuilder;
import com.github.bloodshura.x.util.Pool;
import com.github.bloodshura.x.venus.compiler.VenusLexer;
import com.github.bloodshura.x.venus.compiler.VenusParser;
import com.github.bloodshura.x.venus.component.SimpleContainer;
import com.github.bloodshura.x.venus.exception.compile.ScriptCompileException;
import com.github.bloodshura.x.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.x.venus.executor.ApplicationContext;
import com.github.bloodshura.x.venus.executor.Context;
import com.github.bloodshura.x.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.x.venus.function.Method;
import com.github.bloodshura.x.venus.function.annotation.MethodName;
import com.github.bloodshura.x.venus.function.annotation.MethodVarArgs;
import com.github.bloodshura.x.venus.origin.ScriptMode;
import com.github.bloodshura.x.venus.origin.SimpleScriptOrigin;
import com.github.bloodshura.x.venus.value.Value;

import java.io.IOException;

@MethodName("evaluate")
@MethodVarArgs
public class Evaluate extends Method {
  @Override
  public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
    VenusParser parser = context.getScript().getParser();
    TextBuilder builder = Pool.newBuilder();

    builder.appendln(descriptor.getValues());

    String source = builder.toStringAndClear();
    ApplicationContext appContext = context.getApplicationContext();
    SimpleScriptOrigin origin = new SimpleScriptOrigin("Interpreted-Script", source);
    SimpleContainer container = new SimpleContainer();

    container.setParent(context.getOwner());

    try {
      parser.parse(new VenusLexer(origin), container);

      return appContext.currentExecutor().run(container, ScriptMode.EVALUATION);
    }
    catch (IOException | ScriptCompileException exception) {
      throw new ScriptRuntimeException(context, "Failed to compile script", exception);
    }
  }
}