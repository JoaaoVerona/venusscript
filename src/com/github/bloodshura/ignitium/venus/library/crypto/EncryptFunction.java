package com.github.bloodshura.ignitium.venus.library.crypto;

import com.github.bloodshura.ignitium.collection.view.XArrayView;
import com.github.bloodshura.ignitium.collection.view.XView;
import com.github.bloodshura.ignitium.cryptography.Encrypter;
import com.github.bloodshura.ignitium.cryptography.exception.CryptoException;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.function.Function;
import com.github.bloodshura.ignitium.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.ignitium.venus.type.PrimitiveType;
import com.github.bloodshura.ignitium.venus.type.Type;
import com.github.bloodshura.ignitium.venus.value.BoolValue;
import com.github.bloodshura.ignitium.venus.value.StringValue;
import com.github.bloodshura.ignitium.venus.value.Value;
import com.github.bloodshura.ignitium.venus.value.VariableRefValue;

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