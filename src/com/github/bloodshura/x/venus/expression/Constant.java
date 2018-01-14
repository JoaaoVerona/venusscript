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

package com.github.bloodshura.x.venus.expression;

import com.github.bloodshura.x.util.XApi;
import com.github.bloodshura.x.venus.executor.Context;
import com.github.bloodshura.x.venus.value.Value;

public class Constant implements Expression {
	private final Value value;

	public Constant(Value value) {
		XApi.requireNonNull(value, "value");

		this.value = value;
	}

	public Value getValue() {
		return value;
	}

	@Override
	public Value resolve(Context context) {
		return value;
	}

	@Override
	public String toString() {
		return "const(" + getValue() + ')';
	}
}