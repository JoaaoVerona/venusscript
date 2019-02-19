package com.github.bloodshura.ignitium.venus.component.branch;

import com.github.bloodshura.ignitium.venus.component.Component;
import com.github.bloodshura.ignitium.venus.expression.Expression;

public class Return extends Component {
	private final Expression expression;

	public Return(Expression expression) {
		this.expression = expression;
	}

	public Expression getExpression() {
		return expression;
	}

	@Override
	public String toString() {
		return "return(" + getExpression() + ')';
	}
}