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

package com.github.bloodshura.x.venus.origin;

import com.github.bloodshura.x.io.exception.FileException;
import com.github.bloodshura.x.io.file.File;

import java.io.IOException;

public class FileScriptOrigin implements ScriptOrigin {
  private final File file;

  public FileScriptOrigin(File file) {
    this.file = file;
  }

  @Override
  public ScriptOrigin findRelative(String includePath) {
    try {
      File file = new File(getFile().getParent(), includePath);

      if (file.exists()) {
        return new FileScriptOrigin(file);
      }
    }
    catch (FileException exception) {
    }

    return ScriptOrigin.super.findRelative(includePath);
  }

  public File getFile() {
    return file;
  }

  @Override
  public String getScriptName() {
    return getFile().getFullName();
  }

  @Override
  public String read() throws IOException {
    return getFile().readString();
  }

  @Override
  public String toString() {
    return "fileorigin(" + getScriptName() + ')';
  }
}