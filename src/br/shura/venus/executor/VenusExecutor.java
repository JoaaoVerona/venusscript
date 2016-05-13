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
import br.shura.venus.component.branch.DoWhileContainer;
import br.shura.venus.component.branch.ElseContainer;
import br.shura.venus.component.branch.ElseIfContainer;
import br.shura.venus.component.branch.ForEachContainer;
import br.shura.venus.component.branch.IfContainer;
import br.shura.venus.component.branch.WhileContainer;
import br.shura.venus.component.function.Definition;
import br.shura.venus.exception.InvalidValueTypeException;
import br.shura.venus.exception.ScriptRuntimeException;
import br.shura.venus.value.BoolValue;
import br.shura.venus.value.DecimalValue;
import br.shura.venus.value.IntegerValue;
import br.shura.venus.value.NumericValue;
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
    boolean hadIfAndNotProceed = false;

    while (shouldRun.get() && iterator.hasNext()) {
      Component component = iterator.next();

      if (component instanceof Container) {
        if (component instanceof ForEachContainer) {
          ForEachContainer forContainer = (ForEachContainer) component;
          Value from = forContainer.getFrom().resolve(context);
          Value to = forContainer.getTo().resolve(context);

          if (from instanceof NumericValue) {
            if (to instanceof NumericValue) {
              NumericValue numericFrom = (NumericValue) from;
              NumericValue numericTo = (NumericValue) to;
              boolean isDecimal = numericFrom instanceof DecimalValue || numericTo instanceof DecimalValue;
              Value count = isDecimal ? new DecimalValue(numericFrom.value().doubleValue()) :
                            new IntegerValue(numericFrom.value().longValue());

              while (count.lowerEqualThan(to).value()) {
                context.setVar(forContainer.getVarName(), count);
                run(forContainer, shouldRun);
                count = forContainer.getAdjustment().resolve(context);
              }
            }
            else {
              throw new InvalidValueTypeException(context, "For end \"" + to + "\" is not a numeric value");
            }
          }
          else {
            throw new InvalidValueTypeException(context, "For start \"" + from + "\" is not a numeric value");
          }
        }
        else if (component instanceof WhileContainer) {
          WhileContainer whileContainer = (WhileContainer) component;

          while (true) {
            Value value = whileContainer.getCondition().resolve(context);

            if (value instanceof BoolValue) {
              BoolValue boolValue = (BoolValue) value;

              if (boolValue.value()) {
                run(whileContainer, shouldRun);
              }
              else {
                break;
              }
            }
            else {
              throw new InvalidValueTypeException(context, "Cannot apply while condition in value of type " + value.getType());
            }
          }
        }
        else if (component instanceof DoWhileContainer) {
          DoWhileContainer doWhileContainer = (DoWhileContainer) component;

          while (true) {
            run(doWhileContainer, shouldRun);

            if (!doWhileContainer.hasCondition()) {
              break;
            }

            Value value = doWhileContainer.getCondition().resolve(context);

            if (value instanceof BoolValue) {
              BoolValue boolValue = (BoolValue) value;

              if (!boolValue.value()) {
                break;
              }
            }
            else {
              throw new InvalidValueTypeException(context, "Cannot apply while condition in value of type " + value.getType());
            }
          }
        }
        else if (component instanceof IfContainer || (component instanceof ElseIfContainer && hadIfAndNotProceed)) {
          IfContainer ifContainer = (IfContainer) component;
          Value value = ifContainer.getCondition().resolve(context);

          if (value instanceof BoolValue) {
            BoolValue boolValue = (BoolValue) value;

            if (boolValue.value()) {
              run(ifContainer, shouldRun);
            }
            else {
              hadIfAndNotProceed = true;

              continue;
            }
          }
          else {
            throw new InvalidValueTypeException(context, "Cannot apply if condition in value of type " + value.getType());
          }
        }
        else if (component instanceof ElseContainer) {
          if (hadIfAndNotProceed) {
            run((Container) component, shouldRun);
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

      hadIfAndNotProceed = false;
    }

    return result;
  }
}