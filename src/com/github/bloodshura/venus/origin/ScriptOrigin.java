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

package com.github.bloodshura.venus.origin;

import com.github.bloodshura.venus.compiler.VenusLexer;
import com.github.bloodshura.venus.compiler.VenusParser;
import com.github.bloodshura.venus.component.Script;
import com.github.bloodshura.venus.exception.compile.ScriptCompileException;
import com.github.bloodshura.venus.executor.ApplicationContext;
import com.github.bloodshura.x.loader.resource.PathResource;

import java.io.IOException;

/**
 * ScriptOrigin.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 07/05/16 - 18:35
 * @since GAMMA - 0x3
 */
public interface ScriptOrigin {
  default Script compile(ApplicationContext applicationContext) throws ScriptCompileException {
    VenusLexer lexer;

    try {
      lexer = new VenusLexer(this);
    }
    catch (IOException exception) {
      throw new ScriptCompileException("Could not read script \"" + getScriptName() + "\": " + exception.getClass().getSimpleName() +
        ": " + exception.getMessage());
    }

    Script script = new Script(applicationContext, this);
    VenusParser parser = script.getParser();

    parser.parse(lexer, script);

    return script;
  }

  default ScriptOrigin findRelative(String includePath) {
    PathResource resource = new PathResource(includePath);

    return resource.exists() ? new StreamScriptOrigin(includePath, resource) : null;
  }

  String getScriptName();

  String read() throws IOException;
}