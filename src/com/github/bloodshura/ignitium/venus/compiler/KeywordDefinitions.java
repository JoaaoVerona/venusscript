package com.github.bloodshura.ignitium.venus.compiler;

import com.github.bloodshura.ignitium.collection.view.XView;
import com.github.bloodshura.ignitium.enumeration.Enumerations;

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