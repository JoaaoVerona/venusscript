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
import br.shura.venus.component.function.Argument;
import br.shura.venus.component.function.Definition;
import br.shura.venus.exception.ScriptCompileException;
import br.shura.venus.exception.UnexpectedInputException;
import br.shura.venus.exception.UnexpectedTokenException;
import br.shura.venus.value.Value;
import br.shura.venus.value.ValueType;
import br.shura.x.collection.list.List;
import br.shura.x.collection.list.impl.ArrayList;
import br.shura.x.logging.XLogger;

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
  private String scriptName;

  public ScriptParser(ScriptLexer lexer) {
    this.lexer = lexer;
  }

  public void parse(Script script) throws IOException, ScriptCompileException {
    script.getChildren().clear();
    script.getContext().getVariables().clear();
    script.getDefinitions().clear();
    script.getIncludes().clear();

    String[] nameDefsUnscoped = { "def", "export", "include" };

    this.scriptName = script.getDisplayName();

    Token token;
    Container container = script;

    while ((token = lexer.nextToken()) != null) {
      if (token.getType() == Type.NAME_DEFINITION) {
        if (token.getValue().equals(nameDefsUnscoped[0])) {
          Token typeToken = requireToken();

          if (typeToken.getType() == Type.NAME_DEFINITION) {
            String definitionName = (String) typeToken.getValue();
            List<Argument> arguments = new ArrayList<>();
            Token openParentheseToken = requireToken();

            if (openParentheseToken.getType() == Type.OPEN_PARENTHESE) {
              Token reading;

              while ((reading = requireToken()).getType() != Type.CLOSE_PARENTHESE) {
                if (reading.getType() == Type.NAME_DEFINITION) {
                  ValueType argumentType = ValueType.forIdentifier((String) reading.getValue());

                  if (argumentType != null) {
                    Token argumentToken = requireToken();
                    String argumentName = (String) argumentToken.getValue();

                    if (!KeywordDefinitions.isKeyword(argumentName)) {
                      arguments.add(new Argument(argumentName, argumentType));

                      Token commaOrClose = requireToken();

                      if (commaOrClose.getType() == Type.CLOSE_PARENTHESE) {
                        break;
                      }

                      if (commaOrClose.getType() != Type.COMMA) {
                        bye("Invalid token \"" + commaOrClose + "\"; expected an argument separator (comma) or close parenthese");
                      }
                    }
                    else {
                      bye("Invalid token \"" + reading + "\"; argument name cannot be a keyword");
                    }
                  }
                  else {
                    bye("Invalid token \"" + reading + "\"; expected an argument type (" +
                      new ArrayList<>(ValueType.values()).toString(", ") + ')');
                  }
                }
                else {
                  bye("Invalid token \"" + reading + "\"; expected an argument name");
                }
              }

              Token openBraceToken = requireToken();

              if (openBraceToken.getType() == Type.OPEN_BRACE) {
                Definition definition = new Definition(definitionName, arguments);

                container.getDefinitions().add(definition);
                XLogger.debugln("Added definition::" + definitionName + ", " + arguments);
                container = definition;
              }
              else {
                bye("Invalid token \"" + openBraceToken + "\"; expected an open brace");
              }
            }
            else {
              bye("Invalid token \"" + openParentheseToken + "\"; expected an open parenthese");
            }
          }
          else {
            bye("Invalid token \"" + token + "\"; expected a return type");
          }
        }
        else if (container == null) {
          if (token.getValue().equals(nameDefsUnscoped[1])) {
            // TODO Exporting var
          }
          else if (token.getValue().equals(nameDefsUnscoped[2])) {
            Token next = requireToken();

            if (next.getType() == Type.STRING_LITERAL) {
              // TODO Found include script...
            }
            else {
              bye("Invalid token \"" + next + "\"; expected a string literal");
            }
          }
          else {
            bye("Invalid keyword \"" + token.getValue() + "\"; expected 'define', 'export' or 'include']");
          }
        }
        else {
          bye("Unexpected token \"" + token + "\"");
        }
      }
      else {
        bye("Invalid token \"" + token + "\"; expected a function, keyword or variable");
      }
    }
  }

  protected Value readValue() throws IOException, UnexpectedInputException, UnexpectedTokenException {
    return null; // TODO
  }

  protected void bye(String message) throws UnexpectedTokenException {
    throw new UnexpectedTokenException(scriptName, lexer.currentLine(), message);
  }

  protected Token requireToken() throws IOException, UnexpectedInputException, UnexpectedTokenException {
    Token token = lexer.nextToken();

    if (token == null) {
      throw new UnexpectedTokenException(scriptName, lexer.currentLine(), "Unexpected end of file");
    }

    return token;
  }
}