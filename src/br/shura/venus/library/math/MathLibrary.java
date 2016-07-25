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

package br.shura.venus.library.math;

import br.shura.venus.library.VenusLibrary;
import br.shura.x.math.MathProvider;

import java.lang.reflect.Method;

/**
 * MathLibrary.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 13/05/16 - 03:45
 * @since GAMMA - 0x3
 */
public class MathLibrary extends VenusLibrary {
  public MathLibrary(MathProvider instance) {
    for (Method method : MathProvider.class.getDeclaredMethods()) {
      if (MathFunction.validate(method)) {
        add(new MathFunction(method, instance));
      }
    }

    addAll(RandDecimal.class, RandInt.class);
  }
}