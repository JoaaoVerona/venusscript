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

import br.shura.venus.exception.runtime.InvalidFunctionParameterException;
import br.shura.venus.exception.runtime.ScriptRuntimeException;
import br.shura.venus.executor.Context;
import br.shura.venus.function.FunctionCallDescriptor;
import br.shura.venus.function.VoidMethod;
import br.shura.venus.function.annotation.MethodArgs;
import br.shura.venus.function.annotation.MethodName;
import br.shura.venus.resultor.BinaryOperation;
import br.shura.venus.resultor.FunctionCall;
import br.shura.venus.resultor.Resultor;
import br.shura.venus.resultor.UnaryOperation;
import br.shura.venus.resultor.Variable;
import br.shura.venus.value.BoolValue;
import br.shura.venus.value.Value;
import br.shura.venus.value.ValueType;
import br.shura.x.collection.list.List;
import br.shura.x.collection.list.impl.ArrayList;
import br.shura.x.logging.XLogger;

/**
 * WaitAttribution.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 20/05/16 - 20:19
 * @since GAMMA - 0x3
 */
@MethodArgs(ValueType.ANY)
@MethodName("wait")
public class WaitAttribution extends VoidMethod {
  @Override
  public void callVoid(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
    Resultor resultor = descriptor.getResultors().at(0);
    List<Variable> list = new ArrayList<>();
    Object lock = new Object();

    scan(context, resultor, list);
    list.forEachExceptional(variable -> context.getStructure(variable).addChangeMonitor(lock));

    Value value = resultor.resolve(context); // Maybe value changed after it was resolved.

    while (!(value instanceof BoolValue && ((BoolValue) value).value())) {
      try {
        synchronized (lock) {
          lock.wait();
        }
      }
      catch (InterruptedException exception) {
        XLogger.warnln("Thread " + Thread.currentThread() + " interrupted while 'wait' was locking.");

        break;
      }

      value = resultor.resolve(context);
    }

    list.forEachExceptional(variable -> context.getStructure(variable).removeChangeMonitor(lock));
  }

  private void scan(Context context, Resultor resultor, List<Variable> list) throws ScriptRuntimeException {
    if (resultor instanceof BinaryOperation) {
      BinaryOperation operation = (BinaryOperation) resultor;

      scan(context, operation.getLeft(), list);
      scan(context, operation.getRight(), list);
    }
    else if (resultor instanceof FunctionCall) {
      throw new InvalidFunctionParameterException(context, "Cannot embed a function call on arguments for 'wait' method");
    }
    else if (resultor instanceof UnaryOperation) {
      UnaryOperation operation = (UnaryOperation) resultor;

      scan(context, operation.getResultor(), list);
    }
    else if (resultor instanceof Variable) {
      list.add((Variable) resultor);
    }
  }
}