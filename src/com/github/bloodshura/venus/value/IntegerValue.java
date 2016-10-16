//////////////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2016, João Vitor Verona Biazibetti - All Rights Reserved                /
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

package com.github.bloodshura.venus.value;

import com.github.bloodshura.venus.type.PrimitiveType;

public class IntegerValue extends NumericValue {
  private final long value;

  public IntegerValue(long value) {
    super(PrimitiveType.INTEGER);
    this.value = value;
  }

  @Override
  public Value and(Value value) {
    if (value instanceof IntegerValue) {
      return new IntegerValue(value() & ((IntegerValue) value).value());
    }

    return super.and(value);
  }

  @Override
  public IntegerValue clone() {
    return new IntegerValue(value());
  }

  @Override
  public Integer compareTo(Value value) {
    if (value instanceof NumericValue) {
      NumericValue numeric = (NumericValue) value;

      return value().compareTo(numeric.value().longValue());
    }

    return super.compareTo(value);
  }

  @Override
  public Value divide(Value value) {
    if (value instanceof NumericValue) {
      NumericValue numeric = (NumericValue) value;

      return new IntegerValue(value() / numeric.value().longValue());
    }

    return super.divide(value);
  }

  @Override
  public Value minus(Value value) {
    if (value instanceof NumericValue) {
      NumericValue numeric = (NumericValue) value;

      return new IntegerValue(value() - numeric.value().longValue());
    }

    return super.minus(value);
  }

  @Override
  public Value multiply(Value value) {
    if (value instanceof NumericValue) {
      NumericValue numeric = (NumericValue) value;

      return new IntegerValue(value() * numeric.value().longValue());
    }

    return super.multiply(value);
  }

  @Override
  public Value negate() {
    return new IntegerValue(-value());
  }

  @Override
  public Value or(Value value) {
    if (value instanceof IntegerValue) {
      return new IntegerValue(value() | ((IntegerValue) value).value());
    }

    return super.or(value);
  }

  @Override
  public Value plus(Value value) {
    if (value instanceof NumericValue) {
      NumericValue numeric = (NumericValue) value;

      return new IntegerValue(value() + numeric.value().longValue());
    }

    return super.plus(value);
  }

  @Override
  public Value remainder(Value value) {
    if (value instanceof NumericValue) {
      NumericValue numeric = (NumericValue) value;

      return new IntegerValue(value() % numeric.value().longValue());
    }

    return super.remainder(value);
  }

  @Override
  public Value shiftLeft(Value value) {
    if (value instanceof IntegerValue) {
      IntegerValue integer = (IntegerValue) value;

      return new IntegerValue(value() << integer.value());
    }

    return super.shiftLeft(value);
  }

  @Override
  public Value shiftRight(Value value) {
    if (value instanceof IntegerValue) {
      IntegerValue integer = (IntegerValue) value;

      return new IntegerValue(value() >> integer.value());
    }

    return super.shiftRight(value);
  }

  @Override
  public String toString() {
    return Long.toString(value());
  }

  @Override
  public Long value() {
    return value;
  }
}