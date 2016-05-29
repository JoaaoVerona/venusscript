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

package br.shura.venus.resultor;

import br.shura.venus.exception.runtime.InvalidValueTypeException;
import br.shura.venus.exception.runtime.ScriptRuntimeException;
import br.shura.venus.executor.Context;
import br.shura.venus.value.ObjectValue;
import br.shura.venus.value.Value;

/**
 * InContext.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 29/05/16 - 18:55
 * @since GAMMA - 0x3
 */
public class InContext implements Resultor {
  private final String name;
  private final Resultor resultor;

  public InContext(String name, Resultor resultor) {
    this.name = name;
    this.resultor = resultor;
  }

  public String getName() {
    return name;
  }

  public Resultor getResultor() {
    return resultor;
  }

  @Override
  public Value resolve(Context context) throws ScriptRuntimeException {
    Value value = context.getVarValue(getName());

    if (value instanceof ObjectValue) {
      ObjectValue object = (ObjectValue) value;

      return getResultor().resolve(object.getContext());
    }
    else {
      throw new InvalidValueTypeException(context, "Cannot access " + value.getType() + " as an object");
    }
  }

  @Override
  public String toString() {
    return "incontext(" + getName() + " << " + getResultor() + ')';
  }
}