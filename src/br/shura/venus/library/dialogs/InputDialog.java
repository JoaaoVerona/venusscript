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

package br.shura.venus.library.dialogs;

import br.shura.uiset.dialog.XDialogs;
import br.shura.venus.component.function.Method;
import br.shura.venus.component.function.annotation.MethodName;
import br.shura.venus.component.function.annotation.MethodVarArgs;
import br.shura.venus.exception.ScriptRuntimeException;
import br.shura.venus.executor.Context;
import br.shura.venus.value.StringValue;
import br.shura.venus.value.Value;
import br.shura.x.charset.build.TextBuilder;
import br.shura.x.util.Pool;

/**
 * InputDialog.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 14/05/16 - 19:52
 * @since GAMMA - 0x3
 */
@MethodName("inputDialog")
@MethodVarArgs
public class InputDialog extends Method {
  @Override
  public Value call(Context context, Value... arguments) throws ScriptRuntimeException {
    if (arguments.length == 0) {
      return new StringValue(Pool.EMPTY_STRING);
    }

    String title = arguments.length > 1 ? arguments[0].toString() : null;
    TextBuilder message = Pool.newBuilder();

    for (int i = arguments.length > 1 ? 1 : 0; i < arguments.length; i++) {
      message.append(arguments[i]);
      message.newLine();
    }

    String input = XDialogs.askInput(title, message);

    return new StringValue(input != null ? input : Pool.EMPTY_STRING);
  }
}