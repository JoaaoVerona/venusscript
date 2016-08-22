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

package com.github.bloodshura.venus.library.math;

import com.github.bloodshura.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.venus.executor.Context;
import com.github.bloodshura.venus.function.Function;
import com.github.bloodshura.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.venus.type.PrimitiveType;
import com.github.bloodshura.venus.type.Type;
import com.github.bloodshura.venus.value.IntegerValue;
import com.github.bloodshura.venus.value.Value;
import com.github.bloodshura.x.collection.list.List;
import com.github.bloodshura.x.collection.list.impl.ArrayList;
import com.github.bloodshura.x.collection.view.BasicView;
import com.github.bloodshura.x.collection.view.View;
import com.github.bloodshura.x.math.MathProvider;
import com.github.bloodshura.x.worker.UtilWorker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * MathFunction.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 13/05/16 - 03:17
 * @since GAMMA - 0x3
 */
public class MathFunction implements Function {
  private final List<Type> arguments;
  private final MathProvider instance;
  private final Method method;
  private final String name;

  public MathFunction(Method method, MathProvider instance) {
    this.arguments = new ArrayList<>();
    this.instance = instance;
    this.method = method;
    this.name = method.getName();

    for (Class<?> arg : method.getParameterTypes()) {
      arguments.add(PrimitiveType.forObjectType(arg));
    }
  }

  @Override
  public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
    List<Object> values = new ArrayList<>();
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
  public View<Type> getArgumentTypes() {
    return new BasicView<>(arguments);
  }

  public MathProvider getInstance() {
    return instance;
  }

  public Method getMethod() {
    return method;
  }

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