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

import com.github.bloodshura.x.lang.Resource;

import java.io.IOException;

/**
 * StreamScriptOrigin.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 11/05/16 - 00:45
 * @since GAMMA - 0x3
 */
public class StreamScriptOrigin implements ScriptOrigin {
  private final String name;
  private final Resource resource;

  public StreamScriptOrigin(String name, Resource resource) {
    this.name = name;
    this.resource = resource;
  }

  public Resource getResource() {
    return resource;
  }

  @Override
  public String getScriptName() {
    return name;
  }

  @Override
  public String read() throws IOException {
    return getResource().readString();
  }

  @Override
  public String toString() {
    return "streamorigin(" + getScriptName() + ')';
  }
}