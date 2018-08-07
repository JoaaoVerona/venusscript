/*
 * Copyright (c) 2013-2018, João Vitor Verona Biazibetti - All Rights Reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * https://www.github.com/BloodShura
 */

package com.github.bloodshura.ignitium.venus.test;

import com.github.bloodshura.ignitium.activity.logging.XLogger;
import com.github.bloodshura.ignitium.util.comparator.SimpleEqualizer;
import com.github.bloodshura.ignitium.venus.compiler.Token;
import com.github.bloodshura.ignitium.venus.compiler.Token.Type;
import com.github.bloodshura.ignitium.venus.compiler.VenusLexer;
import com.github.bloodshura.ignitium.venus.exception.compile.UnexpectedInputException;
import com.github.bloodshura.ignitium.venus.origin.ScriptOrigin;
import com.github.bloodshura.ignitium.venus.origin.SimpleScriptOrigin;
import com.github.bloodshura.ignitium.worker.StringWorker;
import org.junit.Test;

import java.io.IOException;

public class Lexer {
	@Test
	public void simpleAssertion() throws IOException, UnexpectedInputException {
		String[] script = { "i = 0", "j = (i + 1)", "while (true) {", "  print(i + j)", "}" };
		ScriptOrigin origin = new SimpleScriptOrigin("test.xs", StringWorker.join('\n', script));
		VenusLexer lexer = new VenusLexer(origin);

		assertToken(lexer, Type.NAME_DEFINITION, "i");
		assertToken(lexer, Type.OPERATOR, "=");
		assertToken(lexer, Type.DECIMAL_LITERAL, "0");
		assertToken(lexer, Type.NEW_LINE, null);
		assertToken(lexer, Type.NAME_DEFINITION, "j");
		assertToken(lexer, Type.OPERATOR, "=");
		assertToken(lexer, Type.OPEN_PARENTHESE, "(");
		assertToken(lexer, Type.NAME_DEFINITION, "i");
		assertToken(lexer, Type.OPERATOR, "+");
		assertToken(lexer, Type.DECIMAL_LITERAL, "1");
		assertToken(lexer, Type.CLOSE_PARENTHESE, ")");
		assertToken(lexer, Type.NEW_LINE, null);
		assertToken(lexer, Type.NAME_DEFINITION, "while");
		assertToken(lexer, Type.OPEN_PARENTHESE, "(");
		assertToken(lexer, Type.NAME_DEFINITION, "true");
		assertToken(lexer, Type.CLOSE_PARENTHESE, ")");
		assertToken(lexer, Type.OPEN_BRACE, "{");
		assertToken(lexer, Type.NEW_LINE, null);
		assertToken(lexer, Type.NAME_DEFINITION, "print");
		assertToken(lexer, Type.OPEN_PARENTHESE, "(");
		assertToken(lexer, Type.NAME_DEFINITION, "i");
		assertToken(lexer, Type.OPERATOR, "+");
		assertToken(lexer, Type.NAME_DEFINITION, "j");
		assertToken(lexer, Type.CLOSE_PARENTHESE, ")");
		assertToken(lexer, Type.NEW_LINE, null);
		assertToken(lexer, Type.CLOSE_BRACE, "}");
		XLogger.println("Assertion passed.");
	}

	@Test
	public void simplePrint() throws IOException, UnexpectedInputException {
		String[] script = { "i = 0", "j = (i + 1024)", "while (true) {", "  print(i + j)", "  i = i + 1", "  j = j - 1", "  i++", "  ++j", "  setvar(502.55)", "}" };
		ScriptOrigin origin = new SimpleScriptOrigin("test.xs", StringWorker.join('\n', script));
		VenusLexer lexer = new VenusLexer(origin);
		Token token;

		while ((token = lexer.nextToken()) != null) {
			XLogger.println("[" + token.getType() + "] " + token.getValue());
		}
	}

	@Test
	public void testCharLiterals() throws IOException, UnexpectedInputException {
		String[] script = { "'\\n'", "'\\\\n'", "'\"'" };
		ScriptOrigin origin = new SimpleScriptOrigin("test.xs", StringWorker.join('\n', script));
		VenusLexer lexer = new VenusLexer(origin);

		assertToken(lexer, Type.CHAR_LITERAL, "\\n");
		assertToken(lexer, Type.NEW_LINE, null);
		assertToken(lexer, Type.CHAR_LITERAL, "\\\\n");
		assertToken(lexer, Type.NEW_LINE, null);
		assertToken(lexer, Type.CHAR_LITERAL, "\"");
	}

	private static void assertToken(VenusLexer lexer, Type type, Object content) throws AssertionError, UnexpectedInputException {
		Token token = lexer.nextToken();

		if (token == null) {
			throw new AssertionError("Expected token, but none found");
		}

		if (token.getType() != type || !SimpleEqualizer.compare(token.getValue(), content)) {
			throw new AssertionError("Expected [" + type + ", " + content + "], received [" + token.getType() + ", " + token.getValue() + ']');
		}
	}
}