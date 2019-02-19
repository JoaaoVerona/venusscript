package com.github.bloodshura.ignitium.venus.component;

import com.github.bloodshura.ignitium.util.XApi;

public class SimpleContainer extends Container {
	private final String name;

	public SimpleContainer() {
		this("container");
	}

	public SimpleContainer(String name) {
		XApi.requireNonNull(name, "name");

		this.name = name;
	}

	@Override
	public String toString() {
		return name + "()";
	}
}