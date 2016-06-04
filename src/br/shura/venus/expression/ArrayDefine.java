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

package br.shura.venus.expression;

import br.shura.venus.exception.runtime.ScriptRuntimeException;
import br.shura.venus.executor.Context;
import br.shura.venus.value.ArrayValue;
import br.shura.venus.value.Value;
import br.shura.x.collection.view.ArrayView;
import br.shura.x.collection.view.View;
import br.shura.x.util.layer.XApi;

/**
 * ArrayDefine.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 22/05/16 - 03:07
 * @since GAMMA - 0x3
 */
public class ArrayDefine implements Expression {
  private final Expression[] expressions;

  public ArrayDefine(Expression... expressions) {
    XApi.requireNonNull(expressions, "expressions");

    this.expressions = expressions;
  }

  public View<Expression> getExpressions() {
    return new ArrayView<>(expressions);
  }

  @Override
  public Value resolve(Context context) throws ScriptRuntimeException {
    ArrayValue value = new ArrayValue(getExpressions().size());
    int i = 0;

    for (Expression expression : getExpressions()) {
      value.set(context, i++, expression.resolve(context));
    }

    return value;
  }

  @Override
  public String toString() {
    return "arrdef(" + getExpressions() + ')';
  }
}