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

package com.github.bloodshura.venus.library.dialogs;

import com.github.bloodshura.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.venus.executor.Context;
import com.github.bloodshura.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.venus.function.Method;
import com.github.bloodshura.venus.function.annotation.MethodArgs;
import com.github.bloodshura.venus.function.annotation.MethodName;
import com.github.bloodshura.venus.value.BoolValue;
import com.github.bloodshura.venus.value.StringValue;
import com.github.bloodshura.venus.value.Value;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

@MethodArgs(StringValue.class)
@MethodName("setTheme")
public class SetTheme extends Method {
  @Override
  public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
    StringValue value = (StringValue) descriptor.get(0);
    String themeName = value.value();
    String themePath = null;

    if (themeName.equalsIgnoreCase("metal")) {
      themePath = UIManager.getCrossPlatformLookAndFeelClassName();
    }
    else if (themeName.equalsIgnoreCase("system")) {
      themePath = UIManager.getSystemLookAndFeelClassName();
    }

    if (themePath != null) {
      try {
        UIManager.setLookAndFeel(themePath);

        return new BoolValue(true);
      }
      catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException exception) {
      }
    }

    return new BoolValue(false);
  }
}