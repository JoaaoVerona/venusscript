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

package com.github.bloodshura.x.venus.value;

import com.github.bloodshura.x.venus.compiler.KeywordDefinitions;
import com.github.bloodshura.x.venus.type.PrimitiveType;

public class BoolValue extends Value {
  private final boolean value;

  public BoolValue(boolean value) {
    super(PrimitiveType.BOOLEAN);
    this.value = value;
  }

  @Override
  public Value and(Value value) {
    if (value instanceof BoolValue) {
      BoolValue bool = (BoolValue) value;

      return new BoolValue(value() && bool.value());
    }

    return super.and(value);
  }

  @Override
  public BoolValue clone() {
    return new BoolValue(value());
  }

  @Override
  public Value not() {
    return new BoolValue(!value());
  }

  @Override
  public Value or(Value value) {
    if (value instanceof BoolValue) {
      BoolValue bool = (BoolValue) value;

      return new BoolValue(value() || bool.value());
    }

    return super.or(value);
  }

  @Override
  public String toString() {
    return value() ? KeywordDefinitions.TRUE : KeywordDefinitions.FALSE;
  }

  @Override
  public Boolean value() {
    return value;
  }
}