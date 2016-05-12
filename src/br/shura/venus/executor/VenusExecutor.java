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

package br.shura.venus.executor;

import br.shura.venus.component.Attribution;
import br.shura.venus.component.Component;
import br.shura.venus.component.Container;
import br.shura.venus.component.FunctionCall;
import br.shura.venus.component.IfContainer;
import br.shura.venus.component.function.Definition;
import br.shura.venus.exception.InvalidValueTypeException;
import br.shura.venus.exception.ScriptRuntimeException;
import br.shura.venus.value.BoolValue;
import br.shura.venus.value.Value;
import br.shura.x.collection.list.ListIterator;
import br.shura.x.lang.mutable.MutableBoolean;

/**
 * VenusExecutor.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 09/05/16 - 02:47
 * @since GAMMA - 0x3
 */
public class VenusExecutor {
  public static Value run(Container container) throws ScriptRuntimeException {
    return run(container, new MutableBoolean(true));
  }

  public static Value run(Container container, MutableBoolean shouldRun) throws ScriptRuntimeException {
    Context context = container.getContext();
    ListIterator<Component> iterator = container.getChildren().iterator();
    Value result = null;

    while (shouldRun.get() && iterator.hasNext()) {
      Component component = iterator.next();

      if (component instanceof Container) {
        if (component instanceof IfContainer) {
          IfContainer ifContainer = (IfContainer) component;
          Value value = ifContainer.getCondition().resolve(context);

          if (value instanceof BoolValue) {
            BoolValue boolValue = (BoolValue) value;

            if (boolValue.value()) {
              run((Container) component, shouldRun);
            }
          }
          else {
            throw new InvalidValueTypeException(context, "Cannot apply if condition in value of type " + value.getType());
          }
        }
        else if (!(component instanceof Definition)) {
          run((Container) component, shouldRun);
        }
      }
      else if (component instanceof Attribution) {
        Attribution attribution = (Attribution) component;

        context.setVar(attribution.getName(), attribution.getResultor().resolve(context));
      }
      else if (component instanceof FunctionCall) {
        FunctionCall functionCall = (FunctionCall) component;

        functionCall.resolve(context);
      }
    }

    return result;
  }
}