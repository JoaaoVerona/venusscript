package com.github.bloodshura.ignitium.venus.type;

import com.github.bloodshura.ignitium.collection.view.XArrayView;
import com.github.bloodshura.ignitium.collection.view.XView;
import com.github.bloodshura.ignitium.enumeration.Enumerations;
import com.github.bloodshura.ignitium.util.XApi;
import com.github.bloodshura.ignitium.venus.expression.Variable;
import com.github.bloodshura.ignitium.venus.function.Function;
import com.github.bloodshura.ignitium.venus.value.ArrayValue;
import com.github.bloodshura.ignitium.venus.value.BoolValue;
import com.github.bloodshura.ignitium.venus.value.DecimalValue;
import com.github.bloodshura.ignitium.venus.value.FunctionRefValue;
import com.github.bloodshura.ignitium.venus.value.IntegerValue;
import com.github.bloodshura.ignitium.venus.value.StringValue;
import com.github.bloodshura.ignitium.venus.value.TypeValue;
import com.github.bloodshura.ignitium.venus.value.Value;
import com.github.bloodshura.ignitium.venus.value.VariableRefValue;
import com.github.bloodshura.ignitium.worker.UtilWorker;

public final class PrimitiveType extends Type {
	public static final Type ANY = new PrimitiveType("any", Value.class, Object.class);
	public static final Type ARRAY = new PrimitiveType("array", ArrayValue.class, Value[].class);
	public static final Type BOOLEAN = new PrimitiveType("bool", BoolValue.class, Boolean.class);
	public static final Type DECIMAL = new PrimitiveType("decimal", DecimalValue.class, Double.class, Float.class);
	public static final Type FUNCTION_REFERENCE = new PrimitiveType("ref", FunctionRefValue.class, Function.class);
	public static final Type INTEGER = new PrimitiveType("int", IntegerValue.class, Integer.class, Long.class);
	public static final Type STRING = new PrimitiveType("string", StringValue.class, String.class);
	public static final Type TYPE = new PrimitiveType("type", TypeValue.class, Type.class);
	public static final Type VARIABLE_REFERENCE = new PrimitiveType("var", VariableRefValue.class, Variable.class);

	private final XView<Class<?>> objectTypes;
	private final Class<? extends Value> valueClass;

	private PrimitiveType(String identifier, Class<? extends Value> valueClass, Class<?>... objectTypes) {
		super(identifier);
		this.objectTypes = new XArrayView<>(objectTypes);
		this.valueClass = valueClass;
	}

	@Override
	public boolean accepts(Class<? extends Value> valueCl) {
		return valueClass.isAssignableFrom(valueCl);
	}

	@Override
	public boolean accepts(Type type) {
		return type instanceof PrimitiveType && accepts(((PrimitiveType) type).valueClass);
	}

	@Override
	public boolean objectAccepts(Class<?> type) {
		return objectTypes.any(object -> object.isAssignableFrom(UtilWorker.fixPrimitiveClass(type)));
	}

	public static Type forIdentifier(String identifier) {
		XApi.requireNonNull(identifier, "identifier");

		for (Type value : values()) {
			if (value.getIdentifier().equals(identifier)) {
				return value;
			}
		}

		return null;
	}

	public static Type forObjectType(Class<?> type) {
		XApi.requireNonNull(type, "type");

		for (Type value : values()) {
			if (value != ANY && value.objectAccepts(type)) {
				return value;
			}
		}

		return ANY;
	}

	public static Type forType(Class<? extends Value> type) {
		XApi.requireNonNull(type, "type");

		for (Type value : values()) {
			if (value != ANY && value.accepts(type)) {
				return value;
			}
		}

		return ANY;
	}

	public static XView<PrimitiveType> values() {
		return Enumerations.values(PrimitiveType.class);
	}
}