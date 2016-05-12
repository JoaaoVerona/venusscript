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

import br.shura.venus.value.Value;
import br.shura.x.collection.view.View;
import br.shura.x.worker.enumeration.Enumerations;

/**
 * OperatorList.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 07/05/16 - 19:49
 * @since GAMMA - 0x3
 */
public class OperatorList {
  public static final BinaryOperator AND = new BinaryOperator("and", Value::and, "&&", "&");
  public static final BinaryOperator DIVIDE = new BinaryOperator("divide", Value::divide, "/");
  public static final BinaryOperator MINUS = new BinaryOperator("minus", Value::minus, "-");
  public static final BinaryOperator MULTIPLY = new BinaryOperator("multiply", Value::multiply, "*");
  public static final BinaryOperator OR = new BinaryOperator("or", Value::or, "||", "|");
  public static final BinaryOperator PLUS = new BinaryOperator("plus", Value::plus, "+");

  public static Operator forIdentifier(String identifier) {
    return values().selectFirst(operator -> operator.getIdentifiers().contains(identifier));
  }

  public static View<Operator> values() {
    return Enumerations.values(OperatorList.class, Operator.class);
  }
}