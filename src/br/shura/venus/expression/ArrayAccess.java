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
import br.shura.x.util.layer.XApi;

/**
 * ArrayAccess.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 22/05/16 - 02:02
 * @since GAMMA - 0x3
 */
public class ArrayAccess implements Expression {
  private final Expression index;
  private final String name;

  public ArrayAccess(String name, Expression index) {
    XApi.requireNonNull(index, "index");
    XApi.requireNonNull(name, "name");

    this.index = index;
    this.name = name;
  }

  public Expression getIndex() {
    return index;
  }

  public String getName() {
    return name;
  }

  @Override
  public Value resolve(Context context) throws ScriptRuntimeException {
    Value value = context.getVarValue(getName());

    if (value instanceof ArrayValue) {
      ArrayValue array = (ArrayValue) value;
      Value index = getIndex().resolve(context);

      if (index instanceof IntegerValue) {
        IntegerValue intIndex = (IntegerValue) index;

        return array.get(context, intIndex.value().intValue());
      }

      throw new InvalidArrayAccessException(context, "Index \"" + index + "\" is of type " +
        index.getType() + "; expected to be an " + PrimitiveType.INTEGER);
    }

    throw new InvalidArrayAccessException(context, "Variable \"" + getName() + "\" is of type " +
      value.getType() + "; expected to be an " + PrimitiveType.ARRAY);
  }

  @Override
  public String toString() {
    return "arr(" + getName() + '[' + getIndex() + "])";
  }
}
