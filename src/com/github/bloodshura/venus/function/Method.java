//////////////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2016, João Vitor Verona Biazibetti - All Rights Reserved                /
//                                                                                       /
// This program is free software: you can redistribute it and/or modify                  /
// it under the terms of the GNU General Public License as published by                  /
// the Free Software Foundation, either version 3 of the License, or                     /
// (at your option) any later version.                                                   /
//                                                                                       /
// This program is distributed in the hope that it will be useful,                       /
// but WITHOUT ANY WARRANTY; without even the implied warranty of                        /
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the                          /
// GNU General Public License for more details.                                          /
//                                                                                       /
// You should have received a copy of the GNU General Public License                     /
// along with this program. If not, see <http://www.gnu.org/licenses/>.                  /
//                                                                                       /
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

      this.arguments = new XArrayView<>(args).map(PrimitiveType::forType);
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