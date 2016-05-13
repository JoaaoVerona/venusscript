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

import br.shura.venus.compiler.VenusLexer;
import br.shura.venus.compiler.VenusParser;
import br.shura.venus.component.Script;
import br.shura.venus.executor.ApplicationContext;
import br.shura.venus.executor.VenusExecutor;
import br.shura.venus.origin.FileScriptOrigin;
import br.shura.venus.origin.ScriptOrigin;
import br.shura.x.collection.list.List;
import br.shura.x.collection.list.impl.ArrayList;
import br.shura.x.collection.view.View;
import br.shura.x.io.file.File;
import br.shura.x.io.file.Folder;
import br.shura.x.logging.ILogger.Level;
import br.shura.x.logging.XLogger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.util.Collection;

/**
 * ExecutorTest.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 11/05/16 - 00:14
 * @since GAMMA - 0x3
 */
@RunWith(Parameterized.class)
public class ExecutorTest {
  public static final Folder DIRECTORY = new Folder("VenusScript/resources");
  private final File file;

  public ExecutorTest(File file) {
    this.file = file;
  }

  @Test
  public void simpleTest() throws Exception {
    XLogger.disable(Level.DEBUG);

    ScriptOrigin origin = new FileScriptOrigin(file);
    VenusLexer lexer = new VenusLexer(origin);
    VenusParser parser = new VenusParser(lexer);
    Script script = new Script(new ApplicationContext(), origin);

    parser.parse(script);
    VenusExecutor.run(script);
  }

  @Parameters
  public static Collection<Object[]> data() throws IOException {
    List<Object[]> data = new ArrayList<>();

    DIRECTORY.getFiles(file -> data.add(new Object[] { file }));

    return data.asCollection(java.util.ArrayList.class);
  }

  public static void main(String[] args) throws Exception {
    View<File> files = DIRECTORY.getAllFilesView();
    int i = 0;

    for (File file : files) {
      XLogger.println(i++ + ". " + file.getRelativePath(DIRECTORY));
    }

    XLogger.print("> ");

    int option = XLogger.scanInt();

    XLogger.scan();
    new ExecutorTest(files.at(option)).simpleTest();
  }
}