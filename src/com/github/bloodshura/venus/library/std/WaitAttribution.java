//////////////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2016, João Vitor Verona Biazibetti - All Rights Reserved                /
//                                                                                       /
// This program is free software: you can redistribute it and/or modify                  /
// it under the terms of the GNU General Public License as published by                  /
// the Free Software Foundation, either version 3 of the License, or                     /
// (at your option) any later version.                                                   /
//                                                                                       /
// This program is distributed in the hope that it will be useful,                       /
// but WITHOUT ANY WARRANTY; without even the implied warranty of                        /
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the                          /
// GNU General Public License for more details.                                          /
//                                                                                       /
// You should have received a copy of the GNU General Public License                     /
// along with this program. If not, see <http://www.gnu.org/licenses/>.                  /
//                                                                                       /
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
import com.github.bloodshura.x.collection.list.XList;
import com.github.bloodshura.x.collection.list.impl.XArrayList;
import com.github.bloodshura.x.logging.XLogger;

@MethodArgs(Value.class)
@MethodName("wait")
public class WaitAttribution extends VoidMethod {
  @Override
  public void callVoid(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
    Expression expression = descriptor.getExpressions().get(0);
    XList<Variable> list = new XArrayList<>();
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

  private void scan(Context context, Expression expression, XList<Variable> list) throws ScriptRuntimeException {
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