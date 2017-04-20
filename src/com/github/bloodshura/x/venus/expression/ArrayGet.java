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

import com.github.bloodshura.x.venus.exception.runtime.InvalidArrayAccessException;
import com.github.bloodshura.x.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.x.venus.executor.Context;
import com.github.bloodshura.x.venus.type.PrimitiveType;
import com.github.bloodshura.x.venus.value.ArrayValue;
import com.github.bloodshura.x.venus.value.IntegerValue;
import com.github.bloodshura.x.venus.value.Value;
import com.github.bloodshura.x.util.XApi;

public class ArrayGet implements Expression {
  private final Expression index;
  private final String name;

  public ArrayGet(String name, Expression index) {
    XApi.requireNonNull(index, "index");
    XApi.requireNonNull(name, "name");

    this.index = index;
    this.name = name;
  }

  public Expression getIndex() {
    return index;
  }

  public String getName() {
    return name;
  }

  @Override
  public Value resolve(Context context) throws ScriptRuntimeException {
    Value value = context.getVarValue(getName());

    if (value instanceof ArrayValue) {
      ArrayValue array = (ArrayValue) value;
      Value index = getIndex().resolve(context);

      if (index instanceof IntegerValue) {
        IntegerValue intIndex = (IntegerValue) index;

        return array.get(context, intIndex.value().intValue());
      }

      throw new InvalidArrayAccessException(context, "Index \"" + index + "\" is of type " +
        index.getType() + "; expected to be an " + PrimitiveType.INTEGER);
    }

    throw new InvalidArrayAccessException(context, "Variable \"" + getName() + "\" is of type " +
      value.getType() + "; expected to be an " + PrimitiveType.ARRAY);
  }

  @Override
  public String toString() {
    return "arr(" + getName() + '[' + getIndex() + "])";
  }
}
