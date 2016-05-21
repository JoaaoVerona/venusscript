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
import br.shura.venus.exception.UndefinedVariableException;
import br.shura.venus.value.Value;
import br.shura.x.collection.map.Map;
import br.shura.x.collection.map.impl.LinkedMap;
import br.shura.x.lang.annotation.Internal;
import br.shura.x.util.layer.XApi;

/**
 * Context.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 01:30
 * @since GAMMA - 0x3
 */
public class Context {
  private int currentLine;
  private VenusExecutor executor;
  private final Container owner;
  private final Context parent;
  private final Map<String, VariableDefinition> variables;

  public Context(Container owner, Context parent) {
    this.owner = owner;
    this.parent = parent;
    this.variables = new LinkedMap<>();
  }

  public VenusExecutor currentExecutor() {
    return executor;
  }

  public int currentLine() {
    return currentLine;
  }

  public Object getLockMonitor(String name) throws UndefinedVariableException {
    XApi.requireNonNull(name, "name");

    VariableDefinition object = findVariable(name);

    return object.value;
  }

  public Container getOwner() {
    return owner;
  }

  public Context getParent() {
    return parent;
  }

  public Value getVar(String name) throws UndefinedVariableException {
    XApi.requireNonNull(name, "name");

    VariableDefinition object = findVariable(name);

    return object.value;
  }

  public Map<String, VariableDefinition> getVariables() {
    return variables;
  }

  public boolean hasParent() {
    return getParent() != null;
  }

  public boolean hasVar(String name) throws UndefinedVariableException {
    XApi.requireNonNull(name, "name");

    return isOwnerOf(name) || (hasParent() && getParent().hasVar(name));
  }

  public boolean isOwnerOf(String name) {
    return getVariables().containsKey(name);
  }

  public void setVar(String name, Value value) {
    if (!changeVar(name, value)) {
      getVariables().add(name, new VariableDefinition(value));
    }
  }

  @Override
  public String toString() {
    return "context(owner=" + getOwner() + ", vars=" + getVariables() + ", parent=" + getParent() + ')';
  }

  protected boolean changeVar(String name, Value value) {
    if (isOwnerOf(name)) {
      getVariables().get(name).value = value;

      return true;
    }

    return hasParent() && getParent().changeVar(name, value);
  }

  protected VariableDefinition findVariable(String name) throws UndefinedVariableException {
    if (isOwnerOf(name)) {
      return getVariables().get(name);
    }

    if (hasParent()) {
      try {
        return getParent().findVariable(name);
      }
      catch (UndefinedVariableException exception) {
      }
    }

    throw new UndefinedVariableException(this, name);
  }

  @Internal
  protected void setCurrentLine(int currentLine) {
    this.currentLine = currentLine;
  }

  @Internal
  protected void setExecutor(VenusExecutor executor) {
    this.executor = executor;
  }

  public static class VariableDefinition {
    public final Object lockMonitor;
    public Value value;

    public VariableDefinition(Value value) {
      this.lockMonitor = new Object();
      this.value = value;
    }
  }
}