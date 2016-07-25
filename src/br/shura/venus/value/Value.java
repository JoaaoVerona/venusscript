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

import br.shura.venus.type.Type;

/**
 * Value.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 08/05/16 - 18:33
 * @since GAMMA - 0x3
 */
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