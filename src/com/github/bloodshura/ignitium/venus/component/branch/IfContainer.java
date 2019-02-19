package com.github.bloodshura.ignitium.venus.component.branch;

import com.github.bloodshura.ignitium.venus.component.Container;
import com.github.bloodshura.ignitium.venus.expression.Expression;

public class IfContainer extends Container {
	private final Expression condition;

	public IfContainer(Expression condition) {
		this.condition = condition;
	}

	public Expression getCondition() {
		return condition;
	}

	@Override
	public String toString() {
		return "if(" + getCondition() + ')';
	}
}