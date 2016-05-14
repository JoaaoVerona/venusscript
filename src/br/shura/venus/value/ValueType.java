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

import br.shura.venus.component.function.Function;
import br.shura.x.collection.view.ArrayView;
import br.shura.x.collection.view.View;
import br.shura.x.util.layer.XApi;
import br.shura.x.worker.StringWorker;
import br.shura.x.worker.UtilWorker;

/**
 * ValueType.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 03:21
 * @since GAMMA - 0x3
 */
public enum ValueType {
  BOOLEAN("bool", BoolValue.class, Boolean.class),
  DECIMAL("decimal", DecimalValue.class, Double.class, Float.class),
  INTEGER("int", IntegerValue.class, Integer.class, Long.class),
  REFERENCE("ref", ReferenceValue.class, Function.class),
  STRING("string", StringValue.class, String.class),
  TYPE("type", TypeValue.class, ValueType.class),
  ANY("any", Value.class, Object.class); // Should be after all other types

  private final String identifier;
  private final String name;
  private final Class<?>[] objectTypes;
  private final Class<? extends Value> type;

  private ValueType(String identifier, Class<? extends Value> type, Class<?>... objectTypes) {
    this.identifier = identifier;
    this.name = StringWorker.capitalize(StringWorker.replace(name(), '_', ' '));
    this.objectTypes = objectTypes;
    this.type = type;
  }

  public boolean accepts(Class<? extends Value> type) {
    return getType().isAssignableFrom(type);
  }

  public boolean accepts(ValueType type) {
    return accepts(type.getType());
  }

  public String getIdentifier() {
    return identifier;
  }

  public View<Class<?>> getObjectTypes() {
    return new ArrayView<>(objectTypes);
  }

  public Class<? extends Value> getType() {
    return type;
  }

  public boolean objectAccepts(Class<?> type) {
    return getObjectTypes().any(object -> object.isAssignableFrom(UtilWorker.fixPrimitiveClass(type)));
  }

  @Override
  public String toString() {
    return name;
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

  public static ValueType forObjectType(Class<?> type) {
    XApi.requireNonNull(type, "type");

    for (ValueType value : values()) {
      if (value.objectAccepts(type)) {
        return value;
      }
    }

    return null;
  }

  public static ValueType forType(Class<? extends Value> type) {
    XApi.requireNonNull(type, "type");

    for (ValueType value : values()) {
      if (value.accepts(type)) {
        return value;
      }
    }

    return null;
  }

  public static ValueType forValue(Value value) {
    XApi.requireNonNull(value, "value");

    return forType(value.getClass());
  }
}