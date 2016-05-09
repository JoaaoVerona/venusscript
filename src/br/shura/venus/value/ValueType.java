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

import br.shura.x.util.layer.XApi;
import br.shura.x.worker.StringWorker;

/**
 * ValueType.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 03:21
 * @since GAMMA - 0x3
 */
public enum ValueType {
  ANY("any", Value.class),
  BOOLEAN("bool", BoolValue.class),
  NUMBER("number", DecimalValue.class);

  private final String identifier;
  private final String name;
  private final Class<? extends Value> type;

  private ValueType(String identifier, Class<? extends Value> type) {
    this.identifier = identifier;
    this.name = StringWorker.capitalize(StringWorker.replace(name(), '_', ' '));
    this.type = type;
  }

  public boolean accepts(Class<?> type) {
    return getType().isAssignableFrom(type);
  }

  public boolean accepts(ValueType type) {
    return accepts(type.getType());
  }

  public String getIdentifier() {
    return identifier;
  }

  public Class<? extends Value> getType() {
    return type;
  }

  @Override
  public String toString() {
    return name;
  }

  public static ValueType forClass(Class<?> type) {
    XApi.requireNonNull(type, "type");

    for (ValueType value : values()) {
      if (value.accepts(type)) {
        return value;
      }
    }

    return null;
  }

  public static ValueType forIdentifier(String identifier) {
    XApi.requireNonNull(identifier, "identifier");

    for (ValueType value : values()) {
      if (value.getIdentifier().equals(identifier)) {
        return value;
      }
    }

    return null;
  }

  public static ValueType forValue(Value value) {
    XApi.requireNonNull(value, "value");

    return forClass(value.getClass());
  }
}