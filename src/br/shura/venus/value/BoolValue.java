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

import br.shura.venus.compiler.KeywordDefinitions;

/**
 * BoolValue.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 08/05/16 - 18:36
 * @since GAMMA - 0x3
 */
public class BoolValue implements Value {
  private final boolean value;

  public BoolValue(boolean value) {
    this.value = value;
  }

  @Override
  public BoolValue and(Value value) {
    if (value instanceof BoolValue) {
      BoolValue bool = (BoolValue) value;

      return new BoolValue(value() && bool.value());
    }

    return null;
  }

  @Override
  public NumericValue divide(Value value) {
    return null;
  }

  @Override
  public BoolValue equals(Value value) {
    return new BoolValue(value instanceof BoolValue && ((BoolValue) value).value() == value());
  }

  @Override
  public NumericValue minus(Value value) {
    return null;
  }

  @Override
  public NumericValue multiply(Value value) {
    return null;
  }

  @Override
  public BoolValue or(Value value) {
    if (value instanceof BoolValue) {
      BoolValue bool = (BoolValue) value;

      return new BoolValue(value() || bool.value());
    }

    return null;
  }

  @Override
  public Value plus(Value value) {
    return null;
  }

  @Override
  public String toString() {
    return value() ? KeywordDefinitions.TRUE : KeywordDefinitions.FALSE;
  }

  public boolean value() {
    return value;
  }
}