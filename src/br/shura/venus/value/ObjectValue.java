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

package br.shura.venus.value;

import br.shura.venus.component.object.ObjectDefinition;
import br.shura.venus.executor.Context;
import br.shura.venus.executor.VariableStructure;
import br.shura.x.charset.build.TextBuilder;
import br.shura.x.collection.tuple.Pair;
import br.shura.x.util.Pool;

/**
 * ObjectValue.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 29/05/16 - 01:09
 * @since GAMMA - 0x3
 */
public class ObjectValue extends Value {
  private final Context context;
  private final ObjectDefinition definition;

  public ObjectValue(ObjectDefinition definition, Context context) {
    super(definition.getType());
    this.context = context;
    this.definition = definition;
  }

  @Override
  public ObjectValue clone() {
    return new ObjectValue(getDefinition(), getContext().clone());
  }

  public Context getContext() {
    return context;
  }

  public ObjectDefinition getDefinition() {
    return definition;
  }

  @Override
  public Object value() {
    return this;
  }

  @Override
  public String toString() {
    TextBuilder attributes = Pool.newBuilder().setSeparator(", ");

    for (Pair<String, VariableStructure> pair : getContext().getVariables()) {
      attributes.append(pair.getLeft() + ": " + pair.getRight() + " [" + pair.getRight().getValue().getType() + ']');
    }

    return getDefinition().getName() + "(" + attributes + ')';
  }
}