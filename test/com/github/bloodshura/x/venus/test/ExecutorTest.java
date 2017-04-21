//////////////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2013-2017, João Vitor Verona Biazibetti - All Rights Reserved           /
//                                                                                       /
// This program is free software: you can redistribute it and/or modify                  /
// it under the terms of the GNU General Public License as published by                  /
// the Free Software Foundation, either version 3 of the License, or                     /
// (at your option) any later version.                                                   /
//                                                                                       /
// This program is distributed in the hope that it will be useful,                       /
// but WITHOUT ANY WARRANTY; without even the implied warranty of                        /
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the                          /
// GNU General Public License for more details.                                          /
//                                                                                       /
// You should have received a copy of the GNU General Public License                     /
// along with this program. If not, see <http://www.gnu.org/licenses/>.                  /
//                                                                                       /
// https://www.github.com/BloodShura                                                     /
//////////////////////////////////////////////////////////////////////////////////////////

package com.github.bloodshura.x.venus.test;

import com.github.bloodshura.x.activity.logging.Logger.Level;
import com.github.bloodshura.x.activity.logging.XLogger;
import com.github.bloodshura.x.collection.list.XList;
import com.github.bloodshura.x.collection.list.impl.XArrayList;
import com.github.bloodshura.x.io.file.Directory;
import com.github.bloodshura.x.io.file.File;
import com.github.bloodshura.x.venus.component.Script;
import com.github.bloodshura.x.venus.executor.ApplicationContext;
import com.github.bloodshura.x.venus.executor.VenusExecutor;
import com.github.bloodshura.x.venus.origin.FileScriptOrigin;
import com.github.bloodshura.x.venus.origin.ScriptMode;
import com.github.bloodshura.x.venus.origin.ScriptOrigin;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ExecutorTest {
  public static final Directory DIRECTORY = new Directory("VenusScript/examples");
  private final File file;

  public ExecutorTest(File file) {
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

    DIRECTORY.getAllFiles(file -> !file.getName().endsWith("_i"), file -> data.add(new Object[] { file }));

    return data.toCollection(ArrayList.class);
  }
}