package com.github.bloodshura.ignitium.venus.component;

import com.github.bloodshura.ignitium.venus.executor.Context;

public class AsyncContainer extends Container {
	private final boolean daemon;

	public AsyncContainer(boolean daemon) {
		this.daemon = daemon;
	}

	public boolean isDaemon() {
		return daemon;
	}

	@Override
	public void setParent(Container parent) {
		super.setParent(parent);
		this.context = new Context(this, parent.getContext());
	}

	@Override
	public String toString() {
		return isDaemon() ? "async(daemon)" : "async()";
	}
}