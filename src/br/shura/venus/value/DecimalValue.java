//////////////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2016, João Vitor Verona Biazibetti - All Rights Reserved                /
//                                                                                       /
// Licensed under the GNU General Public License v3;                                     /
// you may not use this file except in compliance with the License.                      /
//                                                                                       /
// You may obtain a copy of the License at                                               /
//     http://www.gnu.org/licenses/gpl.html                                              /
//                                                                                       /
// Unless required by applicable law or agreed to in writing, software                   /
// distributed under the License is distributed on an "AS IS" BASIS,                     /
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.              /
// See the License for the specific language governing permissions and                   /
// limitations under the License.                                                        /
//                                                                                       /
// Written by João Vitor Verona Biazibetti <joaaoverona@gmail.com>, March 2016           /
// https://www.github.com/BloodShura                                                     /
//////////////////////////////////////////////////////////////////////////////////////////

package br.shura.venus.value;

import br.shura.venus.type.PrimitiveType;

/**
 * DecimalValue.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 08/05/16 - 18:41
 * @since GAMMA - 0x3
 */
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