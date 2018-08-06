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

package com.github.bloodshura.ignitium.venus.library.std;

import com.github.bloodshura.ignitium.venus.library.VenusLibrary;

public class StdLibrary extends VenusLibrary {
	public StdLibrary() {
		// Basic I/O
		addAll(HasScan.class, Print.class, Println.class, Scan.class);

		// Collections
		addAll(NewArray.class, Size.class);

		// Desktop
		addAll(Beep.class, Browse.class, Shell.class);

		// Synchronous
		addAll(Consume.class, Produce.class, WaitAttribution.class, WaitDefinition.class);

		// Utilities
		addAll(Assert.class, Exit.class, ExitWithCode.class, Millis.class, Sleep.class);
	}
}