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

package br.shura.venus.component.function;

import br.shura.venus.component.function.annotation.MethodArgs;
import br.shura.venus.component.function.annotation.MethodName;
import br.shura.venus.component.function.annotation.MethodVarArgs;
import br.shura.venus.value.ValueType;
import br.shura.x.collection.view.ArrayView;
import br.shura.x.collection.view.View;
import br.shura.x.util.layer.XApi;

/**
 * Method.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 02:27
 * @since GAMMA - 0x3
 */
public abstract class Method implements Function {
  private final ValueType[] arguments;
  private final String name;
  private final boolean varArgs;

  public Method() {
    boolean hasMethodArgs = getClass().isAnnotationPresent(MethodArgs.class);
    boolean hasMethodName = getClass().isAnnotationPresent(MethodName.class);
    boolean isMethodVarArgs = getClass().isAnnotationPresent(MethodVarArgs.class);

    XApi.requireState(hasMethodArgs != isMethodVarArgs, "Must contain either @MethodArgs or @MethodVarArgs");
    XApi.requireState(hasMethodName, "No @MethodName found");

    this.arguments = hasMethodArgs ? getClass().getAnnotation(MethodArgs.class).value() : new ValueType[0];
    this.name = getClass().getAnnotation(MethodName.class).value();
    this.varArgs = isMethodVarArgs;
  }

  @Override
  public final View<ValueType> getArgumentTypes() {
    return new ArrayView<>(arguments);
  }

  @Override
  public final String getName() {
    return name;
  }

  @Override
  public final boolean isVarArgs() {
    return varArgs;
  }
}