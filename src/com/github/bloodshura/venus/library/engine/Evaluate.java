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

package com.github.bloodshura.venus.library.engine;

import com.github.bloodshura.venus.compiler.VenusLexer;
import com.github.bloodshura.venus.compiler.VenusParser;
import com.github.bloodshura.venus.component.SimpleContainer;
import com.github.bloodshura.venus.exception.compile.ScriptCompileException;
import com.github.bloodshura.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.venus.executor.ApplicationContext;
import com.github.bloodshura.venus.executor.Context;
import com.github.bloodshura.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.venus.function.Method;
import com.github.bloodshura.venus.function.annotation.MethodName;
import com.github.bloodshura.venus.function.annotation.MethodVarArgs;
import com.github.bloodshura.venus.origin.ScriptMode;
import com.github.bloodshura.venus.origin.SimpleScriptOrigin;
import com.github.bloodshura.venus.value.Value;
import com.github.bloodshura.x.charset.build.TextBuilder;
import com.github.bloodshura.x.util.Pool;

import java.io.IOException;

/**
 * Evaluate.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 21/05/16 - 00:45
 * @since GAMMA - 0x3
 */
@MethodName("evaluate")
@MethodVarArgs
public class Evaluate extends Method {
  @Override
  public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
    VenusParser parser = context.getScript().getParser();
    TextBuilder builder = Pool.newBuilder();

    builder.appendln(descriptor.getValues());

    String source = builder.toStringAndClear();
    ApplicationContext appContext = context.getApplicationContext();
    SimpleScriptOrigin origin = new SimpleScriptOrigin("Interpreted-Script", source);
    SimpleContainer container = new SimpleContainer();

    container.setParent(context.getOwner());

    try {
      parser.parse(new VenusLexer(origin), container);

      return appContext.currentExecutor().run(container, ScriptMode.EVALUATION);
    }
    catch (IOException | ScriptCompileException exception) {
      throw new ScriptRuntimeException(context, "Failed to compile script", exception);
    }
  }
}