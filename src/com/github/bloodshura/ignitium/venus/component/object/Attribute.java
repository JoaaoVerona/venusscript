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