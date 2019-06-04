package com.github.bloodshura.ignitium.venus.expression;

import com.github.bloodshura.ignitium.collection.map.XMap;
import com.github.bloodshura.ignitium.collection.tuple.Pair;
import com.github.bloodshura.ignitium.venus.component.object.Attribute;
import com.github.bloodshura.ignitium.venus.component.object.ObjectDefinition;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.exception.runtime.UndefinedAttributeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.value.ObjectValue;
import com.github.bloodshura.ignitium.venus.value.Value;

public class NewObject implements Expression {
	private final XMap<String, Expression> attributes;
	private final String objectType;

	public NewObject(String objectType, XMap<String, Expression> attributes) {
		this.attributes = attributes;
		this.objectType = objectType;
	}

	public XMap<String, Expression> getAttributes() {
		return attributes;
	}

	public String getObjectType() {
		return objectType;
	}

	@Override
	public Value resolve(Context context) throws ScriptRuntimeException {
		ObjectDefinition definition = context.getOwner().findObjectDefinition(context, getObjectType());
		Context c = new Context(definition, null); // See issue #24

		for (Pair<String, Expression> pair : getAttributes()) {
			Attribute attribute = definition.getAttributes().selectFirst(attrib -> attrib.getName().equals(pair.getLeft()));

			if (attribute != null) {
				Value value = pair.getRight().resolve(context);

				c.setVar(pair.getLeft(), value);
			} else {
				throw new UndefinedAttributeException(context, definition, pair.getLeft());
			}
		}

		for (Attribute attribute : definition.getAttributes()) {
			if (!c.hasVar(attribute.getName()) && attribute.hasDefaultExpression()) {
				c.setVar(attribute.getName(), attribute.getDefaultExpression().resolve(context));
			}
		}

		return new ObjectValue(definition, c);
	}

	@Override
	public String toString() {
		return "new(" + getObjectType() + " << " + getAttributes() + ')';
	}
}
