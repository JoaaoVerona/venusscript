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

package com.github.bloodshura.venus.executor;

import com.github.bloodshura.venus.component.SimpleContainer;
import com.github.bloodshura.venus.exception.runtime.UndefinedVariableException;
import com.github.bloodshura.venus.library.VenusLibrary;
import com.github.bloodshura.venus.library.crypto.CryptoLibrary;
import com.github.bloodshura.venus.library.dialogs.DialogsLibrary;
import com.github.bloodshura.venus.library.dynamic.DynamicLibrary;
import com.github.bloodshura.venus.library.engine.EngineLibrary;
import com.github.bloodshura.venus.library.math.MathLibrary;
import com.github.bloodshura.venus.library.std.StdLibrary;
import com.github.bloodshura.venus.library.system.SystemLibrary;
import com.github.bloodshura.venus.library.time.TimeLibrary;
import com.github.bloodshura.x.collection.map.XMap;
import com.github.bloodshura.x.collection.map.impl.XLinkedMap;
import com.github.bloodshura.x.lang.annotation.Internal;
import com.github.bloodshura.x.logging.XLogger;
import com.github.bloodshura.x.math.impl.FastMath;
import com.github.bloodshura.x.math.impl.JavaMath;
import com.github.bloodshura.x.math.impl.SimpleMath;

import java.util.function.Supplier;

/**
 * ApplicationContext.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 04:14
 * @since GAMMA - 0x3
 */
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