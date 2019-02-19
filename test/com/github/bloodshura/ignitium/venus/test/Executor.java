package com.github.bloodshura.ignitium.venus.test;

import com.github.bloodshura.ignitium.activity.logging.Level;
import com.github.bloodshura.ignitium.activity.logging.XLogger;
import com.github.bloodshura.ignitium.collection.list.XList;
import com.github.bloodshura.ignitium.collection.list.impl.XArrayList;
import com.github.bloodshura.ignitium.io.Directory;
import com.github.bloodshura.ignitium.io.File;
import com.github.bloodshura.ignitium.venus.component.Script;
import com.github.bloodshura.ignitium.venus.executor.ApplicationContext;
import com.github.bloodshura.ignitium.venus.executor.VenusExecutor;
import com.github.bloodshura.ignitium.venus.origin.FileScriptOrigin;
import com.github.bloodshura.ignitium.venus.origin.ScriptMode;
import com.github.bloodshura.ignitium.venus.origin.ScriptOrigin;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@RunWith(Parameterized.class)
public class Executor {
	public static final Directory DIRECTORY = new Directory("VenusScript/examples");
	private final File file;

	public Executor(File file) {
		this.file = file;
	}

	@Test
	public void simpleTest() throws Exception {
		XLogger.disable(Level.DEBUG);

		ScriptOrigin origin = new FileScriptOrigin(file);
		Script script = origin.compile(new ApplicationContext());
		VenusExecutor executor = new VenusExecutor();

		executor.run(script, ScriptMode.NORMAL);
	}

	@Parameters
	public static Collection<Object[]> data() throws IOException {
		XList<Object[]> data = new XArrayList<>();

		DIRECTORY.deepIterateFiles(file -> !file.getName().endsWith("_i"), file -> data.add(new Object[] { file }));

		return data.toCollection(ArrayList.class);
	}
}