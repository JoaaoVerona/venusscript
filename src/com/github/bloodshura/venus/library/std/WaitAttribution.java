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

package com.github.bloodshura.venus.library.std;

import com.github.bloodshura.venus.exception.runtime.InvalidFunctionParameterException;
import com.github.bloodshura.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.venus.executor.Context;
import com.github.bloodshura.venus.expression.BinaryOperation;
import com.github.bloodshura.venus.expression.Expression;
import com.github.bloodshura.venus.expression.FunctionCall;
import com.github.bloodshura.venus.expression.UnaryOperation;
import com.github.bloodshura.venus.expression.Variable;
import com.github.bloodshura.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.venus.function.VoidMethod;
import com.github.bloodshura.venus.function.annotation.MethodArgs;
import com.github.bloodshura.venus.function.annotation.MethodName;
import com.github.bloodshura.venus.value.BoolValue;
import com.github.bloodshura.venus.value.Value;
import com.github.bloodshura.x.collection.list.List;
import com.github.bloodshura.x.collection.list.impl.ArrayList;
import com.github.bloodshura.x.logging.XLogger;

/**
 * WaitAttribution.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 20/05/16 - 20:19
 * @since GAMMA - 0x3
 */
@MethodArgs(Value.class)
@MethodName("wait")
public class WaitAttribution extends VoidMethod {
  @Override
  public void callVoid(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
    Expression expression = descriptor.getExpressions().at(0);
    List<Variable> list = new ArrayList<>();
    Object lock = new Object();

    scan(context, expression, list);
    list.forEachExceptional(variable -> context.getVar(variable).addChangeMonitor(lock));

    Value value = expression.resolve(context); // Maybe value changed after it was resolved.

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

      value = expression.resolve(context);
    }

    list.forEachExceptional(variable -> context.getVar(variable).removeChangeMonitor(lock));
  }

  private void scan(Context context, Expression expression, List<Variable> list) throws ScriptRuntimeException {
    if (expression instanceof BinaryOperation) {
      BinaryOperation operation = (BinaryOperation) expression;

      scan(context, operation.getLeft(), list);
      scan(context, operation.getRight(), list);
    }
    else if (expression instanceof FunctionCall) {
      throw new InvalidFunctionParameterException(context, "Cannot embed a function call on arguments for 'wait' method");
    }
    else if (expression instanceof UnaryOperation) {
      UnaryOperation operation = (UnaryOperation) expression;

      scan(context, operation.getExpression(), list);
    }
    else if (expression instanceof Variable) {
      list.add((Variable) expression);
    }
  }
}