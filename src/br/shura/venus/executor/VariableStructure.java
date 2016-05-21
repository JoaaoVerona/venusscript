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

package br.shura.venus.executor;

import br.shura.venus.value.Value;
import br.shura.x.collection.list.List;
import br.shura.x.collection.list.impl.ArrayList;

/**
 * VariableStructure.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 20/05/16 - 21:26
 * @since GAMMA - 0x3
 */
public class VariableStructure {
  private final List<Object> changeMonitors;
  private final Object lockMonitor;
  private Value value;

  public VariableStructure(Value value) {
    this.changeMonitors = new ArrayList<>();
    this.lockMonitor = new Object();
    this.value = value;
  }

  public void addChangeMonitor(Object monitor) {
    synchronized (changeMonitors) {
      changeMonitors.add(monitor);
    }
  }

  public Object getLockMonitor() {
    return lockMonitor;
  }

  public Value getValue() {
    return value;
  }

  public void removeChangeMonitor(Object monitor) {
    synchronized (changeMonitors) {
      changeMonitors.remove(monitor);
    }
  }

  public void setValue(Value value) {
    this.value = value;

    synchronized (changeMonitors) {
      for (Object monitor : changeMonitors) {
        monitor.notifyAll();
      }
    }
  }
}