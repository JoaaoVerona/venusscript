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
import br.shura.venus.origin.ScriptOrigin;
import br.shura.venus.origin.SimpleScriptOrigin;
import br.shura.x.logging.ILogger.Level;
import br.shura.x.logging.XLogger;
import br.shura.x.worker.StringWorker;
import org.junit.Test;

/**
 * ExecutorTest.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 11/05/16 - 00:14
 * @since GAMMA - 0x3
 */
public class ExecutorTest {
  @Test
  public void simpleTest() throws Exception {
    XLogger.disable(Level.DEBUG);

    String[] content = {
      "using std",
      "def printMyName(string name) {",
      "  print(\"Hello, I'm \" + name + \"!\")",
      "  print(1 + 3 - (5 + 2))",
      "}",
      "i = 0",
      "print(\"Oi, i = \" + i + \".\")",
      "printMyName(\"João Vitor\")",
    };
    ScriptOrigin origin = new SimpleScriptOrigin("test.xs", StringWorker.join('\n', content));
    VenusLexer lexer = new VenusLexer(origin);
    VenusParser parser = new VenusParser(lexer);
    Script script = new Script(new ApplicationContext(), origin);

    parser.parse(script);
    VenusExecutor.run(script);
  }
}