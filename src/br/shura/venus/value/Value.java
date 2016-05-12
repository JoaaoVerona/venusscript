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

/**
 * Value.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 08/05/16 - 18:33
 * @since GAMMA - 0x3
 */
public interface Value {
  default Value and(Value value) {
    return null;
  }

  default Integer compareTo(Value value) {
    return null;
  }

  default NumericValue divide(Value value) {
    return null;
  }

  BoolValue equals(Value value);

  default ValueType getType() {
    return ValueType.forValue(this);
  }

  default NumericValue minus(Value value) {
    return null;
  }

  default NumericValue multiply(Value value) {
    return null;
  }

  default Value negate() {
    return null;
  }

  default Value not() {
    return null;
  }

  default Value or(Value value) {
    return null;
  }

  default Value plus(Value value) {
    return null;
  }
}