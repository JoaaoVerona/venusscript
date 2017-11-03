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

package com.github.bloodshura.x.venus.value;

import com.github.bloodshura.x.venus.type.PrimitiveType;

public class DecimalValue extends NumericValue {
  private final double value;

  public DecimalValue(double value) {
    super(PrimitiveType.DECIMAL);
    this.value = value;
  }

  @Override
  public DecimalValue clone() {
    return new DecimalValue(value());
  }

  @Override
  public Integer compareTo(Value value) {
    if (value instanceof NumericValue) {
      NumericValue numeric = (NumericValue) value;

      return value().compareTo(numeric.value().doubleValue());
    }

    return null;
  }

  @Override
  public Value divide(Value value) {
    if (value instanceof NumericValue) {
      NumericValue numeric = (NumericValue) value;

      return new DecimalValue(value() / numeric.value().doubleValue());
    }

    return super.divide(value);
  }

  @Override
  public Value minus(Value value) {
    if (value instanceof NumericValue) {
      NumericValue numeric = (NumericValue) value;

      return new DecimalValue(value() - numeric.value().doubleValue());
    }

    return super.minus(value);
  }

  @Override
  public Value multiply(Value value) {
    if (value instanceof NumericValue) {
      NumericValue numeric = (NumericValue) value;

      return new DecimalValue(value() * numeric.value().doubleValue());
    }

    return super.multiply(value);
  }

  @Override
  public Value negate() {
    return new DecimalValue(-value());
  }

  @Override
  public Value plus(Value value) {
    if (value instanceof NumericValue) {
      NumericValue numeric = (NumericValue) value;

      return new DecimalValue(value() + numeric.value().doubleValue());
    }

    return super.plus(value);
  }

  @Override
  public Value remainder(Value value) {
    if (value instanceof NumericValue) {
      NumericValue numeric = (NumericValue) value;

      return new DecimalValue(value() % numeric.value().doubleValue());
    }

    return super.remainder(value);
  }

  @Override
  public Double value() {
    return value;
  }

  @Override
  public String toString() {
    return Double.toString(value());
  }
}