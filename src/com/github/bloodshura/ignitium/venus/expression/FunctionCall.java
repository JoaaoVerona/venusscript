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

package com.github.bloodshura.ignitium.venus.expression;

import com.github.bloodshura.ignitium.venus.type.PrimitiveType;
import com.github.bloodshura.ignitium.venus.type.Type;
import com.github.bloodshura.ignitium.venus.value.DecimalValue;
import com.github.bloodshura.ignitium.venus.value.IntegerValue;
import com.github.bloodshura.ignitium.venus.value.Value;
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