package com.github.bloodshura.ignitium.venus.exception.runtime;

import com.github.bloodshura.ignitium.venus.component.object.ObjectDefinition;
import com.github.bloodshura.ignitium.venus.executor.Context;

public class UndefinedAttributeException extends ScriptRuntimeException {
	public UndefinedAttributeException(Context context, ObjectDefinition definition, String attributeName) {
		super(context, "No attribute named \"" + attributeName + "\" in object " + definition.getName());
	}
}