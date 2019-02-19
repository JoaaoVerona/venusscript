package com.github.bloodshura.ignitium.venus.component.branch;

import com.github.bloodshura.ignitium.venus.component.Container;
import com.github.bloodshura.ignitium.venus.expression.Expression;

public class DoWhileContainer extends Container implements Breakable {
	private Expression condition;

	public DoWhileContainer(Expression condition) {
		this.condition = condition;
	}

	public Expression getCondition() {
		return condition;
	}

	public boolean hasCondition() {
		return getCondition() != null;
	}

	public void setCondition(Expression condition) {
		this.condition = condition;
	}

	@Override
	public String toString() {
		return "dowhile(" + getCondition() + ')';
	}
}