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

package com.github.bloodshura.venus.component.object;

import com.github.bloodshura.venus.component.Container;
import com.github.bloodshura.venus.executor.Context;
import com.github.bloodshura.venus.type.ObjectType;
import com.github.bloodshura.venus.type.Type;
import com.github.bloodshura.x.collection.list.XList;
import com.github.bloodshura.x.collection.list.impl.XArrayList;

public class ObjectDefinition extends Container {
  private final XList<Attribute> attributes;
  private final String name;
  private final Type type;

  public ObjectDefinition(String name) {
    this.attributes = new XArrayList<>();
    this.name = name;
    this.type = new ObjectType(name);
  }

  public XList<Attribute> getAttributes() {
    return attributes;
  }

  public String getName() {
    return name;
  }

  public Type getType() {
    return type;
  }

  @Override
  public void setParent(Container parent) {
    super.setParent(parent);
    this.context = new Context(this, parent.getContext());
  }

  @Override
  public String toString() {
    return "objectdef(" + getName() + ')';
  }
}