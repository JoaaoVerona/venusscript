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

package br.shura.venus.origin;

import br.shura.venus.library.MethodLibrary;
import br.shura.venus.library.std.StdLibrary;

import java.io.IOException;

/**
 * ScriptOrigin.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 07/05/16 - 18:35
 * @since GAMMA - 0x3
 */
public interface ScriptOrigin {
  ScriptOrigin findInclude(String includeName);

  default MethodLibrary findLibrary(String libraryName) {
    if (libraryName.equals("std")) {
      return new StdLibrary();
    }

    return null;
  }

  String getScriptName();

  String read() throws IOException;
}