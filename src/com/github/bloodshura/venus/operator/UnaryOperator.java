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

package com.github.bloodshura.venus.operator;

import com.github.bloodshura.venus.executor.Context;
import com.github.bloodshura.venus.value.Value;
import com.github.bloodshura.x.collection.view.XArrayView;
import com.github.bloodshura.x.collection.view.XView;
import com.github.bloodshura.x.lang.layer.XApi;

import java.util.function.Function;

public class UnaryOperator implements Operator {
  private final Function<Value, Value> function;
  private final XView<String> identifiers;
  private final String name;

  public UnaryOperator(String name, Function<Value, Value> function, String... identifiers) {
    XApi.requireNonNull(function, "function");
    XApi.requireNonNull(identifiers, "identifiers");
    XApi.requireNonNull(name, "name");

    this.function = function;
    this.identifiers = new XArrayView<>(identifiers);
    this.name = name;
  }

  public Function<Value, Value> getFunction() {
    return function;
  }

  @Override
  public XView<String> getIdentifiers() {
    return identifiers;
  }

  public final Value operate(Context context, Value value) {
    return getFunction().apply(value);
  }

  @Override
  public String toString() {
    return name;
  }
}