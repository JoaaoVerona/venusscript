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

package com.github.bloodshura.x.venus.library;

import com.github.bloodshura.x.venus.function.Function;
import com.github.bloodshura.x.venus.type.Type;
import com.github.bloodshura.x.collection.list.impl.XArrayList;
import com.github.bloodshura.x.collection.view.XView;
import com.github.bloodshura.x.lang.layer.XApi;

public class LibraryList extends XArrayList<VenusLibrary> {
  public Function findFunction(String name, XView<Type> argumentTypes) {
    XApi.requireNonNull(name, "name");

    Function found = null;
    Function foundVarArgs = null;

    for (VenusLibrary library : this) {
      for (Function function : library) {
        if (function.accepts(name, argumentTypes)) {
          if (function.isVarArgs()) {
            if (foundVarArgs == null) {
              foundVarArgs = function;
            }
          }
          else {
            found = function;
          }
        }
      }
    }

    return found != null ? found : foundVarArgs;
  }
}