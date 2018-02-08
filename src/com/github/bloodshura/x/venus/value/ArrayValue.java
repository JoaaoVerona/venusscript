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

package com.github.bloodshura.x.venus.value;

import com.github.bloodshura.x.charset.TextBuilder;
import com.github.bloodshura.x.util.XApi;
import com.github.bloodshura.x.util.comparator.SimpleEqualizer;
import com.github.bloodshura.x.util.iterator.ArrayIterator;
import com.github.bloodshura.x.venus.exception.runtime.InvalidArrayAccessException;
import com.github.bloodshura.x.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.x.venus.executor.Context;
import com.github.bloodshura.x.venus.type.PrimitiveType;
import com.github.bloodshura.x.worker.ArrayWorker;

import java.util.Iterator;

public class ArrayValue extends IterableValue {
	private final Value[] values;

	public ArrayValue(int size) {
		this(new Value[size]);
	}

	public ArrayValue(Value... values) {
		super(PrimitiveType.ARRAY);
		XApi.requireNonNull(values, "values");

		this.values = values;
	}

	@Override
	public ArrayValue clone() {
		return new ArrayValue(ArrayWorker.copyOf(value()));
	}

	@Override
	public BoolValue equals(Value value) {
		if (value instanceof ArrayValue) {
			ArrayValue array = (ArrayValue) value;

			return new BoolValue(size() == array.size() && SimpleEqualizer.compare(value(), array.value()));
		}

		return new BoolValue(false);
	}

	public Value get(Context context, int index) throws ScriptRuntimeException {
		if (index < 0 || index >= size()) {
			throw new InvalidArrayAccessException(context, "Out of range array index: " + index + ", expected between 0~" + (size() - 1));
		}

		return value()[index];
	}

	@Override
	public Iterator<Value> iterator() {
		return new ArrayIterator<>(value());
	}

	public void set(Context context, int index, Value value) throws ScriptRuntimeException {
		if (index < 0 || index >= size()) {
			throw new InvalidArrayAccessException(context, "Out of range array index: " + index + ", expected between 0~" + (size() - 1));
		}

		value()[index] = value;
	}

	public int size() {
		return value().length;
	}

	@Override
	public String toString() {
		return toString(this);
	}

	@Override
	public Value[] value() {
		return values;
	}

	private static <E> String toString(ArrayValue array) {
		TextBuilder builder = new TextBuilder().setSeparator(", ");

		for (Value value : array.value()) {
			if (value instanceof ArrayValue) {
				builder.append('[' + toString((ArrayValue) value) + ']');
			}
			else {
				builder.append(value);
			}
		}

		return '[' + builder.toString() + ']';
	}
}