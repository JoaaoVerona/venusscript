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

package br.shura.venus.value;

import br.shura.x.util.layer.XApi;

/**
 * StringValue.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 08/05/16 - 20:47
 * @since GAMMA - 0x3
 */
public class StringValue implements Value {
  private final String value;

  public StringValue(String value) {
    XApi.requireNonNull(value, "value");

    this.value = value;
  }

  @Override
  public Integer compareTo(Value value) {
    if (value instanceof StringValue) {
      StringValue string = (StringValue) value;

      return value().compareToIgnoreCase(string.value());
    }

    return null;
  }

  public boolean isCharacter() {
    return value().length() == 1;
  }

  @Override
  public StringValue plus(Value value) {
    return new StringValue(value() + value);
  }

  public char toCharacter() {
    XApi.requireState(isCharacter(), "Cannot convert multi character StringValue to single character");

    return value().charAt(0);
  }

  @Override
  public String value() {
    return value;
  }

  @Override
  public String toString() {
    return value();
  }
}