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

import br.shura.x.collection.view.View;
import br.shura.x.worker.enumeration.Enumerations;

/**
 * KeywordDefinitions.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 07/05/16 - 18:14
 * @since GAMMA - 0x3
 */
public class KeywordDefinitions {
  public static final char COMMENTER = '#';
  public static final String ASYNC = "async";
  public static final String BREAK = "break";
  public static final String CONTINUE = "continue";
  public static final String DEFINE = "def";
  public static final String DO = "do";
  public static final String ELSE = "else";
  public static final String EXPORT = "export";
  public static final String FALSE = "false";
  public static final String FOR = "for";
  public static final String IF = "if";
  public static final String IN = "in";
  public static final String INCLUDE = "include";
  public static final String TRUE = "true";
  public static final String USING = "using";
  public static final String WHILE = "while";

  public static boolean isKeyword(String definition) {
    return values().contains(definition);
  }

  public static View<String> values() {
    return Enumerations.values(KeywordDefinitions.class, String.class);
  }
}