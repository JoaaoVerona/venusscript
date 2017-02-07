//////////////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2013-2017, João Vitor Verona Biazibetti - All Rights Reserved           /
//                                                                                       /
// This program is free software: you can redistribute it and/or modify                  /
// it under the terms of the GNU General Public License as published by                  /
// the Free Software Foundation, either version 3 of the License, or                     /
// (at your option) any later version.                                                   /
//                                                                                       /
// This program is distributed in the hope that it will be useful,                       /
// but WITHOUT ANY WARRANTY; without even the implied warranty of                        /
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the                          /
// GNU General Public License for more details.                                          /
//                                                                                       /
// You should have received a copy of the GNU General Public License                     /
// along with this program. If not, see <http://www.gnu.org/licenses/>.                  /
//                                                                                       /
// https://www.github.com/BloodShura                                                     /
//////////////////////////////////////////////////////////////////////////////////////////

package com.github.bloodshura.venus.compiler;

import com.github.bloodshura.venus.exception.compile.UnexpectedTokenException;
import com.github.bloodshura.venus.expression.BinaryOperation;
import com.github.bloodshura.venus.expression.Expression;
import com.github.bloodshura.venus.expression.InContext;
import com.github.bloodshura.venus.expression.UnaryOperation;
import com.github.bloodshura.venus.operator.BinaryOperator;
import com.github.bloodshura.venus.operator.Operator;
import com.github.bloodshura.venus.operator.UnaryOperator;
import com.github.bloodshura.x.collection.store.impl.XStack;

public class BuildingExpression {
  private String inContext;
  private Operator operator;
  private Expression expression;
  private final XStack<UnaryOperator> unaryWhenAlready;

  public BuildingExpression() {
    this.unaryWhenAlready = new XStack<>();
  }

  public void addInContext(VenusParser parser, Token owner, String context) throws UnexpectedTokenException {
    if (inContext != null) {
      parser.bye(owner, "already in object access \"" + inContext + "\"");
    }

    this.inContext = context;
  }

  public void addOperator(VenusParser parser, Token owner, Operator op) throws UnexpectedTokenException {
    if (hasOperator()) {
      if (op instanceof UnaryOperator) {
        unaryWhenAlready.push((UnaryOperator) op);

        return;
      }
      else {
        parser.bye(owner, "already have an operator \"" + operator + "\"");
      }
    }

    if (op instanceof BinaryOperator && !hasResultor()) {
      parser.bye(owner, "no left operation value");
    }

    if (op instanceof UnaryOperator && hasResultor()) {
      parser.bye(owner, "cannot apply unary operator to left-sided value");
    }

    this.operator = op;
  }

  public void addExpression(VenusParser parser, Token owner, Expression rslt) throws UnexpectedTokenException {
    if (!hasResultor()) {
      if (operator instanceof UnaryOperator) {
        while (!unaryWhenAlready.isEmpty()) {
          rslt = new UnaryOperation(unaryWhenAlready.pop(), rslt);
        }

        setExpression(new UnaryOperation((UnaryOperator) operator, rslt));
      }
      else {
        setExpression(rslt);
      }
    }
    else if (hasOperator()) {
      if (operator instanceof BinaryOperator) {
        while (!unaryWhenAlready.isEmpty()) {
          rslt = new UnaryOperation(unaryWhenAlready.pop(), rslt);
        }

        setExpression(new BinaryOperation((BinaryOperator) operator, expression, rslt));
      }
      else {
        parser.bye("Excepted a binary or unary operator, received " + operator.getClass().getSimpleName());
      }

      this.operator = null;
    }
    else {
      parser.bye(owner, "expected operator, found another expression");
    }
  }

  public Expression build() {
    return expression;
  }

  public boolean hasOperator() {
    return operator != null;
  }

  public boolean hasResultor() {
    return expression != null;
  }

  private void setExpression(Expression expression) {
    if (inContext != null) {
      this.expression = new InContext(inContext, expression);

      this.inContext = null;
    }
    else {
      this.expression = expression;
    }
  }
}