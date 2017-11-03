/*
 * Copyright (c) 2013-2017, João Vitor Verona Biazibetti - All Rights Reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * https://www.github.com/BloodShura
 */

package com.github.bloodshura.x.venus.compiler;

import com.github.bloodshura.x.charset.TextBuilder;
import com.github.bloodshura.x.collection.list.XList;
import com.github.bloodshura.x.collection.list.impl.XArrayList;
import com.github.bloodshura.x.collection.map.XMap;
import com.github.bloodshura.x.collection.map.impl.XLinkedMap;
import com.github.bloodshura.x.math.BaseConverter;
import com.github.bloodshura.x.util.Pool;
import com.github.bloodshura.x.venus.component.AsyncContainer;
import com.github.bloodshura.x.venus.component.Component;
import com.github.bloodshura.x.venus.component.Container;
import com.github.bloodshura.x.venus.component.Script;
import com.github.bloodshura.x.venus.component.SimpleComponent;
import com.github.bloodshura.x.venus.component.SimpleContainer;
import com.github.bloodshura.x.venus.component.branch.*;
import com.github.bloodshura.x.venus.component.object.Attribute;
import com.github.bloodshura.x.venus.component.object.ObjectDefinition;
import com.github.bloodshura.x.venus.exception.compile.ScriptCompileException;
import com.github.bloodshura.x.venus.exception.compile.UnexpectedTokenException;
import com.github.bloodshura.x.venus.expression.*;
import com.github.bloodshura.x.venus.function.Argument;
import com.github.bloodshura.x.venus.function.Definition;
import com.github.bloodshura.x.venus.library.VenusLibrary;
import com.github.bloodshura.x.venus.operator.BinaryOperator;
import com.github.bloodshura.x.venus.operator.Operator;
import com.github.bloodshura.x.venus.operator.OperatorList;
import com.github.bloodshura.x.venus.type.PrimitiveType;
import com.github.bloodshura.x.venus.type.Type;
import com.github.bloodshura.x.venus.value.BoolValue;
import com.github.bloodshura.x.venus.value.DecimalValue;
import com.github.bloodshura.x.venus.value.FunctionRefValue;
import com.github.bloodshura.x.venus.value.IntegerValue;
import com.github.bloodshura.x.venus.value.StringValue;
import com.github.bloodshura.x.venus.value.TypeValue;
import com.github.bloodshura.x.venus.value.Value;
import com.github.bloodshura.x.venus.value.VariableRefValue;
import com.github.bloodshura.x.worker.ParseWorker;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class VenusParser {
  private Container container;
  private VenusLexer lexer;
  private boolean nextAsyncable;
  private boolean nextDaemon;
  private final Script script;

  public VenusParser(Script script) {
    this.script = script;
  }

  public synchronized void parse(VenusLexer lexer, Container target) throws ScriptCompileException {
    this.container = target;
    this.lexer = lexer;

    Token token;
    boolean justExitedIfContainer = false;

    while ((token = lexer.nextToken()) != null) {
      if (container instanceof ObjectDefinition) {
        if (token.getType() == Token.Type.NAME_DEFINITION) {
          if (token.getValue().equals(KeywordDefinitions.DEFINE)) {
            parseDefinition(false);

            continue;
          }

          bye(token, "expected a definition");
        }
        else if (token.getType() != Token.Type.CLOSE_BRACE && token.getType() != Token.Type.NEW_LINE) {
          bye(token, "expected a definition");
        }
      }

      if (token.getType() == Token.Type.GLOBAL_ACCESS) {
        lexer.reRead(token);
        addComponent(readExpression(Token.Type.NEW_LINE), true);
      }
      else if (token.getType() == Token.Type.NAME_DEFINITION) {
        if (token.getValue().equals(KeywordDefinitions.ASYNC)) {
          if (nextAsyncable) {
            bye(token, "duplicated 'async' keyword");
          }

          this.nextAsyncable = true;
        }
        else if (token.getValue().equals(KeywordDefinitions.BREAK) || token.getValue().equals(KeywordDefinitions.CONTINUE)) {
          requireToken(Token.Type.NEW_LINE, "expected a new line");

          Container lookup = container;
          boolean foundContinuable = false;

          while (lookup != null) {
            if (lookup instanceof Breakable) {
              foundContinuable = true;

              break;
            }

            // If there is a definition, at run-time it will be in a single context,
            // so do not let lookuping a definition's parents
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
          parseDefinition(true);
        }
        else if (token.getValue().equals(KeywordDefinitions.DO)) {
          requireToken(Token.Type.OPEN_BRACE, "expected an open brace");
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
        else if (token.getValue().equals(KeywordDefinitions.OBJECT)) {
          parseObject();
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
          lexer.reRead(token);
          addComponent(readExpression(Token.Type.NEW_LINE), true);
        }

        justExitedIfContainer = false;
      }
      else if (token.getType() == Token.Type.OPEN_BRACE) {
        addContainer(new SimpleContainer(), true);
      }
      else if (token.getType() == Token.Type.CLOSE_BRACE) {
        if (container != script) {
          if (container instanceof IfContainer) {
            justExitedIfContainer = true;
          }

          if (container instanceof DoWhileContainer) {
            DoWhileContainer doWhileContainer = (DoWhileContainer) container;
            Token test = lexer.nextToken();

            if (test.getType() == Token.Type.NEW_LINE) {
              test = lexer.nextToken();
            }

            if (test.getType() == Token.Type.NAME_DEFINITION && test.getValue().equals(KeywordDefinitions.WHILE)) {
              Expression expression = readExpression(Token.Type.NEW_LINE);

              doWhileContainer.setCondition(expression);
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
      else if (token.getType() != Token.Type.NEW_LINE) {
        lexer.reRead(token);
        addComponent(readExpression(Token.Type.NEW_LINE), true);
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

  protected void addComponent(Expression expression, boolean asyncable) throws UnexpectedTokenException {
    addComponent(new SimpleComponent(expression), asyncable);
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

    if (token.getType() == Token.Type.FUNC_REF) {
      Token next = requireToken();

      if (next.getType() == Token.Type.NAME_DEFINITION) {
        return new FunctionRefValue(next.getValue());
      }

      lexer.reRead(next);
    }

    if (token.getType() == Token.Type.BINARY_LITERAL) {
      try {
        return new IntegerValue(BaseConverter.encodeToLong(value, BaseConverter.BINARY));
      }
      catch (NumberFormatException exception) {
        bye(token, "illegal binary value \"" + value + "\"");
      }
    }

    if (token.getType() == Token.Type.COLON) {
      Token next = requireToken();

      if (next.getType() == Token.Type.GLOBAL_ACCESS) {
        Token next2 = requireToken();

        if (next2.getType() == Token.Type.NAME_DEFINITION) {
          return new VariableRefValue(next.getValue() + next2.getValue());
        }

        lexer.reRead(next2);
      }
      else if (next.getType() == Token.Type.NAME_DEFINITION) {
        return new VariableRefValue(next.getValue());
      }

      lexer.reRead(next);
    }

    if (token.getType() == Token.Type.CHAR_LITERAL || token.getType() == Token.Type.STRING_LITERAL) {
      return new StringValue(value);
    }

    if (token.getType() == Token.Type.DECIMAL_LITERAL) {
      if (ParseWorker.isLong(value)) {
        return new IntegerValue(ParseWorker.toLong(value));
      }

      if (ParseWorker.isDouble(value)) {
        return new DecimalValue(ParseWorker.toDouble(value));
      }

      bye(token, "illegal decimal value \"" + value + "\"");
    }

    if (token.getType() == Token.Type.HEXADECIMAL_LITERAL) {
      try {
        return new IntegerValue(BaseConverter.encodeToLong(value, BaseConverter.HEXADECIMAL));
      }
      catch (NumberFormatException exception) {
        bye(token, "illegal hexadecimal value \"" + value + "\"");
      }
    }

    if (token.getType() == Token.Type.NAME_DEFINITION) {
      if (value.equals(KeywordDefinitions.TRUE)) {
        return new BoolValue(true);
      }

      if (value.equals(KeywordDefinitions.FALSE)) {
        return new BoolValue(false);
      }
    }

    if (token.getType() == Token.Type.OPERATOR && token.getValue().equals("*")) {
      Token next = requireToken();

      if (next.getType() == Token.Type.NAME_DEFINITION) {
        String keyword = next.getValue();
        Type type = PrimitiveType.forIdentifier(keyword);

        if (type != null) {
          return new TypeValue(type);
        }
      }

      lexer.reRead(next);
    }

    return null;
  }

  protected Object parseArrayElementOperation(String currentNameDef, Expression index, String operatorStr, Token errorToken, boolean mustBeUnary) throws ScriptCompileException {
    if (operatorStr.equals("=")) {
      Expression expression = readExpression(token -> token.getType() != Token.Type.NEW_LINE && token.getType() != Token.Type.CLOSE_PARENTHESE,
        token -> true);

      return new ArraySet(currentNameDef, index, expression);
    }

    Operator opr = OperatorList.forIdentifier(operatorStr, mustBeUnary);

    if (opr != null) {
      return opr;
    }

    // Is attribution
    if (operatorStr.endsWith("=")) {
      String operatorIdentifier = operatorStr.substring(0, operatorStr.length() - 1);
      Operator operator = OperatorList.forIdentifier(operatorIdentifier, false); // false for bye(excepted bin opr)

      if (operator != null) {
        if (operator instanceof BinaryOperator) {
          Expression expression = readExpression(token -> token.getType() != Token.Type.NEW_LINE && token.getType() != Token.Type.CLOSE_PARENTHESE,
            token -> true);
          BinaryOperation operation = new BinaryOperation((BinaryOperator) operator,
            new ArrayGet(currentNameDef, index), expression);

          return new ArraySet(currentNameDef, index, operation);
        }

        bye(errorToken, "expected an attribution with binary operator (+=, -=, ...)");
      }
      else {
        bye(errorToken, "expected a valid attribution operator (=, +=, -=, ...)");
      }
    }

    bye(errorToken, "expected a valid " + (mustBeUnary ? "unary operator" : "operator") + " (+, -, *, /, %, ...)");

    return null; // Will not happen
  }

  protected void parseDefinition(boolean isGlobal) throws ScriptCompileException {
    Token typeToken = requireToken(Token.Type.NAME_DEFINITION, "expected a definition name");
    String definitionName = typeToken.getValue();
    XList<Argument> arguments = new XArrayList<>();

    requireToken(Token.Type.OPEN_PARENTHESE, "expected an open parenthese");

    Token reading;

    while ((reading = requireToken()).getType() != Token.Type.CLOSE_PARENTHESE) { // Reads definition arguments
      if (reading.getType() == Token.Type.NAME_DEFINITION) {
        Type argumentType = PrimitiveType.forIdentifier(reading.getValue());

        if (argumentType != null) {
          Token argumentToken = requireToken(Token.Type.NAME_DEFINITION, "expected an argument name");
          String argumentName = argumentToken.getValue();

          if (!KeywordDefinitions.isKeyword(argumentName)) {
            arguments.add(new Argument(argumentName, argumentType));

            Token commaOrClose = requireToken();

            if (commaOrClose.getType() == Token.Type.CLOSE_PARENTHESE) {
              break;
            }

            if (commaOrClose.getType() != Token.Type.COMMA) {
              bye(commaOrClose, "expected an argument separator (comma) or close parenthese");
            }
          }
          else {
            bye(argumentToken, "argument name cannot be a keyword");
          }
        }
        else {
          bye(reading, "expected an argument value type (" + PrimitiveType.values().toString(", ") + ") or object type");
        }
      }
      else {
        bye(reading, "expected an argument name");
      }
    }

    requireToken(Token.Type.OPEN_BRACE, "expected an open brace");
    addContainer(new Definition(definitionName, arguments, isGlobal), false);
  }

  protected void parseElse() throws ScriptCompileException {
    Token next = requireToken();

    if (next.getType() == Token.Type.NAME_DEFINITION && next.getValue().equals(KeywordDefinitions.IF)) {
      parseIf(true);
    }
    else {
      lexer.reRead(next);
      requireToken(Token.Type.OPEN_BRACE, "expected an open brace");
      addContainer(new ElseContainer(), false);
    }
  }

  protected void parseExport() throws ScriptCompileException {
    Token nameToken = requireToken(Token.Type.NAME_DEFINITION, "expected a variable name");
    String variableName = nameToken.getValue();

    if (!KeywordDefinitions.isKeyword(variableName)) {
      Token attributionToken = requireToken();

      if (attributionToken.getType() == Token.Type.OPERATOR && attributionToken.getValue().equals("=")) {
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
    Token varNameToken = requireToken(Token.Type.NAME_DEFINITION, "expected a variable name");

    requireToken(Token.Type.NAME_DEFINITION, "expected 'in' token");

    Token next = requireToken();

    if (next.getType() == Token.Type.OPEN_PARENTHESE) {
      Expression[] arguments = readFunctionArguments();

      requireToken(Token.Type.OPEN_BRACE, "expected an open brace");

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
    else {
      lexer.reRead(next);

      Expression iterable = readExpression(Token.Type.OPEN_BRACE);
      String varName = varNameToken.getValue();
      ForEachContainer forContainer = new ForEachContainer(varName, iterable);

      addContainer(forContainer, true);
    }
  }

  protected void parseIf(boolean isElseIf) throws ScriptCompileException {
    Expression expression = readExpression(Token.Type.OPEN_BRACE);
    IfContainer ifContainer = isElseIf ? new ElseIfContainer(expression) : new IfContainer(expression);

    addContainer(ifContainer, false);
  }

  protected void parseInclude() throws ScriptCompileException {
    Token next = requireToken(Token.Type.STRING_LITERAL, "expected a string literal as including script");
    String includeName = next.getValue();
    boolean maybe = false;
    Token maybeOrNewLine = requireToken();

    if (maybeOrNewLine.getType() == Token.Type.NAME_DEFINITION) {
      if (maybeOrNewLine.getValue().equals("maybe")) {
        maybe = true;
        requireToken(Token.Type.NEW_LINE, "expected new line");
      }
      else {
        bye(maybeOrNewLine, "expected 'maybe' or new line");
      }
    }
    else if (maybeOrNewLine.getType() != Token.Type.NEW_LINE) {
      bye(maybeOrNewLine, "expected 'maybe' or new line");
    }


    try {
      script.include(includeName, maybe);
    }
    catch (ScriptCompileException exception) {
      bye('"' + exception.getMessage() + '"');
    }
  }

  protected void parseObject() throws ScriptCompileException {
    Token nameToken = requireToken(Token.Type.NAME_DEFINITION, "expected an object name");

    requireToken(Token.Type.OPEN_PARENTHESE, "expected an open parenthese");

    ObjectDefinition definition = new ObjectDefinition(nameToken.getValue());
    Token next;

    while ((next = requireToken()).getType() != Token.Type.CLOSE_PARENTHESE) {
      if (next.getType() == Token.Type.NAME_DEFINITION) {
        Token test = requireToken();
        Expression defaultExpression = null;

        if (test.getType() == Token.Type.COLON) {
          defaultExpression = readExpression(token -> token.getType() != Token.Type.COMMA &&
            token.getType() != Token.Type.CLOSE_PARENTHESE, token -> token.getType() == Token.Type.CLOSE_PARENTHESE);
        }
        else {
          lexer.reRead(test);
        }

        definition.getAttributes().add(new Attribute(next.getValue(), defaultExpression));
      }
      else if (next.getType() != Token.Type.COMMA) {
        bye(next, "expected an attribute name or close parenthese");
      }
    }

    next = lexer.nextToken();

    if (next != null) {
      if (next.getType() == Token.Type.OPEN_BRACE) {
        addContainer(definition, false);

        return;
      }

      lexer.reRead(next);
    }

    addContainer(definition, false);

    do {
      this.container = container.getParent();
    }
    while (container instanceof AsyncContainer);
  }

  protected Object parseOperation(String currentNameDef, String operatorStr, Token errorToken, boolean mustBeUnary) throws ScriptCompileException {
    if (operatorStr.equals("=")) {
      Expression expression = readExpression(token -> token.getType() != Token.Type.NEW_LINE && token.getType() != Token.Type.CLOSE_PARENTHESE,
        token -> true);

      return new Attribution(currentNameDef, expression);
    }

    Operator opr = OperatorList.forIdentifier(operatorStr, mustBeUnary);

    if (opr != null) {
      return opr;
    }

    // Is attribution
    if (operatorStr.endsWith("=")) {
      String operatorIdentifier = operatorStr.substring(0, operatorStr.length() - 1);
      Operator operator = OperatorList.forIdentifier(operatorIdentifier, false); // false for bye(excepted bin opr)

      if (operator != null) {
        if (operator instanceof BinaryOperator) {
          Expression expression = readExpression(token -> token.getType() != Token.Type.NEW_LINE && token.getType() != Token.Type.CLOSE_PARENTHESE,
            token -> true);
          BinaryOperation operation = new BinaryOperation((BinaryOperator) operator, new Variable(currentNameDef), expression);

          return new Attribution(currentNameDef, operation);
        }

        bye(errorToken, "expected an attribution with binary operator (+=, -=, ...)");
      }
      else {
        bye(errorToken, "expected a valid attribution operator (=, +=, -=, ...)");
      }
    }

    bye(errorToken, "expected a valid " + (mustBeUnary ? "unary operator" : "operator") + " (+, -, *, /, %, ...)");

    return null; // Will not happen
  }

  protected void parseReturn() throws ScriptCompileException {
    Token test = lexer.nextToken();

    if (test == null || test.getType() == Token.Type.NEW_LINE) {
      addComponent(new Return(null), false);
    }
    else {
      lexer.reRead(test);
      addComponent(new Return(readExpression(Token.Type.NEW_LINE)), false);
    }
  }

  protected void parseUsing() throws ScriptCompileException {
    Token nameToken = requireToken(Token.Type.NAME_DEFINITION, "expected a library name (without quotes)");

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
    Expression expression = readExpression(Token.Type.OPEN_BRACE);
    WhileContainer whileContainer = new WhileContainer(expression);

    addContainer(whileContainer, true);
  }

  protected Expression readExpression(Predicate<Token> process) throws ScriptCompileException {
    return readExpression(process, token -> false);
  }

  protected Expression readExpression(Predicate<Token> process, Predicate<Token> reReadLast) throws ScriptCompileException {
    BuildingExpression expression = new BuildingExpression();
    String nameDef = null;
    Expression arrayIndex = null;
    Token nameDefToken = null;
    Token token;

    while (process.test(token = requireToken())) {
      if (nameDef == null) {
        Value value;

        try {
          value = getValueOf(token);

          if (value != null) {
            expression.addExpression(this, token, new Constant(value));

            continue;
          }
        }
        catch (UnexpectedTokenException exception) {
        }
      }

      if (token.getType() == Token.Type.OBJECT_ACCESS) {
        if (nameDef != null) {
          expression.addInContext(this, nameDefToken, nameDef);
          arrayIndex = null;
          nameDef = null;
        }
        else {
          bye(token, "expected a name definition before object access");
        }
      }
      else if (token.getType() == Token.Type.GLOBAL_ACCESS) {
        if (nameDef != null) {
          bye(token, "expected open parenthese (function) or operator after a name definition");
        }

        Token next = requireToken(Token.Type.NAME_DEFINITION, "expected a variable name");

        nameDef = token.getValue() + next.getValue();
        nameDefToken = next;
      }
      else if (token.getType() == Token.Type.OPERATOR) {
        String operator = readOperator(token.getValue());

        if (nameDef != null) {
          Object r;

          if (arrayIndex != null) {
            r = parseArrayElementOperation(nameDef, arrayIndex, operator, token, false);
          }
          else {
            r = parseOperation(nameDef, operator, token, false);
          }

          if (r instanceof Expression) {
            expression.addExpression(this, token, (Expression) r);
          }
          else if (r instanceof Operator) {
            expression.addExpression(this, nameDefToken, arrayIndex != null ? new ArrayGet(nameDef, arrayIndex) : new Variable(nameDef));
            expression.addOperator(this, token, (Operator) r);
          }
          else {
            bye(token, "unknown type " + r.getClass().getName());
          }

          arrayIndex = null;
          nameDef = null;
        }
        else {
          Object r = parseOperation(null, operator, token, !expression.hasResultor() || expression.hasOperator());

          if (r instanceof Operator) {
            expression.addOperator(this, token, (Operator) r);
          }
          else {
            bye(token, "unknown type " + r.getClass().getName());
          }
        }
      }
      else if (token.getType() == Token.Type.OPEN_BRACKET) {
        if (nameDef != null) {
          if (arrayIndex == null) {
            arrayIndex = readExpression(Token.Type.CLOSE_BRACKET);
          }
          else {
            bye(token, "already has index expression");
          }
        }
        else {
          Expression[] expressions = readExpressions(Token.Type.COMMA, Token.Type.CLOSE_BRACKET);

          expression.addExpression(this, token, new ArrayLiteral(expressions));
        }
      }
      else if (token.getType() == Token.Type.OPEN_PARENTHESE) {
        if (nameDef != null) {
          Expression[] arguments = readFunctionArguments();

          expression.addExpression(this, nameDefToken, new FunctionCall(nameDef, arguments));
          nameDef = null;
        }
        else {
          expression.addExpression(this, token, readExpression(Token.Type.CLOSE_PARENTHESE));
        }
      }
      else if (nameDef != null) {
        bye(token, "expected open parenthese (function) or operator after a name definition");
      }
      else if (token.getType() == Token.Type.NAME_DEFINITION) {
        if (token.getValue().equals(KeywordDefinitions.NEW)) {
          Token objectTypeToken = requireToken(Token.Type.NAME_DEFINITION, "expected an object name");

          requireToken(Token.Type.OPEN_PARENTHESE, "expected an open parenthese");

          XMap<String, Expression> attributes = new XLinkedMap<>();

          while (true) {
            Token t = requireToken();

            if (t.getType() == Token.Type.CLOSE_PARENTHESE) {
              break;
            }

            if (t.getType() == Token.Type.NAME_DEFINITION) {
              requireToken(Token.Type.COLON, "expected an attribute separator (colon)");

              Expression attribExpression = readExpression(o -> o.getType() != Token.Type.COMMA && o.getType() != Token.Type.CLOSE_PARENTHESE,
                o -> o.getType() == Token.Type.CLOSE_PARENTHESE);

              attributes.add(t.getValue(), attribExpression);
            }
            else {
              bye(t, "expected an attribute name");
            }
          }

          expression.addExpression(this, token, new NewObject(objectTypeToken.getValue(), attributes));
        }
        else {
          nameDef = token.getValue();
          nameDefToken = token;
        }
      }
      else if (token.getType() != Token.Type.NEW_LINE) {
        bye(token, "unexpected token");
      }
    }

    if (nameDef != null) {
      if (arrayIndex != null) {
        expression.addExpression(this, nameDefToken, new ArrayGet(nameDef, arrayIndex));
      }
      else {
        expression.addExpression(this, nameDefToken, new Variable(nameDef));
      }
    }

    if (reReadLast.test(token)) {
      lexer.reRead(token);
    }

    Expression result = expression.build();

    if (result == null) {
      bye(token, "expected an expression/value");
    }

    return result;
  }

  protected Expression readExpression(Token.Type stopAt) throws ScriptCompileException {
    return readExpression(stopAt, token -> false);
  }

  protected Expression readExpression(Token.Type stopAt, Predicate<Token> reReadLast) throws ScriptCompileException {
    return readExpression(token -> token.getType() != stopAt, reReadLast);
  }

  // This also consumes the last 'end' token
  protected Expression[] readExpressions(Token.Type separator, Token.Type end) throws ScriptCompileException {
    XList<Expression> result = new XArrayList<>();
    Token token;

    while ((token = requireToken()).getType() != end) {
      lexer.reRead(token);
      result.add(readExpression(
        t -> t.getType() != end && t.getType() != separator,
        t -> t.getType() == end));
    }

    return result.toArray();
  }

  protected Expression[] readFunctionArguments() throws ScriptCompileException {
    return readExpressions(Token.Type.COMMA, Token.Type.CLOSE_PARENTHESE);
  }

  protected String readOperator(String start) throws ScriptCompileException {
    TextBuilder operatorStr = Pool.newBuilder();
    Token operatorToken;

    if (start != null) {
      operatorStr.append(start);
    }

    while ((operatorToken = requireToken()).getType() == Token.Type.OPERATOR) {
      String op = operatorToken.getValue();

      if (OperatorList.forIdentifier(op, true) != null && !operatorStr.isEmpty()) {
        break;
      }

      operatorStr.append(operatorToken.getValue());
    }

    lexer.reRead(operatorToken); // Last token have type != OPERATOR

    return operatorStr.toStringAndClear();
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

    if (token.getType() != Token.Type.NEW_LINE) {
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

  protected Token requireToken(Token.Type type, String errorMessage) throws ScriptCompileException {
    Token token = requireToken();

    if (token.getType() != type) {
      bye(token, errorMessage);
    }

    return token;
  }
}