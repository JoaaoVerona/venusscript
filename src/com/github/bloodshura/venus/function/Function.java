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

package com.github.bloodshura.venus.function;

import com.github.bloodshura.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.venus.executor.Context;
import com.github.bloodshura.venus.type.PrimitiveType;
import com.github.bloodshura.venus.type.Type;
import com.github.bloodshura.venus.value.Value;
import com.github.bloodshura.x.collection.view.View;
import com.github.bloodshura.x.lang.Nameable;
import com.github.bloodshura.x.util.layer.XApi;

/**
 * Function.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 02:24
 * @since GAMMA - 0x3
 */
public interface Function extends Nameable {
  default boolean accepts(String name, View<Type> argumentTypes) {
    XApi.requireNonNull(name, "name");

    if (getName().equals(name)) {
      if (argumentTypes == null) {
        return true;
      }

      if (getArgumentCount() == argumentTypes.size()) {
        for (int i = 0; i < getArgumentCount(); i++) {
          Type required = getArgumentTypes().at(i);
          Type found = argumentTypes.at(i);

          if (!required.accepts(found) && (required != PrimitiveType.DECIMAL || found != PrimitiveType.INTEGER)) {
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

  Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException;

  default int getArgumentCount() {
    return getArgumentTypes().size();
  }

  View<Type> getArgumentTypes();

  boolean isVarArgs();
}