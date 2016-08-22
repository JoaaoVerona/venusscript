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

package com.github.bloodshura.venus.component;

import com.github.bloodshura.venus.component.object.ObjectDefinition;
import com.github.bloodshura.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.venus.exception.runtime.UndefinedFunctionException;
import com.github.bloodshura.venus.exception.runtime.UndefinedValueTypeException;
import com.github.bloodshura.venus.executor.Context;
import com.github.bloodshura.venus.function.Definition;
import com.github.bloodshura.venus.function.Function;
import com.github.bloodshura.venus.type.Type;
import com.github.bloodshura.venus.value.FunctionRefValue;
import com.github.bloodshura.venus.value.Value;
import com.github.bloodshura.x.collection.list.XList;
import com.github.bloodshura.x.collection.list.impl.XArrayList;
import com.github.bloodshura.x.collection.view.XView;
import com.github.bloodshura.x.util.layer.XApi;

/**
 * Container.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 01:40
 * @since GAMMA - 0x3
 */
public abstract class Container extends Component {
  protected Context context;
  private final XList<Component> children;

  public Container() {
    this.children = new XArrayList<>();

    getChildren().addInsertionListener(component -> component.setParent(this));
  }

  public Function findFunction(Context context, String name, XView<Type> argumentTypes) throws ScriptRuntimeException {
    XApi.requireNonNull(name, "name");

    if (context.hasVar(name)) {
      Value value = context.getVarValue(name); // Should not need to catch UndefinedVariableException since we already
                                               // checked that the variable exists
      if (value instanceof FunctionRefValue) {
        FunctionRefValue reference = (FunctionRefValue) value;

        return context.getOwner().findFunction(context, reference.value(), null);
      }
    }

    Definition foundVarArgs = null;

    for (Definition definition : getChildren().selectType(Definition.class)) {
      if (definition.accepts(name, argumentTypes)) {
        if (definition.isVarArgs()) {
          foundVarArgs = definition;
        }
        else {
          return definition;
        }
      }
    }

    if (foundVarArgs != null) {
      return foundVarArgs;
    }

    if (hasParent()) {
      try {
        return getParent().findFunction(context, name, argumentTypes);
      }
      catch (UndefinedFunctionException exception) {
      }
    }

    throw new UndefinedFunctionException(context, name, argumentTypes);
  }

  public ObjectDefinition findObjectDefinition(Context context, String name) throws ScriptRuntimeException {
    for (ObjectDefinition definition : getChildren().selectType(ObjectDefinition.class)) {
      if (definition.getName().equals(name)) {
        return definition;
      }
    }

    if (hasParent()) {
      try {
        return getParent().findObjectDefinition(context, name);
      }
      catch (UndefinedValueTypeException exception) {
      }
    }

    throw new UndefinedValueTypeException(context, name);
  }

  public Type findType(Context context, String name) throws ScriptRuntimeException {
    for (ObjectDefinition definition : getChildren().selectType(ObjectDefinition.class)) {
      if (definition.getName().equals(name)) {
        return definition.getType();
      }
    }

    if (hasParent()) {
      try {
        return getParent().findType(context, name);
      }
      catch (UndefinedValueTypeException exception) {
      }
    }

    throw new UndefinedValueTypeException(context, name);
  }

  public XList<Component> getChildren() {
    return children;
  }

  public Context getContext() {
    return context;
  }

  @Override
  public void setParent(Container parent) {
    super.setParent(parent);
    this.context = parent.getContext();
  }
}