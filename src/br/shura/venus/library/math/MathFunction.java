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

package br.shura.venus.library.math;

import br.shura.venus.exception.runtime.ScriptRuntimeException;
import br.shura.venus.executor.Context;
import br.shura.venus.function.Function;
import br.shura.venus.function.FunctionCallDescriptor;
import br.shura.venus.type.PrimitiveTypes;
import br.shura.venus.type.Type;
import br.shura.venus.value.IntegerValue;
import br.shura.venus.value.Value;
import br.shura.venus.value.ValueType;
import br.shura.x.collection.list.List;
import br.shura.x.collection.list.impl.ArrayList;
import br.shura.x.collection.view.BasicView;
import br.shura.x.collection.view.View;
import br.shura.x.math.IMath;
import br.shura.x.worker.UtilWorker;

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
  private final IMath instance;
  private final Method method;
  private final String name;

  public MathFunction(Method method, IMath instance) {
    this.arguments = new ArrayList<>();
    this.instance = instance;
    this.method = method;
    this.name = method.getName();

    for (Class<?> arg : method.getParameterTypes()) {
      arguments.add(PrimitiveTypes.forObjectType(arg));
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

  public IMath getInstance() {
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
    if (ValueType.forObjectType(method.getReturnType()) == null) {
      return false;
    }

    for (Class<?> arg : method.getParameterTypes()) {
      if (ValueType.forObjectType(arg) == null) {
        return false;
      }
    }

    return true;
  }
}