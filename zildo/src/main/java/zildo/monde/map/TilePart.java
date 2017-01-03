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

package zildo.monde.map;

import zildo.monde.map.Case.TileLevel;
import zildo.monde.util.Point;

/**
 * @author Tchegito
 *
 */
public class TilePart {

	final Point offset;
	final int value;
	final TileLevel level;
	
	public TilePart(int offX, int offY, int value) {
		this(offX, offY, value, TileLevel.BACK);
	}
	
	public TilePart(int offX, int offY, int value, TileLevel lev) {
		this.offset = new Point(offX, offY);
		this.value = value;
		this.level = lev;
	}

}