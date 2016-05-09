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

import br.shura.venus.compiler.Token.Type;
import br.shura.venus.component.Attribution;
import br.shura.venus.component.Container;
import br.shura.venus.component.FunctionCall;
import br.shura.venus.component.Script;
import br.shura.venus.component.function.Argument;
import br.shura.venus.component.function.Definition;
import br.shura.venus.exception.ScriptCompileException;
import br.shura.venus.exception.UnexpectedInputException;
import br.shura.venus.exception.UnexpectedTokenException;
import br.shura.venus.operator.Operator;
import br.shura.venus.operator.OperatorList;
import br.shura.venus.origin.ScriptOrigin;
import br.shura.venus.resultor.Operation;
import br.shura.venus.resultor.Resultor;
import br.shura.venus.resultor.Variable;
import br.shura.venus.value.BoolValue;
import br.shura.venus.value.DecimalValue;
import br.shura.venus.value.IntegerValue;
import br.shura.venus.value.StringValue;
import br.shura.venus.value.Value;
import br.shura.venus.value.ValueType;
import br.shura.x.collection.list.List;
import br.shura.x.collection.list.impl.ArrayList;
import br.shura.x.logging.XLogger;
import br.shura.x.worker.ParseWorker;

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
          container = parseDefinition(container);
        }
        else if (token.getValue().equals(KeywordDefinitions.EXPORT)) {
          if (container == script) {
            parseExport(script);
          }
          else {
            bye(token, "cannot use 'export' keyword inside container");
          }
        }
        else if (token.getValue().equals(KeywordDefinitions.INCLUDE)) {
          if (container == script) {
            parseInclude(script);
          }
          else {
            bye(token, "cannot use 'import' keyword inside container");
          }
        }
        else { // Should be variable attribution OR function call
          String name = (String) token.getValue();
          Token next = requireToken();

          if (next.getType() == Type.OPERATOR) {
            String attrib = (String) next.getValue();

            if (attrib.equals("=")) {
              Resultor resultor = readResultor(Type.NEW_LINE);
              Attribution attribution = new Attribution(name, resultor);

              container.getChildren().add(attribution);
              XLogger.debugln("Added attribution " + attribution);
            }
            else if (attrib.endsWith("=")) {
              String operatorIdentifier = attrib.substring(0, attrib.length() - 1);
              Operator operator = OperatorList.forIdentifier(operatorIdentifier);

              if (operator != null) {
                Resultor resultor = readResultor(Type.NEW_LINE);
                Operation operation = new Operation(operator, new Variable(name), resultor);
                Attribution attribution = new Attribution(name, operation);

                container.getChildren().add(attribution);
                XLogger.debugln("Added op_attribution " + attribution);
              }
              else {
                bye(next, "expected a valid attribution operator (=, +=, -=, ...)");
              }
            }
            else {
              bye(next, "expected an attribution operator (=, +=, -=, ...)");
            }
          }
          else if (next.getType() == Type.OPEN_PARENTHESE) {
            Resultor[] arguments = readFunctionArguments();

            requireToken(Type.CLOSE_PARENTHESE, "expected a close parenthese");
            requireNewLine();

            FunctionCall functionCall = new FunctionCall(name, arguments);

            container.getChildren().add(functionCall);
            XLogger.debugln("Added function call: " + functionCall);
          }
          else {
            bye(next, "expected attribution operator or function call");
          }
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
      else if (token.getType() != Type.NEW_LINE) {
        bye(token, "expected a name definition or close brace");
      }
    }
  }

  protected void bye(String message) throws UnexpectedTokenException {
    throw new UnexpectedTokenException(scriptName, lexer.currentLine(), message);
  }

  // Do not call other bye() method, for better stacktrace
  protected void bye(Token token, String message) throws UnexpectedTokenException {
    throw new UnexpectedTokenException(scriptName, lexer.currentLine(), "Invalid token \"" + token + "\"; " + message);
  }

  protected Definition parseDefinition(Container container) throws ScriptCompileException {
    Token typeToken = requireToken(Type.NAME_DEFINITION, "expected a return type");
    String definitionName = (String) typeToken.getValue();
    List<Argument> arguments = new ArrayList<>();

    requireToken(Type.OPEN_PARENTHESE, "expected an open parenthese");

    Token reading;

    while ((reading = requireToken()).getType() != Type.CLOSE_PARENTHESE) { // Reads definition arguments
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
          bye(reading, "expected an argument type (" + new ArrayList<>(ValueType.values()).toString(", ") + ')');
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

    return definition;
  }

  protected void parseExport(Script script) throws ScriptCompileException {
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

  protected void parseInclude(Script script) throws ScriptCompileException {
    Token next = requireToken(Type.STRING_LITERAL, "expected a string literal as including script");
    String includePath = (String) next.getValue();
    boolean maybe = false;
    Token maybeOrNewLine = requireToken();

    if (maybeOrNewLine.getType() == Type.NAME_DEFINITION) {
      if (maybeOrNewLine.getValue().equals("maybe")) {
        maybe = true;
        requireToken(Type.NEW_LINE, "expected new line");
      }
      else {
        bye(maybeOrNewLine, "expected 'maybe' or new line");
      }
    }
    else if (maybeOrNewLine.getType() != Type.NEW_LINE) {
      bye(maybeOrNewLine, "expected 'maybe' or new line");
    }

    ScriptOrigin includeOrigin = script.getOrigin().findInclude(includePath);

    if (includeOrigin != null) {
      ScriptLexer lexer = null;

      try {
        lexer = new ScriptLexer(includeOrigin);
      }
      catch (IOException exception) {
        bye("Could not read script \"" + includeOrigin.getScriptName() + "\": " + exception.getClass().getSimpleName() +
          ": " + exception.getMessage());
      }

      ScriptParser parser = new ScriptParser(lexer);
      Script includeScript = new Script(script.getApplicationContext(), includeOrigin);

      parser.parse(includeScript);
      script.getIncludes().add(includeScript);
      XLogger.debugln("Added include script \"" + includeOrigin.getScriptName() + "\".");
    }
    else if (maybe) {
      XLogger.debugln("Not found include script \"" + includePath + "\", but it was marked as maybe.");
    }
    else {
      bye("Could not find script \"" + includePath + "\".");
    }
  }

  protected Resultor[] readFunctionArguments() throws UnexpectedInputException, UnexpectedTokenException {
    List<Resultor> arguments = new ArrayList<>();
    Token token;

    while ((token = requireToken()).getType() != Type.CLOSE_PARENTHESE) {
      arguments.add(readResultor(Type.COMMA));
    }

    lexer.reRead(token);

    return arguments.toArray();
  }

  protected Resultor readResultor(Type stopAt) throws UnexpectedInputException, UnexpectedTokenException {
    return null; // TODO Here is where the "big shit" will happen
  }

  protected Value readValue() throws UnexpectedInputException, UnexpectedTokenException {
    Token token = requireToken();
    String value = (String) token.getValue();

    if (token.getType() == Type.CHAR_LITERAL || token.getType() == Type.STRING_LITERAL) {
      return new StringValue(value);
    }

    if (token.getType() == Type.NAME_DEFINITION) {
      if (value.equals(KeywordDefinitions.TRUE)) {
        return new BoolValue(true);
      }

      if (value.equals(KeywordDefinitions.FALSE)) {
        return new BoolValue(false);
      }
    }

    if (token.getType() == Type.NUMBER_LITERAL) {
      if (ParseWorker.isDouble(value)) {
        return new DecimalValue(ParseWorker.toDouble(value));
      }

      if (ParseWorker.isLong(value)) {
        return new IntegerValue(ParseWorker.toLong(value));
      }

      bye(token, "illegal numeric value \"" + value + "\"");
    }

    bye(token, "expected a value literal (boolean/char/number/string)");

    return null; // Will not happen since bye() will always throw an exception, but fck-u compiler
  }

  protected void requireNewLine() throws UnexpectedInputException, UnexpectedTokenException {
    Token token = requireToken();

    if (token.getType() != Type.NEW_LINE) {
      bye(token, "expected a new line");
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