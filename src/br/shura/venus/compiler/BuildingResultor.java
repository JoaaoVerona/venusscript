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

package br.shura.venus.compiler;

import br.shura.venus.exception.UnexpectedTokenException;
import br.shura.venus.operator.Operator;
import br.shura.venus.resultor.Operation;
import br.shura.venus.resultor.Resultor;

/**
 * BuildingResultor.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 10/05/16 - 23:01
 * @since GAMMA - 0x3
 */
public class BuildingResultor {
  private Operator operator;
  private Resultor resultor;

  public void addOperator(VenusParser parser, Token owner, Operator op) throws UnexpectedTokenException {
    if (operator != null) {
      parser.bye(owner, "already have an operator");
    }

    if (resultor == null) {
      parser.bye(owner, "no left operation value");
    }

    this.operator = op;
  }

  public void addResultor(VenusParser parser, Token owner, Resultor rslt) throws UnexpectedTokenException {
    if (resultor == null) {
      this.resultor = rslt;
    }
    else if (operator != null) {
      this.resultor = new Operation(operator, resultor, rslt);
      this.operator = null;
    }
    else {
      parser.bye(owner, "expected operator, found another resultor");
    }
  }

  public Resultor build() {
    return resultor;
  }
}