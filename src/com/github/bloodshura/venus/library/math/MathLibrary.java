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

package com.github.bloodshura.venus.library.math;

import com.github.bloodshura.venus.library.VenusLibrary;
import com.github.bloodshura.x.math.MathProvider;

import java.lang.reflect.Method;

public class MathLibrary extends VenusLibrary {
  public MathLibrary(MathProvider instance) {
    for (Method method : MathProvider.class.getDeclaredMethods()) {
      if (MathFunction.validate(method)) {
        add(new MathFunction(method, instance));
      }
    }

    addAll(RandDecimal.class, RandInt.class);
  }
}