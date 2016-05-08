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

package br.shura.venus.component.function;

import br.shura.venus.Context;
import br.shura.venus.component.Container;
import br.shura.venus.exception.ScriptRuntimeException;
import br.shura.venus.value.Value;
import br.shura.venus.value.ValueType;
import br.shura.x.collection.list.List;
import br.shura.x.collection.view.BasicView;
import br.shura.x.collection.view.View;

/**
 * Definition.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 15:34
 * @since GAMMA - 0x3
 */
public final class Definition extends Container implements Function {
  private final List<Argument> arguments;
  private final String name;

  public Definition(String name, List<Argument> arguments) {
    this.arguments = arguments;
    this.name = name;
  }

  @Override
  public Value call(Context context, Value... arguments) throws ScriptRuntimeException {
    return null; // TODO
  }

  @Override
  public int getArgumentCount() { // Default impl. of getArgumentCount() calls getArgumentTypes(), but our impl. is expensive
    return arguments.size();
  }

  @Override
  public View<ValueType> getArgumentTypes() {
    return getArguments().reduce(Argument::getType);
  }

  public View<Argument> getArguments() {
    return new BasicView<>(arguments);
  }

  @Override
  public String getDisplayName() {
    return '@' + getName();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isVarArgs() {
    return false;
  }

  @Override
  public void setParent(Container parent) {
    super.setParent(parent);
    this.context = new Context(this, parent.getContext());
  }

  @Override
  public String toString() {
    return "definition(" + getName() + ')';
  }
}