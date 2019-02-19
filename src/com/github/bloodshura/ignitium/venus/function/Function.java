package com.github.bloodshura.ignitium.venus.function;

import com.github.bloodshura.ignitium.collection.view.XView;
import com.github.bloodshura.ignitium.lang.Nameable;
import com.github.bloodshura.ignitium.util.XApi;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.type.PrimitiveType;
import com.github.bloodshura.ignitium.venus.type.Type;
import com.github.bloodshura.ignitium.venus.value.Value;

public interface Function extends Nameable {
	default boolean accepts(String name, XView<Type> argumentTypes) {
		XApi.requireNonNull(name, "name");

		if (getName().equals(name)) {
			if (argumentTypes == null) {
				return true;
			}

			if (getArgumentCount() == argumentTypes.size()) {
				for (int i = 0; i < getArgumentCount(); i++) {
					Type required = getArgumentTypes().get(i);
					Type found = argumentTypes.get(i);

					if (!required.accepts(found) && (required != PrimitiveType.DECIMAL || found != PrimitiveType.INTEGER)) {
						return false;
					}
				}

				return true;
			}
			else if (isVarArgs()) {
				return true;
			}
		}

		return false;
	}

	Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException;

	default int getArgumentCount() {
		return getArgumentTypes().size();
	}

	XView<Type> getArgumentTypes();

	boolean isVarArgs();
}