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

import br.shura.venus.exception.runtime.InvalidArrayAccessException;
import br.shura.venus.exception.runtime.ScriptRuntimeException;
import br.shura.venus.executor.Context;
import br.shura.x.charset.build.TextBuilder;
import br.shura.x.util.Pool;
import br.shura.x.util.comparator.SimpleEqualizer;
import br.shura.x.util.iterator.ArrayIterator;
import br.shura.x.util.layer.XApi;

import java.util.Iterator;

/**
 * ArrayValue.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 22/05/16 - 02:05
 * @since GAMMA - 0x3
 */
public class ArrayValue implements IterableValue {
  private final Value[] values;

  public ArrayValue(int size) {
    this(new Value[size]);
  }

  public ArrayValue(Value... values) {
    XApi.requireNonNull(values, "values");

    this.values = values;
  }

  @Override
  public BoolValue equals(Value value) {
    if (value instanceof ArrayValue) {
      ArrayValue array = (ArrayValue) value;

      return new BoolValue(size() == array.size() && SimpleEqualizer.compare(value(), array.value()));
    }

    return new BoolValue(false);
  }

  public Value get(Context context, int index) throws ScriptRuntimeException {
    if (index < 0 || index >= size()) {
      throw new InvalidArrayAccessException(context, "Out of range array index: " + index + ", expected between 0~" + (size() - 1));
    }

    return value()[index];
  }

  @Override
  public Iterator<Value> iterator() {
    return new ArrayIterator<>(value());
  }

  public void set(Context context, int index, Value value) throws ScriptRuntimeException {
    if (index < 0 || index >= size()) {
      throw new InvalidArrayAccessException(context, "Out of range array index: " + index + ", expected between 0~" + (size() - 1));
    }

    value()[index] = value;
  }

  public int size() {
    return value().length;
  }

  @Override
  public String toString() {
    return toString(this);
  }

  @Override
  public Value[] value() {
    return values;
  }

  private static <E> String toString(ArrayValue array) {
    TextBuilder builder = Pool.newBuilder().setSeparator(", ");

    for (Value value : array.value()) {
      if (value instanceof ArrayValue) {
        builder.append('[' + toString((ArrayValue) value) + ']');
      }
      else {
        builder.append(value);
      }
    }

    return '[' + builder.toString() + ']';
  }
}