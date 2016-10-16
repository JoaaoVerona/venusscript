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
import com.github.bloodshura.venus.executor.ApplicationContext;
import com.github.bloodshura.venus.origin.ScriptOrigin;
import com.github.bloodshura.venus.origin.SimpleScriptOrigin;
import com.github.bloodshura.x.logging.Logger.Level;
import com.github.bloodshura.x.logging.XLogger;
import com.github.bloodshura.x.worker.StringWorker;
import org.junit.Test;

import java.io.IOException;

/**
 * ParserTest.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 07/05/16 - 18:34
 * @since GAMMA - 0x3
 */
public class ParserTest {
  @Test
  public void simplePrint() throws IOException, ScriptCompileException {
    XLogger.disable(Level.DEBUG);

    String[] content = {
      "export MY_VAR = 0",
      "export MY_STRING = \"oi\"",
      "def printMyName(string name) {",
      "  print(\"Hello, I'm \" + name + \"!\")",
      "  print(1 + 3 - (5 + 2))",
      "  j = (i + 1) * k",
      "}"
    };
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