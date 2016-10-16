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

package com.github.bloodshura.venus.executor;

import com.github.bloodshura.venus.component.AsyncContainer;
import com.github.bloodshura.venus.component.Component;
import com.github.bloodshura.venus.component.Container;
import com.github.bloodshura.venus.component.SimpleComponent;
import com.github.bloodshura.venus.component.branch.Break;
import com.github.bloodshura.venus.component.branch.Continue;
import com.github.bloodshura.venus.component.branch.DoWhileContainer;
import com.github.bloodshura.venus.component.branch.ElseContainer;
import com.github.bloodshura.venus.component.branch.ElseIfContainer;
import com.github.bloodshura.venus.component.branch.ForEachContainer;
import com.github.bloodshura.venus.component.branch.ForRangeContainer;
import com.github.bloodshura.venus.component.branch.IfContainer;
import com.github.bloodshura.venus.component.branch.Return;
import com.github.bloodshura.venus.component.branch.WhileContainer;
import com.github.bloodshura.venus.exception.runtime.InvalidValueTypeException;
import com.github.bloodshura.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.venus.expression.Expression;
import com.github.bloodshura.venus.function.Definition;
import com.github.bloodshura.venus.origin.ScriptMode;
import com.github.bloodshura.venus.value.BoolValue;
import com.github.bloodshura.venus.value.DecimalValue;
import com.github.bloodshura.venus.value.IntegerValue;
import com.github.bloodshura.venus.value.IterableValue;
import com.github.bloodshura.venus.value.NumericValue;
import com.github.bloodshura.venus.value.Value;
import com.github.bloodshura.x.collection.list.XListIterator;
import com.github.bloodshura.x.collection.store.impl.XQueue;
import com.github.bloodshura.x.logging.XLogger;
import com.github.bloodshura.x.runnable.XThread;
import com.github.bloodshura.x.runnable.collection.ThreadPool;

import java.util.function.Supplier;

/**
 * VenusExecutor.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 09/05/16 - 02:47
 * @since GAMMA - 0x3
 */
public class VenusExecutor {
  private final XQueue<ScriptRuntimeException> asyncExceptions;
  private final ThreadPool<XThread> asyncThreads;
  private boolean breaking;
  private boolean continuing;
  private boolean shouldRun;

  public VenusExecutor() {
    this.asyncExceptions = new XQueue<>();
    this.asyncThreads = new ThreadPool<>();
    this.shouldRun = true;
  }

  public Value run(Container container, ScriptMode mode) throws ScriptRuntimeException {
    return run(container, mode, () -> shouldRun);
  }

  public void stop() {
    this.shouldRun = false;
  }

  protected Value run(Container container, ScriptMode mode, Supplier<Boolean> shouldRun) throws ScriptRuntimeException {
    Context context = container.getContext();
    XListIterator<Component> iterator = container.getChildren().iterator();
    Value result = new IntegerValue(0);
    boolean hadIfAndNotProceed = false;

    container.getApplicationContext().setExecutor(this);

    while (shouldRun.get() && iterator.hasNext()) {
      Component component = iterator.next();

      if (breaking || continuing) {
        break;
      }

      if (!asyncExceptions.isEmpty()) {
        throw asyncExceptions.poll();
      }

      container.getApplicationContext().setCurrentLine(component.getSourceLine());

      if (component instanceof Container) {
        if (component instanceof AsyncContainer) {
          AsyncContainer asyncContainer = (AsyncContainer) component;
          XThread thread = new XThread("AsyncVenusThread", () -> {
            VenusExecutor executor = new VenusExecutor();

            try {
              executor.run(asyncContainer, mode, () -> VenusExecutor.this.shouldRun);
            }
            catch (ScriptRuntimeException exception) {
              asyncExceptions.push(exception);
            }
            catch (Exception exception) {
              XLogger.println(exception);
            }
          });

          asyncThreads.add(thread);
          thread.setDaemon(asyncContainer.isDaemon());
          thread.start();
        }
        else if (component instanceof ForEachContainer) {
          ForEachContainer forContainer = (ForEachContainer) component;
          Expression expression = forContainer.getIterable();
          Value value = expression.resolve(context);

          if (value instanceof IterableValue) {
            IterableValue iterable = (IterableValue) value;

            for (Value element : iterable) {
              context.setVar(forContainer.getVarName(), element);
              run(forContainer, mode, shouldRun);

              if (breaking) {
                this.breaking = false;

                break;
              }

              if (continuing) {
                this.continuing = false;
              }
            }
          }
          else {
            throw new InvalidValueTypeException(context, "For value \"" + value + "\" is not iterable");
          }
        }
        else if (component instanceof ForRangeContainer) {
          ForRangeContainer forContainer = (ForRangeContainer) component;
          Value from = forContainer.getFrom().resolve(context);
          Value to = forContainer.getTo().resolve(context);

          if (from instanceof NumericValue) {
            if (to instanceof NumericValue) {
              NumericValue numericFrom = (NumericValue) from;
              NumericValue numericTo = (NumericValue) to;
              boolean isDecimal = numericFrom instanceof DecimalValue || numericTo instanceof DecimalValue;
              Value count = isDecimal ? new DecimalValue(numericFrom.value().doubleValue()) :
                            new IntegerValue(numericFrom.value().longValue());

              while (count.lowerEqualThan(to).value().equals(true)) {
                context.setVar(forContainer.getVarName(), count);
                run(forContainer, mode, shouldRun);

                if (breaking) {
                  this.breaking = false;

                  break;
                }

                if (continuing) {
                  this.continuing = false;
                }

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
                run(whileContainer, mode, shouldRun);

                if (breaking) {
                  this.breaking = false;

                  break;
                }

                if (continuing) {
                  this.continuing = false;
                }
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
            run(doWhileContainer, mode, shouldRun);

            if (breaking) {
              this.breaking = false;

              break;
            }

            if (continuing) {
              this.continuing = false;
            }

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
              run(ifContainer, mode, shouldRun);
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
            run((Container) component, mode, shouldRun);
          }
        }
        else if (!(component instanceof Definition)) {
          run((Container) component, mode, shouldRun);
        }
      }
      else if (component instanceof Break) {
        this.breaking = true;
      }
      else if (component instanceof Continue) {
        this.continuing = true;
      }
      else if (component instanceof Return) {
        Return returner = (Return) component;
        Value value = returner.getExpression().resolve(context);

        if (value != null) {
          result = value;
        }

        break;
      }
      else if (component instanceof SimpleComponent) {
        SimpleComponent simple = (SimpleComponent) component;
        Value value = simple.getExpression().resolve(context);

        if (value != null) {
          if (mode == ScriptMode.EVALUATION) {
            result = value;
          }
          else if (mode == ScriptMode.INTERACTIVE) {
            result = value;
            XLogger.println(value);
          }
        }
      }

      hadIfAndNotProceed = false;
    }

    //asyncThreads.join(); // TODO Bugged. Removed temporarily.

    if (!asyncExceptions.isEmpty()) {
      throw asyncExceptions.poll();
    }

    return result;
  }
}