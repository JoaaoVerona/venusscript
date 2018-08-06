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

package com.github.bloodshura.ignitium.venus.executor;

import com.github.bloodshura.ignitium.venus.value.Value;
import com.github.bloodshura.ignitium.collection.list.XList;
import com.github.bloodshura.ignitium.collection.list.impl.XArrayList;

public class VariableStructure {
	private final XList<Object> changeMonitors;
	private final Object lockMonitor;
	private Value value;

	public VariableStructure(Value value) {
		this.changeMonitors = new XArrayList<>();
		this.lockMonitor = new Object();
		this.value = value;
	}

	public void addChangeMonitor(Object monitor) {
		synchronized (changeMonitors) {
			changeMonitors.add(monitor);
		}
	}

	public Object getLockMonitor() {
		return lockMonitor;
	}

	public Value getValue() {
		return value;
	}

	public void removeChangeMonitor(Object monitor) {
		synchronized (changeMonitors) {
			changeMonitors.remove(monitor);
		}
	}

	public void setValue(Value value) {
		this.value = value;

		synchronized (changeMonitors) {
			for (Object monitor : changeMonitors) {
				synchronized (monitor) {
					monitor.notifyAll();
				}
			}
		}
	}

	@Override
	public String toString() {
		return getValue().toString();
	}
}