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

package com.github.bloodshura.x.venus.component;

import com.github.bloodshura.x.lang.annotation.Internal;
import com.github.bloodshura.x.util.XApi;
import com.github.bloodshura.x.venus.executor.ApplicationContext;

public abstract class Component {
	private Container parent;
	private int sourceLine;

	public ApplicationContext getApplicationContext() {
		XApi.requireState(getParent() != null, "Could not retrieve application context; no parent available");

		return getParent().getApplicationContext();
	}

	public final Container getParent() {
		return parent;
	}

	public Script getScript() {
		XApi.requireState(getParent() != null, "Could not retrieve script; no parent available");

		return getParent().getScript();
	}

	public final int getSourceLine() {
		return sourceLine;
	}

	public final boolean hasParent() {
		return getParent() != null;
	}

	@Internal
	public void setParent(Container parent) {
		this.parent = parent;
	}

	@Internal
	public void setSourceLine(int sourceLine) {
		this.sourceLine = sourceLine;
	}

	@Override
	public abstract String toString();
}