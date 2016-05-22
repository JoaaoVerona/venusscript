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
import br.shura.venus.component.ArrayAttribution;
import br.shura.venus.component.AsyncContainer;
import br.shura.venus.component.Attribution;
import br.shura.venus.component.Component;
import br.shura.venus.component.Container;
import br.shura.venus.component.FunctionCall;
import br.shura.venus.component.Script;
import br.shura.venus.component.SimpleContainer;
import br.shura.venus.component.branch.Break;
import br.shura.venus.component.branch.Breakable;
import br.shura.venus.component.branch.Continue;
import br.shura.venus.component.branch.DoWhileContainer;
import br.shura.venus.component.branch.ElseContainer;
import br.shura.venus.component.branch.ElseIfContainer;
import br.shura.venus.component.branch.ForRangeContainer;
import br.shura.venus.component.branch.IfContainer;
import br.shura.venus.component.branch.Return;
import br.shura.venus.component.branch.WhileContainer;
import br.shura.venus.exception.compile.ScriptCompileException;
import br.shura.venus.exception.compile.UnexpectedTokenException;
import br.shura.venus.function.Argument;
import br.shura.venus.function.Definition;
import br.shura.venus.library.VenusLibrary;
import br.shura.venus.operator.BinaryOperator;
import br.shura.venus.operator.Operator;
import br.shura.venus.operator.OperatorList;
import br.shura.venus.resultor.ArrayAccess;
import br.shura.venus.resultor.ArrayDefine;
import br.shura.venus.resultor.BinaryOperation;
import br.shura.venus.resultor.Constant;
import br.shura.venus.resultor.FunctionRef;
import br.shura.venus.resultor.Resultor;
import br.shura.venus.resultor.Variable;
import br.shura.venus.value.BoolValue;
import br.shura.venus.value.DecimalValue;
import br.shura.venus.value.IntegerValue;
import br.shura.venus.value.StringValue;
import br.shura.venus.value.TypeValue;
import br.shura.venus.value.Value;
import br.shura.venus.value.ValueType;
import br.shura.venus.value.VariableRefValue;
import br.shura.x.charset.build.TextBuilder;
import br.shura.x.collection.list.List;
import br.shura.x.collection.list.impl.ArrayList;
import br.shura.x.math.number.BaseConverter;
import br.shura.x.util.Pool;
import br.shura.x.worker.ParseWorker;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * VenusParser.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 04:31
 * @since GAMMA - 0x3
 */
public class VenusParser {
  private Container container;
  private VenusLexer lexer;
  private boolean nextAsyncable;
  private boolean nextDaemon;
  private final Script script;

  public VenusParser(Script script) {
    this.script = script;
  }

  public void parse(VenusLexer lexer, Container c) throws ScriptCompileException {
    this.container = c;
    this.lexer = lexer;

    Token token;
    boolean justExitedIfContainer = false;

    while ((token = lexer.nextToken()) != null) {
      if (token.getType() == Type.DOLLAR_SIGN) {
        Token next = requireToken(Type.NAME_DEFINITION, "expected a variable name");

        parseAttributionOrFunctionCall('$' + next.getValue(), false);
      }
      else if (token.getType() == Type.NAME_DEFINITION) {
        if (token.getValue().equals(KeywordDefinitions.ASYNC)) {
          if (nextAsyncable) {
            bye(token, "duplicated 'async' keyword");
          }

          this.nextAsyncable = true;
        }
        else if (token.getValue().equals(KeywordDefinitions.BREAK) || token.getValue().equals(KeywordDefinitions.CONTINUE)) {
          requireToken(Type.NEW_LINE, "expected a new line");

          Container lookup = container;
          boolean foundContinuable = false;

          while (lookup != null) {
            if (lookup instanceof Breakable) {
              foundContinuable = true;

              break;
            }

            // If there is a definition, at run-time it will be another context,
            // so do not let lookuping parents
            if (lookup instanceof Definition) {
              break;
            }

            lookup = lookup.getParent();
          }

          if (foundContinuable) {
            addComponent(token.getValue().equals(KeywordDefinitions.BREAK) ? new Break() : new Continue(), false);
          }
          else {
            bye(token, "there is no parent container available");
          }
        }
        else if (token.getValue().equals(KeywordDefinitions.DAEMON)) {
          if (nextAsyncable) {
            if (nextDaemon) {
              bye(token, "duplicated 'daemon' keyword");
            }

            this.nextDaemon = true;
          }
          else {
            bye(token, "'daemon' keyword must come after an 'async' keyword");
          }
        }
        else if (token.getValue().equals(KeywordDefinitions.DEFINE)) {
          parseDefinition();
        }
        else if (token.getValue().equals(KeywordDefinitions.DO)) {
          requireToken(Type.OPEN_BRACE, "expected an open brace");
          addContainer(new DoWhileContainer(null), true);
        }
        else if (token.getValue().equals(KeywordDefinitions.ELSE)) {
          if (justExitedIfContainer) {
            parseElse();
          }
          else {
            bye(token, "no previous 'if' container");
          }
        }
        else if (token.getValue().equals(KeywordDefinitions.EXPORT)) {
          if (container == script) {
            parseExport();
          }
          else {
            bye(token, "cannot use 'export' keyword inside container");
          }
        }
        else if (token.getValue().equals(KeywordDefinitions.FOR)) {
          parseFor();
        }
        else if (token.getValue().equals(KeywordDefinitions.IF)) {
          parseIf(false);
        }
        else if (token.getValue().equals(KeywordDefinitions.INCLUDE)) {
          if (container == script) {
            parseInclude();
          }
          else {
            bye(token, "cannot use 'import' keyword inside container");
          }
        }
        else if (token.getValue().equals(KeywordDefinitions.RETURN)) {
          parseReturn();
        }
        else if (token.getValue().equals(KeywordDefinitions.USING)) {
          if (container == script) {
            parseUsing();
          }
          else {
            bye(token, "cannot use 'using' keyword inside container");
          }
        }
        else if (token.getValue().equals(KeywordDefinitions.WHILE)) {
          parseWhile();
        }
        else {
          parseAttributionOrFunctionCall(token.getValue(), true);
        }

        justExitedIfContainer = false;
      }
      else if (token.getType() == Type.OPEN_BRACE) {
        addContainer(new SimpleContainer(), true);
      }
      else if (token.getType() == Type.CLOSE_BRACE) {
        if (container != script) {
          if (container instanceof IfContainer) {
            justExitedIfContainer = true;
          }

          if (container instanceof DoWhileContainer) {
            DoWhileContainer doWhileContainer = (DoWhileContainer) container;
            Token test = lexer.nextToken();

            if (test.getType() == Type.NEW_LINE) {
              test = lexer.nextToken();
            }

            if (test.getType() == Type.NAME_DEFINITION && test.getValue().equals(KeywordDefinitions.WHILE)) {
              Resultor resultor = readResultor(Type.NEW_LINE);

              doWhileContainer.setCondition(resultor);
            }
            else {
              lexer.reRead(test);
            }
          }

          do {
            this.container = container.getParent();
          }
          while (container instanceof AsyncContainer);
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

  protected void addComponent(Component component, boolean asyncable) throws UnexpectedTokenException {
    if (nextAsyncable && !asyncable) {
      this.nextAsyncable = false;

      bye("Cannot apply 'async' keyword to component " + component);
    }

    if (asyncable && nextAsyncable) {
      AsyncContainer asyncContainer = new AsyncContainer(nextDaemon);

      asyncContainer.setSourceLine(lexer.currentLine());
      container.getChildren().add(asyncContainer);
      asyncContainer.getChildren().add(component);

      this.nextAsyncable = false;
      this.nextDaemon = false;
    }
    else {
      component.setSourceLine(lexer.currentLine());
      container.getChildren().add(component);
    }
  }

  protected void addContainer(Container container, boolean asyncable) throws UnexpectedTokenException {
    addComponent(container, asyncable);

    this.container = container;
  }

  protected void bye(String message) throws UnexpectedTokenException {
    throw new UnexpectedTokenException(script.getDisplayName(), lexer.currentLine(), message);
  }

  // Do not call other bye() method, for better stacktrace
  protected void bye(Token token, String message) throws UnexpectedTokenException {
    throw new UnexpectedTokenException(script.getDisplayName(), lexer.currentLine(), "Invalid token \"" + token + "\"; " + message);
  }

  protected Value getValueOf(Token token) throws ScriptCompileException {
    String value = token.getValue();

    if (token.getType() == Type.BINARY_LITERAL) {
      try {
        return new IntegerValue(BaseConverter.encodeToLong(value, BaseConverter.BINARY));
      }
      catch (NumberFormatException exception) {
        bye(token, "illegal binary value \"" + value + "\"");
      }
    }

    if (token.getType() == Type.COLON) {
      Token next = requireToken();

      if (next.getType() == Type.DOLLAR_SIGN) {
        Token next2 = requireToken();

        if (next2.getType() == Type.NAME_DEFINITION) {
          return new VariableRefValue(new Variable(next.getValue() + next2.getValue()));
        }

        lexer.reRead(next2);
      }
      else if (next.getType() == Type.NAME_DEFINITION) {
        return new VariableRefValue(new Variable(next.getValue()));
      }

      lexer.reRead(next);
    }

    if (token.getType() == Type.CHAR_LITERAL || token.getType() == Type.STRING_LITERAL) {
      return new StringValue(value);
    }

    if (token.getType() == Type.DECIMAL_LITERAL) {
      if (ParseWorker.isLong(value)) {
        return new IntegerValue(ParseWorker.toLong(value));
      }

      if (ParseWorker.isDouble(value)) {
        return new DecimalValue(ParseWorker.toDouble(value));
      }

      bye(token, "illegal decimal value \"" + value + "\"");
    }

    if (token.getType() == Type.HEXADECIMAL_LITERAL) {
      try {
        return new IntegerValue(BaseConverter.encodeToLong(value, BaseConverter.HEXADECIMAL));
      }
      catch (NumberFormatException exception) {
        bye(token, "illegal hexadecimal value \"" + value + "\"");
      }
    }

    if (token.getType() == Type.NAME_DEFINITION) {
      if (value.equals(KeywordDefinitions.TRUE)) {
        return new BoolValue(true);
      }

      if (value.equals(KeywordDefinitions.FALSE)) {
        return new BoolValue(false);
      }
    }

    if (token.getType() == Type.OPERATOR && token.getValue().equals("*")) {
      Token next = requireToken();

      if (next.getType() == Type.NAME_DEFINITION) {
        String keyword = next.getValue();
        ValueType type = ValueType.forIdentifier(keyword);

        if (type != null) {
          return new TypeValue(type);
        }
      }

      lexer.reRead(next);
    }

    return null;
  }

  // This is called when we already parsed the name and the OPEN_BRACKET token (so we go
  // directly to parsing the index)
  protected void parseArrayAttribution(String name) throws ScriptCompileException {
    Resultor index = readResultor(Type.CLOSE_BRACKET);
    Token operatorToken = requireToken(Type.OPERATOR, "expected an attribution operator");
    Resultor resultor = parseAttributionHelper(name, operatorToken);
    ArrayAttribution attribution = new ArrayAttribution(name, index, resultor);

    addComponent(attribution, false);
  }

  protected void parseAttribution(String name, Token operatorToken) throws ScriptCompileException {
    Resultor resultor = parseAttributionHelper(name, operatorToken);
    Attribution attribution = new Attribution(name, resultor);

    addComponent(attribution, false);
  }

  protected Resultor parseAttributionHelper(String name, Token operatorToken) throws ScriptCompileException {
    String attribOperator = operatorToken.getValue();

    if (attribOperator.equals("=")) {
      return readResultor(Type.NEW_LINE);
    }

    attribOperator = readOperator(attribOperator);

    if (attribOperator.endsWith("=")) {
      String operatorIdentifier = attribOperator.substring(0, attribOperator.length() - 1);
      Operator operator = OperatorList.forIdentifier(operatorIdentifier, false); // false for bye(excepted bin opr)

      if (operator != null) {
        if (operator instanceof BinaryOperator) {
          Resultor resultor = readResultor(Type.NEW_LINE);

          return new BinaryOperation((BinaryOperator) operator, new Variable(name), resultor);
        }

        bye(operatorToken, "expected an attribution with binary operator (+=, -=, ...)");
      }
      else {
        bye(operatorToken, "expected a valid attribution operator (=, +=, -=, ...)");
      }
    }
    else {
      bye(operatorToken, "expected an attribution operator (=, +=, -=, ...)");
    }

    bye(operatorToken, "reached end of parseAttributionHelper");

    return null; // Will not happen
  }

  protected void parseAttributionOrFunctionCall(String name, boolean canBeFunctionCall) throws ScriptCompileException {
    Token next = requireToken();

    if (next.getType() == Type.OPEN_BRACKET) {
      parseArrayAttribution(name);
    }
    else if (next.getType() == Type.OPERATOR) {
      parseAttribution(name, next);
    }
    else if (next.getType() == Type.OPEN_PARENTHESE && canBeFunctionCall) {
      Resultor[] arguments = readFunctionArguments();

      requireNewLine();
      addComponent(new FunctionCall(name, arguments), true);
    }
    else {
      bye(next, "expected attribution operator or function call");
    }
  }

  protected void parseDefinition() throws ScriptCompileException {
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
    addContainer(new Definition(definitionName, arguments), false);
  }

  protected void parseElse() throws ScriptCompileException {
    Token next = requireToken();

    if (next.getType() == Type.NAME_DEFINITION && next.getValue().equals(KeywordDefinitions.IF)) {
      parseIf(true);
    }
    else {
      lexer.reRead(next);
      requireToken(Type.OPEN_BRACE, "expected an open brace");
      addContainer(new ElseContainer(), false);
    }
  }

  protected void parseExport() throws ScriptCompileException {
    Token nameToken = requireToken(Type.NAME_DEFINITION, "expected a variable name");
    String variableName = nameToken.getValue();

    if (!KeywordDefinitions.isKeyword(variableName)) {
      Token attributionToken = requireToken();

      if (attributionToken.getType() == Type.OPERATOR && attributionToken.getValue().equals("=")) {
        Value value = readValue();

        script.getApplicationContext().setVar(variableName, value);
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

  protected void parseFor() throws ScriptCompileException {
    Token varNameToken = requireToken(Type.NAME_DEFINITION, "expected a variable name");

    requireToken(Type.NAME_DEFINITION, "expected 'in' token");
    requireToken(Type.OPEN_PARENTHESE, "expected an open parenthese");

    Resultor[] arguments = readFunctionArguments();

    requireToken(Type.OPEN_BRACE, "expected an open brace");

    if (arguments.length == 2 || arguments.length == 3) {
      String varName = varNameToken.getValue();
      ForRangeContainer forContainer = new ForRangeContainer(varName, arguments[0], arguments[1],
        arguments.length == 3 ? arguments[2] : new BinaryOperation(OperatorList.PLUS, new Variable(varName),
          new Constant(new IntegerValue(1))));

      addContainer(forContainer, true);
    }
    else {
      bye("Expected 2 arguments to for definition; received " + arguments.length);
    }
  }

  protected void parseIf(boolean isElseIf) throws ScriptCompileException {
    Resultor resultor = readResultor(Type.OPEN_BRACE);
    IfContainer ifContainer = isElseIf ? new ElseIfContainer(resultor) : new IfContainer(resultor);

    addContainer(ifContainer, false);
  }

  protected void parseInclude() throws ScriptCompileException {
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


    try {
      script.include(includeName, maybe);
    }
    catch (ScriptCompileException exception) {
      bye('"' + exception.getMessage() + '"');
    }
  }

  protected void parseReturn() throws ScriptCompileException {
    Token test = lexer.nextToken();

    if (test == null || test.getType() == Type.NEW_LINE) {
      addComponent(new Return(null), false);
    }
    else {
      lexer.reRead(test);
      addComponent(new Return(readResultor(Type.NEW_LINE)), false);
    }
  }

  protected void parseUsing() throws ScriptCompileException {
    Token nameToken = requireToken(Type.NAME_DEFINITION, "expected a library name (without quotes)");

    requireNewLine();

    String libraryName = nameToken.getValue();
    Supplier<VenusLibrary> supplier = script.getApplicationContext().getLibrarySuppliers().get(libraryName);
    VenusLibrary library;

    if (supplier != null && (library = supplier.get()) != null) {
      script.getLibraryList().add(library);
    }
    else {
      bye(nameToken, "could not find a library named \"" + libraryName + "\"");
    }
  }

  protected void parseWhile() throws ScriptCompileException {
    Resultor resultor = readResultor(Type.OPEN_BRACE);
    WhileContainer whileContainer = new WhileContainer(resultor);

    addContainer(whileContainer, true);
  }

  protected Resultor[] readFunctionArguments() throws ScriptCompileException {
    return readResultors(Type.COMMA, Type.CLOSE_PARENTHESE);
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

      if (token.getType() == Type.AT_SIGN) {
        Token next = requireToken(Type.NAME_DEFINITION, "expected a function name as reference");

        resultor.addResultor(this, next, new FunctionRef(next.getValue()));
      }
      else if (token.getType() == Type.DOLLAR_SIGN) {
        Token next = requireToken(Type.NAME_DEFINITION, "expected a variable name");

        resultor.addResultor(this, next, new Variable(token.getValue() + next.getValue()));
      }
      else if (token.getType() == Type.OPERATOR) {
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
      else if (token.getType() == Type.OPEN_BRACKET) {
        if (nameDef != null) {
          Resultor index = readResultor(Type.CLOSE_BRACKET);

          resultor.addResultor(this, nameDefToken, new ArrayAccess(nameDef, index));
          nameDef = null;
        }
        else {
          Resultor[] resultors = readResultors(Type.COMMA, Type.CLOSE_BRACKET);

          resultor.addResultor(this, token, new ArrayDefine(resultors));
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
      else if (token.getType() != Type.NEW_LINE) {
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

  // This also consumes the last 'end' token
  protected Resultor[] readResultors(Type separator, Type end) throws ScriptCompileException {
    List<Resultor> result = new ArrayList<>();
    Token token;

    while ((token = requireToken()).getType() != end) {
      lexer.reRead(token);
      result.add(readResultor(
        t -> t.getType() != end && t.getType() != separator,
        t -> t.getType() == end));
    }

    return result.toArray();
  }

  protected Value readValue() throws ScriptCompileException {
    Token token = requireToken();
    Value value = getValueOf(token);

    if (value == null) {
      bye(token, "expected a value literal (array/boolean/char/number/string/type)");
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