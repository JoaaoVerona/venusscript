package com.github.bloodshura.ignitium.venus.component.branch;

import com.github.bloodshura.ignitium.venus.component.Container;
import com.github.bloodshura.ignitium.venus.expression.Expression;

public class WhileContainer extends Container implements Breakable {
	private final Expression condition;

	public WhileContainer(Expression condition) {
		this.condition = condition;
	}

	public Expression getCondition() {
		return condition;
	}

	@Override
	public String toString() {
		return "while(" + getCondition() + ')';
	}
}