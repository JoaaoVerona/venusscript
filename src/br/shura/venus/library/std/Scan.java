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

package br.shura.venus.library.std;

import br.shura.venus.exception.runtime.InvalidValueTypeException;
import br.shura.venus.exception.runtime.ScriptRuntimeException;
import br.shura.venus.executor.Context;
import br.shura.venus.function.FunctionCallDescriptor;
import br.shura.venus.function.Method;
import br.shura.venus.function.annotation.MethodArgs;
import br.shura.venus.function.annotation.MethodName;
import br.shura.venus.type.PrimitiveType;
import br.shura.venus.type.Type;
import br.shura.venus.value.BoolValue;
import br.shura.venus.value.DecimalValue;
import br.shura.venus.value.IntegerValue;
import br.shura.venus.value.StringValue;
import br.shura.venus.value.TypeValue;
import br.shura.venus.value.Value;
import br.shura.x.logging.Logger;
import br.shura.x.worker.ParseWorker;
import br.shura.x.worker.exception.InvalidParseException;

/**
 * Scan.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 13/05/16 - 02:45
 * @since GAMMA - 0x3
 */
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
          String line = logger.scan();

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
        catch (InvalidParseException exception) {
        }
      }
    }

    throw new ScriptRuntimeException(context, "No input resource defined");
  }
}