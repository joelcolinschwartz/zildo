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

package zildo.monde.map.accessor;

import zildo.monde.map.Case;

/**
 * @author Tchegito
 *
 */
public class HighestFloorAccessor extends AreaAccessor {

	@Override
	public AccessedCase get_mapcase(int x, int y) {
		byte floor = area.getHighestFloor();
		Case c = null;
		while (floor>=0) {
			c = area.getMapcaseWithoutOverflow(x, y, floor);
			if (c != null) break;
			floor--;
		}
		accCase.c = c;
		accCase.floor = floor;
		return accCase;
	}

}
