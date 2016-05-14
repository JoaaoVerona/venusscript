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

import br.shura.venus.exception.ScriptRuntimeException;
import br.shura.venus.executor.Context;
import br.shura.venus.value.Value;
import br.shura.venus.value.ValueType;
import br.shura.x.collection.view.View;
import br.shura.x.lang.INameable;
import br.shura.x.util.layer.XApi;

/**
 * Function.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 02:24
 * @since GAMMA - 0x3
 */
public interface Function extends INameable {
  default boolean accepts(String name, View<ValueType> argumentTypes) {
    XApi.requireNonNull(name, "name");

    if (getName().equals(name)) {
      if (argumentTypes == null) {
        return true;
      }

      if (getArgumentCount() == argumentTypes.size()) {
        for (int i = 0; i < getArgumentCount(); i++) {
          ValueType required = getArgumentTypes().at(i);
          ValueType found = argumentTypes.at(i);

          if (!required.accepts(found) && (required != ValueType.DECIMAL || found != ValueType.INTEGER)) {
            return false;
          }
        }

        return true;
      }
      else if (isVarArgs()) {
        return true;
      }
    }

    return false;
  }

  Value call(Context context, Value... arguments) throws ScriptRuntimeException;

  default int getArgumentCount() {
    return getArgumentTypes().size();
  }

  View<ValueType> getArgumentTypes();

  boolean isVarArgs();
}