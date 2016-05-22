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

package br.shura.venus.library.engine;

import br.shura.venus.exception.runtime.InvalidFunctionParameterException;
import br.shura.venus.exception.runtime.ScriptRuntimeException;
import br.shura.venus.exception.runtime.UndefinedFunctionException;
import br.shura.venus.executor.Context;
import br.shura.venus.function.FunctionCallDescriptor;
import br.shura.venus.function.Method;
import br.shura.venus.function.annotation.MethodName;
import br.shura.venus.function.annotation.MethodVarArgs;
import br.shura.venus.value.BoolValue;
import br.shura.venus.value.TypeValue;
import br.shura.venus.value.Value;
import br.shura.venus.value.ValueType;
import br.shura.x.collection.list.List;
import br.shura.x.collection.list.impl.ArrayList;
import br.shura.x.collection.view.BasicView;

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
    List<ValueType> types = new ArrayList<>();

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