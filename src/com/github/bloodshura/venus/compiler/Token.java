//////////////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2016, João Vitor Verona Biazibetti - All Rights Reserved                /
//                                                                                       /
// This program is free software: you can redistribute it and/or modify                  /
// it under the terms of the GNU General Public License as published by                  /
// the Free Software Foundation, either version 3 of the License, or                     /
// (at your option) any later version.                                                   /
//                                                                                       /
// This program is distributed in the hope that it will be useful,                       /
// but WITHOUT ANY WARRANTY; without even the implied warranty of                        /
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the                          /
// GNU General Public License for more details.                                          /
//                                                                                       /
// You should have received a copy of the GNU General Public License                     /
// along with this program. If not, see <http://www.gnu.org/licenses/>.                  /
//                                                                                       /
// https://www.github.com/BloodShura                                                     /
//////////////////////////////////////////////////////////////////////////////////////////

package com.github.bloodshura.venus.compiler;

import com.github.bloodshura.x.object.Base;

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
    BINARY_LITERAL,
    DECIMAL_LITERAL,
    HEXADECIMAL_LITERAL,
    STRING_LITERAL,
    CHAR_LITERAL,
    OPERATOR,
    COMMA,
    FUNC_REF,
    NEW_LINE,
    COLON,
    GLOBAL_ACCESS,
    OBJECT_ACCESS
  }
}