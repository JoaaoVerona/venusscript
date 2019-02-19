package com.github.bloodshura.ignitium.venus.library.dynamic;

import com.github.bloodshura.ignitium.venus.component.Script;
import com.github.bloodshura.ignitium.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.ignitium.venus.executor.Context;
import com.github.bloodshura.ignitium.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.ignitium.venus.function.VoidMethod;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodArgs;
import com.github.bloodshura.ignitium.venus.function.annotation.MethodName;
import com.github.bloodshura.ignitium.venus.library.VenusLibrary;
import com.github.bloodshura.ignitium.venus.value.StringValue;

import java.util.function.Supplier;

@MethodArgs(StringValue.class)
@MethodName("dynamicUsing")
public class DynamicUsing extends VoidMethod {
	@Override
	public void callVoid(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
		StringValue libraryName = (StringValue) descriptor.get(0);
		Script script = context.getScript();
		Supplier<VenusLibrary> supplier = script.getApplicationContext().getLibrarySuppliers().get(libraryName.value());
		VenusLibrary library;

		if (supplier != null && (library = supplier.get()) != null) {
			script.getLibraryList().add(library);
		}
		else {
			throw new ScriptRuntimeException(context, "Could not find a library named \"" + libraryName + "\"");
		}
	}
}