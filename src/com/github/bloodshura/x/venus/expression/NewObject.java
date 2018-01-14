/*
 * Copyright (c) 2013-2018, João Vitor Verona Biazibetti - All Rights Reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * https://www.github.com/BloodShura
 */

package com.github.bloodshura.x.venus.expression;

import com.github.bloodshura.x.collection.map.XMap;
import com.github.bloodshura.x.collection.tuple.Pair;
import com.github.bloodshura.x.venus.component.object.Attribute;
import com.github.bloodshura.x.venus.component.object.ObjectDefinition;
import com.github.bloodshura.x.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.x.venus.exception.runtime.UndefinedAttributeException;
import com.github.bloodshura.x.venus.executor.Context;
import com.github.bloodshura.x.venus.value.ObjectValue;
import com.github.bloodshura.x.venus.value.Value;

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
			}
			else {
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