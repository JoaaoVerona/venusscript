package com.github.bloodshura.ignitium.venus.test;

import com.github.bloodshura.ignitium.activity.logging.Level;
import com.github.bloodshura.ignitium.activity.logging.XLogger;
import com.github.bloodshura.ignitium.venus.component.Component;
import com.github.bloodshura.ignitium.venus.component.Container;
import com.github.bloodshura.ignitium.venus.component.Script;
import com.github.bloodshura.ignitium.venus.exception.compile.ScriptCompileException;
import com.github.bloodshura.ignitium.venus.executor.ApplicationContext;
import com.github.bloodshura.ignitium.venus.origin.ScriptOrigin;
import com.github.bloodshura.ignitium.venus.origin.SimpleScriptOrigin;
import com.github.bloodshura.ignitium.worker.StringWorker;
import org.junit.Test;

import java.io.IOException;

public class Parser {
	@Test
	public void simplePrint() throws IOException, ScriptCompileException {
		XLogger.disable(Level.DEBUG);

		String[] content = { "export MY_VAR = 0", "export MY_STRING = \"oi\"", "def printMyName(string name) {", "  print(\"Hello, I'm \" + name + \"!\")", "  print(1 + 3 - (5 + 2))", "  j = (i + 1) * k", "}" };
		ScriptOrigin origin = new SimpleScriptOrigin("test.xs", StringWorker.join('\n', content));
		Script script = origin.compile(new ApplicationContext());

		print(script);
	}

	public static void print(Component component) {
		XLogger.println(component);

		if (component instanceof Container) {
			XLogger.pushTab();

			for (Component child : ((Container) component).getChildren()) {
				print(child);
			}

			XLogger.popTab();
		}
	}
}