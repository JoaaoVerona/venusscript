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

package com.github.bloodshura.venus.library.std;

import com.github.bloodshura.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.venus.executor.Context;
import com.github.bloodshura.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.venus.function.Method;
import com.github.bloodshura.venus.function.annotation.MethodArgs;
import com.github.bloodshura.venus.function.annotation.MethodName;
import com.github.bloodshura.venus.value.ArrayValue;
import com.github.bloodshura.venus.value.IntegerValue;
import com.github.bloodshura.venus.value.Value;
import com.github.bloodshura.x.worker.ArrayWorker;

@MethodArgs({ IntegerValue.class, Value.class })
@MethodName("newArray")
public class NewArray extends Method {
  @Override
  public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
    IntegerValue size = (IntegerValue) descriptor.get(0);
    Value[] content = new Value[size.value().intValue()];

    ArrayWorker.fill(content, descriptor.get(1));

    return new ArrayValue(content);
  }
}