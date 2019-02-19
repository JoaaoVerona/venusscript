package com.github.bloodshura.ignitium.venus.expression;

import com.github.bloodshura.ignitium.collection.list.XList;
import com.github.bloodshura.ignitium.collection.list.impl.XArrayList;
import com.github.bloodshura.ignitium.collection.view.XArrayView;
import com.github.bloodshura.ignitium.collection.view.XBasicView;
import com.github.bloodshura.ignitium.collection.view.XView;
import com.github.bloodshura.ignitium.venus.exception.runtime.InvalidFunctionParameterException;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.function.Function;
import com.github.bloodshura.ignitium.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.ignitium.venus.type.PrimitiveType;
import com.github.bloodshura.ignitium.venus.type.Type;
import com.github.bloodshura.ignitium.venus.value.DecimalValue;
import com.github.bloodshura.ignitium.venus.value.IntegerValue;
import com.github.bloodshura.ignitium.venus.value.Value;

public class FunctionCall implements Expression {
	private final Expression[] arguments;
	private final String functionName;

	public FunctionCall(String functionName, Expression... arguments) {
		this.arguments = arguments;
		this.functionName = functionName;
	}

	public XView<Expression> getArguments() {
		return new XArrayView<>(arguments);
	}

	public String getFunctionName() {
		return functionName;
	}

	@Override
	public Value resolve(Context context) throws ScriptRuntimeException {
		XView<Value> values = getArguments().mapExceptional(expression -> expression.resolve(context));
		XView<Type> types = values.map(Value::getType);
		Function function = context.getOwner().findFunction(context, getFunctionName(), types);
		XList<Value> list = new XArrayList<>();
		int i = 0;

		// This check is necessary because of function references being untyped (issue #9).
		if (!function.isVarArgs() && types.size() != function.getArgumentTypes().size()) {
			throw new InvalidFunctionParameterException(context, "Function \"" + function + "\" expected " + function.getArgumentTypes().size() + " arguments; received " + types.size());
		}

		for (Value value : values) {
			if (!function.isVarArgs()) {
				Type required = function.getArgumentTypes().get(i);

				if (value.getType() == PrimitiveType.INTEGER && required == PrimitiveType.DECIMAL) {
					value = new DecimalValue(((IntegerValue) value).value());
				}

				// This check is necessary because of function references being untyped (issue #9).
				if (!required.accepts(value.getType())) {
					throw new InvalidFunctionParameterException(context, "Function \"" + function + "\" expected " + required + " as " + (i + 1) + (i == 0 ? "st" : i == 1 ? "nd" : i == 2 ? "rd" : "th") + " argument; received " + value.getType());
				}
			}

			list.add(value);
			i++;
		}

		return function.call(context, new FunctionCallDescriptor(this, getArguments(), new XBasicView<>(list)));
	}

	@Override
	public String toString() {
		return "functioncall(" + getFunctionName() + " <-- [" + getArguments().toString(", ") + "])";
	}
}