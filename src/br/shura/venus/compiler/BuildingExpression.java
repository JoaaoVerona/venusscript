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

import br.shura.venus.exception.compile.UnexpectedTokenException;
import br.shura.venus.expression.BinaryOperation;
import br.shura.venus.expression.Expression;
import br.shura.venus.expression.InContext;
import br.shura.venus.expression.UnaryOperation;
import br.shura.venus.operator.BinaryOperator;
import br.shura.venus.operator.Operator;
import br.shura.venus.operator.UnaryOperator;
import br.shura.x.collection.store.impl.Stack;

/**
 * BuildingExpression.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 10/05/16 - 23:01
 * @since GAMMA - 0x3
 */
public class BuildingExpression {
  private String inContext;
  private Operator operator;
  private Expression expression;
  private final Stack<UnaryOperator> unaryWhenAlready;

  public BuildingExpression() {
    this.unaryWhenAlready = new Stack<>();
  }

  public void addInContext(VenusParser parser, Token owner, String context) throws UnexpectedTokenException {
    if (inContext != null) {
      parser.bye(owner, "already in object access \"" + inContext + "\"");
    }

    this.inContext = context;
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

  public void addExpression(VenusParser parser, Token owner, Expression rslt) throws UnexpectedTokenException {
    if (!hasResultor()) {
      if (operator instanceof UnaryOperator) {
        while (!unaryWhenAlready.isEmpty()) {
          rslt = new UnaryOperation(unaryWhenAlready.pop(), rslt);
        }

        setExpression(new UnaryOperation((UnaryOperator) operator, rslt));
      }
      else {
        setExpression(rslt);
      }
    }
    else if (hasOperator()) {
      if (operator instanceof BinaryOperator) {
        while (!unaryWhenAlready.isEmpty()) {
          rslt = new UnaryOperation(unaryWhenAlready.pop(), rslt);
        }

        setExpression(new BinaryOperation((BinaryOperator) operator, expression, rslt));
      }
      else {
        parser.bye("Excepted a binary or unary operator, received " + operator.getClass().getSimpleName());
      }

      this.operator = null;
    }
    else {
      parser.bye(owner, "expected operator, found another expression");
    }
  }

  public Expression build() {
    return expression;
  }

  public boolean hasOperator() {
    return operator != null;
  }

  public boolean hasResultor() {
    return expression != null;
  }

  private void setExpression(Expression expression) {
    if (inContext != null) {
      this.expression = new InContext(inContext, expression);

      this.inContext = null;
    }
    else {
      this.expression = expression;
    }
  }
}