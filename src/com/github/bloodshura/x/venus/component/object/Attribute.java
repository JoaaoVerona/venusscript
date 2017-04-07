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

package com.github.bloodshura.x.venus.component.object;

import javax.annotation.Nonnull;

import com.github.bloodshura.x.venus.expression.Expression;
import com.github.bloodshura.x.object.Base;

public class Attribute extends Base {
  private final Expression defaultExpression;
  private final String name;

  public Attribute(String name, Expression defaultExpression) {
    this.defaultExpression = defaultExpression;
    this.name = name;
  }

  public Expression getDefaultExpression() {
    return defaultExpression;
  }

  public String getName() {
    return name;
  }

  public boolean hasDefaultExpression() {
    return getDefaultExpression() != null;
  }

  @Nonnull
  @Override
  protected Object[] stringValues() {
    return new Object[] { getName(), getDefaultExpression() };
  }
}