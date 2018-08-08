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

package com.github.bloodshura.ignitium.venus.test;

import com.github.bloodshura.ignitium.activity.logging.XLogger;
import com.github.bloodshura.ignitium.activity.scanning.XScanner;
import com.github.bloodshura.ignitium.collection.view.XView;
import com.github.bloodshura.ignitium.io.Directory;
import com.github.bloodshura.ignitium.io.File;
import com.github.bloodshura.ignitium.venus.component.Component;
import com.github.bloodshura.ignitium.venus.component.Container;
import com.github.bloodshura.ignitium.venus.component.Script;
import com.github.bloodshura.ignitium.venus.exception.compile.ScriptCompileException;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.ApplicationContext;
import com.github.bloodshura.ignitium.venus.executor.VenusExecutor;
import com.github.bloodshura.ignitium.venus.origin.FileScriptOrigin;
import com.github.bloodshura.ignitium.venus.origin.ScriptMode;
import com.github.bloodshura.ignitium.venus.origin.ScriptOrigin;
import com.github.bloodshura.ignitium.worker.ParseWorker;

import static com.github.bloodshura.ignitium.sys.XSystem.*;

public class InteractiveTester {
	public static final Directory DIRECTORY = new Directory("VenusScript/examples");
	public static final boolean LIGHTWEIGHT_ERRORS = false;

	public static void main(String[] args) throws Exception {
		XView<File> files = DIRECTORY.getDeepFiles();
		int i = 0;

		for (File file : files) {
			XLogger.println(i++ + ". " + file.getRelativePath(DIRECTORY));
		}

		XLogger.print("> ");

		int option = -1;
		boolean printAst = false;

		while (option < 0 || option >= files.size()) {
			String optionStr = XScanner.scan();

			if (optionStr.startsWith("*")) {
				optionStr = optionStr.substring(1);
				printAst = true;
			}

			if (ParseWorker.isInt(optionStr)) {
				option = ParseWorker.toInt(optionStr);
			}
		}

		File file = files.get(option);
		ScriptOrigin origin = new FileScriptOrigin(file);
		Script script;

		if (LIGHTWEIGHT_ERRORS) {
			try {
				script = origin.compile(new ApplicationContext());
			}
			catch (ScriptCompileException exception) {
				XLogger.warnln("COMPILE ERR: " + exception.getMessage());

				return;
			}
		}
		else {
			script = origin.compile(new ApplicationContext());
		}

		if (printAst) {
			print(script);
			XLogger.newLine();
		}

		VenusExecutor executor = new VenusExecutor();
		long start = millis();

		if (LIGHTWEIGHT_ERRORS) {
			try {
				executor.run(script, ScriptMode.NORMAL);
			}
			catch (ScriptRuntimeException exception) {
				XLogger.warnln("RUNTIME ERR: " + exception.getMessage());

				return;
			}
		}
		else {
			executor.run(script, ScriptMode.NORMAL);
		}

		long duration = millis() - start;

		XLogger.println("Duration: " + duration + "ms");
	}

	private static void print(Component component) {
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