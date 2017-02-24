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

import com.github.bloodshura.x.venus.type.Type;

public abstract class Value implements Cloneable {
  private final Type type;

  public Value(Type type) {
    this.type = type;
  }

  public Value and(Value value) {
    return null;
  }

  @Override
  public abstract Value clone();

  public Integer compareTo(Value value) {
    return null;
  }

  public Value divide(Value value) {
    return null;
  }

  public BoolValue equals(Value value) {
    return new BoolValue(value().equals(value.value()));
  }

  public final Type getType() {
    return type;
  }

  public Value higherEqualThan(Value value) {
    Integer comparation = compareTo(value);

    return comparation != null ? new BoolValue(comparation >= 0) : null;
  }

  public Value higherThan(Value value) {
    Integer comparation = compareTo(value);

    return comparation != null ? new BoolValue(comparation > 0) : null;
  }

  public Value lowerEqualThan(Value value) {
    Integer comparation = compareTo(value);

    return comparation != null ? new BoolValue(comparation <= 0) : null;
  }

  public Value lowerThan(Value value) {
    Integer comparation = compareTo(value);

    return comparation != null ? new BoolValue(comparation < 0) : null;
  }

  public Value minus(Value value) {
    return null;
  }

  public Value multiply(Value value) {
    return null;
  }

  public Value negate() {
    return null;
  }

  public Value not() {
    return null;
  }

  public Value or(Value value) {
    return null;
  }

  public Value plus(Value value) {
    if (value instanceof StringValue) {
      StringValue string = (StringValue) value;

      return new StringValue(value() + string.value());
    }

    return null;
  }

  public Value remainder(Value value) {
    return null;
  }

  public Value shiftLeft(Value value) {
    return null;
  }

  public Value shiftRight(Value value) {
    return null;
  }

  public abstract Object value();

  // TODO Not OO, but fast (no need to use Reflection, etc)
  public static Value construct(Object object) {
    if (object instanceof Boolean) {
      return new BoolValue((Boolean) object);
    }

    if (object instanceof CharSequence) {
      return new StringValue(object.toString());
    }

    if (object instanceof Double || object instanceof Float) {
      return new DecimalValue((Double) object);
    }

    if (object instanceof Number) {
      return new IntegerValue(((Number) object).longValue());
    }

    if (object instanceof Type) {
      return new TypeValue((Type) object);
    }

    return null;
  }
}