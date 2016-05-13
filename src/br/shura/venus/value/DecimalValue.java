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

/**
 * DecimalValue.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 08/05/16 - 18:41
 * @since GAMMA - 0x3
 */
public class DecimalValue implements NumericValue {
  private final double value;

  public DecimalValue(double value) {
    this.value = value;
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
  public DecimalValue divide(Value value) {
    if (value instanceof NumericValue) {
      NumericValue numeric = (NumericValue) value;

      return new DecimalValue(value() / numeric.value().doubleValue());
    }

    return null;
  }

  @Override
  public DecimalValue minus(Value value) {
    if (value instanceof NumericValue) {
      NumericValue numeric = (NumericValue) value;

      return new DecimalValue(value() - numeric.value().doubleValue());
    }

    return null;
  }

  @Override
  public DecimalValue multiply(Value value) {
    if (value instanceof NumericValue) {
      NumericValue numeric = (NumericValue) value;

      return new DecimalValue(value() * numeric.value().doubleValue());
    }

    return null;
  }

  @Override
  public DecimalValue negate() {
    return new DecimalValue(-value());
  }

  @Override
  public DecimalValue plus(Value value) {
    if (value instanceof NumericValue) {
      NumericValue numeric = (NumericValue) value;

      return new DecimalValue(value() + numeric.value().doubleValue());
    }

    return null;
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