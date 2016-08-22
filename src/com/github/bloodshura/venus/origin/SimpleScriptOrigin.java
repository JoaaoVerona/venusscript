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

package com.github.bloodshura.venus.origin;

import java.io.IOException;

/**
 * SimpleScriptOrigin.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 07/05/16 - 19:16
 * @since GAMMA - 0x3
 */
public class SimpleScriptOrigin implements ScriptOrigin {
  private final String content;
  private final String name;

  public SimpleScriptOrigin(String name, String content) {
    this.content = content;
    this.name = name;
  }

  @Override
  public String getScriptName() {
    return name;
  }

  @Override
  public String read() throws IOException {
    return content;
  }

  @Override
  public String toString() {
    return "simpleorigin(" + getScriptName() + ')';
  }
}