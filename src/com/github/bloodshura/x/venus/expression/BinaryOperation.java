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

package com.github.bloodshura.x.venus.expression;

import com.github.bloodshura.x.venus.exception.runtime.IncompatibleTypesException;
import com.github.bloodshura.x.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.x.venus.executor.Context;
import com.github.bloodshura.x.venus.operator.BinaryOperator;
import com.github.bloodshura.x.venus.value.Value;
import com.github.bloodshura.x.util.XApi;

public class BinaryOperation implements Expression {
  private final Expression left;
  private final BinaryOperator operator;
  private final Expression right;

  public BinaryOperation(BinaryOperator operator, Expression left, Expression right) {
    XApi.requireNonNull(left, "left");
    XApi.requireNonNull(operator, "operator");
    XApi.requireNonNull(right, "right");

    this.left = left;
    this.operator = operator;
    this.right = right;
  }

  public Expression getLeft() {
    return left;
  }

  public BinaryOperator getOperator() {
    return operator;
  }

  public Expression getRight() {
    return right;
  }

  @Override
  public Value resolve(Context context) throws ScriptRuntimeException {
    Value left = getLeft().resolve(context);
    Value right = getRight().resolve(context);
    Value result = getOperator().operate(context, left, right);

    if (result == null) {
      throw new IncompatibleTypesException(context, "Operator " + getOperator() + " cannot be applied with " +
        left.getType() + " and " + right.getType());
    }

    return result;
  }

  @Override
  public String toString() {
    return "bioperation([" + getLeft() + "] " + getOperator() + " [" + getRight() + "])";
  }
}