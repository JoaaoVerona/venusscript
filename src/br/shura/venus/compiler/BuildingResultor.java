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
import br.shura.venus.operator.BinaryOperator;
import br.shura.venus.operator.Operator;
import br.shura.venus.operator.UnaryOperator;
import br.shura.venus.resultor.BinaryOperation;
import br.shura.venus.resultor.Resultor;
import br.shura.venus.resultor.UnaryOperation;
import br.shura.x.collection.store.impl.Stack;

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
  private final Stack<UnaryOperator> unaryWhenAlready;

  public BuildingResultor() {
    this.unaryWhenAlready = new Stack<>();
  }

  public void addOperator(VenusParser parser, Token owner, Operator op) throws UnexpectedTokenException {
    if (hasOperator()) {
      if (op instanceof UnaryOperator) {
        unaryWhenAlready.push((UnaryOperator) op);

        return;
      }
      else {
        parser.bye(owner, "already have an operator \"" + operator + "\"");
      }
    }

    if (op instanceof BinaryOperator && !hasResultor()) {
      parser.bye(owner, "no left operation value");
    }

    if (op instanceof UnaryOperator && hasResultor()) {
      parser.bye(owner, "cannot apply unary operator to left-sided value");
    }

    this.operator = op;
  }

  public void addResultor(VenusParser parser, Token owner, Resultor rslt) throws UnexpectedTokenException {
    if (!hasResultor()) {
      if (operator instanceof UnaryOperator) {
        while (!unaryWhenAlready.isEmpty()) {
          rslt = new UnaryOperation(unaryWhenAlready.pop(), rslt);
        }

        this.resultor = new UnaryOperation((UnaryOperator) operator, rslt);
      }
      else {
        this.resultor = rslt;
      }
    }
    else if (hasOperator()) {
      if (operator instanceof BinaryOperator) {
        while (!unaryWhenAlready.isEmpty()) {
          rslt = new UnaryOperation(unaryWhenAlready.pop(), rslt);
        }

        this.resultor = new BinaryOperation((BinaryOperator) operator, resultor, rslt);
      }
      else {
        parser.bye("Excepted a binary or unary operator, received " + operator.getClass().getSimpleName());
      }

      this.operator = null;
    }
    else {
      parser.bye(owner, "expected operator, found another resultor");
    }
  }

  public Resultor build() {
    return resultor;
  }

  public boolean hasOperator() {
    return operator != null;
  }

  public boolean hasResultor() {
    return resultor != null;
  }
}