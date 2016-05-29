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

package br.shura.venus.library.dialogs;

import br.shura.venus.exception.runtime.ScriptRuntimeException;
import br.shura.venus.executor.Context;
import br.shura.venus.function.FunctionCallDescriptor;
import br.shura.venus.function.Method;
import br.shura.venus.function.annotation.MethodArgs;
import br.shura.venus.function.annotation.MethodName;
import br.shura.venus.value.BoolValue;
import br.shura.venus.value.StringValue;
import br.shura.venus.value.Value;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * SetTheme.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 14/05/16 - 20:34
 * @since GAMMA - 0x3
 */
@MethodArgs(StringValue.class)
@MethodName("setTheme")
public class SetTheme extends Method {
  @Override
  public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
    StringValue value = (StringValue) descriptor.get(0);
    String themeName = value.value();
    String themePath = null;

    if (themeName.equalsIgnoreCase("metal")) {
      themePath = UIManager.getCrossPlatformLookAndFeelClassName();
    }
    else if (themeName.equalsIgnoreCase("system")) {
      themePath = UIManager.getSystemLookAndFeelClassName();
    }

    if (themePath != null) {
      try {
        UIManager.setLookAndFeel(themePath);

        return new BoolValue(true);
      }
      catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException exception) {
      }
    }

    return new BoolValue(false);
  }
}