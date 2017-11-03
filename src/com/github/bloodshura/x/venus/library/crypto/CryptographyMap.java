/*
 * Copyright (c) 2013-2017, João Vitor Verona Biazibetti - All Rights Reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * https://www.github.com/BloodShura
 */

package com.github.bloodshura.x.venus.library.crypto;

import com.github.bloodshura.x.collection.map.impl.XLinkedMap;
import com.github.bloodshura.x.cryptography.impl.hash.*;
import com.github.bloodshura.x.cryptography.impl.twoway.Base64;
import com.github.bloodshura.x.cryptography.impl.twoway.Bytaway;
import com.github.bloodshura.x.cryptography.impl.twoway.ByteSwitch;
import com.github.bloodshura.x.cryptography.impl.twoway.CaesarCipher;

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
    return register(
      "base64", Base64.class,
      "bcrypt", BlowfishCrypt.class,
      "bytaway", Bytaway.class,
      "byteswitch", ByteSwitch.class,
      "caesar", CaesarCipher.class,
      "haval128", Haval128.class,
      "haval160", Haval160.class,
      "haval192", Haval192.class,
      "haval224", Haval224.class,
      "haval256", Haval256.class,
      "md2", Md2.class,
      "md4", Md4.class,
      "md5", Md5.class,
      "rmd128", RipeMd128.class,
      "rmd160", RipeMd160.class,
      "sha", Sha.class,
      "sha224", Sha224.class,
      "sha256", Sha256.class,
      "sha384", Sha384.class,
      "sha512", Sha512.class,
      "tiger", Tiger.class,
      "whirlpool", Whirlpool.class
    );
  }
}