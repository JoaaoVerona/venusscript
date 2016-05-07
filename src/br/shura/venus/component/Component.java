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

package br.shura.venus.component;

import br.shura.venus.Context;
import br.shura.venus.Script;
import br.shura.x.lang.annotation.Internal;
import br.shura.x.util.layer.XApi;

/**
 * Component.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 01:39
 * @since GAMMA - 0x3
 */
public abstract class Component {
  private Container parent;

  public Context getApplicationContext() {
    XApi.requireState(getParent() != null, "Could not retrieve application context; no parent available");

    return getParent().getApplicationContext();
  }

  public Container getParent() {
    return parent;
  }

  public Script getScript() {
    XApi.requireState(getParent() != null, "Could not retrieve script; no parent available");

    return getParent().getScript();
  }

  public boolean hasParent() {
    return getParent() != null;
  }

  @Internal
  public void setParent(Container parent) {
    this.parent = parent;
  }
}