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

package br.shura.venus.library.engine;

import br.shura.venus.compiler.VenusLexer;
import br.shura.venus.compiler.VenusParser;
import br.shura.venus.component.SimpleContainer;
import br.shura.venus.exception.compile.ScriptCompileException;
import br.shura.venus.exception.runtime.ScriptRuntimeException;
import br.shura.venus.executor.Context;
import br.shura.venus.function.FunctionCallDescriptor;
import br.shura.venus.function.VoidMethod;
import br.shura.venus.function.annotation.MethodName;
import br.shura.venus.function.annotation.MethodVarArgs;
import br.shura.venus.origin.SimpleScriptOrigin;
import br.shura.x.charset.build.TextBuilder;
import br.shura.x.util.Pool;

import java.io.IOException;

/**
 * Interpret.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 20/05/16 - 01:08
 * @since GAMMA - 0x3
 */
@MethodName("interpret")
@MethodVarArgs
public class Interpret extends VoidMethod {
  @Override
  public void callVoid(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
    VenusParser parser = context.getOwner().getScript().getParser();
    TextBuilder builder = Pool.newBuilder();

    builder.appendln(descriptor.getValues());

    String source = builder.toStringAndClear();
    SimpleScriptOrigin origin = new SimpleScriptOrigin("Interpreted-Script", source);
    SimpleContainer container = new SimpleContainer();

    container.setParent(context.getOwner());

    try {
      parser.parse(new VenusLexer(origin), container);
      context.currentExecutor().run(container);
    }
    catch (IOException | ScriptCompileException exception) {
      throw new ScriptRuntimeException(context, "Failed to compile script", exception);
    }
  }
}