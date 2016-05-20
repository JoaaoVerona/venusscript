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

import br.shura.venus.exception.ScriptRuntimeException;
import br.shura.venus.executor.Context;
import br.shura.venus.function.VoidMethod;
import br.shura.venus.function.annotation.MethodArgs;
import br.shura.venus.function.annotation.MethodName;
import br.shura.venus.resultor.Variable;
import br.shura.venus.value.IntegerValue;
import br.shura.venus.value.Value;
import br.shura.venus.value.ValueType;
import br.shura.venus.value.VariableRefValue;
import br.shura.x.logging.XLogger;

/**
 * Consume.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 16/05/16 - 00:59
 * @since GAMMA - 0x3
 */
@MethodArgs(ValueType.VARIABLE_REFERENCE)
@MethodName("consume")
public class Consume extends VoidMethod {
  @Override
  public void callVoid(Context context, Value... arguments) throws ScriptRuntimeException {
    VariableRefValue reference = (VariableRefValue) arguments[0];
    Variable variable = reference.value();
    Object monitor;

    synchronized ((monitor = context.getMonitor(variable.getName()))) {
      Value value = variable.resolve(context);
      long val = 0;

      if (value instanceof IntegerValue) {
        IntegerValue intValue = (IntegerValue) value;

        val = intValue.value();
      }

      if (val <= 0) {
        try {
          monitor.wait();
          context.setVar(variable.getName(), value.minus(new IntegerValue(1)));
        }
        catch (InterruptedException exception) {
          XLogger.warnln("Thread " + Thread.currentThread() + " interrupted while consumer was waiting.");
        }
      }
    }
  }
}