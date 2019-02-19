package com.github.bloodshura.ignitium.venus.library.dialogs;

import com.github.bloodshura.ignitium.charset.TextBuilder;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.ignitium.venus.function.Method;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodName;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodVarArgs;
import com.github.bloodshura.ignitium.venus.value.BoolValue;
import com.github.bloodshura.ignitium.venus.value.Value;
import com.github.bloodshura.sparkium.desktop.dialogs.Dialogs;

@MethodName("askDialog")
@MethodVarArgs
public class AskDialog extends Method {
	@Override
	public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
		if (descriptor.isEmpty()) {
			return new BoolValue(false);
		}

		String title = descriptor.transform(0, Value::toString, null);
		TextBuilder message = new TextBuilder();
		int offset = descriptor.count() > 1 ? 1 : 0;

		for (int i = offset; i < descriptor.count(); i++) {
			message.append(descriptor.get(i));
			message.newLine();
		}

		return new BoolValue(Dialogs.ask(title, message));
	}
}