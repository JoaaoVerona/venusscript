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

package br.shura.venus.library;

import br.shura.venus.function.Function;
import br.shura.venus.value.ValueType;
import br.shura.x.collection.list.impl.ArrayList;
import br.shura.x.collection.view.View;
import br.shura.x.util.layer.XApi;

/**
 * LibraryList.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 07/05/16 - 17:42
 * @since GAMMA - 0x3
 */
public class LibraryList extends ArrayList<VenusLibrary> {
  public Function findFunction(String name, View<ValueType> argumentTypes) {
    XApi.requireNonNull(name, "name");

    Function found = null;
    Function foundVarArgs = null;

    for (VenusLibrary library : this) {
      for (Function function : library) {
        if (function.accepts(name, argumentTypes)) {
          if (function.isVarArgs()) {
            if (foundVarArgs == null) {
              foundVarArgs = function;
            }
          }
          else {
            found = function;
          }
        }
      }
    }

    return found != null ? found : foundVarArgs;
  }
}