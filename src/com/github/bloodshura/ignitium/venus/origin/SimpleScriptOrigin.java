package com.github.bloodshura.ignitium.venus.origin;

import java.io.IOException;

public class SimpleScriptOrigin implements ScriptOrigin {
	private final String content;
	private final String name;

	public SimpleScriptOrigin(String name, String content) {
		this.content = content;
		this.name = name;
	}

	@Override
	public String getScriptName() {
		return name;
	}

	@Override
	public String read() throws IOException {
		return content;
	}

	@Override
	public String toString() {
		return "simpleorigin(" + getScriptName() + ')';
	}
}