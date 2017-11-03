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

package com.github.bloodshura.x.venus.library.math;

import com.github.bloodshura.x.math.random.XRandom;
import com.github.bloodshura.x.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.x.venus.executor.Context;
import com.github.bloodshura.x.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.x.venus.function.Method;
import com.github.bloodshura.x.venus.function.annotation.MethodArgs;
import com.github.bloodshura.x.venus.function.annotation.MethodName;
import com.github.bloodshura.x.venus.value.DecimalValue;
import com.github.bloodshura.x.venus.value.Value;

@MethodArgs({ DecimalValue.class, DecimalValue.class })
@MethodName("randDecimal")
public class RandDecimal extends Method {
  @Override
  public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
    DecimalValue min = (DecimalValue) descriptor.get(0);
    DecimalValue max = (DecimalValue) descriptor.get(1);
    double minValue = min.value();
    double maxValue = max.value();

    if (minValue > maxValue) {
      double temp = maxValue;

      maxValue = minValue;
      minValue = temp;
    }

    return new DecimalValue(XRandom.INSTANCE.nextDouble(minValue, maxValue));
  }
}