package com.github.bloodshura.ignitium.venus.value;

import com.github.bloodshura.ignitium.charset.TextBuilder;
import com.github.bloodshura.ignitium.util.XApi;
import com.github.bloodshura.ignitium.util.comparator.SimpleEqualizer;
import com.github.bloodshura.ignitium.util.iterator.ArrayIterator;
import com.github.bloodshura.ignitium.venus.exception.runtime.InvalidArrayAccessException;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.type.PrimitiveType;
import com.github.bloodshura.ignitium.worker.ArrayWorker;

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