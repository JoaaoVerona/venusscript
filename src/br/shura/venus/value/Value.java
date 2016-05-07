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

import br.shura.venus.Context;
import br.shura.venus.exception.InvalidValueTypeException;
import br.shura.venus.exception.ScriptRuntimeException;

import java.math.BigDecimal;

/**
 * Value.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 05/05/16 - 14:42
 * @since GAMMA - 0x3
 */
public abstract class Value {
  public final boolean equals(Context context, Value object) throws ScriptRuntimeException {
    return resolve(context).equals(object.resolve(context));
  }

  public abstract Object resolve(Context context) throws ScriptRuntimeException;

  public final ValueType resolveType(Context context) throws ScriptRuntimeException {
    return ValueType.forClass(resolve(context).getClass());
  }

  public final boolean toBooleanState(Context context) throws ScriptRuntimeException {
    Object value = resolve(context);

    return (value instanceof Boolean && (Boolean) value) ||
      (value instanceof Number && ((Number) value).longValue() > 0);
  }

  public final BigDecimal toNumericState(Context context) throws ScriptRuntimeException {
    Object value = resolve(context);

    if (value instanceof BigDecimal) {
      return (BigDecimal) value;
    }

    throw new InvalidValueTypeException(context, "//TODOmsg// toNumericState nope");
  }

  @Override
  public abstract String toString();
}