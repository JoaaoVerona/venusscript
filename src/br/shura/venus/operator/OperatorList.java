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

package br.shura.venus.operator;

import br.shura.venus.value.Constant;
import br.shura.x.collection.view.View;
import br.shura.x.worker.enumeration.Enumerations;

import java.math.RoundingMode;

/**
 * OperatorList.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 07/05/16 - 19:49
 * @since GAMMA - 0x3
 */
public class OperatorList {
  public static final Operator AND = new Operator("and", "&&",
    (context, left, right) -> new Constant(left.toBooleanState(context) && right.toBooleanState(context)));
  public static final Operator DIVIDE = new Operator("divide", "/", // TODO See RoundingMode and test
    (context, left, right) -> new Constant(left.toNumericState(context).divide(right.toNumericState(context), RoundingMode.FLOOR)));
  public static final Operator MINUS = new Operator("minus", "-",
    (context, left, right) -> new Constant(left.toNumericState(context).subtract(right.toNumericState(context))));
  public static final Operator MULTIPLY = new Operator("multiply", "*",
    (context, left, right) -> new Constant(left.toNumericState(context).multiply(right.toNumericState(context))));
  public static final Operator OR = new Operator("or", "||",
    (context, left, right) -> new Constant(left.toBooleanState(context) || right.toBooleanState(context)));
  public static final Operator PLUS = new Operator("plus", "+",
    (context, left, right) -> new Constant(left.toNumericState(context).add(right.toNumericState(context))));

  public static View<Operator> values() {
    return Enumerations.values(OperatorList.class, Operator.class);
  }
}