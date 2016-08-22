//////////////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2016, João Vitor Verona Biazibetti - All Rights Reserved                /
//                                                                                       /
// Licensed under the GNU General Public License v3;                                     /
// you may not use this file except in compliance with the License.                      /
//                                                                                       /
// You may obtain a copy of the License at                                               /
//     http://www.gnu.org/licenses/gpl.html                                              /
//                                                                                       /
// Unless required by applicable law or agreed to in writing, software                   /
// distributed under the License is distributed on an "AS IS" BASIS,                     /
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.              /
// See the License for the specific language governing permissions and                   /
// limitations under the License.                                                        /
//                                                                                       /
// Written by João Vitor Verona Biazibetti <joaaoverona@gmail.com>, March 2016           /
// https://www.github.com/BloodShura                                                     /
//////////////////////////////////////////////////////////////////////////////////////////

package com.github.bloodshura.venus.test;

import com.github.bloodshura.venus.component.Script;
import com.github.bloodshura.venus.executor.ApplicationContext;
import com.github.bloodshura.venus.executor.VenusExecutor;
import com.github.bloodshura.venus.origin.FileScriptOrigin;
import com.github.bloodshura.venus.origin.ScriptMode;
import com.github.bloodshura.venus.origin.ScriptOrigin;
import com.github.bloodshura.x.collection.list.List;
import com.github.bloodshura.x.collection.list.impl.ArrayList;
import com.github.bloodshura.x.io.file.File;
import com.github.bloodshura.x.io.file.Folder;
import com.github.bloodshura.x.logging.Logger.Level;
import com.github.bloodshura.x.logging.XLogger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.util.Collection;

/**
 * ExecutorTest.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 11/05/16 - 00:14
 * @since GAMMA - 0x3
 */
@RunWith(Parameterized.class)
public class ExecutorTest {
  public static final Folder DIRECTORY = new Folder("VenusScript/examples");
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
    List<Object[]> data = new ArrayList<>();

    DIRECTORY.getAllFiles(file -> !file.getName().endsWith("_i"), file -> data.add(new Object[] { file }));

    return data.asCollection(java.util.ArrayList.class);
  }
}