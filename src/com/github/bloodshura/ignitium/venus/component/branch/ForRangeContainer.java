package com.github.bloodshura.ignitium.venus.component.branch;

import com.github.bloodshura.ignitium.venus.component.Container;
import com.github.bloodshura.ignitium.venus.expression.Expression;

public class ForRangeContainer extends Container implements Breakable {
	private final Expression adjustment;
	private final Expression from;
	private final Expression to;
	private final String varName;

	public ForRangeContainer(String varName, Expression from, Expression to, Expression adjustment) {
		this.adjustment = adjustment;
		this.from = from;
		this.to = to;
		this.varName = varName;
	}

	public Expression getAdjustment() {
		return adjustment;
	}

	public Expression getFrom() {
		return from;
	}

	public Expression getTo() {
		return to;
	}

	public String getVarName() {
		return varName;
	}

	@Override
	public String toString() {
		return "for(" + getVarName() + " in [" + getFrom() + ", " + getTo() + "] with " + getAdjustment() + ')';
	}
}