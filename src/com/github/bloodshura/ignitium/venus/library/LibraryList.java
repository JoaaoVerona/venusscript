package com.github.bloodshura.ignitium.venus.library;

import com.github.bloodshura.ignitium.collection.list.impl.XArrayList;
import com.github.bloodshura.ignitium.collection.view.XView;
import com.github.bloodshura.ignitium.util.XApi;
import com.github.bloodshura.ignitium.venus.function.Function;
import com.github.bloodshura.ignitium.venus.type.Type;

public class LibraryList extends XArrayList<VenusLibrary> {
	public Function findFunction(String name, XView<Type> argumentTypes) {
		XApi.requireNonNull(name, "name");

		Function found = null;
		Function foundVarArgs = null;

		for (VenusLibrary library : this) {
			for (Function function : library) {
				if (function.accepts(name, argumentTypes)) {
					if (function.isVarArgs()) {
						if (foundVarArgs == null) {
							foundVarArgs = function;
						}
					} else {
						found = function;
					}
				}
			}
		}

		return found != null ? found : foundVarArgs;
	}
}
