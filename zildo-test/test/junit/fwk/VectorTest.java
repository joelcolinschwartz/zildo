/**
 * The Land of Alembrum
 * Copyright (C) 2006-2016 Evariste Boussaton
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package junit.fwk;

import org.junit.Test;

import org.junit.Assert;
import zildo.monde.util.Vector2f;

/**
 * @author Tchegito
 *
 */
public class VectorTest {

	@Test
	public void rotation() {
		Vector2f vec = new Vector2f(-0.5f, 0.5f);
		float distance = vec.norm();
		Assert.assertEquals(vec.rotX().x, -distance, 0.0001d);
		Assert.assertEquals(vec.rotX().y, 0, 0.0001d);
		vec.x = 0.5f;
		Assert.assertEquals(vec.rotX().x, distance, 0.0001d);
		Assert.assertEquals(vec.rotX().y, 0, 0.0001d);
	}
}
