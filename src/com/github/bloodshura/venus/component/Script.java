//////////////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2013-2017, João Vitor Verona Biazibetti - All Rights Reserved           /
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
import com.github.bloodshura.x.activity.logging.XLogger;
import com.github.bloodshura.x.collection.list.XList;
import com.github.bloodshura.x.collection.list.impl.XArrayList;
import com.github.bloodshura.x.collection.view.XView;

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