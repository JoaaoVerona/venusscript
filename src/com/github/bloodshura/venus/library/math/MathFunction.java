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

package com.github.bloodshura.venus.library.math;

import com.github.bloodshura.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.venus.executor.Context;
import com.github.bloodshura.venus.function.Function;
import com.github.bloodshura.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.venus.type.PrimitiveType;
import com.github.bloodshura.venus.type.Type;
import com.github.bloodshura.venus.value.IntegerValue;
import com.github.bloodshura.venus.value.Value;
import com.github.bloodshura.x.collection.list.XList;
import com.github.bloodshura.x.collection.list.impl.XArrayList;
import com.github.bloodshura.x.collection.view.XBasicView;
import com.github.bloodshura.x.collection.view.XView;
import com.github.bloodshura.x.math.MathProvider;
import com.github.bloodshura.x.lang.layer.NotNull;
import com.github.bloodshura.x.worker.UtilWorker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MathFunction implements Function {
  private final XList<Type> arguments;
  private final MathProvider instance;
  private final Method method;
  private final String name;

  public MathFunction(Method method, MathProvider instance) {
    this.arguments = new XArrayList<>();
    this.instance = instance;
    this.method = method;
    this.name = method.getName();

    for (Class<?> arg : method.getParameterTypes()) {
      arguments.add(PrimitiveType.forObjectType(arg));
    }
  }

  @Override
  public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
    XList<Object> values = new XArrayList<>();
    int i = 0;

    for (Value argument : descriptor.getValues()) {
      if (argument instanceof IntegerValue && UtilWorker.fixPrimitiveClass(method.getParameterTypes()[i]) == Integer.class) {
        values.add(((Long) argument.value()).intValue());
      }
      else {
        values.add(argument.value());
      }

      i++;
    }

    try {
      Object result = getMethod().invoke(getInstance(), values.toArray());

      if (getMethod().getReturnType() == void.class && result == null) {
        return null;
      }

      Value value = Value.construct(result);

      if (value == null) {
        throw new ScriptRuntimeException(context, "Math method \"" + method.getName() + "\" returned untranslatable value of type " + result.getClass().getSimpleName() + "?!");
      }

      return value;
    }
    catch (IllegalAccessException | InvocationTargetException exception) {
      throw new ScriptRuntimeException(context, "Could not call math function \"" + getName() + "\"", exception);
    }
  }

  @Override
  public XView<Type> getArgumentTypes() {
    return new XBasicView<>(arguments);
  }

  public MathProvider getInstance() {
    return instance;
  }

  public Method getMethod() {
    return method;
  }

  @NotNull
  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isVarArgs() {
    return false;
  }

  public static boolean validate(Method method) {
    if (PrimitiveType.forObjectType(method.getReturnType()) == null) {
      return false;
    }

    for (Class<?> arg : method.getParameterTypes()) {
      if (PrimitiveType.forObjectType(arg) == null) {
        return false;
      }
    }

    return true;
  }
}