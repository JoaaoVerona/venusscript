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

package br.shura.venus.executor;

import br.shura.venus.component.Container;
import br.shura.venus.library.VenusLibrary;
import br.shura.venus.library.crypto.CryptoLibrary;
import br.shura.venus.library.dialogs.DialogsLibrary;
import br.shura.venus.library.dynamic.DynamicLibrary;
import br.shura.venus.library.engine.EngineLibrary;
import br.shura.venus.library.math.MathLibrary;
import br.shura.venus.library.std.StdLibrary;
import br.shura.venus.library.system.SystemLibrary;
import br.shura.x.collection.map.Map;
import br.shura.x.collection.map.impl.LinkedMap;
import br.shura.x.math.impl.FastMath;
import br.shura.x.math.impl.JavaMath;
import br.shura.x.math.impl.SimpleMath;

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
  private final Map<String, Supplier<VenusLibrary>> librarySuppliers;

  public ApplicationContext() {
    super(new Container() {
      @Override
      public String toString() {
        return "APPLICATION";
      }
    }, null);
    this.librarySuppliers = new LinkedMap<>();

    getLibrarySuppliers().add("crypto", CryptoLibrary::new);
    getLibrarySuppliers().add("dialogs", DialogsLibrary::new);
    getLibrarySuppliers().add("dynamic", DynamicLibrary::new);
    getLibrarySuppliers().add("engine", EngineLibrary::new);
    getLibrarySuppliers().add("math", () -> new MathLibrary(new SimpleMath()));
    getLibrarySuppliers().add("math_fast", () -> new MathLibrary(new FastMath()));
    getLibrarySuppliers().add("math_java", () -> new MathLibrary(new JavaMath()));
    getLibrarySuppliers().add("std", StdLibrary::new);
    getLibrarySuppliers().add("system", SystemLibrary::new);
  }

  public Map<String, Supplier<VenusLibrary>> getLibrarySuppliers() {
    return librarySuppliers;
  }
}