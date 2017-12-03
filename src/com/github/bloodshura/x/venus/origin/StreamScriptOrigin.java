/*
 * Copyright (c) 2013-2017, João Vitor Verona Biazibetti - All Rights Reserved
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

package com.github.bloodshura.x.venus.origin;

import com.github.bloodshura.x.resource.Resource;

import java.io.IOException;

public class StreamScriptOrigin implements ScriptOrigin {
	private final String name;
	private final Resource resource;

	public StreamScriptOrigin(String name, Resource resource) {
		this.name = name;
		this.resource = resource;
	}

	public Resource getResource() {
		return resource;
	}

	@Override
	public String getScriptName() {
		return name;
	}

	@Override
	public String read() throws IOException {
		return getResource().readString();
	}

	@Override
	public String toString() {
		return "streamorigin(" + getScriptName() + ')';
	}
}