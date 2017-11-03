/*
 * Copyright (c) 2013-2017, João Vitor Verona Biazibetti - All Rights Reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * https://www.github.com/BloodShura
 */

package com.github.bloodshura.x.venus.executor;

import com.github.bloodshura.x.collection.map.XMap;
import com.github.bloodshura.x.collection.map.impl.XLinkedMap;
import com.github.bloodshura.x.util.XApi;
import com.github.bloodshura.x.venus.component.Container;
import com.github.bloodshura.x.venus.component.Script;
import com.github.bloodshura.x.venus.exception.runtime.UndefinedVariableException;
import com.github.bloodshura.x.venus.expression.Variable;
import com.github.bloodshura.x.venus.value.Value;

public class Context implements Cloneable {
  private static int NEXT_ID = 0;
  private final int id;
  private final Container owner;
  private final Context parent;
  private final XMap<String, VariableStructure> variables;

  public Context(Container owner, Context parent) {
    this.id = NEXT_ID++;
    this.owner = owner;
    this.parent = parent;
    this.variables = new XLinkedMap<>();
  }

  @Override
  public Context clone() {
    return cloneWith(getOwner(), getParent());
  }

  public Context cloneWith(Container owner, Context parent) {
    Context context = new Context(owner, parent);

    context.getVariables().addAll(getVariables());

    return context;
  }

  public ApplicationContext getApplicationContext() {
    return getOwner().getApplicationContext();
  }

  public Container getOwner() {
    return owner;
  }

  public Context getParent() {
    return parent;
  }

  public Script getScript() {
    return getOwner().getScript();
  }

  public VariableStructure getVar(String name) throws UndefinedVariableException {
    if (name.length() > 1 && name.charAt(0) == '$') {
      return getApplicationContext().getVar(name.substring(1));
    }

    if (isOwnerOf(name)) {
      return getVariables().get(name);
    }

    if (hasParent()) {
      try {
        return getParent().getVar(name);
      }
      catch (UndefinedVariableException exception) {
      }
    }

    throw new UndefinedVariableException(this, name);
  }

  public VariableStructure getVar(Variable variable) throws UndefinedVariableException {
    return getVar(variable.getName());
  }

  public Value getVarValue(String name) throws UndefinedVariableException {
    XApi.requireNonNull(name, "name");

    return getVar(name).getValue();
  }

  public Value getVarValue(Variable variable) throws UndefinedVariableException {
    return getVarValue(variable.getName());
  }

  public XMap<String, VariableStructure> getVariables() {
    return variables;
  }

  public boolean hasParent() {
    return getParent() != null;
  }

  public boolean hasVar(String name) throws UndefinedVariableException {
    if (name.length() > 1 && name.charAt(0) == '$') {
      return getApplicationContext().hasVar(name.substring(1));
    }

    return isOwnerOf(name) || (hasParent() && getParent().hasVar(name));
  }

  public boolean isOwnerOf(String name) {
    return getVariables().containsKey(name);
  }

  public void setVar(String name, Value value) {
    if (!changeVar(name, value)) {
      getVariables().add(name, new VariableStructure(value));
    }
  }

  public String toDetailedString() {
    return "context(owner=" + getOwner() + ", vars=" + getVariables() + ", parent=" + getParent() + ')';
  }

  @Override
  public String toString() {
    return "#" + id;
  }

  protected boolean changeVar(String name, Value value) {
    if (name.length() > 1 && name.charAt(0) == '$') {
      getOwner().getApplicationContext().setVar(name.substring(1), value);

      return true;
    }

    if (isOwnerOf(name)) {
      getVariables().get(name).setValue(value);

      return true;
    }

    return hasParent() && getParent().changeVar(name, value);
  }
}