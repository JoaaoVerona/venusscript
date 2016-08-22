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

package com.github.bloodshura.venus.operator;

import com.github.bloodshura.venus.executor.Context;
import com.github.bloodshura.venus.value.Value;
import com.github.bloodshura.x.collection.view.XArrayView;
import com.github.bloodshura.x.collection.view.XView;
import com.github.bloodshura.x.util.layer.XApi;

import java.util.function.Function;

/**
 * UnaryOperator.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 12/05/16 - 00:15
 * @since GAMMA - 0x3
 */
public class UnaryOperator implements Operator {
  private final Function<Value, Value> function;
  private final XView<String> identifiers;
  private final String name;

  public UnaryOperator(String name, Function<Value, Value> function, String... identifiers) {
    XApi.requireNonNull(function, "function");
    XApi.requireNonNull(identifiers, "identifiers");
    XApi.requireNonNull(name, "name");

    this.function = function;
    this.identifiers = new XArrayView<>(identifiers);
    this.name = name;
  }

  public Function<Value, Value> getFunction() {
    return function;
  }

  @Override
  public XView<String> getIdentifiers() {
    return identifiers;
  }

  public final Value operate(Context context, Value value) {
    return getFunction().apply(value);
  }

  @Override
  public String toString() {
    return name;
  }
}