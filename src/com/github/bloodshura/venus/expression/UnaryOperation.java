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

import com.github.bloodshura.venus.exception.runtime.IncompatibleTypesException;
import com.github.bloodshura.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.venus.executor.Context;
import com.github.bloodshura.venus.operator.UnaryOperator;
import com.github.bloodshura.venus.value.Value;
import com.github.bloodshura.x.util.layer.XApi;

/**
 * UnaryOperation.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 12/05/16 - 00:20
 * @since GAMMA - 0x3
 */
public class UnaryOperation implements Expression {
  private final UnaryOperator operator;
  private final Expression expression;

  public UnaryOperation(UnaryOperator operator, Expression expression) {
    XApi.requireNonNull(expression, "expression");
    XApi.requireNonNull(operator, "operator");

    this.operator = operator;
    this.expression = expression;
  }

  public UnaryOperator getOperator() {
    return operator;
  }

  public Expression getExpression() {
    return expression;
  }

  @Override
  public Value resolve(Context context) throws ScriptRuntimeException {
    Value value = getExpression().resolve(context);
    Value result = getOperator().operate(context, value);

    if (result == null) {
      throw new IncompatibleTypesException(context, "Operator " + getOperator() + " cannot be applied with " + value.getType());
    }

    return result;
  }

  @Override
  public String toString() {
    return "unioperation(" + getOperator() + " [" + getExpression() + "])";
  }
}