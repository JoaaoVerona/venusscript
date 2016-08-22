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

package com.github.bloodshura.venus.library.engine;

import com.github.bloodshura.venus.exception.runtime.InvalidFunctionParameterException;
import com.github.bloodshura.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.venus.exception.runtime.UndefinedFunctionException;
import com.github.bloodshura.venus.executor.Context;
import com.github.bloodshura.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.venus.function.Method;
import com.github.bloodshura.venus.function.annotation.MethodName;
import com.github.bloodshura.venus.function.annotation.MethodVarArgs;
import com.github.bloodshura.venus.type.Type;
import com.github.bloodshura.venus.value.BoolValue;
import com.github.bloodshura.venus.value.TypeValue;
import com.github.bloodshura.venus.value.Value;
import com.github.bloodshura.x.collection.list.List;
import com.github.bloodshura.x.collection.list.impl.ArrayList;
import com.github.bloodshura.x.collection.view.BasicView;

/**
 * HasFunction.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 21/05/16 - 00:35
 * @since GAMMA - 0x3
 */
@MethodName("hasFunction")
@MethodVarArgs
public class HasFunction extends Method {
  @Override
  public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
    if (descriptor.isEmpty()) {
      throw new InvalidFunctionParameterException(context, "Expected at least function name");
    }

    String name = descriptor.get(0).toString();
    List<Type> types = new ArrayList<>();

    for (int i = 1; i < descriptor.count(); i++) {
      Value value = descriptor.get(i);

      if (value instanceof TypeValue) {
        TypeValue typeValue = (TypeValue) value;

        types.add(typeValue.value());
      }
      else {
        throw new InvalidFunctionParameterException(context, "Expected value type, received " + value.getType());
      }
    }

    try {
      context.getOwner().findFunction(context, name, new BasicView<>(types));

      return new BoolValue(true);
    }
    catch (UndefinedFunctionException exception) {
      return new BoolValue(false);
    }
  }
}