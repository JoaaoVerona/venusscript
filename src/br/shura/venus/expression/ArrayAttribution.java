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

package br.shura.venus.expression;

import br.shura.venus.exception.runtime.InvalidArrayAccessException;
import br.shura.venus.exception.runtime.ScriptRuntimeException;
import br.shura.venus.executor.Context;
import br.shura.venus.type.PrimitiveType;
import br.shura.venus.value.ArrayValue;
import br.shura.venus.value.IntegerValue;
import br.shura.venus.value.Value;

/**
 * ArrayAttribution.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 22/05/16 - 02:17
 * @since GAMMA - 0x3
 */
public class ArrayAttribution implements Expression {
  private final String name;
  private final Expression index;
  private final Expression expression;

  public ArrayAttribution(String name, Expression index, Expression expression) {
    this.index = index;
    this.name = name;
    this.expression = expression;
  }

  public Expression getIndex() {
    return index;
  }

  public String getName() {
    return name;
  }

  public Expression getExpression() {
    return expression;
  }

  @Override
  public Value resolve(Context context) throws ScriptRuntimeException {
    Value value = context.getVarValue(getName());

    if (value instanceof ArrayValue) {
      ArrayValue array = (ArrayValue) value;
      Value index = getIndex().resolve(context);

      if (index instanceof IntegerValue) {
        IntegerValue intIndex = (IntegerValue) index;
        Value result = getExpression().resolve(context);

        array.set(context, intIndex.value().intValue(), result);

        return result;
      }

      throw new InvalidArrayAccessException(context, "Index \"" + index + "\" is of type " +
        index.getType() + "; expected to be an " + PrimitiveType.INTEGER);
    }

    throw new InvalidArrayAccessException(context, "Variable \"" + getName() + "\" is of type " +
      value.getType() + "; expected to be an " + PrimitiveType.ARRAY);
  }

  @Override
  public String toString() {
    return "arrayAttribution(" + getName() + '[' + getIndex() + "]=" + getExpression() + ')';
  }
}