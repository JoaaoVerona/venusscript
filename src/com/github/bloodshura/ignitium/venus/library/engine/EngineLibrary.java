package com.github.bloodshura.ignitium.venus.library.engine;

import com.github.bloodshura.ignitium.venus.library.VenusLibrary;

public class EngineLibrary extends VenusLibrary {
	public EngineLibrary() {
		addAll(Evaluate.class, HasFunction.class, Interpret.class, Run.class);
	}
}