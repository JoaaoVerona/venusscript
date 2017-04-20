//////////////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2013-2017, João Vitor Verona Biazibetti - All Rights Reserved           /
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

package com.github.bloodshura.x.venus.component;

import com.github.bloodshura.x.venus.component.object.ObjectDefinition;
import com.github.bloodshura.x.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.x.venus.exception.runtime.UndefinedFunctionException;
import com.github.bloodshura.x.venus.exception.runtime.UndefinedValueTypeException;
import com.github.bloodshura.x.venus.executor.Context;
import com.github.bloodshura.x.venus.function.Definition;
import com.github.bloodshura.x.venus.function.Function;
import com.github.bloodshura.x.venus.type.Type;
import com.github.bloodshura.x.venus.value.FunctionRefValue;
import com.github.bloodshura.x.venus.value.Value;
import com.github.bloodshura.x.collection.list.XList;
import com.github.bloodshura.x.collection.list.impl.XArrayList;
import com.github.bloodshura.x.collection.view.XView;
import com.github.bloodshura.x.util.XApi;

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