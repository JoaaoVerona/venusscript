//////////////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2016, João Vitor Verona Biazibetti - All Rights Reserved                /
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

package com.github.bloodshura.venus.library.crypto;

import com.github.bloodshura.crypto.Decrypter;
import com.github.bloodshura.crypto.exception.CryptoException;
import com.github.bloodshura.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.venus.executor.Context;
import com.github.bloodshura.venus.function.Function;
import com.github.bloodshura.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.venus.type.PrimitiveType;
import com.github.bloodshura.venus.type.Type;
import com.github.bloodshura.venus.value.BoolValue;
import com.github.bloodshura.venus.value.StringValue;
import com.github.bloodshura.venus.value.Value;
import com.github.bloodshura.venus.value.VariableRefValue;
import com.github.bloodshura.x.collection.view.XArrayView;
import com.github.bloodshura.x.collection.view.XView;

/**
 * DecryptFunction.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 17/05/16 - 13:01
 * @since GAMMA - 0x3
 */
public class DecryptFunction implements Function {
  private final XView<Type> argumentTypes;
  private final Decrypter decrypter;
  private final String name;

  public DecryptFunction(String name, Decrypter decrypter) {
    this.argumentTypes = new XArrayView<>(PrimitiveType.STRING, PrimitiveType.VARIABLE_REFERENCE);
    this.decrypter = decrypter;
    this.name = name;
  }

  @Override
  public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
    StringValue value = (StringValue) descriptor.get(0);
    VariableRefValue reference = (VariableRefValue) descriptor.get(1);

    try {
      String result = getDecrypter().decryptToStr(value.value());

      context.setVar(reference.value(), new StringValue(result));

      return new BoolValue(true);
    }
    catch (CryptoException exception) {
      return new BoolValue(false);
    }
  }

  @Override
  public XView<Type> getArgumentTypes() {
    return argumentTypes;
  }

  public Decrypter getDecrypter() {
    return decrypter;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isVarArgs() {
    return false;
  }
}