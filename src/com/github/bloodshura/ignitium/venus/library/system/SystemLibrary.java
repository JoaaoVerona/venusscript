package com.github.bloodshura.ignitium.venus.library.system;

import com.github.bloodshura.ignitium.venus.library.VenusLibrary;

public class SystemLibrary extends VenusLibrary {
	public SystemLibrary() {
		addAll(GetEnvVar.class, GetProperty.class);
	}
}