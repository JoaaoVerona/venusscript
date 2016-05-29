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

package br.shura.venus.type;

import br.shura.venus.value.Value;
import br.shura.x.collection.view.ArrayView;
import br.shura.x.collection.view.View;
import br.shura.x.worker.UtilWorker;

/**
 * Type.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 29/05/16 - 02:15
 * @since GAMMA - 0x3
 */
public final class Type {
  private final String identifier;
  private final Class<?>[] objectTypes;
  private final Class<? extends Value> valueClass;

  public Type(String identifier, Class<? extends Value> valueClass, Class<?>... objectTypes) {
    this.identifier = identifier;
    this.objectTypes = objectTypes;
    this.valueClass = valueClass;
  }

  public boolean accepts(Class<? extends Value> valueClass) {
    return getValueClass().isAssignableFrom(valueClass);
  }

  public boolean accepts(Type type) {
    return accepts(type.getValueClass());
  }

  public String getIdentifier() {
    return identifier;
  }

  public View<Class<?>> getObjectTypes() {
    return new ArrayView<>(objectTypes);
  }

  public Class<? extends Value> getValueClass() {
    return valueClass;
  }

  public boolean objectAccepts(Class<?> type) {
    return getObjectTypes().any(object -> object.isAssignableFrom(UtilWorker.fixPrimitiveClass(type)));
  }

  @Override
  public String toString() {
    return getIdentifier();
  }
}