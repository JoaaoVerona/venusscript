//////////////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2016, João Vitor Verona Biazibetti - All Rights Reserved                /
//                                                                                       /
// Licensed under the GNU General Public License v3;                                     /
// you may not use this file except in compliance with the License.                      /
//                                                                                       /
// You may obtain a copy of the License at                                               /
//     http://www.gnu.org/licenses/gpl.html                                              /
//                                                                                       /
// Unless required by applicable law or agreed to in writing, software                   /
// distributed under the License is distributed on an "AS IS" BASIS,                     /
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.              /
// See the License for the specific language governing permissions and                   /
// limitations under the License.                                                        /
//                                                                                       /
// Written by João Vitor Verona Biazibetti <joaaoverona@gmail.com>, March 2016           /
// https://www.github.com/BloodShura                                                     /
//////////////////////////////////////////////////////////////////////////////////////////

package br.shura.venus.library.math;

import br.shura.venus.exception.ScriptRuntimeException;
import br.shura.venus.executor.Context;
import br.shura.venus.function.FunctionCallDescriptor;
import br.shura.venus.function.Method;
import br.shura.venus.function.annotation.MethodArgs;
import br.shura.venus.function.annotation.MethodName;
import br.shura.venus.value.DecimalValue;
import br.shura.venus.value.Value;
import br.shura.venus.value.ValueType;
import br.shura.x.math.random.XRandom;

/**
 * RandDecimal.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 17/05/16 - 13:59
 * @since GAMMA - 0x3
 */
@MethodArgs({ ValueType.DECIMAL, ValueType.DECIMAL })
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