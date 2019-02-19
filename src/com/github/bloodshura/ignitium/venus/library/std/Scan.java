package com.github.bloodshura.ignitium.venus.library.std;

import com.github.bloodshura.ignitium.activity.logging.Logger;
import com.github.bloodshura.ignitium.activity.scanning.XScanner;
import com.github.bloodshura.ignitium.venus.exception.runtime.InvalidValueTypeException;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.ignitium.venus.function.Method;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodArgs;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodName;
import com.github.bloodshura.ignitium.venus.type.PrimitiveType;
import com.github.bloodshura.ignitium.venus.type.Type;
import com.github.bloodshura.ignitium.venus.value.BoolValue;
import com.github.bloodshura.ignitium.venus.value.DecimalValue;
import com.github.bloodshura.ignitium.venus.value.IntegerValue;
import com.github.bloodshura.ignitium.venus.value.StringValue;
import com.github.bloodshura.ignitium.venus.value.TypeValue;
import com.github.bloodshura.ignitium.venus.value.Value;
import com.github.bloodshura.ignitium.worker.ParseWorker;

@MethodArgs(TypeValue.class)
@MethodName("scan")
public class Scan extends Method {
	@Override
	public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
		Logger logger = context.getApplicationContext().getUserData("in", Logger.class);

		if (logger != null) {
			TypeValue value = (TypeValue) descriptor.get(0);
			Type type = value.value();

			while (true) {
				try {
					String line = XScanner.scan();

					if (type == PrimitiveType.BOOLEAN) {
						return new BoolValue(ParseWorker.toBoolean(line));
					}

					if (type == PrimitiveType.DECIMAL) {
						return new DecimalValue(ParseWorker.toDouble(line));
					}

					if (type == PrimitiveType.INTEGER) {
						return new IntegerValue(ParseWorker.toLong(line));
					}

					if (type == PrimitiveType.STRING) {
						return new StringValue(line);
					}

					if (type == PrimitiveType.TYPE) {
						Type lookup = PrimitiveType.forIdentifier(line);

						if (lookup != null) {
							return new TypeValue(lookup);
						}

						continue;
					}

					throw new InvalidValueTypeException(context, "Cannot scan for an input of type " + type);
				}
				catch (NumberFormatException ignored) {
				}
			}
		}

		throw new ScriptRuntimeException(context, "No input resource defined");
	}
}