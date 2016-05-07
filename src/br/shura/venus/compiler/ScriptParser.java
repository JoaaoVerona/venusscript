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

  public void parse(Script script) throws ScriptCompileException {
    script.getChildren().clear();
    script.getContext().getVariables().clear();
    script.getDefinitions().clear();
    script.getIncludes().clear();

    this.scriptName = script.getDisplayName();

    Token token;
    Container container = script;

    while ((token = lexer.nextToken()) != null) {
      if (token.getType() == Type.NAME_DEFINITION) {
        if (token.getValue().equals(KeywordDefinitions.DEFINE)) {
          Token typeToken = requireToken(Type.NAME_DEFINITION, "expected a return type");
          String definitionName = (String) typeToken.getValue();
          List<Argument> arguments = new ArrayList<>();

          requireToken(Type.OPEN_PARENTHESE, "expected an open parenthese");

          Token reading;

          while ((reading = requireToken()).getType() != Type.CLOSE_PARENTHESE) {
            if (reading.getType() == Type.NAME_DEFINITION) {
              ValueType argumentType = ValueType.forIdentifier((String) reading.getValue());

              if (argumentType != null) {
                Token argumentToken = requireToken(Type.NAME_DEFINITION, "expected an argument name");
                String argumentName = (String) argumentToken.getValue();

                if (!KeywordDefinitions.isKeyword(argumentName)) {
                  arguments.add(new Argument(argumentName, argumentType));

                  Token commaOrClose = requireToken();

                  if (commaOrClose.getType() == Type.CLOSE_PARENTHESE) {
                    break;
                  }

                  if (commaOrClose.getType() != Type.COMMA) {
                    bye(commaOrClose, "expected an argument separator (comma) or close parenthese");
                  }
                }
                else {
                  bye(reading, "argument name cannot be a keyword");
                }
              }
              else {
                bye(reading, "expected an argument type (" +
                  new ArrayList<>(ValueType.values()).toString(", ") + ')');
              }
            }
            else {
              bye(reading, "expected an argument name");
            }
          }

          requireToken(Type.OPEN_BRACE, "expected an open brace");

          Definition definition = new Definition(definitionName, arguments);

          container.getDefinitions().add(definition);
          XLogger.debugln("Added definition::" + definitionName + ", " + arguments);
          container = definition;
        }
        else if (container == null) {
          if (token.getValue().equals(KeywordDefinitions.EXPORT)) {
            Token nameToken = requireToken(Type.NAME_DEFINITION, "expected a variable name");
            String variableName = (String) nameToken.getValue();

            if (!KeywordDefinitions.isKeyword(variableName)) {
              Token attributionToken = requireToken();

              if (attributionToken.getType() == Type.OPERATOR && attributionToken.getValue().equals("=")) {
                Value value = readValue();

                script.getApplicationContext().setVar(variableName, value);
                XLogger.debugln("Added exported var::" + variableName + ", val::" + value);
                requireNewLine();
              }
              else {
                bye(attributionToken, "expected an attribution character '='");
              }
            }
            else {
              bye(nameToken, "variable name cannot be a keyword");
            }
          }
          else if (token.getValue().equals(KeywordDefinitions.INCLUDE)) {
            Token next = requireToken(Type.STRING_LITERAL, "expected a string literal as including script");

            // TODO Found include script...
          }
          else {
            bye("Invalid keyword \"" + token.getValue() + "\"; expected '" + KeywordDefinitions.DEFINE + "', '" +
              KeywordDefinitions.EXPORT + "' or '" + KeywordDefinitions.INCLUDE + "'");
          }
        }
        else { // TODO Container != null, then probably is variable attribution
          bye("Unexpected token \"" + token + "\"");
        }
      }
      else if (token.getType() == Type.CLOSE_BRACE) {
        if (container != script) {
          XLogger.debugln("Found close brace, changing container from " + container + " to " + container.getParent());
          container = container.getParent();
        }
        else {
          bye(token, "no container to close");
        }
      }
      else {
        bye(token, "expected a name definition or close brace");
      }
    }
  }

  protected void bye(String message) throws UnexpectedTokenException {
    throw new UnexpectedTokenException(scriptName, lexer.currentLine(), message);
  }

  protected void bye(Token token, String message) throws UnexpectedTokenException {
    bye("Invalid token \"" + token + "\"; " + message);
  }

  protected Value readValue() throws UnexpectedInputException, UnexpectedTokenException {
    return null; // TODO
  }

  protected void requireNewLine() throws UnexpectedInputException, UnexpectedTokenException {
    Token token = lexer.nextToken();

    if (token != null && token.getType() != Type.NEW_LINE) {
      bye("Expected a new line");
    }
  }

  protected Token requireToken() throws UnexpectedInputException, UnexpectedTokenException {
    Token token = lexer.nextToken();

    if (token == null) {
      bye("Unexpected end of file");
    }

    return token;
  }

  protected Token requireToken(Type type, String errorMessage) throws UnexpectedInputException, UnexpectedTokenException {
    Token token = requireToken();

    if (token.getType() != type) {
      bye(token, errorMessage);
    }

    return token;
  }
}