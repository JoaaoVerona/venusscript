package com.github.bloodshura.ignitium.venus.library.math;

import com.github.bloodshura.ignitium.venus.library.VenusLibrary;

import java.lang.reflect.Method;

public class MathLibrary extends VenusLibrary {
	public MathLibrary() {
		for (Method method : Math.class.getDeclaredMethods()) {
			if (MathFunction.validate(method)) {
				add(new MathFunction(method));
			}
		}

		addAll(RandDecimal.class, RandInt.class);
	}
}