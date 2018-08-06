/*
 * Copyright (c) 2013-2018, João Vitor Verona Biazibetti - All Rights Reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * https://www.github.com/BloodShura
 */

package com.github.bloodshura.ignitium.venus.component.branch;

import com.github.bloodshura.ignitium.venus.expression.Expression;
import com.github.bloodshura.ignitium.venus.component.Container;

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