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

package com.github.bloodshura.x.venus.executor;

import com.github.bloodshura.x.venus.component.SimpleContainer;
import com.github.bloodshura.x.venus.exception.runtime.UndefinedVariableException;
import com.github.bloodshura.x.venus.library.VenusLibrary;
import com.github.bloodshura.x.venus.library.crypto.CryptoLibrary;
import com.github.bloodshura.x.venus.library.dialogs.DialogsLibrary;
import com.github.bloodshura.x.venus.library.dynamic.DynamicLibrary;
import com.github.bloodshura.x.venus.library.engine.EngineLibrary;
import com.github.bloodshura.x.venus.library.math.MathLibrary;
import com.github.bloodshura.x.venus.library.std.StdLibrary;
import com.github.bloodshura.x.venus.library.system.SystemLibrary;
import com.github.bloodshura.x.venus.library.time.TimeLibrary;
import com.github.bloodshura.x.activity.logging.XLogger;
import com.github.bloodshura.x.collection.map.XMap;
import com.github.bloodshura.x.collection.map.impl.XLinkedMap;
import com.github.bloodshura.x.lang.annotation.Internal;
import com.github.bloodshura.x.math.impl.FastMath;
import com.github.bloodshura.x.math.impl.JavaMath;
import com.github.bloodshura.x.math.impl.SimpleMath;

import java.util.function.Supplier;

public class ApplicationContext extends Context {
  private int currentLine;
  private VenusExecutor executor;
  private final XMap<String, Supplier<VenusLibrary>> librarySuppliers;
  private final XMap<String, Object> userData;

  public ApplicationContext() {
    super(new SimpleContainer("APPLICATION"), null);
    this.librarySuppliers = new XLinkedMap<>();
    this.userData = new XLinkedMap<>();

    getLibrarySuppliers().add("crypto", CryptoLibrary::new);
    getLibrarySuppliers().add("dialogs", DialogsLibrary::new);
    getLibrarySuppliers().add("dynamic", DynamicLibrary::new);
    getLibrarySuppliers().add("engine", EngineLibrary::new);
    getLibrarySuppliers().add("math", () -> new MathLibrary(new SimpleMath()));
    getLibrarySuppliers().add("math_fast", () -> new MathLibrary(new FastMath()));
    getLibrarySuppliers().add("math_java", () -> new MathLibrary(new JavaMath()));
    getLibrarySuppliers().add("std", StdLibrary::new);
    getLibrarySuppliers().add("system", SystemLibrary::new);
    getLibrarySuppliers().add("time", TimeLibrary::new);
    setUserData("in", XLogger.getInstance());
    setUserData("out", (OutputReference) XLogger::print);
  }

  @Override
  public ApplicationContext clone() {
    ApplicationContext context = new ApplicationContext();

    context.getLibrarySuppliers().addAll(getLibrarySuppliers());
    context.userData.addAll(userData);
    context.setCurrentLine(currentLine());

    return context;
  }

  public int currentLine() {
    return currentLine;
  }

  public VenusExecutor currentExecutor() {
    return executor;
  }

  public XMap<String, Supplier<VenusLibrary>> getLibrarySuppliers() {
    return librarySuppliers;
  }

  public <E> E getUserData(String name, Class<E> type) throws UndefinedVariableException {
    Object value = userData.get(name);

    if (value != null && type.isAssignableFrom(value.getClass())) {
      return (E) value;
    }

    throw new UndefinedVariableException(this, name);
  }

  public void setUserData(String name, Object value) {
    userData.set(name, value);
  }

  @Internal
  void setExecutor(VenusExecutor executor) {
    this.executor = executor;
  }

  @Internal
  void setCurrentLine(int currentLine) {
    this.currentLine = currentLine;
  }
}