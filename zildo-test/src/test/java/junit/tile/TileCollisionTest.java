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

package junit.tile;

import org.junit.Test;

import tools.EngineUT;

import org.junit.Assert;

import zildo.monde.map.Area;
import zildo.monde.map.Tile;
import zildo.monde.map.TileCollision;
import zildo.monde.map.Case.TileLevel;
import zildo.monde.sprites.Reverse;
import zildo.monde.sprites.Rotation;
import zildo.monde.sprites.persos.ControllablePerso;
import zildo.monde.sprites.persos.PersoPlayer;
import zildo.server.EngineZildo;

public class TileCollisionTest extends EngineUT {
	
	TileCollision tileCollision;
	
	private String[] halfCorner = { 
			 "0000000011111111",
			 "0000000001111111",
			 "0000000000111111",
			 "0000000000011111",
			 "0000000000001111",
			 "0000000000000111",
			 "0000000000000011",
			 "0000000000000001",
			 "0000000000000000",
			 "0000000000000000",
			 "0000000000000000",
			 "0000000000000000",
			 "0000000000000000",
			 "0000000000000000",
			 "0000000000000000",
			 "0000000000000000"};

	@Test
	public void halfCorner() {
		tileCollision = TileCollision.getInstance();
		
		checkValues(256 + 37, halfCorner, Reverse.NOTHING);
		checkValues(256 + 36, halfCorner, Reverse.HORIZONTAL);
	}

	private void checkValues(int nTile, String[] value, Reverse rev) {
		for (int y=0;y<16;y++) {
			String i = value[y];
			System.out.println("Checking collision for "+i);
			for (int x=0;x<16;x++) {
				int xx = x;
				if (rev == Reverse.HORIZONTAL) {
					xx = 15-x;
				}
				boolean result = '1' == i.charAt(xx);
				boolean col = tileCollision.collide(x, y, nTile, Reverse.NOTHING, Rotation.NOTHING, 0);
				Assert.assertTrue("collision should have been "+result+" for ("+x+", "+y+")", result == col);
			}
		}
	}
	
	@Test
	public void checkMudWater() {
		tileCollision = TileCollision.getInstance();
		
		int mudWaterTile = Tile.T_WATER_MUD;
		
		// Check same Z level on same tile
		Assert.assertEquals(-2, tileCollision.getBottomZ(8, 8, mudWaterTile, false));
		Assert.assertEquals(-2, tileCollision.getBottomZ(0, 0, mudWaterTile, false));
		
		Assert.assertEquals(-2, tileCollision.getBottomZ(11, 9, mudWaterTile, false));
		Assert.assertEquals(-2, tileCollision.getBottomZ(10, 1, mudWaterTile, false));
		
		// Check perso bottom z
		PersoPlayer hero = (PersoPlayer) spawnZildo(619, 249);
		hero.setAppearance(ControllablePerso.PRINCESS_BUNNY);
		
		Area area = EngineZildo.mapManagement.getCurrentMap();
		area.writemap(38, 15, 54);
		area.writemap(37, 15, 54);
		area.writemap(39, 15, 54);
		area.writemap(38, 15, Tile.T_WATER_MUD, TileLevel.BACK2);
		int heroZ = EngineZildo.mapManagement.getPersoBottomZ(hero);
		Assert.assertEquals(-2, heroZ);
	}
}