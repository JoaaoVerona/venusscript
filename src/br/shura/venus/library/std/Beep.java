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

package br.shura.venus.library.std;

import br.shura.venus.component.function.Method;
import br.shura.venus.component.function.annotation.MethodArgs;
import br.shura.venus.component.function.annotation.MethodName;
import br.shura.venus.exception.ScriptRuntimeException;
import br.shura.venus.executor.Context;
import br.shura.venus.value.DecimalValue;
import br.shura.venus.value.IntegerValue;
import br.shura.venus.value.Value;
import br.shura.venus.value.ValueType;
import br.shura.x.sys.XSystem;

/**
 * Beep.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 13/05/16 - 17:37
 * @since GAMMA - 0x3
 */
@MethodArgs({ ValueType.DECIMAL, ValueType.INTEGER })
@MethodName("beep")
public class Beep extends Method {
  @Override
  public Value call(Context context, Value... arguments) throws ScriptRuntimeException {
    DecimalValue frequency = (DecimalValue) arguments[0];
    IntegerValue duration = (IntegerValue) arguments[1];

    XSystem.getDesktop().beep(frequency.value(), duration.value());

    return null;
  }
}