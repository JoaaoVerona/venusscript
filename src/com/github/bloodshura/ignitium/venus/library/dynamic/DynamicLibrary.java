package com.github.bloodshura.ignitium.venus.library.dynamic;

import com.github.bloodshura.ignitium.venus.library.VenusLibrary;

public class DynamicLibrary extends VenusLibrary {
	public DynamicLibrary() {
		addAll(DynamicInclude.class, DynamicUsing.class);
	}
}