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

package com.github.bloodshura.ignitium.venus.library.engine;

import com.github.bloodshura.ignitium.charset.TextBuilder;
import com.github.bloodshura.ignitium.venus.compiler.VenusLexer;
import com.github.bloodshura.ignitium.venus.compiler.VenusParser;
import com.github.bloodshura.ignitium.venus.component.SimpleContainer;
import com.github.bloodshura.ignitium.venus.exception.compile.ScriptCompileException;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.ApplicationContext;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.ignitium.venus.function.VoidMethod;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodName;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodVarArgs;
import com.github.bloodshura.ignitium.venus.origin.ScriptMode;
import com.github.bloodshura.ignitium.venus.origin.SimpleScriptOrigin;

import java.io.IOException;

@MethodName("interpret")
@MethodVarArgs
public class Interpret extends VoidMethod {
	@Override
	public void callVoid(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
		VenusParser parser = context.getScript().getParser();
		TextBuilder builder = new TextBuilder();

		builder.appendln(descriptor.getValues());

		String source = builder.toStringAndClear();
		ApplicationContext appContext = context.getApplicationContext();
		SimpleScriptOrigin origin = new SimpleScriptOrigin("Interpreted-Script", source);
		SimpleContainer container = new SimpleContainer();

		container.setParent(context.getOwner());

		try {
			parser.parse(new VenusLexer(origin), container);
			appContext.currentExecutor().run(container, ScriptMode.INTERACTIVE);
		}
		catch (IOException | ScriptCompileException exception) {
			throw new ScriptRuntimeException(context, "Failed to compile script", exception);
		}
	}
}