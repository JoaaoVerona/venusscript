package com.github.bloodshura.ignitium.venus.component.branch;

import com.github.bloodshura.ignitium.venus.component.Container;
import com.github.bloodshura.ignitium.venus.expression.Expression;

public class ForEachContainer extends Container implements Breakable {
	private final Expression iterable;
	private final String varName;

	public ForEachContainer(String varName, Expression iterable) {
		this.iterable = iterable;
		this.varName = varName;
	}

	public Expression getIterable() {
		return iterable;
	}

	public String getVarName() {
		return varName;
	}

	@Override
	public String toString() {
		return "foreach(" + getVarName() + " in " + getIterable() + ')';
	}
}