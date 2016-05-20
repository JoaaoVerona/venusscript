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

package br.shura.venus.function;

import br.shura.venus.component.FunctionCall;
import br.shura.venus.resultor.Resultor;
import br.shura.venus.value.Value;
import br.shura.x.collection.view.View;

import java.util.function.Function;

/**
 * FunctionCallDescriptor.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 20/05/16 - 20:24
 * @since GAMMA - 0x3
 */
public class FunctionCallDescriptor {
  private final FunctionCall caller;
  private final View<Resultor> resultors;
  private final View<Value> values;

  public FunctionCallDescriptor(FunctionCall caller, View<Resultor> resultors, View<Value> values) {
    this.caller = caller;
    this.resultors = resultors;
    this.values = values;
  }

  public int count() {
    return getValues().size();
  }

  public Value get(int index) {
    return getValues().at(index);
  }

  public FunctionCall getCaller() {
    return caller;
  }

  public Value getOr(int index, Value value) {
    return index >= 0 && index < getValues().size() ? getValues().at(index) : value;
  }

  public View<Resultor> getResultors() {
    return resultors;
  }

  public View<Value> getValues() {
    return values;
  }

  public boolean isEmpty() {
    return count() == 0;
  }

  public <E> E transform(int index, Function<Value, E> function, E or) {
    if (index >= 0 && index < getValues().size()) {
      Value value = get(index);

      return function.apply(value);
    }

    return or;
  }
}