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

package br.shura.venus.component.branch;

import br.shura.venus.component.Container;
import br.shura.venus.resultor.Resultor;

/**
 * ForEachContainer.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 12/05/16 - 14:37
 * @since GAMMA - 0x3
 */
public class ForEachContainer extends Container {
  private final Resultor adjustment;
  private final Resultor from;
  private final Resultor to;
  private final String varName;

  public ForEachContainer(String varName, Resultor from, Resultor to, Resultor adjustment) {
    this.adjustment = adjustment;
    this.from = from;
    this.to = to;
    this.varName = varName;
  }

  public Resultor getAdjustment() {
    return adjustment;
  }

  @Override
  public String getDisplayName() {
    return "foreach(" + getVarName() + " in [" + getFrom() + ", " + getTo() + "])";
  }

  public Resultor getFrom() {
    return from;
  }

  public Resultor getTo() {
    return to;
  }

  public String getVarName() {
    return varName;
  }
}