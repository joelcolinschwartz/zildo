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

package junit.area;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import tools.EngineUT;
import zildo.monde.items.Item;
import zildo.monde.items.ItemKind;
import zildo.monde.sprites.Reverse;
import zildo.monde.sprites.SpriteEntity;
import zildo.monde.sprites.desc.ElementDescription;
import zildo.monde.sprites.persos.Perso;
import zildo.monde.sprites.persos.ia.mover.PhysicMoveOrder;
import zildo.monde.util.Angle;
import zildo.monde.util.Point;
import zildo.monde.util.Pointf;
import zildo.monde.util.Vector2f;
import zildo.server.EngineZildo;

/**
 * @author Tchegito
 *
 */
public class CheckLargeObjectCollision extends EngineUT{

	SpriteEntity waterLily;
	Perso zildo;
	
	private void init(int x, int y) {
		mapUtils.loadMap("igorvillage");
		EngineZildo.persoManagement.clearPersos(true);

		

		// Spawn water lily
		waterLily = EngineZildo.spriteManagement.spawnSprite(
				ElementDescription.WATER_LEAF,
				x, y,
				false, Reverse.NOTHING, false); // 113,259
		waterLily.setName("leaf");
		
		zildo = spawnZildo(x, y);
		zildo.walkTile(false);
		
		// Wait end of scripts
		waitEndOfScripting();
	}
	
	/**
	 * Run the whole thing, and check every frame that Zildo's relative location hasn't changed.
	 * If it happens, so there's definitely a bug, because we don't want platform moves without him.
	 */
	private void runAndCheck() {
		int frame = 0;
		
		Pointf relativeZildoLoc = new Pointf(zildo.x - waterLily.x, zildo.y - waterLily.y);
		while (frame++<500) {
			renderFrames(1);
			Assert.assertTrue("Zildo relative location has changed !! It should not happen !",
					relativeZildoLoc.x == zildo.x - waterLily.x && relativeZildoLoc.y == zildo.y - waterLily.y);
			
		}
	}
	@Test
	public void testMove() {
		init(133, 381);

		Point location = new Point(0, -16);
		waterLily.setMover(new PhysicMoveOrder(location.x, location.y));

		runAndCheck();
	}
	
	@Test
	public void testMoveOnBorder() {
		init(15, 242);

		Point location = new Point(0, 16);
		waterLily.setMover(new PhysicMoveOrder(location.x, location.y));

		runAndCheck();
	}
	
	// This test leads to map switch
	@Test
	public void testMoveOnBorderOutside() {
		init(-2, 242);

		Point location = new Point(0, 16);
		waterLily.setMover(new PhysicMoveOrder(location.x, location.y));

		runAndCheck();
	}
	
	/** Check that hero can scroll map when he's on the leaf, attacking with the sword. (Ruben's list: B9) **/
	@Test
	public void testAttackingOnLeaf() {
		// To reproduce this, we used a "recorded" sequence of sword swinging at precise time, in certain angles
		// The cause of the bug was an "askedEvent" send with finalEvent. We disabled it when scene has "locked='false'" attribute
		Map<Integer, Angle> swingSword = new HashMap<>();
		swingSword.put(474, Angle.EST);
		swingSword.put(496, Angle.EST);
		swingSword.put(508, Angle.EST);
		swingSword.put(519, Angle.EST);
		swingSword.put(534, Angle.EST);
		swingSword.put(546, Angle.EST);
		swingSword.put(557, Angle.EST);
		swingSword.put(569, Angle.EST);
		swingSword.put(581, Angle.EST);
		swingSword.put(593, Angle.EST);
		swingSword.put(605, Angle.EST);
		swingSword.put(616, Angle.EST);
		swingSword.put(628, Angle.EST);
		swingSword.put(641, Angle.EST);
		swingSword.put(653, Angle.EST);
		swingSword.put(664, Angle.EST);
		swingSword.put(688, Angle.SUD);
		swingSword.put(720, Angle.EST);
		swingSword.put(731, Angle.EST);
		swingSword.put(746, Angle.EST);
		swingSword.put(758, Angle.EST);
		swingSword.put(768, Angle.EST);

		mapUtils.loadMap("igorvillage");
		EngineZildo.persoManagement.clearPersos(true);
		
		zildo = spawnZildo(420, 340);
		waitEndOfScripting();
		EngineZildo.scriptManagement.accomplishQuest("summonLeafVillage", true);
		renderFrames(1);
		while (EngineZildo.scriptManagement.isQuestProcessing("summonLeafVillage")) {
			renderFrames(1);
		}
		
		waterLily = EngineZildo.spriteManagement.getNamedEntity("leaf");
		Assert.assertNotNull(waterLily);
		zildo.setPos(new Vector2f(428.78207, 354.8249));
		zildo.walkTile(false);
		Assert.assertTrue(zildo.isOnPlatform());
		zildo.setWeapon(new Item(ItemKind.SWORD));

		String currentMapName = EngineZildo.mapManagement.getCurrentMap().getName();
		int nbChangeMaps = 0;
		for (int i=474;i<800;i++) {
			renderFrames(1);
			Angle a = swingSword.get(i);
			if (a != null) {
				zildo.setAngle(a);
				zildo.attack();
			}

			// Detect if map has changed
			String mapName = EngineZildo.mapManagement.getCurrentMap().getName();
			if (!mapName.equals(currentMapName)) {
				currentMapName = mapName;
				nbChangeMaps++;
			}
		}
		
		Assert.assertEquals(1, nbChangeMaps);
		Assert.assertEquals("igorlily", EngineZildo.mapManagement.getCurrentMap().getName());
	}
	
	/** After some regression about BOTTOMLESS waterlily couldn't reach the bridge **/
	@Test
	public void leafReachTheBridge() {
		init(414, 378);
		waterLily.setMover(new PhysicMoveOrder(0, -8));
		renderFrames(16);
		Assert.assertTrue(waterLily.y < 368);
	}
}
