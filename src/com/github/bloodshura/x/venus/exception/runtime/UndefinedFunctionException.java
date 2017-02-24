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

package com.github.bloodshura.x.venus.exception.runtime;

import com.github.bloodshura.x.venus.executor.Context;
import com.github.bloodshura.x.venus.type.Type;
import com.github.bloodshura.x.collection.view.XView;

public class UndefinedFunctionException extends ScriptRuntimeException {
  public UndefinedFunctionException(Context context, String functionName, XView<Type> argumentTypes) {
    super(context, "No definition found for a function named \"" + functionName + "\" taking argument types: " + argumentTypes);
  }
}