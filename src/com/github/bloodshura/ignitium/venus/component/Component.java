package com.github.bloodshura.ignitium.venus.component;

import com.github.bloodshura.ignitium.lang.annotation.Internal;
import com.github.bloodshura.ignitium.util.XApi;
import com.github.bloodshura.ignitium.venus.executor.ApplicationContext;

public abstract class Component {
	private Container parent;
	private int sourceLine;

	public ApplicationContext getApplicationContext() {
		XApi.requireState(getParent() != null, "Could not retrieve application context; no parent available");

		return getParent().getApplicationContext();
	}

	public final Container getParent() {
		return parent;
	}

	public Script getScript() {
		XApi.requireState(getParent() != null, "Could not retrieve script; no parent available");

		return getParent().getScript();
	}

	public final int getSourceLine() {
		return sourceLine;
	}

	public final boolean hasParent() {
		return getParent() != null;
	}

	@Internal
	public void setParent(Container parent) {
		this.parent = parent;
	}

	@Internal
	public void setSourceLine(int sourceLine) {
		this.sourceLine = sourceLine;
	}

	@Override
	public abstract String toString();
}