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

package br.shura.venus.component;

import br.shura.venus.exception.runtime.ScriptRuntimeException;
import br.shura.venus.exception.runtime.UndefinedFunctionException;
import br.shura.venus.exception.runtime.UndefinedVariableException;
import br.shura.venus.executor.Context;
import br.shura.venus.function.Definition;
import br.shura.venus.function.Function;
import br.shura.venus.value.FunctionRefValue;
import br.shura.venus.value.Value;
import br.shura.venus.value.ValueType;
import br.shura.x.collection.list.List;
import br.shura.x.collection.list.impl.ArrayList;
import br.shura.x.collection.view.View;
import br.shura.x.util.layer.XApi;

/**
 * Container.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 01:40
 * @since GAMMA - 0x3
 */
public abstract class Container extends Component {
  protected Context context;
  private final List<Component> children;

  public Container() {
    this.children = new ArrayList<>();

    getChildren().addInsertionListener(component -> component.setParent(this));
  }

  public Function findFunction(Context context, String name, View<ValueType> argumentTypes) throws ScriptRuntimeException {
    XApi.requireNonNull(name, "name");

    try {
      Value value = context.getVar(name);

      if (value instanceof FunctionRefValue) {
        FunctionRefValue reference = (FunctionRefValue) value;

        return context.getOwner().findFunction(context, reference.value(), null);
      }
    }
    catch (UndefinedVariableException exception) {
    }

    Definition foundVarArgs = null;

    for (Definition definition : getChildren().selectType(Definition.class)) {
      if (definition.accepts(name, argumentTypes)) {
        if (definition.isVarArgs()) {
          foundVarArgs = definition;
        }
        else {
          return definition;
        }
      }
    }

    if (foundVarArgs != null) {
      return foundVarArgs;
    }

    if (hasParent()) {
      try {
        return getParent().findFunction(context, name, argumentTypes);
      }
      catch (UndefinedFunctionException exception) {
      }
    }

    throw new UndefinedFunctionException(context, name, argumentTypes);
  }

  public List<Component> getChildren() {
    return children;
  }

  public Context getContext() {
    return context;
  }

  @Override
  public void setParent(Container parent) {
    super.setParent(parent);
    this.context = parent.getContext();
  }
}