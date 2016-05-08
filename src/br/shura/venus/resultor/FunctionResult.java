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

package br.shura.venus.resultor;

import br.shura.venus.Context;
import br.shura.venus.component.function.Function;
import br.shura.venus.exception.ScriptRuntimeException;
import br.shura.x.collection.view.ArrayView;
import br.shura.x.collection.view.View;

/**
 * FunctionResult.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 03:28
 * @since GAMMA - 0x3
 */
public class FunctionResult extends Resultor {
  private final Resultor[] arguments;
  private final String functionName;

  public FunctionResult(String functionName, Resultor... arguments) {
    this.arguments = arguments;
    this.functionName = functionName;
  }

  public View<Resultor> getArguments() {
    return new ArrayView<>(arguments);
  }

  public String getFunctionName() {
    return functionName;
  }

  @Override
  public Object resolve(Context context) throws ScriptRuntimeException {
    Function function = context.getOwner().findFunction(context, getFunctionName(), getArguments().size());

    function.validateArguments(context, arguments);

    return function.call(context, arguments);
  }

  @Override
  public String toString() {
    return "functionresult(" + getFunctionName() + " <-- [" + getArguments().toString(", ") + "])";
  }
}