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

package com.github.bloodshura.venus.library.dialogs;

import com.github.bloodshura.dialogs.XDialogs;
import com.github.bloodshura.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.venus.executor.Context;
import com.github.bloodshura.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.venus.function.VoidMethod;
import com.github.bloodshura.venus.function.annotation.MethodName;
import com.github.bloodshura.venus.function.annotation.MethodVarArgs;
import com.github.bloodshura.venus.value.Value;
import com.github.bloodshura.x.charset.build.TextBuilder;
import com.github.bloodshura.x.sys.enumeration.AlertType;
import com.github.bloodshura.x.util.Pool;

/**
 * InfoDialog.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 14/05/16 - 19:50
 * @since GAMMA - 0x3
 */
@MethodName("infoDialog")
@MethodVarArgs
public class InfoDialog extends VoidMethod {
  @Override
  public void callVoid(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
    if (descriptor.isEmpty()) {
      return;
    }

    String title = descriptor.transform(0, Value::toString, null);
    TextBuilder message = Pool.newBuilder();
    int offset = descriptor.count() > 1 ? 1 : 0;

    for (int i = offset; i < descriptor.count(); i++) {
      message.append(descriptor.get(i));
      message.newLine();
    }

    XDialogs.show(AlertType.INFORMATION, title, message);
  }
}