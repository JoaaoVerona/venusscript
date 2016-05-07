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
import br.shura.venus.component.function.Function;
import br.shura.venus.exception.ScriptRuntimeException;
import br.shura.x.collection.list.List;
import br.shura.x.collection.list.impl.ArrayList;

/**
 * Script.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 01:36
 * @since GAMMA - 0x3
 */
public class Script extends Container {
  private final ApplicationContext appContext;
  private final List<Script> includes;
  private final ScriptOrigin origin;

  public Script(ApplicationContext appContext, ScriptOrigin origin) {
    this.appContext = appContext;
    this.context = new Context(this, null);
    this.includes = new ArrayList<>();
    this.origin = origin;
  }

  @Override
  public Function findFunction(Context context, String name, int argumentCount) throws ScriptRuntimeException {
    try {
      return super.findFunction(context, name, argumentCount);
    }
    catch (ScriptRuntimeException exception) {
      for (Script script : getIncludes()) {
        try {
          return script.findFunction(context, name, argumentCount);
        }
        catch (ScriptRuntimeException exception2) {
        }
      }

      throw exception;
    }
  }

  @Override
  public ApplicationContext getApplicationContext() {
    return appContext;
  }

  @Override
  public String getDisplayName() {
    return getOrigin().getScriptName();
  }

  public List<Script> getIncludes() {
    return includes;
  }

  public ScriptOrigin getOrigin() {
    return origin;
  }

  @Override
  public Script getScript() {
    return this;
  }

  @Override
  protected void setParent(Container parent) {
    throw new IllegalStateException("Cannot define a script's parent");
  }
}