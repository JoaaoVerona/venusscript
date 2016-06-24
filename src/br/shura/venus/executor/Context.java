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

package br.shura.venus.executor;

import br.shura.venus.component.Container;
import br.shura.venus.component.Script;
import br.shura.venus.exception.runtime.UndefinedVariableException;
import br.shura.venus.expression.Variable;
import br.shura.venus.value.Value;
import br.shura.x.collection.map.Map;
import br.shura.x.collection.map.impl.LinkedMap;
import br.shura.x.lang.ICloneable;
import br.shura.x.util.layer.XApi;

/**
 * Context.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 01:30
 * @since GAMMA - 0x3
 */
public class Context implements ICloneable<Context> {
  private static int NEXT_ID = 0;
  private final int id;
  private final Container owner;
  private final Context parent;
  private final Map<String, VariableStructure> variables;

  public Context(Container owner, Context parent) {
    this.id = NEXT_ID++;
    this.owner = owner;
    this.parent = parent;
    this.variables = new LinkedMap<>();
  }

  @Override
  public Context clone() {
    return cloneWith(getOwner(), getParent());
  }

  public Context cloneWith(Container owner, Context parent) {
    Context context = new Context(owner, parent);

    context.getVariables().addAll(getVariables());

    return context;
  }

  public ApplicationContext getApplicationContext() {
    return getOwner().getApplicationContext();
  }

  public Container getOwner() {
    return owner;
  }

  public Context getParent() {
    return parent;
  }

  public Script getScript() {
    return getOwner().getScript();
  }

  public VariableStructure getVar(String name) throws UndefinedVariableException {
    if (name.length() > 1 && name.charAt(0) == '$') {
      return getApplicationContext().getVar(name.substring(1));
    }

    if (isOwnerOf(name)) {
      return getVariables().get(name);
    }

    if (hasParent()) {
      try {
        return getParent().getVar(name);
      }
      catch (UndefinedVariableException exception) {
      }
    }

    throw new UndefinedVariableException(this, name);
  }

  public VariableStructure getVar(Variable variable) throws UndefinedVariableException {
    return getVar(variable.getName());
  }

  public Value getVarValue(String name) throws UndefinedVariableException {
    XApi.requireNonNull(name, "name");

    return getVar(name).getValue();
  }

  public Value getVarValue(Variable variable) throws UndefinedVariableException {
    return getVarValue(variable.getName());
  }

  public Map<String, VariableStructure> getVariables() {
    return variables;
  }

  public boolean hasParent() {
    return getParent() != null;
  }

  public boolean hasVar(String name) throws UndefinedVariableException {
    if (name.length() > 1 && name.charAt(0) == '$') {
      return getApplicationContext().hasVar(name.substring(1));
    }

    return isOwnerOf(name) || (hasParent() && getParent().hasVar(name));
  }

  public boolean isOwnerOf(String name) {
    return getVariables().containsKey(name);
  }

  public void setVar(String name, Value value) {
    if (!changeVar(name, value)) {
      getVariables().add(name, new VariableStructure(value));
    }
  }

  public String toDetailedString() {
    return "context(owner=" + getOwner() + ", vars=" + getVariables() + ", parent=" + getParent() + ')';
  }

  @Override
  public String toString() {
    return "#" + id;
  }

  protected boolean changeVar(String name, Value value) {
    if (name.length() > 1 && name.charAt(0) == '$') {
      getOwner().getApplicationContext().setVar(name.substring(1), value);

      return true;
    }

    if (isOwnerOf(name)) {
      getVariables().get(name).setValue(value);

      return true;
    }

    return hasParent() && getParent().changeVar(name, value);
  }
}