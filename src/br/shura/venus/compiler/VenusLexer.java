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

import br.shura.venus.compiler.Token.Type;
import br.shura.venus.exception.UnexpectedInputException;
import br.shura.venus.origin.ScriptOrigin;
import br.shura.x.charset.build.TextBuilder;
import br.shura.x.collection.store.impl.Queue;
import br.shura.x.util.Pool;

import java.io.IOException;

import static br.shura.venus.compiler.VenusLexer.State.*;

/**
 * VenusLexer.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 06/05/16 - 04:23
 * @since GAMMA - 0x3
 */
public class VenusLexer {
  private final TextBuilder buildingToken;
  private boolean insideComment;
  private int line;
  private final ScriptOrigin origin;
  private int position;
  private final Queue<Token> reread;
  private State state;
  private final String string;

  public VenusLexer(ScriptOrigin origin) throws IOException {
    this.buildingToken = Pool.newBuilder();
    this.origin = origin;
    this.reread = new Queue<>();
    this.string = origin.read();
  }

  public int currentLine() {
    return line;
  }

  public Token nextToken() throws UnexpectedInputException {
    if (!reread.isEmpty()) {
      return reread.poll();
    }

    while (canRead()) {
      char ch = read();
      char lastChar = !buildingToken.isEmpty() ? buildingToken.charAt(buildingToken.length() - 1) : 0;
      boolean isDigit = Character.isDigit(ch);
      boolean isLetter = Character.isLetter(ch) || ch == '_';

      if (ch == '\n') {
        this.insideComment = false;

        if (state == IN_CHAR_LITERAL) {
          bye("End of line found, but unclosed character literal");
        }

        if (state == IN_STRING_LITERAL) {
          bye("End of line found, but unclosed string literal");
        }

        if (state == IN_NAME_DEFINITION) {
          back();

          this.state = null;

          return new Token(Type.NAME_DEFINITION, buildingToken.toStringAndClear());
        }

        if (state == IN_NUMBER_LITERAL) {
          back();

          this.state = null;

          return new Token(Type.NUMBER_LITERAL, buildingToken.toStringAndClear());
        }

        this.line++;

        return new Token(Type.NEW_LINE, null);
      }

      if (insideComment) {
        continue;
      }

      if (ch == '"' && lastChar != '\\' && state != IN_CHAR_LITERAL) {
        if (state == IN_STRING_LITERAL) {
          this.state = null;

          return new Token(Type.STRING_LITERAL, buildingToken.toStringAndClear());
        }

        if (state == IN_NUMBER_LITERAL) {
          bye("Double quotes found while parsing a number literal");
        }

        if (state == IN_NAME_DEFINITION) {
          bye("Double quotes found while parsing a name definition");
        }

        this.state = IN_STRING_LITERAL;

        continue;
      }

      if (ch == '\'' && lastChar != '\\' && state != IN_STRING_LITERAL) {
        if (state == IN_CHAR_LITERAL) {
          this.state = null;

          return new Token(Type.CHAR_LITERAL, buildingToken.toStringAndClear());
        }

        if (state == IN_NUMBER_LITERAL) {
          bye("Single quote found while parsing a number literal");
        }

        if (state == IN_NAME_DEFINITION) {
          bye("Single quote found while parsing a name definition");
        }

        this.state = IN_CHAR_LITERAL;

        continue;
      }

      if (state == IN_NUMBER_LITERAL) {
        if (isLetter) {
          bye("Letter found while parsing a number literal");
        }

        if (!isDigit && ch != '.') {
          back();

          this.state = null;

          return new Token(Type.NUMBER_LITERAL, buildingToken.toStringAndClear());
        }
      }
      else if (state != IN_CHAR_LITERAL && state != IN_STRING_LITERAL) {
        if (ch == ' ' || ch == '\t') {
          if (state == IN_NAME_DEFINITION) {
            this.state = null;

            return new Token(Type.NAME_DEFINITION, buildingToken.toStringAndClear());
          }

          continue;
        }

        if (state == IN_NAME_DEFINITION && !isLetter && !isDigit) {
          back();

          this.state = null;

          return new Token(Type.NAME_DEFINITION, buildingToken.toStringAndClear());
        }

        if (!isLetter && !isDigit) {
          if (ch == ',') {
            return new Token(Type.COMMA, ch);
          }

          if (ch == '@') {
            return new Token(Type.AT_SIGN, ch);
          }

          if (ch == '{') {
            return new Token(Type.OPEN_BRACE, ch);
          }

          if (ch == '[') {
            return new Token(Type.OPEN_BRACKET, ch);
          }

          if (ch == '(') {
            return new Token(Type.OPEN_PARENTHESE, ch);
          }

          if (ch == '}') {
            return new Token(Type.CLOSE_BRACE, ch);
          }

          if (ch == ']') {
            return new Token(Type.CLOSE_BRACKET, ch);
          }

          if (ch == ')') {
            return new Token(Type.CLOSE_PARENTHESE, ch);
          }

          if (ch == KeywordDefinitions.COMMENTER) {
            this.insideComment = true;

            continue;
          }

          return new Token(Type.OPERATOR, ch);
        }

        if (isDigit && state != IN_NAME_DEFINITION) {
          this.state = IN_NUMBER_LITERAL;
        }
        else if (isLetter && state != IN_NAME_DEFINITION) {
          this.state = IN_NAME_DEFINITION;
        }
      }

      buildingToken.append(ch);
    }

    return null;
  }

  public void reRead(Token token) {
    reread.push(token);
  }

  protected void back() {
    this.position--;
  }

  protected void bye(String message) throws UnexpectedInputException {
    throw new UnexpectedInputException(origin.getScriptName(), currentLine(), message);
  }

  protected boolean canRead() {
    return position <= string.length();
  }

  protected char read() {
    if (position == string.length()) {
      this.line++;
      this.position++;

      return '\n';
    }

    return string.charAt(position++);
  }

  public enum State {
    IN_STRING_LITERAL,
    IN_NUMBER_LITERAL,
    IN_CHAR_LITERAL,
    IN_NAME_DEFINITION
  }
}