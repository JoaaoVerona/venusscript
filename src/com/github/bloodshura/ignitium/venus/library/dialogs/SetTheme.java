package com.github.bloodshura.ignitium.venus.library.dialogs;

import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.ignitium.venus.function.Method;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodArgs;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodName;
import com.github.bloodshura.ignitium.venus.value.BoolValue;
import com.github.bloodshura.ignitium.venus.value.StringValue;
import com.github.bloodshura.ignitium.venus.value.Value;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

@MethodArgs(StringValue.class)
@MethodName("setTheme")
public class SetTheme extends Method {
	@Override
	public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
		StringValue value = (StringValue) descriptor.get(0);
		String themeName = value.value();
		String themePath = null;

		if (themeName.equalsIgnoreCase("metal")) {
			themePath = UIManager.getCrossPlatformLookAndFeelClassName();
		} else if (themeName.equalsIgnoreCase("system")) {
			themePath = UIManager.getSystemLookAndFeelClassName();
		}

		if (themePath != null) {
			try {
				UIManager.setLookAndFeel(themePath);

				return new BoolValue(true);
			} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ignored) {
			}
		}

		return new BoolValue(false);
	}
}
