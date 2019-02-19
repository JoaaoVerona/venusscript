package com.github.bloodshura.ignitium.venus.library.std;

import com.github.bloodshura.ignitium.activity.logging.XLogger;
import com.github.bloodshura.ignitium.collection.list.XList;
import com.github.bloodshura.ignitium.collection.list.impl.XArrayList;
import com.github.bloodshura.ignitium.venus.exception.runtime.InvalidFunctionParameterException;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.expression.BinaryOperation;
import com.github.bloodshura.ignitium.venus.expression.Expression;
import com.github.bloodshura.ignitium.venus.expression.FunctionCall;
import com.github.bloodshura.ignitium.venus.expression.UnaryOperation;
import com.github.bloodshura.ignitium.venus.expression.Variable;
import com.github.bloodshura.ignitium.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.ignitium.venus.function.VoidMethod;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodArgs;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodName;
import com.github.bloodshura.ignitium.venus.value.BoolValue;
import com.github.bloodshura.ignitium.venus.value.Value;

@MethodArgs(Value.class)
@MethodName("wait")
public class WaitAttribution extends VoidMethod {
	@Override
	public void callVoid(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
		Expression expression = descriptor.getExpressions().get(0);
		XList<Variable> list = new XArrayList<>();
		Object lock = new Object();

		scan(context, expression, list);
		list.forEachExceptional(variable -> context.getVar(variable).addChangeMonitor(lock));

		Value value = expression.resolve(context); // Maybe value changed after it was resolved.

		while (!(value instanceof BoolValue && ((BoolValue) value).value())) {
			try {
				synchronized (lock) {
					lock.wait();
				}
			}
			catch (InterruptedException exception) {
				XLogger.warnln("Thread " + Thread.currentThread() + " interrupted while 'wait' was locking.");

				break;
			}

			value = expression.resolve(context);
		}

		list.forEachExceptional(variable -> context.getVar(variable).removeChangeMonitor(lock));
	}

	private void scan(Context context, Expression expression, XList<Variable> list) throws ScriptRuntimeException {
		if (expression instanceof BinaryOperation) {
			BinaryOperation operation = (BinaryOperation) expression;

			scan(context, operation.getLeft(), list);
			scan(context, operation.getRight(), list);
		}
		else if (expression instanceof FunctionCall) {
			throw new InvalidFunctionParameterException(context, "Cannot embed a function call on arguments for 'wait' method");
		}
		else if (expression instanceof UnaryOperation) {
			UnaryOperation operation = (UnaryOperation) expression;

			scan(context, operation.getExpression(), list);
		}
		else if (expression instanceof Variable) {
			list.add((Variable) expression);
		}
	}
}