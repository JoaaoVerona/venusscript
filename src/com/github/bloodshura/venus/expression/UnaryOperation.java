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

package com.github.bloodshura.venus.expression;

import com.github.bloodshura.venus.exception.runtime.IncompatibleTypesException;
import com.github.bloodshura.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.venus.executor.Context;
import com.github.bloodshura.venus.operator.UnaryOperator;
import com.github.bloodshura.venus.value.Value;
import com.github.bloodshura.x.lang.layer.XApi;

public class UnaryOperation implements Expression {
  private final UnaryOperator operator;
  private final Expression expression;

  public UnaryOperation(UnaryOperator operator, Expression expression) {
    XApi.requireNonNull(expression, "expression");
    XApi.requireNonNull(operator, "operator");

    this.operator = operator;
    this.expression = expression;
  }

  public UnaryOperator getOperator() {
    return operator;
  }

  public Expression getExpression() {
    return expression;
  }

  @Override
  public Value resolve(Context context) throws ScriptRuntimeException {
    Value value = getExpression().resolve(context);
    Value result = getOperator().operate(context, value);

    if (result == null) {
      throw new IncompatibleTypesException(context, "Operator " + getOperator() + " cannot be applied with " + value.getType());
    }

    return result;
  }

  @Override
  public String toString() {
    return "unioperation(" + getOperator() + " [" + getExpression() + "])";
  }
}