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

package br.shura.venus.expression;

import br.shura.venus.component.object.Attribute;
import br.shura.venus.component.object.ObjectDefinition;
import br.shura.venus.exception.runtime.ScriptRuntimeException;
import br.shura.venus.exception.runtime.UndefinedAttributeException;
import br.shura.venus.executor.Context;
import br.shura.venus.value.ObjectValue;
import br.shura.venus.value.Value;
import br.shura.x.collection.map.Map;
import br.shura.x.collection.tuple.Pair;

/**
 * NewObject.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 29/05/16 - 19:01
 * @since GAMMA - 0x3
 */
public class NewObject implements Expression {
  private final Map<String, Expression> attributes;
  private final String objectType;

  public NewObject(String objectType, Map<String, Expression> attributes) {
    this.attributes = attributes;
    this.objectType = objectType;
  }

  public Map<String, Expression> getAttributes() {
    return attributes;
  }

  public String getObjectType() {
    return objectType;
  }

  @Override
  public Value resolve(Context context) throws ScriptRuntimeException {
    ObjectDefinition definition = context.getOwner().findObjectDefinition(context, getObjectType());
    Context c = new Context(definition, null);

    for (Pair<String, Expression> pair : getAttributes()) {
      Attribute attribute = definition.getAttributes().selectFirst(attrib -> attrib.getName().equals(pair.getLeft()));

      if (attribute != null) {
        Value value = pair.getRight().resolve(context);

        c.setVar(pair.getLeft(), value);
      }
      else {
        throw new UndefinedAttributeException(context, definition, pair.getLeft());
      }
    }

    for (Attribute attribute : definition.getAttributes()) {
      if (!c.hasVar(attribute.getName()) && attribute.hasDefaultExpression()) {
        c.setVar(attribute.getName(), attribute.getDefaultExpression().resolve(context));
      }
    }

    return new ObjectValue(definition, c);
  }

  @Override
  public String toString() {
    return "new(" + getObjectType() + " << " + getAttributes() + ')';
  }
}