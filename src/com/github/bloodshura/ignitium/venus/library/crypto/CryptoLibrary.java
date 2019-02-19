package com.github.bloodshura.ignitium.venus.library.crypto;

import com.github.bloodshura.ignitium.collection.tuple.Pair;
import com.github.bloodshura.ignitium.cryptography.Decrypter;
import com.github.bloodshura.ignitium.cryptography.Encrypter;
import com.github.bloodshura.ignitium.venus.library.VenusLibrary;

public class CryptoLibrary extends VenusLibrary {
	public CryptoLibrary() {
		this(new CryptographyMap().registerDefaults());
	}

	public CryptoLibrary(CryptographyMap map) {
		for (Pair<String, Object> pair : map) {
			if (pair.getRight() instanceof Encrypter) {
				add(new EncryptFunction(pair.getLeft(), (Encrypter) pair.getRight()));
			}

			if (pair.getRight() instanceof Decrypter) {
				add(new DecryptFunction("un" + pair.getLeft(), (Decrypter) pair.getRight()));
			}
		}
	}
}