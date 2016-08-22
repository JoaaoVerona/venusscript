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

package com.github.bloodshura.venus.function;

import com.github.bloodshura.venus.component.Container;
import com.github.bloodshura.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.venus.executor.Context;
import com.github.bloodshura.venus.origin.ScriptMode;
import com.github.bloodshura.venus.type.Type;
import com.github.bloodshura.venus.value.Value;
import com.github.bloodshura.x.collection.list.XList;
import com.github.bloodshura.x.collection.view.XBasicView;
import com.github.bloodshura.x.collection.view.XView;

/**
 * Definition.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 15:34
 * @since GAMMA - 0x3
 */
public final class Definition extends Container implements Function {
  private final XList<Argument> arguments;
  private final boolean global;
  private final String name;

  public Definition(String name, XList<Argument> arguments, boolean global) {
    this.arguments = arguments;
    this.global = global;
    this.name = name;
  }

  @Override
  public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
    int i = 0;

    if (!isGlobal()) {
      this.context = new Context(this, context);
    }

    for (Argument argument : getArguments()) {
      getContext().setVar(argument.getName(), descriptor.get(i++));
    }

    return getApplicationContext().currentExecutor().run(this, ScriptMode.NORMAL);
  }

  @Override
  public int getArgumentCount() { // Default impl. of getArgumentCount() calls getArgumentTypes(), but our impl. is expensive
    return arguments.size();
  }

  @Override
  public XView<Type> getArgumentTypes() {
    return getArguments().reduce(Argument::getType);
  }

  public XView<Argument> getArguments() {
    return new XBasicView<>(arguments);
  }

  @Override
  public String getName() {
    return name;
  }

  public boolean isGlobal() {
    return global;
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