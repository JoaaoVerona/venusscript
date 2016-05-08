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

package br.shura.venus;

import br.shura.venus.component.Container;
import br.shura.venus.exception.ScriptRuntimeException;
import br.shura.venus.exception.UndefinedVariableException;
import br.shura.venus.resultor.Resultor;
import br.shura.x.collection.map.Map;
import br.shura.x.collection.map.impl.LinkedMap;
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
  private final Container owner;
  private final Context parent;
  private final Map<String, Resultor> variables;

  public Context(Container owner, Context parent) {
    this.owner = owner;
    this.parent = parent;
    this.variables = new LinkedMap<>();
  }

  public int currentLine() {
    return 0;
  }

  public Container getOwner() {
    return owner;
  }

  public Context getParent() {
    return parent;
  }

  public Resultor getVar(String name) throws UndefinedVariableException {
    XApi.requireNonNull(name, "name");

    Resultor object = getVariables().get(name);

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

    if (name.length() > 1 && name.charAt(0) == '$') {
      try {
        object = getOwner().getApplicationContext().getVar(name.substring(1));

        if (object != null) {
          return object;
        }
      }
      catch (UndefinedVariableException exception) {
      }
    }

    throw new UndefinedVariableException(this, name);
  }

  public Object getVarValue(String name) throws ScriptRuntimeException {
    Object value = getVar(name);

    while (value instanceof Resultor) {
      value = ((Resultor) value).resolve(this);
    }

    return this;
  }

  public Map<String, Resultor> getVariables() {
    return variables;
  }

  public boolean hasParent() {
    return getParent() != null;
  }

  public void setVar(String name, Resultor value) {
    getVariables().set(name, value);
  }
}