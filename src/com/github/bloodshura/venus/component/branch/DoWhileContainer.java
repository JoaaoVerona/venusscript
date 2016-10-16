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

package com.github.bloodshura.venus.component.branch;

import com.github.bloodshura.venus.component.Container;
import com.github.bloodshura.venus.expression.Expression;

public class DoWhileContainer extends Container implements Breakable {
  private Expression condition;

  public DoWhileContainer(Expression condition) {
    this.condition = condition;
  }

  public Expression getCondition() {
    return condition;
  }

  public boolean hasCondition() {
    return getCondition() != null;
  }

  public void setCondition(Expression condition) {
    this.condition = condition;
  }

  @Override
  public String toString() {
    return "dowhile(" + getCondition() + ')';
  }
}