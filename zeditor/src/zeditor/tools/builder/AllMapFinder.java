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

package zeditor.tools.builder;

import zildo.monde.map.Area;
import zildo.monde.map.Case;
import zildo.server.EngineZildo;

/**
 * @author Tchegito
 *
 */
public class AllMapFinder extends AllMapProcessor {

	@Override
	protected boolean run() {
		Area area = EngineZildo.mapManagement.getCurrentMap();
		
		for (int y = 0 ; y < area.getDim_y() ; y++) {
			for (int x = 0 ; x < area.getDim_x() ; x++) {
				Case mapCase = area.get_mapcase(x, y);
				if (mapCase.getBackTile().getValue() == 256*2 + 131) {
					System.out.println(area.getName());
				}
			}
		}
		
		return false;
	}
	
	public static void main(String[] args) {
		new AllMapFinder().modifyAllMaps();
	}
}
