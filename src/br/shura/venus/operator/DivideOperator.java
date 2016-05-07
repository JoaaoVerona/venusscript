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

import br.shura.venus.Context;
import br.shura.venus.exception.ScriptRuntimeException;
import br.shura.venus.value.Constant;
import br.shura.venus.value.Value;

/**
 * DivideOperator.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 04:06
 * @since GAMMA - 0x3
 */
public class DivideOperator extends Operator {
  public DivideOperator() {
    super("divide", "/");
  }

  @Override
  public Value operate(Context context, Value left, Value right) throws ScriptRuntimeException {
    return new Constant(left.toNumericState(context).divide(right.toNumericState(context)));
  }
}