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

package com.github.bloodshura.venus.library;

import com.github.bloodshura.venus.function.Function;
import com.github.bloodshura.x.collection.list.impl.XArrayList;

/**
 * VenusLibrary.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 07/05/16 - 17:40
 * @since GAMMA - 0x3
 */
public class VenusLibrary extends XArrayList<Function> {
  public boolean add(Class<? extends Function> object) {
    try {
      return add(object.newInstance());
    }
    catch (IllegalAccessException | InstantiationException exception) {
      throw new IllegalArgumentException("Could not instantiate method class \"" + object.getName() + "\"");
    }
  }

  @SafeVarargs
  public final boolean addAll(Class<? extends Function>... objects) {
    boolean allAdded = true;

    for (Class<? extends Function> object : objects) {
      if (!add(object)) {
        allAdded = false;
      }
    }

    return allAdded;
  }
}