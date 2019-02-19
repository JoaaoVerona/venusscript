package com.github.bloodshura.ignitium.venus.library.dialogs;

import com.github.bloodshura.ignitium.venus.library.VenusLibrary;

public class DialogsLibrary extends VenusLibrary {
	public DialogsLibrary() {
		addAll(AskDialog.class, Dialog.class, ErrorDialog.class, InfoDialog.class, InputDialog.class, SetTheme.class, WarnDialog.class);
	}
}