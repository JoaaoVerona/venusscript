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

package com.github.bloodshura.venus.function;

import com.github.bloodshura.venus.function.annotation.MethodArgs;
import com.github.bloodshura.venus.function.annotation.MethodName;
import com.github.bloodshura.venus.function.annotation.MethodVarArgs;
import com.github.bloodshura.venus.type.PrimitiveType;
import com.github.bloodshura.venus.type.Type;
import com.github.bloodshura.venus.value.Value;
import com.github.bloodshura.x.collection.view.XArrayView;
import com.github.bloodshura.x.collection.view.XBasicView;
import com.github.bloodshura.x.collection.view.XView;
import com.github.bloodshura.x.util.layer.XApi;

/**
 * Method.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 02:27
 * @since GAMMA - 0x3
 */
public abstract class Method implements Function {
  private final XView<Type> arguments;
  private final String name;
  private final boolean varArgs;

  public Method() {
    boolean hasMethodArgs = getClass().isAnnotationPresent(MethodArgs.class);
    boolean hasMethodName = getClass().isAnnotationPresent(MethodName.class);
    boolean isMethodVarArgs = getClass().isAnnotationPresent(MethodVarArgs.class);

    XApi.requireState(!hasMethodArgs || (hasMethodArgs != isMethodVarArgs), "Must contain either @MethodArgs or @MethodVarArgs");
    XApi.requireState(hasMethodName, "No @MethodName found");

    if (hasMethodArgs) {
      Class<? extends Value>[] args = getClass().getAnnotation(MethodArgs.class).value();

      this.arguments = new XArrayView<>(args).reduce(PrimitiveType::forType);
    }
    else {
      this.arguments = new XBasicView<>();
    }

    this.name = getClass().getAnnotation(MethodName.class).value();
    this.varArgs = isMethodVarArgs;
  }

  @Override
  public final XView<Type> getArgumentTypes() {
    return arguments;
  }

  @Override
  public final String getName() {
    return name;
  }

  @Override
  public final boolean isVarArgs() {
    return varArgs;
  }

  @Override
  public final String toString() {
    return "method(" + getName() + ')';
  }
}