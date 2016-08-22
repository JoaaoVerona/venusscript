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

package com.github.bloodshura.venus.expression;

import com.github.bloodshura.venus.exception.runtime.InvalidFunctionParameterException;
import com.github.bloodshura.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.venus.executor.Context;
import com.github.bloodshura.venus.function.Function;
import com.github.bloodshura.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.venus.type.PrimitiveType;
import com.github.bloodshura.venus.type.Type;
import com.github.bloodshura.venus.value.DecimalValue;
import com.github.bloodshura.venus.value.IntegerValue;
import com.github.bloodshura.venus.value.Value;
import com.github.bloodshura.x.collection.list.XList;
import com.github.bloodshura.x.collection.list.impl.XArrayList;
import com.github.bloodshura.x.collection.view.XArrayView;
import com.github.bloodshura.x.collection.view.XBasicView;
import com.github.bloodshura.x.collection.view.XView;

/**
 * FunctionCall.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 17:13
 * @since GAMMA - 0x3
 */
public class FunctionCall implements Expression {
  private final Expression[] arguments;
  private final String functionName;

  public FunctionCall(String functionName, Expression... arguments) {
    this.arguments = arguments;
    this.functionName = functionName;
  }

  public XView<Expression> getArguments() {
    return new XArrayView<>(arguments);
  }

  public String getFunctionName() {
    return functionName;
  }

  @Override
  public Value resolve(Context context) throws ScriptRuntimeException {
    XView<Value> values = getArguments().reduceExceptional(expression -> expression.resolve(context));
    XView<Type> types = values.reduce(Value::getType);
    Function function = context.getOwner().findFunction(context, getFunctionName(), types);
    XList<Value> list = new XArrayList<>();
    int i = 0;

    // This check is necessary because of function references being untyped (issue #9).
    if (!function.isVarArgs() && types.size() != function.getArgumentTypes().size()) {
      throw new InvalidFunctionParameterException(context, "Function \"" + function + "\" expected " +
        function.getArgumentTypes().size() + " arguments; received " + types.size());
    }

    for (Value value : values) {
      if (!function.isVarArgs()) {
        Type required = function.getArgumentTypes().at(i);

        if (value.getType() == PrimitiveType.INTEGER && required == PrimitiveType.DECIMAL) {
          value = new DecimalValue(((IntegerValue) value).value());
        }

        // This check is necessary because of function references being untyped (issue #9).
        if (!required.accepts(value.getType())) {
          throw new InvalidFunctionParameterException(context, "Function \"" + function + "\" expected " +
            required + " as " + (i + 1) + (i == 0 ? "st" : i == 1 ? "nd" : i == 2 ? "rd" : "th") + " argument; received " +
            value.getType());
        }
      }

      list.add(value);
      i++;
    }

    return function.call(context, new FunctionCallDescriptor(this, getArguments(), new XBasicView<>(list)));
  }

  @Override
  public String toString() {
    return "functioncall(" + getFunctionName() + " <-- [" + getArguments().toString(", ") + "])";
  }
}