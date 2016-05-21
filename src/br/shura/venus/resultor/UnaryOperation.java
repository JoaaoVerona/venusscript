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

package br.shura.venus.resultor;

import br.shura.venus.exception.IncompatibleTypesException;
import br.shura.venus.exception.ScriptRuntimeException;
import br.shura.venus.executor.Context;
import br.shura.venus.operator.UnaryOperator;
import br.shura.venus.value.Value;

/**
 * UnaryOperation.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 12/05/16 - 00:20
 * @since GAMMA - 0x3
 */
public class UnaryOperation implements Resultor {
  private final UnaryOperator operator;
  private final Resultor resultor;

  public UnaryOperation(UnaryOperator operator, Resultor resultor) {
    this.operator = operator;
    this.resultor = resultor;
  }

  public UnaryOperator getOperator() {
    return operator;
  }

  public Resultor getResultor() {
    return resultor;
  }

  @Override
  public Value resolve(Context context) throws ScriptRuntimeException {
    Value value = getResultor().resolve(context);
    Value result = getOperator().operate(context, value);

    if (result == null) {
      throw new IncompatibleTypesException(context, "Operator " + getOperator() + " cannot be applied with " + value.getType());
    }

    return result;
  }

  @Override
  public String toString() {
    return "operation(" + getOperator() + " [" + getResultor() + "])";
  }
}