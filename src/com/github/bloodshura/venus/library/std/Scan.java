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

package com.github.bloodshura.venus.library.std;

import com.github.bloodshura.venus.exception.runtime.InvalidValueTypeException;
import com.github.bloodshura.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.venus.executor.Context;
import com.github.bloodshura.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.venus.function.Method;
import com.github.bloodshura.venus.function.annotation.MethodArgs;
import com.github.bloodshura.venus.function.annotation.MethodName;
import com.github.bloodshura.venus.type.PrimitiveType;
import com.github.bloodshura.venus.type.Type;
import com.github.bloodshura.venus.value.BoolValue;
import com.github.bloodshura.venus.value.DecimalValue;
import com.github.bloodshura.venus.value.IntegerValue;
import com.github.bloodshura.venus.value.StringValue;
import com.github.bloodshura.venus.value.TypeValue;
import com.github.bloodshura.venus.value.Value;
import com.github.bloodshura.x.activity.logging.Logger;
import com.github.bloodshura.x.activity.scanning.XScanner;
import com.github.bloodshura.x.worker.ParseWorker;

@MethodArgs(TypeValue.class)
@MethodName("scan")
public class Scan extends Method {
  @Override
  public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
    Logger logger = context.getApplicationContext().getUserData("in", Logger.class);

    if (logger != null) {
      TypeValue value = (TypeValue) descriptor.get(0);
      Type type = value.value();

      while (true) {
        try {
          String line = XScanner.scan();

          if (type == PrimitiveType.BOOLEAN) {
            return new BoolValue(ParseWorker.toBoolean(line));
          }

          if (type == PrimitiveType.DECIMAL) {
            return new DecimalValue(ParseWorker.toDouble(line));
          }

          if (type == PrimitiveType.INTEGER) {
            return new IntegerValue(ParseWorker.toLong(line));
          }

          if (type == PrimitiveType.STRING) {
            return new StringValue(line);
          }

          if (type == PrimitiveType.TYPE) {
            Type lookup = PrimitiveType.forIdentifier(line);

            if (lookup != null) {
              return new TypeValue(lookup);
            }

            continue;
          }

          throw new InvalidValueTypeException(context, "Cannot scan for an input of type " + type);
        }
        catch (NumberFormatException exception) {
        }
      }
    }

    throw new ScriptRuntimeException(context, "No input resource defined");
  }
}