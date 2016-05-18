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
  private final Map<String, Object> monitors;
  private final Container owner;
  private final Context parent;
  private final Map<String, Value> variables;

  public Context(Container owner, Context parent) {
    this.monitors = new LinkedMap<>();
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

  public Object getMonitor(String name) throws UndefinedVariableException {
    XApi.requireNonNull(name, "name");

    Object object = monitors.get(name);

    if (object != null) {
      return object;
    }

    if (hasParent()) {
      try {
        object = getParent().getMonitor(name);

        if (object != null) {
          return object;
        }
      }
      catch (UndefinedVariableException exception) {
      }
    }

    throw new UndefinedVariableException(this, name);
  }

  public Container getOwner() {
    return owner;
  }

  public Context getParent() {
    return parent;
  }

  public Value getVar(String name) throws UndefinedVariableException {
    XApi.requireNonNull(name, "name");

    Value object = getVariables().get(name);

    if (object != null) {
      return object;
    }

    if (hasParent()) {
      try {
        object = getParent().getVar(name);

        if (object != null) {
          return object;
        }
      }
      catch (UndefinedVariableException exception) {
      }
    }

    throw new UndefinedVariableException(this, name);
  }

  public Map<String, Value> getVariables() {
    return variables;
  }

  public boolean hasParent() {
    return getParent() != null;
  }

  public boolean isOwnerOf(String name) {
    return getVariables().containsKey(name);
  }

  public void setVar(String name, Value value) {
    if (!changeVar(name, value)) {
      monitors.add(name, new Object());
      getVariables().add(name, value);
    }
  }

  protected boolean changeVar(String name, Value value) {
    if (isOwnerOf(name)) {
      getVariables().set(name, value);

      return true;
    }

    return hasParent() && getParent().changeVar(name, value);
  }

  @Override
  public String toString() {
    return "context(owner=" + getOwner() + ", vars=" + getVariables() + ", parent=" + getParent() + ')';
  }

  @Internal
  protected void setCurrentLine(int currentLine) {
    this.currentLine = currentLine;
  }

  @Internal
  protected void setExecutor(VenusExecutor executor) {
    this.executor = executor;
  }
}