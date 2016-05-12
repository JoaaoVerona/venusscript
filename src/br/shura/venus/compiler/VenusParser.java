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
import br.shura.venus.component.IfContainer;
import br.shura.venus.component.Script;
import br.shura.venus.component.function.Argument;
import br.shura.venus.component.function.Definition;
import br.shura.venus.exception.ScriptCompileException;
import br.shura.venus.exception.UnexpectedTokenException;
import br.shura.venus.library.MethodLibrary;
import br.shura.venus.operator.BinaryOperator;
import br.shura.venus.operator.Operator;
import br.shura.venus.operator.OperatorList;
import br.shura.venus.origin.ScriptOrigin;
import br.shura.venus.resultor.BinaryOperation;
import br.shura.venus.resultor.Constant;
import br.shura.venus.resultor.Resultor;
import br.shura.venus.resultor.Variable;
import br.shura.venus.value.BoolValue;
import br.shura.venus.value.DecimalValue;
import br.shura.venus.value.IntegerValue;
import br.shura.venus.value.StringValue;
import br.shura.venus.value.Value;
import br.shura.venus.value.ValueType;
import br.shura.x.charset.build.TextBuilder;
import br.shura.x.collection.list.List;
import br.shura.x.collection.list.impl.ArrayList;
import br.shura.x.logging.XLogger;
import br.shura.x.util.Pool;
import br.shura.x.worker.ParseWorker;

import java.io.IOException;
import java.util.function.Predicate;

/**
 * VenusParser.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 04:31
 * @since GAMMA - 0x3
 */
public class VenusParser {
  private final VenusLexer lexer;
  private String scriptName;

  public VenusParser(VenusLexer lexer) {
    this.lexer = lexer;
  }

  public void parse(Script script) throws ScriptCompileException {
    script.getChildren().clear();
    script.getContext().getVariables().clear();
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
        else if (token.getValue().equals(KeywordDefinitions.IF)) {
          container = parseIf(container);
        }
        else if (token.getValue().equals(KeywordDefinitions.INCLUDE)) {
          if (container == script) {
            parseInclude(script);
          }
          else {
            bye(token, "cannot use 'import' keyword inside container");
          }
        }
        else if (token.getValue().equals(KeywordDefinitions.USING)) {
          if (container == script) {
            parseUsing(script);
          }
          else {
            bye(token, "cannot use 'using' keyword inside container");
          }
        }
        else { // Should be variable attribution OR function call
          String name = token.getValue();
          Token next = requireToken();

          if (next.getType() == Type.OPERATOR) {
            String attrib = next.getValue();

            if (attrib.equals("=")) {
              Resultor resultor = readResultor(Type.NEW_LINE);
              Attribution attribution = new Attribution(name, resultor);

              container.getChildren().add(attribution);
            }
            else {
              attrib = readOperator(attrib);

              if (attrib.endsWith("=")) {
                String operatorIdentifier = attrib.substring(0, attrib.length() - 1);
                Operator operator = OperatorList.forIdentifier(operatorIdentifier, false); // false for bye(excepted bin opr)

                if (operator != null) {
                  if (operator instanceof BinaryOperator) {
                    Resultor resultor = readResultor(Type.NEW_LINE);
                    BinaryOperation operation = new BinaryOperation((BinaryOperator) operator, new Variable(name), resultor);
                    Attribution attribution = new Attribution(name, operation);

                    container.getChildren().add(attribution);
                  }
                  else {
                    bye(next, "expected an attribution with binary operator (+=, -=, ...)");
                  }
                }
                else {
                  bye(next, "expected a valid attribution operator (=, +=, -=, ...)");
                }
              }
              else {
                bye(next, "expected an attribution operator (=, +=, -=, ...)");
              }
            }
          }
          else if (next.getType() == Type.OPEN_PARENTHESE) {
            Resultor[] arguments = readFunctionArguments();

            requireNewLine();

            FunctionCall functionCall = new FunctionCall(name, arguments);

            container.getChildren().add(functionCall);
          }
          else {
            bye(next, "expected attribution operator or function call");
          }
        }
      }
      else if (token.getType() == Type.CLOSE_BRACE) {
        if (container != script) {
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

  protected Value getValueOf(Token token) throws UnexpectedTokenException {
    String value = token.getValue();

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
      if (ParseWorker.isLong(value)) {
        return new IntegerValue(ParseWorker.toLong(value));
      }

      if (ParseWorker.isDouble(value)) {
        return new DecimalValue(ParseWorker.toDouble(value));
      }

      bye(token, "illegal numeric value \"" + value + "\"");
    }

    return null;
  }

  protected Definition parseDefinition(Container container) throws ScriptCompileException {
    Token typeToken = requireToken(Type.NAME_DEFINITION, "expected a return type");
    String definitionName = typeToken.getValue();
    List<Argument> arguments = new ArrayList<>();

    requireToken(Type.OPEN_PARENTHESE, "expected an open parenthese");

    Token reading;

    while ((reading = requireToken()).getType() != Type.CLOSE_PARENTHESE) { // Reads definition arguments
      if (reading.getType() == Type.NAME_DEFINITION) {
        ValueType argumentType = ValueType.forIdentifier(reading.getValue());

        if (argumentType != null) {
          Token argumentToken = requireToken(Type.NAME_DEFINITION, "expected an argument name");
          String argumentName = argumentToken.getValue();

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

    container.getChildren().add(definition);

    return definition;
  }

  protected void parseExport(Script script) throws ScriptCompileException {
    Token nameToken = requireToken(Type.NAME_DEFINITION, "expected a variable name");
    String variableName = nameToken.getValue();

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

  protected IfContainer parseIf(Container container) throws ScriptCompileException {
    Resultor resultor = readResultor(Type.OPEN_BRACE);
    IfContainer ifContainer = new IfContainer(resultor);

    container.getChildren().add(ifContainer);

    return ifContainer;
  }

  protected void parseInclude(Script script) throws ScriptCompileException {
    Token next = requireToken(Type.STRING_LITERAL, "expected a string literal as including script");
    String includeName = next.getValue();
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

    ScriptOrigin includeOrigin = script.getOrigin().findInclude(includeName);

    if (includeOrigin == null) {
      includeOrigin = ScriptOrigin.defaultInclude(includeName);
    }

    if (includeOrigin != null) {
      VenusLexer lexer = null;

      try {
        lexer = new VenusLexer(includeOrigin);
      }
      catch (IOException exception) {
        bye("Could not read script \"" + includeOrigin.getScriptName() + "\": " + exception.getClass().getSimpleName() +
          ": " + exception.getMessage());
      }

      VenusParser parser = new VenusParser(lexer);
      Script includeScript = new Script(script.getApplicationContext(), includeOrigin);

      parser.parse(includeScript);
      script.getIncludes().add(includeScript);
      XLogger.debugln("Added include script \"" + includeOrigin.getScriptName() + "\".");
    }
    else if (maybe) {
      XLogger.debugln("Not found include script \"" + includeName + "\", but it was marked as maybe.");
    }
    else {
      bye("Could not find script \"" + includeName + "\".");
    }
  }

  protected void parseUsing(Script script) throws ScriptCompileException {
    Token nameToken = requireToken(Type.NAME_DEFINITION, "expected a library name (without quotes)");

    requireNewLine();

    String libraryName = nameToken.getValue();
    MethodLibrary library = ScriptOrigin.defaultLibrary(libraryName);

    if (library != null) {
      script.getLibraryList().add(library);
      XLogger.debugln("Added used lib \"" + library.getClass().getName() + "\"");
    }
    else {
      bye(nameToken, "unknown library named \"" + libraryName + "\"");
    }
  }

  // This also consumes ending' CLOSE_PARENTHESE
  protected Resultor[] readFunctionArguments() throws ScriptCompileException {
    List<Resultor> arguments = new ArrayList<>();
    Token token;

    while ((token = requireToken()).getType() != Type.CLOSE_PARENTHESE) {
      lexer.reRead(token);
      arguments.add(readResultor(
        t -> t.getType() != Type.CLOSE_PARENTHESE && t.getType() != Type.COMMA,
        t -> t.getType() == Type.CLOSE_PARENTHESE));
    }

    return arguments.toArray();
  }

  protected String readOperator(String start) throws ScriptCompileException {
    TextBuilder operatorStr = Pool.newBuilder();
    Token operatorToken;

    if (start != null) {
      operatorStr.append(start);
    }

    while ((operatorToken = requireToken()).getType() == Type.OPERATOR) {
      String op = operatorToken.getValue();

      if (OperatorList.forIdentifier(op, true) != null && !operatorStr.isEmpty()) {
        break;
      }

      operatorStr.append(operatorToken.getValue());
    }

    lexer.reRead(operatorToken); // Last token have type != OPERATOR

    return operatorStr.toStringAndClear();
  }

  protected Resultor readResultor(Predicate<Token> process) throws ScriptCompileException {
    return readResultor(process, token -> false);
  }

  protected Resultor readResultor(Predicate<Token> process, Predicate<Token> reReadLast) throws ScriptCompileException {
    BuildingResultor resultor = new BuildingResultor();
    String nameDef = null;
    Token nameDefToken = null;
    Token token;

    while (process.test(token = requireToken())) {
      if (nameDef == null) {
        Value value;

        try {
          value = getValueOf(token);

          if (value != null) {
            resultor.addResultor(this, token, new Constant(value));

            continue;
          }
        }
        catch (UnexpectedTokenException exception) {
        }
      }

      if (token.getType() == Type.OPERATOR) {
        if (nameDef != null) {
          resultor.addResultor(this, nameDefToken, new Variable(nameDef));
          nameDef = null;
        }

        String operatorStr = readOperator(token.getValue());
        Operator operator = OperatorList.forIdentifier(operatorStr, !resultor.hasResultor());

        if (operator != null) {
          resultor.addOperator(this, token, operator);
        }
        else {
          bye(token, "expected a valid attribution operator (=, +=, -=, ...)");
        }
      }
      else if (token.getType() == Type.OPEN_PARENTHESE) {
        if (nameDef != null) {
          Resultor[] arguments = readFunctionArguments();

          resultor.addResultor(this, nameDefToken, new FunctionCall(nameDef, arguments));
          nameDef = null;
        }
        else {
          resultor.addResultor(this, token, readResultor(Type.CLOSE_PARENTHESE));
        }
      }
      else if (token.getType() == Type.NAME_DEFINITION) {
        nameDef = token.getValue();
        nameDefToken = token;
      }
      else if (nameDef != null) {
        bye(token, "expected open parenthese (function) or operator after a name definition");
      }
      else {
        bye(token, "unexpected token");
      }
    }

    if (nameDef != null) {
      resultor.addResultor(this, nameDefToken, new Variable(nameDef));
    }

    if (reReadLast.test(token)) {
      lexer.reRead(token);
    }

    Resultor result = resultor.build();

    if (result == null) {
      bye(token, "expected a resultor/value");
    }

    return result;
  }

  protected Resultor readResultor(Type stopAt) throws ScriptCompileException {
    return readResultor(stopAt, token -> false);
  }

  protected Resultor readResultor(Type stopAt, Predicate<Token> reReadLast) throws ScriptCompileException {
    return readResultor(token -> token.getType() != stopAt, reReadLast);
  }

  protected Value readValue() throws ScriptCompileException {
    Token token = requireToken();
    Value value = getValueOf(token);

    if (value == null) {
      bye(token, "expected a value literal (boolean/char/number/string)");
    }

    return value;
  }

  protected void requireNewLine() throws ScriptCompileException {
    Token token = requireToken();

    if (token.getType() != Type.NEW_LINE) {
      bye(token, "expected a new line");
    }
  }

  protected Token requireToken() throws ScriptCompileException {
    Token token = lexer.nextToken();

    if (token == null) {
      bye("Unexpected end of file");
    }

    return token;
  }

  protected Token requireToken(Type type, String errorMessage) throws ScriptCompileException {
    Token token = requireToken();

    if (token.getType() != type) {
      bye(token, errorMessage);
    }

    return token;
  }
}