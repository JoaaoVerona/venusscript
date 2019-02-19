package com.github.bloodshura.ignitium.venus.component.object;

import com.github.bloodshura.ignitium.object.Base;
import com.github.bloodshura.ignitium.venus.expression.Expression;

import javax.annotation.Nonnull;

public class Attribute extends Base {
	private final Expression defaultExpression;
	private final String name;

	public Attribute(String name, Expression defaultExpression) {
		this.defaultExpression = defaultExpression;
		this.name = name;
	}

	public Expression getDefaultExpression() {
		return defaultExpression;
	}

	public String getName() {
		return name;
	}

	public boolean hasDefaultExpression() {
		return getDefaultExpression() != null;
	}

	@Nonnull
	@Override
	protected Object[] stringValues() {
		return new Object[] { getName(), getDefaultExpression() };
	}
}