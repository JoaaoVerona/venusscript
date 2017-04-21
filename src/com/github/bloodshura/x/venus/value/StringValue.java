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

import com.github.bloodshura.x.util.XApi;
import com.github.bloodshura.x.venus.type.PrimitiveType;

public class StringValue extends Value {
  private final String value;

  public StringValue(String value) {
    super(PrimitiveType.STRING);
    XApi.requireNonNull(value, "value");

    this.value = value;
  }

  @Override
  public StringValue clone() {
    return new StringValue(value());
  }

  @Override
  public Integer compareTo(Value value) {
    if (value instanceof StringValue) {
      StringValue string = (StringValue) value;

      return value().compareToIgnoreCase(string.value());
    }

    return null;
  }

  public boolean isCharacter() {
    return value().length() == 1;
  }

  @Override
  public StringValue plus(Value value) {
    return new StringValue(value() + value);
  }

  public char toCharacter() {
    XApi.requireState(isCharacter(), "Cannot convert multi character StringValue to single character");

    return value().charAt(0);
  }

  @Override
  public String toString() {
    return value();
  }

  @Override
  public String value() {
    return value;
  }
}