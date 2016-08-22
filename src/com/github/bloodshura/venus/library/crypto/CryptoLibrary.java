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

package com.github.bloodshura.venus.library.crypto;

import com.github.bloodshura.crypto.Decrypter;
import com.github.bloodshura.crypto.Encrypter;
import com.github.bloodshura.crypto.util.CryptographyMap;
import com.github.bloodshura.venus.library.VenusLibrary;
import com.github.bloodshura.x.collection.tuple.Pair;

/**
 * CryptoLibrary.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 17/05/16 - 12:49
 * @since GAMMA - 0x3
 */
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