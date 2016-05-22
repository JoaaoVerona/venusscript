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

package br.shura.venus.resultor;

import br.shura.venus.exception.runtime.InvalidArrayAccessException;
import br.shura.venus.exception.runtime.ScriptRuntimeException;
import br.shura.venus.executor.Context;
import br.shura.venus.value.ArrayValue;
import br.shura.venus.value.Value;

/**
 * ArrayAccess.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 22/05/16 - 02:02
 * @since GAMMA - 0x3
 */
public class ArrayAccess implements Resultor {
  private final Variable array;
  private final int index;

  public ArrayAccess(Variable array, int index) {
    this.array = array;
    this.index = index;
  }

  public Variable getArray() {
    return array;
  }

  public int getIndex() {
    return index;
  }

  @Override
  public Value resolve(Context context) throws ScriptRuntimeException {
    Value value = getArray().resolve(context);

    if (value instanceof ArrayValue) {
      ArrayValue array = (ArrayValue) value;

      return array.get(context, getIndex());
    }

    throw new InvalidArrayAccessException(context, "Variable \"" + getArray().getName() + "\" is of type " +
      value.getType() + "; expected to be an array");
  }

  @Override
  public String toString() {
    return "arr(" + getArray().getName() + '[' + getIndex() + "])";
  }
}
