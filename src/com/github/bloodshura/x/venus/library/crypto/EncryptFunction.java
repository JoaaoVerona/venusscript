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

package com.github.bloodshura.x.venus.library.crypto;

import com.github.bloodshura.x.cryptography.Encrypter;
import com.github.bloodshura.x.cryptography.exception.CryptoException;
import com.github.bloodshura.x.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.x.venus.executor.Context;
import com.github.bloodshura.x.venus.function.Function;
import com.github.bloodshura.x.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.x.venus.type.PrimitiveType;
import com.github.bloodshura.x.venus.type.Type;
import com.github.bloodshura.x.venus.value.BoolValue;
import com.github.bloodshura.x.venus.value.StringValue;
import com.github.bloodshura.x.venus.value.Value;
import com.github.bloodshura.x.venus.value.VariableRefValue;
import com.github.bloodshura.x.collection.view.XArrayView;
import com.github.bloodshura.x.collection.view.XView;
import javax.annotation.Nonnull;

public class EncryptFunction implements Function {
  private final XView<Type> argumentTypes;
  private final Encrypter encrypter;
  private final String name;

  public EncryptFunction(String name, Encrypter encrypter) {
    this.argumentTypes = new XArrayView<>(PrimitiveType.STRING, PrimitiveType.VARIABLE_REFERENCE);
    this.encrypter = encrypter;
    this.name = name;
  }

  @Override
  public Value call(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
    StringValue value = (StringValue) descriptor.get(0);
    VariableRefValue reference = (VariableRefValue) descriptor.get(1);

    try {
      String result = getEncrypter().encryptToStr(value.value());

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

  public Encrypter getEncrypter() {
    return encrypter;
  }

  @Nonnull
  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isVarArgs() {
    return false;
  }
}