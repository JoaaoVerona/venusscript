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

package br.shura.venus.component.function;

import br.shura.venus.Context;
import br.shura.venus.exception.InvalidFunctionParameterException;
import br.shura.venus.exception.ScriptRuntimeException;
import br.shura.venus.value.Value;
import br.shura.venus.value.ValueType;
import br.shura.x.collection.view.View;
import br.shura.x.lang.INameable;

/**
 * Function.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 02:24
 * @since GAMMA - 0x3
 */
public interface Function extends INameable {
  Value call(Context context, Value... arguments) throws ScriptRuntimeException;

  default int getArgumentCount() {
    return getArgumentTypes().size();
  }

  View<ValueType> getArgumentTypes();

  boolean isVarArgs();

  default void validateArguments(Context context, Value... arguments) throws ScriptRuntimeException {
    if (isVarArgs()) {
      return;
    }

    if (getArgumentCount() != arguments.length) {
      throw new InvalidFunctionParameterException(context, this, "Function " + this + " takes " + getArgumentCount() + " arguments, but passed " + arguments.length);
    }

    for (int i = 0; i < arguments.length; i++) {
      ValueType required = getArgumentTypes().at(i);
      ValueType found = ValueType.forValue(arguments[i]);

      if (!required.accepts(found)) {
        throw new InvalidFunctionParameterException(context, this, "Function " + this + " expected " + required +
          " as " + (i + 1) + (i == 0 ? "st" : i == 1 ? "nd" : i == 2 ? "rd" : "th") + " argument, but passed " + found);
      }
    }
  }
}