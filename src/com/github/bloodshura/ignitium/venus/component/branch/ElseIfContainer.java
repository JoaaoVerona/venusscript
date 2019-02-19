package com.github.bloodshura.ignitium.venus.component.branch;

import com.github.bloodshura.ignitium.venus.expression.Expression;

public class ElseIfContainer extends IfContainer {
	public ElseIfContainer(Expression condition) {
		super(condition);
	}

	@Override
	public String toString() {
		return "elseif(" + getCondition() + ')';
	}
}