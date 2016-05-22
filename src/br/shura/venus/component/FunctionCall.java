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

package br.shura.venus.component;

import br.shura.venus.exception.runtime.InvalidFunctionParameterException;
import br.shura.venus.exception.runtime.ScriptRuntimeException;
import br.shura.venus.executor.Context;
import br.shura.venus.function.Function;
import br.shura.venus.function.FunctionCallDescriptor;
import br.shura.venus.resultor.Resultor;
import br.shura.venus.value.DecimalValue;
import br.shura.venus.value.IntegerValue;
import br.shura.venus.value.Value;
import br.shura.venus.value.ValueType;
import br.shura.x.collection.list.List;
import br.shura.x.collection.list.impl.ArrayList;
import br.shura.x.collection.view.ArrayView;
import br.shura.x.collection.view.BasicView;
import br.shura.x.collection.view.View;

/**
 * FunctionCall.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 17:13
 * @since GAMMA - 0x3
 */
public class FunctionCall extends Component implements Resultor {
  private final Resultor[] arguments;
  private final String functionName;

  public FunctionCall(String functionName, Resultor... arguments) {
    this.arguments = arguments;
    this.functionName = functionName;
  }

  public View<Resultor> getArguments() {
    return new ArrayView<>(arguments);
  }

  public String getFunctionName() {
    return functionName;
  }

  @Override
  public Value resolve(Context context) throws ScriptRuntimeException {
    View<Value> values = getArguments().reduceExceptional(resultor -> resultor.resolve(context));
    View<ValueType> types = values.reduce(Value::getType);
    Function function = context.getOwner().findFunction(context, getFunctionName(), types);
    List<Value> list = new ArrayList<>();
    int i = 0;

    // This check is necessary because of function references being untyped (issue #9).
    if (!function.isVarArgs() && types.size() != function.getArgumentTypes().size()) {
      throw new InvalidFunctionParameterException(context, "Function \"" + function + "\" expected " +
        function.getArgumentTypes().size() + " arguments; received " + types.size());
    }

    for (Value value : values) {
      if (!function.isVarArgs()) {
        ValueType required = function.getArgumentTypes().at(i);

        if (value.getType() == ValueType.INTEGER && required == ValueType.DECIMAL) {
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

    return function.call(context, new FunctionCallDescriptor(this, getArguments(), new BasicView<>(list)));
  }

  @Override
  public String toString() {
    return "functioncall(" + getFunctionName() + " <-- [" + getArguments().toString(", ") + "])";
  }
}