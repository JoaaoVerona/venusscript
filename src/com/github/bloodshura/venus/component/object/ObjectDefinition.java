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

package com.github.bloodshura.venus.component.object;

import com.github.bloodshura.venus.component.Container;
import com.github.bloodshura.venus.executor.Context;
import com.github.bloodshura.venus.type.ObjectType;
import com.github.bloodshura.venus.type.Type;
import com.github.bloodshura.x.collection.list.XList;
import com.github.bloodshura.x.collection.list.impl.XArrayList;

/**
 * ObjectDefinition.java
 *
 * @author <a href="https://www.github.com/BloodShura">BloodShura</a> (João Vitor Verona Biazibetti)
 * @contact joaaoverona@gmail.com
 * @date 29/05/16 - 01:06
 * @since GAMMA - 0x3
 */
public class ObjectDefinition extends Container {
  private final XList<Attribute> attributes;
  private final String name;
  private final Type type;

  public ObjectDefinition(String name) {
    this.attributes = new XArrayList<>();
    this.name = name;
    this.type = new ObjectType(name);
  }

  public XList<Attribute> getAttributes() {
    return attributes;
  }

  public String getName() {
    return name;
  }

  public Type getType() {
    return type;
  }

  @Override
  public void setParent(Container parent) {
    super.setParent(parent);
    this.context = new Context(this, parent.getContext());
  }

  @Override
  public String toString() {
    return "objectdef(" + getName() + ')';
  }
}