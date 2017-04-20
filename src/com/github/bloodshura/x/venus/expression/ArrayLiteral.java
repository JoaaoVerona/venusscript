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

import com.github.bloodshura.x.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.x.venus.executor.Context;
import com.github.bloodshura.x.venus.value.ArrayValue;
import com.github.bloodshura.x.venus.value.Value;
import com.github.bloodshura.x.collection.view.XArrayView;
import com.github.bloodshura.x.collection.view.XView;
import com.github.bloodshura.x.util.XApi;

public class ArrayLiteral implements Expression {
  private final Expression[] expressions;

  public ArrayLiteral(Expression... expressions) {
    XApi.requireNonNull(expressions, "expressions");

    this.expressions = expressions;
  }

  public XView<Expression> getExpressions() {
    return new XArrayView<>(expressions);
  }

  @Override
  public Value resolve(Context context) throws ScriptRuntimeException {
    ArrayValue value = new ArrayValue(getExpressions().size());
    int i = 0;

    for (Expression expression : getExpressions()) {
      value.set(context, i++, expression.resolve(context));
    }

    return value;
  }

  @Override
  public String toString() {
    return "arrdef(" + getExpressions() + ')';
  }
}