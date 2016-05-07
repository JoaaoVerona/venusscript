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

package br.shura.venus.exception;

import br.shura.venus.Context;
import br.shura.x.exception.CatchableException;

/**
 * ScriptRuntimeException.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 05/05/16 - 14:42
 * @since GAMMA - 0x3
 */
public class ScriptRuntimeException extends CatchableException {
  private final Context context;

  public ScriptRuntimeException(Context context, CharSequence message) {
    super(message + " at line " + context.currentLine() + " in script \"" + context.getOwner().getDisplayName() + "\"");
    this.context = context;
  }

  public ScriptRuntimeException(Context context, Exception exception) {
    super("Internal runtime exception at line " + context.currentLine() + " in script \"" + context.getOwner().getDisplayName() +
      "\"", exception);
    this.context = context;
  }

  public Context getContext() {
    return context;
  }
}