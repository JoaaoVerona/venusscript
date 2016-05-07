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
import br.shura.venus.compiler.Token.Type;
import br.shura.venus.exception.UnexpectedInputException;
import br.shura.venus.origin.ScriptOrigin;
import br.shura.venus.origin.SimpleScriptOrigin;
import br.shura.x.logging.XLogger;
import br.shura.x.util.comparator.SimpleEqualizer;
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
      "j = (i + 1024)",
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

  @Test
  public void simpleAssertion() throws IOException, UnexpectedInputException {
    String[] script = {
      "i = 0",
      "j = (i + 1)",
      "while (true) {",
      "  print(i + j)",
      "}"
    };
    ScriptOrigin origin = new SimpleScriptOrigin("test.xs", StringWorker.join('\n', script));
    ScriptLexer lexer = new ScriptLexer(origin);

    assertToken(lexer, Type.NAME_DEFINITION, "i");
    assertToken(lexer, Type.OPERATOR, "=");
    assertToken(lexer, Type.NUMBER_LITERAL, "0");
    assertToken(lexer, Type.NEW_LINE, null);
    assertToken(lexer, Type.NAME_DEFINITION, "j");
    assertToken(lexer, Type.OPERATOR, "=");
    assertToken(lexer, Type.OPEN_PARENTHESE, '(');
    assertToken(lexer, Type.NAME_DEFINITION, "i");
    assertToken(lexer, Type.OPERATOR, "+");
    assertToken(lexer, Type.NUMBER_LITERAL, "1");
    assertToken(lexer, Type.CLOSE_PARENTHESE, ')');
    assertToken(lexer, Type.NEW_LINE, null);
    assertToken(lexer, Type.NAME_DEFINITION, "while");
    assertToken(lexer, Type.OPEN_PARENTHESE, '(');
    assertToken(lexer, Type.NAME_DEFINITION, "true");
    assertToken(lexer, Type.CLOSE_PARENTHESE, ')');
    assertToken(lexer, Type.OPEN_BRACE, '{');
    assertToken(lexer, Type.NEW_LINE, null);
    assertToken(lexer, Type.NAME_DEFINITION, "print");
    assertToken(lexer, Type.OPEN_PARENTHESE, '(');
    assertToken(lexer, Type.NAME_DEFINITION, "i");
    assertToken(lexer, Type.OPERATOR, "+");
    assertToken(lexer, Type.NAME_DEFINITION, "j");
    assertToken(lexer, Type.CLOSE_PARENTHESE, ')');
    assertToken(lexer, Type.NEW_LINE, null);
    assertToken(lexer, Type.CLOSE_BRACE, '}');
    Token token;

    while ((token = lexer.nextToken()) != null) {
      XLogger.println("[" + token.getType() + "] " + token.getValue());
    }
  }

  private void assertToken(ScriptLexer lexer, Type type, Object content) throws AssertionError, UnexpectedInputException {
    Token token = lexer.nextToken();

    if (token == null) {
      throw new AssertionError("Expected token, but none found");
    }

    if (token.getType() != type || !SimpleEqualizer.compare(token.getValue(), content)) {
      throw new AssertionError("Expected [" + type + ", " + content + "], received [" + token.getType() + ", " +
        token.getValue() + ']');
    }
  }
}