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

package com.github.bloodshura.venus.test;

import com.github.bloodshura.venus.component.Component;
import com.github.bloodshura.venus.component.Container;
import com.github.bloodshura.venus.component.Script;
import com.github.bloodshura.venus.exception.compile.ScriptCompileException;
import com.github.bloodshura.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.venus.executor.ApplicationContext;
import com.github.bloodshura.venus.executor.VenusExecutor;
import com.github.bloodshura.venus.origin.FileScriptOrigin;
import com.github.bloodshura.venus.origin.ScriptMode;
import com.github.bloodshura.venus.origin.ScriptOrigin;
import com.github.bloodshura.x.collection.view.XView;
import com.github.bloodshura.x.io.file.File;
import com.github.bloodshura.x.io.file.Folder;
import com.github.bloodshura.x.logging.XLogger;
import com.github.bloodshura.x.worker.ParseWorker;

import static com.github.bloodshura.x.sys.XSystem.*;

/**
 * InteractiveTester.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 13/05/16 - 21:57
 * @since GAMMA - 0x3
 */
public class InteractiveTester {
  public static final Folder DIRECTORY = new Folder("VenusScript/examples");
  public static final boolean LIGHTWEIGHT_ERRORS = false;

  public static void main(String[] args) throws Exception {
    XView<File> files = DIRECTORY.getAllFilesView();
    int i = 0;

    for (File file : files) {
      XLogger.println(i++ + ". " + file.getRelativePath(DIRECTORY));
    }

    XLogger.print("> ");

    int option = -1;
    boolean printAst = false;

    while (option < 0 || option >= files.size()) {
      String optionStr = XLogger.scan();

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