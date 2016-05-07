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

package br.shura.venus.compiler;

import br.shura.venus.Script;
import br.shura.venus.compiler.Token.Type;
import br.shura.venus.component.Container;
import br.shura.venus.exception.ScriptCompileException;
import br.shura.venus.exception.UnexpectedInputException;
import br.shura.venus.exception.UnexpectedTokenException;
import br.shura.venus.value.ValueType;

import java.io.IOException;

/**
 * ScriptParser.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 04:31
 * @since GAMMA - 0x3
 */
public class ScriptParser {
  private final ScriptLexer lexer;

  public ScriptParser(ScriptLexer lexer) {
    this.lexer = lexer;
  }

  public void parse(Script script) throws IOException, ScriptCompileException {
    script.getChildren().clear();
    script.getContext().getVariables().clear();
    script.getDefinitions().clear();
    script.getIncludes().clear();

    String[] nameDefsUnscoped = { "def", "export", "include" };
    String scriptName = script.getDisplayName();

    Token token;
    Container container = null;

    while ((token = lexer.nextToken()) != null) {
      int currentLine = lexer.currentLine();

      if (token.getType() == Type.NAME_DEFINITION) {
        if (token.getValue().equals(nameDefsUnscoped[0])) {
          Token typeToken = requireToken(scriptName, currentLine);

          if (typeToken.getType() == Type.NAME_DEFINITION) {
            ValueType type = ValueType.forIdentifier((String) typeToken.getValue());

            if (type != null) {
              // TODO New definition start (already got return type)
            }
            else {
              throw new UnexpectedTokenException(scriptName, currentLine, "Unrecognized function return type \"" +
                token.getValue() + "\"");
            }
          }
          else {
            throw new UnexpectedTokenException(scriptName, currentLine, "Invalid token \"" + token + "\"; expected a return type");
          }
        }
        else if (container == null) {
          if (token.getValue().equals(nameDefsUnscoped[1])) {
            // TODO Exporting var
          }
          else if (token.getValue().equals(nameDefsUnscoped[2])) {
            Token next = requireToken(scriptName, currentLine);

            if (next.getType() == Type.STRING_LITERAL) {
              // TODO Found include script...
            }
            else {
              throw new UnexpectedTokenException(scriptName, currentLine, "Invalid token \"" + next + "\"; expected a string literal");
            }
          }
          else {
            throw new UnexpectedTokenException(scriptName, currentLine, "Invalid keyword \"" + token.getValue() +
              "\"; expected 'define', 'export' or 'include']");
          }
        }
        else {
          throw new UnexpectedTokenException(scriptName, currentLine, "Unexpected token \"" + token + "\"");
        }
      }
      else {
        throw new UnexpectedTokenException(scriptName, currentLine, "Invalid token \"" + token + "\"; expected a function, keyword or variable");
      }
    }
  }

  protected Token requireToken(String scriptName, int currentLine) throws IOException, UnexpectedInputException, UnexpectedTokenException {
    Token token = lexer.nextToken();

    if (token == null) {
      throw new UnexpectedTokenException(scriptName, currentLine, "Unexpected end of file");
    }

    return token;
  }
}