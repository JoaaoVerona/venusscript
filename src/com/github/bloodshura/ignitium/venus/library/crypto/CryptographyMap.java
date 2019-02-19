package com.github.bloodshura.ignitium.venus.library.crypto;

import com.github.bloodshura.ignitium.collection.map.impl.XLinkedMap;
import com.github.bloodshura.ignitium.cryptography.impl.hash.*;
import com.github.bloodshura.ignitium.cryptography.impl.twoway.Base64;
import com.github.bloodshura.ignitium.cryptography.impl.twoway.Bytaway;
import com.github.bloodshura.ignitium.cryptography.impl.twoway.ByteSwitch;
import com.github.bloodshura.ignitium.cryptography.impl.twoway.CaesarCipher;

import javax.annotation.Nonnull;

public class CryptographyMap extends XLinkedMap<String, Object> {
	@Nonnull
	public CryptographyMap register(@Nonnull Object... objects) {
		for (int i = 0; i < objects.length; i += 2) {
			try {
				String key = (String) objects[i];
				Object value;

				if (objects[i + 1] instanceof Class) {
					Class<?> crypterClass = (Class<?>) objects[i + 1];

					value = crypterClass.newInstance();
				}
				else {
					value = objects[i + 1];
				}

				set(key.toLowerCase(), value);
			}
			catch (IllegalAccessException | InstantiationException | SecurityException exception) {
				throw new IllegalArgumentException("Invalid argument " + i + ':', exception);
			}
		}

		return this;
	}

	@Nonnull
	public CryptographyMap registerDefaults() {
		return register("base64", Base64.class, "bcrypt", BlowfishCrypt.class, "bytaway", Bytaway.class, "byteswitch", ByteSwitch.class, "caesar", CaesarCipher.class, "haval128", Haval128.class, "haval160", Haval160.class, "haval192", Haval192.class, "haval224", Haval224.class, "haval256", Haval256.class, "md2", Md2.class, "md4", Md4.class, "md5", Md5.class, "rmd128", RipeMd128.class, "rmd160", RipeMd160.class, "sha", Sha.class, "sha224", Sha224.class, "sha256", Sha256.class, "sha384", Sha384.class, "sha512", Sha512.class, "tiger", Tiger.class, "whirlpool", Whirlpool.class);
	}
}