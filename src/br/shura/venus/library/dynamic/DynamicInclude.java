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

package br.shura.venus.library.dynamic;

import br.shura.venus.component.Script;
import br.shura.venus.exception.ScriptCompileException;
import br.shura.venus.exception.ScriptRuntimeException;
import br.shura.venus.executor.Context;
import br.shura.venus.function.FunctionCallDescriptor;
import br.shura.venus.function.VoidMethod;
import br.shura.venus.function.annotation.MethodArgs;
import br.shura.venus.function.annotation.MethodName;
import br.shura.venus.value.BoolValue;
import br.shura.venus.value.StringValue;
import br.shura.venus.value.ValueType;

/**
 * DynamicInclude.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 14/05/16 - 00:16
 * @since GAMMA - 0x3
 */
@MethodArgs({ ValueType.STRING, ValueType.BOOLEAN })
@MethodName("dynamicInclude")
public class DynamicInclude extends VoidMethod {
  @Override
  public void callVoid(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
    StringValue includeName = (StringValue) descriptor.get(0);
    BoolValue maybe = (BoolValue) descriptor.get(1);
    Script script = context.getOwner().getScript();

    try {
      script.include(includeName.value(), maybe.value());
    }
    catch (ScriptCompileException exception) {
      throw new ScriptRuntimeException(context, "Could not include script: " + exception.getMessage(), exception.getCause());
    }
  }
}