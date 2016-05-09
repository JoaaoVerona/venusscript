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

import br.shura.venus.executor.Context;
import br.shura.venus.value.Value;
import br.shura.x.collection.view.ArrayView;
import br.shura.x.collection.view.View;
import br.shura.x.object.Base;

import java.util.function.BiFunction;

/**
 * Operator.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 01:51
 * @since GAMMA - 0x3
 */
public class Operator extends Base {
  private final BiFunction<Value, Value, Value> function;
  private final View<String> identifiers;
  private final String name;

  public Operator(String name, BiFunction<Value, Value, Value> function, String... identifiers) {
    this.function = function;
    this.identifiers = new ArrayView<>(identifiers);
    this.name = name;
  }

  public BiFunction<Value, Value, Value> getFunction() {
    return function;
  }

  public View<String> getIdentifiers() {
    return identifiers;
  }

  public final Value operate(Context context, Value left, Value right) {
    return getFunction().apply(left, right);
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  protected Object[] stringValues() {
    return new Object[] { toString(), getIdentifiers() };
  }
}