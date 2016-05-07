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

import br.shura.venus.compiler.ScriptLexer;
import br.shura.venus.compiler.Token;
import br.shura.venus.exception.UnexpectedInputException;
import br.shura.venus.origin.ScriptOrigin;
import br.shura.venus.origin.SimpleScriptOrigin;
import br.shura.x.logging.XLogger;
import br.shura.x.worker.StringWorker;
import org.junit.Test;

import java.io.IOException;

/**
 * LexerTest.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 07/05/16 - 18:33
 * @since GAMMA - 0x3
 */
public class LexerTest {
  @Test
  public void simplePrint() throws IOException, UnexpectedInputException {
    String[] script = {
      "i = 0",
      "j = 1024",
      "while (true) {",
      "  print(i + j)",
      "  i = i + 1",
      "  j = j - 1",
      "  i++",
      "  ++j",
      "  setvar(502.55)",
      "}"
    };
    ScriptOrigin origin = new SimpleScriptOrigin("test.xs", StringWorker.join('\n', script));
    ScriptLexer lexer = new ScriptLexer(origin);
    Token token;

    while ((token = lexer.nextToken()) != null) {
      XLogger.println("[" + token.getType() + "] " + token.getValue());
    }
  }
}