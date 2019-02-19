package com.github.bloodshura.ignitium.venus.compiler;

import com.github.bloodshura.ignitium.object.Base;

import javax.annotation.Nonnull;

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

	@Nonnull
	@Override
	public String toString() {
		if (getValue() != null) {
			return getType().toString() + '[' + getValue() + ']';
		}

		return getType().toString();
	}

	@Nonnull
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