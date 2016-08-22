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

package com.github.bloodshura.venus.component;

import com.github.bloodshura.venus.compiler.VenusParser;
import com.github.bloodshura.venus.exception.compile.ScriptCompileException;
import com.github.bloodshura.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.venus.executor.ApplicationContext;
import com.github.bloodshura.venus.executor.Context;
import com.github.bloodshura.venus.function.Function;
import com.github.bloodshura.venus.library.LibraryList;
import com.github.bloodshura.venus.origin.ScriptOrigin;
import com.github.bloodshura.venus.type.Type;
import com.github.bloodshura.x.collection.list.XList;
import com.github.bloodshura.x.collection.list.impl.XArrayList;
import com.github.bloodshura.x.collection.view.XView;
import com.github.bloodshura.x.logging.XLogger;

/**
 * Script.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 01:36
 * @since GAMMA - 0x3
 */
public class Script extends Container {
  private final ApplicationContext appContext;
  private final XList<Script> includes;
  private final LibraryList libraryList;
  private final ScriptOrigin origin;
  private final VenusParser parser;

  public Script(ApplicationContext appContext, ScriptOrigin origin) {
    this.appContext = appContext;
    this.context = new Context(this, null);
    this.includes = new XArrayList<>();
    this.libraryList = new LibraryList();
    this.origin = origin;
    this.parser = new VenusParser(this);
  }

  @Override
  public Function findFunction(Context context, String name, XView<Type> argumentTypes) throws ScriptRuntimeException {
    try {
      return super.findFunction(context, name, argumentTypes);
    }
    catch (ScriptRuntimeException exception) {
      for (Script script : getIncludes()) {
        try {
          return script.findFunction(context, name, argumentTypes);
        }
        catch (ScriptRuntimeException exception2) {
        }
      }

      Function function = getLibraryList().findFunction(name, argumentTypes);

      if (function != null) {
        return function;
      }

      throw exception;
    }
  }

  @Override
  public ApplicationContext getApplicationContext() {
    return appContext;
  }

  public String getDisplayName() {
    return getOrigin().getScriptName();
  }

  public XList<Script> getIncludes() {
    return includes;
  }

  public LibraryList getLibraryList() {
    return libraryList;
  }

  public ScriptOrigin getOrigin() {
    return origin;
  }

  public VenusParser getParser() {
    return parser;
  }

  @Override
  public Script getScript() {
    return this;
  }

  public void include(String includePath, boolean maybe) throws ScriptCompileException {
    ScriptOrigin origin = getOrigin().findRelative(includePath);

    if (origin != null) {
      Script script = origin.compile(getApplicationContext());

      getIncludes().add(script);
    }
    else if (maybe) {
      XLogger.debugln("Not found include script \"" + includePath + "\", but it was marked as maybe.");
    }
    else {
      throw new ScriptCompileException("Could not find script \"" + includePath + "\"");
    }
  }

  @Deprecated
  @Override
  public void setParent(Container parent) {
    throw new IllegalStateException("Cannot define a script's parent");
  }

  @Override
  public String toString() {
    return "script(name=" + getDisplayName() + ", origin=" + getOrigin() + ", includes=" + getIncludes() + ')';
  }
}