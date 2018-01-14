/*
 * Copyright (c) 2013-2018, João Vitor Verona Biazibetti - All Rights Reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * https://www.github.com/BloodShura
 */

package com.github.bloodshura.x.venus.component.branch;

import com.github.bloodshura.x.venus.component.Container;
import com.github.bloodshura.x.venus.expression.Expression;

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