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
  public BoolValue and(Value value) {
    return null;
  }

  @Override
  public NumericValue divide(Value value) {
    return null;
  }

  @Override
  public BoolValue equals(Value value) {
    return new BoolValue(value instanceof StringValue && ((StringValue) value).value().equals(value()));
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
    return null;
  }

  @Override
  public NumericValue plus(Value value) {
    return null;
  }

  public String value() {
    return value;
  }
}