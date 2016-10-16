//////////////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2016, João Vitor Verona Biazibetti - All Rights Reserved                /
//                                                                                       /
// This program is free software: you can redistribute it and/or modify                  /
// it under the terms of the GNU General Public License as published by                  /
// the Free Software Foundation, either version 3 of the License, or                     /
// (at your option) any later version.                                                   /
//                                                                                       /
// This program is distributed in the hope that it will be useful,                       /
// but WITHOUT ANY WARRANTY; without even the implied warranty of                        /
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the                          /
// GNU General Public License for more details.                                          /
//                                                                                       /
// You should have received a copy of the GNU General Public License                     /
// along with this program. If not, see <http://www.gnu.org/licenses/>.                  /
//                                                                                       /
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