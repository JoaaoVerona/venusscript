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

package com.github.bloodshura.venus.value;

import com.github.bloodshura.venus.compiler.KeywordDefinitions;
import com.github.bloodshura.venus.type.PrimitiveType;

/**
 * BoolValue.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 08/05/16 - 18:36
 * @since GAMMA - 0x3
 */
public class BoolValue extends Value {
  private final boolean value;

  public BoolValue(boolean value) {
    super(PrimitiveType.BOOLEAN);
    this.value = value;
  }

  @Override
  public Value and(Value value) {
    if (value instanceof BoolValue) {
      BoolValue bool = (BoolValue) value;

      return new BoolValue(value() && bool.value());
    }

    return super.and(value);
  }

  @Override
  public BoolValue clone() {
    return new BoolValue(value());
  }

  @Override
  public Value not() {
    return new BoolValue(!value());
  }

  @Override
  public Value or(Value value) {
    if (value instanceof BoolValue) {
      BoolValue bool = (BoolValue) value;

      return new BoolValue(value() || bool.value());
    }

    return super.or(value);
  }

  @Override
  public String toString() {
    return value() ? KeywordDefinitions.TRUE : KeywordDefinitions.FALSE;
  }

  @Override
  public Boolean value() {
    return value;
  }
}