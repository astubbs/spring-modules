/*
 * Created on Aug 11, 2005
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * Copyright @2007 the original author or authors.
 */
package org.springmodules.cache.serializable;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import org.springmodules.util.Objects;

/**
 * Abstraction of a pet.
 *
 * @author Omar Irbouh
 * @author Alex Ruiz
 */
public class Puppy {

	private String name;

	public Puppy() {
	}

	public Puppy(String name) {
		setName(name);
	}

	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Puppy)) {
			return false;
		}

		Puppy puppy = (Puppy) obj;
		return ObjectUtils.nullSafeEquals(name, puppy.name);
	}

	public final String getName() {
		return name;
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		return Objects.nullSafeHashCode(name);
	}

	public final void setName(String name) {
		this.name = name;
	}

	/**
	 * @see Object#toString()
	 */
	public String toString() {
		return Objects.identityToString(this)
				.append("[name=")
				.append(StringUtils.quote(name))
				.append(']')
				.toString();
	}

}