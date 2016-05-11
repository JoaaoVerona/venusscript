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

import br.shura.venus.component.function.Definition;
import br.shura.venus.component.function.Function;
import br.shura.venus.exception.ScriptRuntimeException;
import br.shura.venus.exception.UndefinedFunctionException;
import br.shura.venus.executor.Context;
import br.shura.x.collection.list.List;
import br.shura.x.collection.list.impl.ArrayList;
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

  public Function findFunction(Context context, String name, int argumentCount) throws ScriptRuntimeException {
    XApi.requireNonNull(name, "name");

    for (Definition definition : getChildren().selectType(Definition.class)) {
      if (definition.getName().equals(name) && definition.getArgumentCount() == argumentCount) {
        return definition;
      }
    }

    if (hasParent()) {
      try {
        return getParent().findFunction(context, name, argumentCount);
      }
      catch (UndefinedFunctionException exception) {
      }
    }

    throw new UndefinedFunctionException(context, name, argumentCount);
  }

  public List<Component> getChildren() {
    return children;
  }

  public Context getContext() {
    return context;
  }

  public abstract String getDisplayName();

  @Override
  public void setParent(Container parent) {
    super.setParent(parent);
    this.context = parent.getContext();
  }
}