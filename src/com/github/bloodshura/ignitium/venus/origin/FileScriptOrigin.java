package com.github.bloodshura.ignitium.venus.origin;

import com.github.bloodshura.ignitium.io.File;
import com.github.bloodshura.ignitium.io.FileException;

import java.io.IOException;

public class FileScriptOrigin implements ScriptOrigin {
	private final File file;

	public FileScriptOrigin(File file) {
		this.file = file;
	}

	@Override
	public ScriptOrigin findRelative(String includePath) {
		try {
			File file = new File(getFile().getParent(), includePath);

			if (file.exists()) {
				return new FileScriptOrigin(file);
			}
		}
		catch (FileException ignored) {
		}

		return ScriptOrigin.super.findRelative(includePath);
	}

	public File getFile() {
		return file;
	}

	@Override
	public String getScriptName() {
		return getFile().getFullName();
	}

	@Override
	public String read() throws IOException {
		return getFile().readString();
	}

	@Override
	public String toString() {
		return "fileorigin(" + getScriptName() + ')';
	}
}
