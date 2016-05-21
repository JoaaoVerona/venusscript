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

import br.shura.venus.library.VenusLibrary;

/**
 * StdLibrary.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 09/05/16 - 20:29
 * @since GAMMA - 0x3
 */
public class StdLibrary extends VenusLibrary {
  public StdLibrary() {
    // Basic I/O
    addAll(HasScan.class, Print.class, Println.class, Scan.class);

    // Desktop
    addAll(Beep.class, Browse.class, Shell.class);

    // Synchronous
    addAll(Consume.class, Produce.class, WaitAttribution.class, WaitDefinition.class);

    // Utilities
    addAll(Assert.class, Millis.class, Sleep.class);
  }
}