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

import br.shura.x.util.layer.XApi;

/**
 * Value.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 08/05/16 - 18:33
 * @since GAMMA - 0x3
 */
public interface Value {
  default Value and(Value value) {
    return null;
  }

  default Integer compareTo(Value value) {
    return null;
  }

  default NumericValue divide(Value value) {
    return null;
  }

  default BoolValue equals(Value value) {
    return new BoolValue(value.value().equals(value()));
  }

  default ValueType getType() {
    return ValueType.forValue(this);
  }

  default BoolValue higherEqualThan(Value value) {
    Integer comparation = compareTo(value);

    return comparation != null ? new BoolValue(comparation >= 0) : null;
  }

  default BoolValue higherThan(Value value) {
    Integer comparation = compareTo(value);

    return comparation != null ? new BoolValue(comparation > 0) : null;
  }

  default BoolValue lowerEqualThan(Value value) {
    Integer comparation = compareTo(value);

    return comparation != null ? new BoolValue(comparation <= 0) : null;
  }

  default BoolValue lowerThan(Value value) {
    Integer comparation = compareTo(value);

    return comparation != null ? new BoolValue(comparation < 0) : null;
  }

  default NumericValue minus(Value value) {
    return null;
  }

  default NumericValue multiply(Value value) {
    return null;
  }

  default Value negate() {
    return null;
  }

  default Value not() {
    return null;
  }

  default Value or(Value value) {
    return null;
  }

  default Value plus(Value value) {
    return null;
  }

  Object value();

  static Value clone(Value value) {
    Value result = construct(value.value());

    XApi.require(result != null, "Could not clone value of type " + value.getType());

    return result;
  }

  // TODO Not OO, but fast (no need to use Reflection, etc)
  static Value construct(Object object) {
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
      return new IntegerValue((Long) object);
    }

    if (object instanceof ValueType) {
      return new TypeValue((ValueType) object);
    }

    return null;
  }
}