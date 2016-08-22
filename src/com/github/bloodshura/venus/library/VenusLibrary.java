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

package com.github.bloodshura.venus.library;

import com.github.bloodshura.venus.function.Function;
import com.github.bloodshura.x.collection.list.impl.ArrayList;

/**
 * VenusLibrary.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 07/05/16 - 17:40
 * @since GAMMA - 0x3
 */
public class VenusLibrary extends ArrayList<Function> {
  public boolean add(Class<? extends Function> object) {
    try {
      return add(object.newInstance());
    }
    catch (IllegalAccessException | InstantiationException exception) {
      throw new IllegalArgumentException("Could not instantiate method class \"" + object.getName() + "\"");
    }
  }

  @SafeVarargs
  public final boolean addAll(Class<? extends Function>... objects) {
    boolean allAdded = true;

    for (Class<? extends Function> object : objects) {
      if (!add(object)) {
        allAdded = false;
      }
    }

    return allAdded;
  }
}