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

import com.github.bloodshura.x.collection.view.XView;
import com.github.bloodshura.x.worker.enumeration.Enumerations;

public class KeywordDefinitions {
  public static final String ASYNC = "async";
  public static final String BREAK = "break";
  public static final char COLON = ':';
  public static final char COMMENTER = '#';
  public static final String CONTINUE = "continue";
  public static final String DAEMON = "daemon";
  public static final String DEFINE = "def";
  public static final String DO = "do";
  public static final String ELSE = "else";
  public static final String EXPORT = "export";
  public static final String FALSE = "false";
  public static final String FOR = "for";
  public static final char FUNCTION_REFERENCE = '@';
  public static final char GLOBAL_ACCESS = '$';
  public static final String IF = "if";
  public static final String IN = "in";
  public static final String INCLUDE = "include";
  public static final String NEW = "new";
  public static final String OBJECT = "object";
  public static final char OBJECT_ACCESS = '.';
  public static final String RETURN = "return";
  public static final String TRUE = "true";
  public static final String USING = "using";
  public static final String WHILE = "while";

  public static boolean isKeyword(String definition) {
    return values().contains(definition);
  }

  public static XView<String> values() {
    return Enumerations.values(KeywordDefinitions.class, String.class);
  }
}