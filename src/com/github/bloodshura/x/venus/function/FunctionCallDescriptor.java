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

package com.github.bloodshura.x.venus.function;

import com.github.bloodshura.x.collection.view.XView;
import com.github.bloodshura.x.venus.expression.Expression;
import com.github.bloodshura.x.venus.expression.FunctionCall;
import com.github.bloodshura.x.venus.value.Value;

import java.util.function.Function;

public class FunctionCallDescriptor {
	private final FunctionCall caller;
	private final XView<Expression> expressions;
	private final XView<Value> values;

	public FunctionCallDescriptor(FunctionCall caller, XView<Expression> expressions, XView<Value> values) {
		this.caller = caller;
		this.expressions = expressions;
		this.values = values;
	}

	public int count() {
		return getValues().size();
	}

	public Value get(int index) {
		return getValues().get(index);
	}

	public FunctionCall getCaller() {
		return caller;
	}

	public Value getOr(int index, Value value) {
		return index >= 0 && index < getValues().size() ? getValues().get(index) : value;
	}

	public XView<Expression> getExpressions() {
		return expressions;
	}

	public XView<Value> getValues() {
		return values;
	}

	public boolean isEmpty() {
		return count() == 0;
	}

	public <E> E transform(int index, Function<Value, E> function, E or) {
		if (index >= 0 && index < getValues().size()) {
			Value value = get(index);

			return function.apply(value);
		}

		return or;
	}
}