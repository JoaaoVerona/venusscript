package com.github.bloodshura.ignitium.venus.origin;

import com.github.bloodshura.ignitium.resource.PathResource;
import com.github.bloodshura.ignitium.venus.compiler.VenusLexer;
import com.github.bloodshura.ignitium.venus.compiler.VenusParser;
import com.github.bloodshura.ignitium.venus.component.Script;
import com.github.bloodshura.ignitium.venus.exception.compile.ScriptCompileException;
import com.github.bloodshura.ignitium.venus.executor.ApplicationContext;

import java.io.IOException;

public interface ScriptOrigin {
	default Script compile(ApplicationContext applicationContext) throws ScriptCompileException {
		VenusLexer lexer;

		try {
			lexer = new VenusLexer(this);
		} catch (IOException exception) {
			throw new ScriptCompileException("Could not read script \"" + getScriptName() + "\": " + exception.getClass().getSimpleName() + ": " + exception.getMessage());
		}

		Script script = new Script(applicationContext, this);
		VenusParser parser = script.getParser();

		parser.parse(lexer, script);

		return script;
	}

	default ScriptOrigin findRelative(String includePath) {
		PathResource resource = new PathResource(includePath);

		return resource.exists() ? new StreamScriptOrigin(includePath, resource) : null;
	}

	String getScriptName();

	String read() throws IOException;
}
