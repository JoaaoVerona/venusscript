package com.github.bloodshura.ignitium.venus.library.engine;

import com.github.bloodshura.ignitium.venus.component.Script;
import com.github.bloodshura.ignitium.venus.exception.compile.ScriptCompileException;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.executor.VenusExecutor;
import com.github.bloodshura.ignitium.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.ignitium.venus.function.Method;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodArgs;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodName;
import com.github.bloodshura.ignitium.venus.origin.ScriptMode;
import com.github.bloodshura.ignitium.venus.origin.ScriptOrigin;
import com.github.bloodshura.ignitium.venus.value.BoolValue;
import com.github.bloodshura.ignitium.venus.value.StringValue;
import com.github.bloodshura.ignitium.venus.value.Value;

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
			} catch (ScriptCompileException exception) {
				throw new ScriptRuntimeException(context, "Failed to compile script", exception);
			}
		}

		return new BoolValue(false);
	}
}
