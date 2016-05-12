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

package br.shura.venus.compiler;

import br.shura.x.object.Base;

/**
 * Token.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 04:25
 * @since GAMMA - 0x3
 */
public class Token extends Base {
  private final Type type;
  private final String value;

  public Token(Type type, char value) {
    this(type, Character.toString(value));
  }

  public Token(Type type, String value) {
    this.type = type;
    this.value = value;
  }

  public Type getType() {
    return type;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    if (getValue() != null) {
      return getType().toString() + '[' + getValue() + ']';
    }

    return getType().toString();
  }

  @Override
  protected Object[] stringValues() {
    return new Object[] { getType(), getValue() };
  }

  public enum Type {
    NAME_DEFINITION,
    OPEN_BRACE,
    CLOSE_BRACE,
    OPEN_BRACKET,
    CLOSE_BRACKET,
    OPEN_PARENTHESE,
    CLOSE_PARENTHESE,
    NUMBER_LITERAL,
    STRING_LITERAL,
    CHAR_LITERAL,
    OPERATOR,
    COMMA,
    COLON,
    AT_SIGN,
    NEW_LINE,
    COMMENTER
  }
}