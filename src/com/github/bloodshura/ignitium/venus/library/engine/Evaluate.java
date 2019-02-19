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
import com.github.bloodshura.ignitium.venus.function.Method;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodName;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodVarArgs;
import com.github.bloodshura.ignitium.venus.origin.ScriptMode;
import com.github.bloodshura.ignitium.venus.origin.SimpleScriptOrigin;
import com.github.bloodshura.ignitium.venus.value.Value;

import java.io.IOException;

@MethodName("evaluate")
@MethodVarArgs
public class Evaluate extends Method {
	@Override
	public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
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

			return appContext.currentExecutor().run(container, ScriptMode.EVALUATION);
		}
		catch (IOException | ScriptCompileException exception) {
			throw new ScriptRuntimeException(context, "Failed to compile script", exception);
		}
	}
}