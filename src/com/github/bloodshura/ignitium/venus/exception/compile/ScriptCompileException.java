package com.github.bloodshura.ignitium.venus.exception.compile;

import com.github.bloodshura.ignitium.exception.CheckedException;

public class ScriptCompileException extends CheckedException {
	public ScriptCompileException(CharSequence message) {
		super(message);
	}

	public ScriptCompileException(String scriptName, int currentLine, CharSequence message) {
		this(message + " at line " + (currentLine + 1) + " of script \"" + scriptName + "\"");
	}
}