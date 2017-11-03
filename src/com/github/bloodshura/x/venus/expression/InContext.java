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

package com.github.bloodshura.x.venus.expression;

import com.github.bloodshura.x.venus.exception.runtime.InvalidValueTypeException;
import com.github.bloodshura.x.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.x.venus.executor.Context;
import com.github.bloodshura.x.venus.value.ObjectValue;
import com.github.bloodshura.x.venus.value.Value;

public class InContext implements Expression {
  private final Expression expression;
  private final String name;

  public InContext(String name, Expression expression) {
    this.name = name;
    this.expression = expression;
  }

  public Expression getExpression() {
    return expression;
  }

  public String getName() {
    return name;
  }

  @Override
  public Value resolve(Context context) throws ScriptRuntimeException {
    Value value = context.getVarValue(getName());

    if (value instanceof ObjectValue) {
      ObjectValue object = (ObjectValue) value;

      return getExpression().resolve(object.getContext()); // See issue #24
    }
    else {
      throw new InvalidValueTypeException(context, getName() + " has type " + value.getType() + "; expected to be an object");
    }
  }

  @Override
  public String toString() {
    return "incontext(" + getName() + " << " + getExpression() + ')';
  }
}