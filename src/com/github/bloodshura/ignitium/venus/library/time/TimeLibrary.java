package com.github.bloodshura.ignitium.venus.library.time;

import com.github.bloodshura.ignitium.venus.library.VenusLibrary;

public class TimeLibrary extends VenusLibrary {
	public TimeLibrary() {
		addAll(GetDay.class, GetHour.class, GetMinute.class, GetMonth.class, GetSecond.class, GetYear.class);
	}
}