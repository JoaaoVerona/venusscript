//////////////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2016, João Vitor Verona Biazibetti - All Rights Reserved                /
//                                                                                       /
// Licensed under the GNU General Public License v3;                                     /
// you may not use this file except in compliance with the License.                      /
//                                                                                       /
// You may obtain a copy of the License at                                               /
//     http://www.gnu.org/licenses/gpl.html                                              /
//                                                                                       /
// Unless required by applicable law or agreed to in writing, software                   /
// distributed under the License is distributed on an "AS IS" BASIS,                     /
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.              /
// See the License for the specific language governing permissions and                   /
// limitations under the License.                                                        /
//                                                                                       /
// Written by João Vitor Verona Biazibetti <joaaoverona@gmail.com>, March 2016           /
// https://www.github.com/BloodShura                                                     /
//////////////////////////////////////////////////////////////////////////////////////////

package br.shura.venus.test;

import br.shura.venus.component.Component;
import br.shura.venus.component.Container;
import br.shura.venus.component.Script;
import br.shura.venus.exception.ScriptCompileException;
import br.shura.venus.exception.ScriptRuntimeException;
import br.shura.venus.executor.ApplicationContext;
import br.shura.venus.executor.VenusExecutor;
import br.shura.venus.origin.FileScriptOrigin;
import br.shura.venus.origin.ScriptOrigin;
import br.shura.x.collection.view.View;
import br.shura.x.io.file.File;
import br.shura.x.io.file.Folder;
import br.shura.x.logging.XLogger;
import br.shura.x.worker.ParseWorker;

import static br.shura.x.sys.XSystem.millis;

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
    View<File> files = DIRECTORY.getAllFilesView();
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

    File file = files.at(option);
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
        executor.run(script);
      }
      catch (ScriptRuntimeException exception) {
        XLogger.warnln("RUNTIME ERR: " + exception.getMessage());

        return;
      }
    }
    else {
      executor.run(script);
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