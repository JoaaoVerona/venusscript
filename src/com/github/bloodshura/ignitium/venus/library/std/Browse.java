package com.github.bloodshura.ignitium.venus.library.std;

import com.github.bloodshura.ignitium.io.Url;
import com.github.bloodshura.ignitium.io.UrlException;
import com.github.bloodshura.ignitium.sys.XSystem;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.ignitium.venus.function.Method;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodArgs;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodName;
import com.github.bloodshura.ignitium.venus.value.BoolValue;
import com.github.bloodshura.ignitium.venus.value.StringValue;
import com.github.bloodshura.ignitium.venus.value.Value;

@MethodArgs(StringValue.class)
@MethodName("browse")
public class Browse extends Method {
	@Override
	public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
		StringValue path = (StringValue) descriptor.get(0);

		try {
			XSystem.getDesktop().browse(new Url(path.value()));

			return new BoolValue(true);
		}
		catch (UrlException exception) {
			return new BoolValue(false);
		}
	}
}