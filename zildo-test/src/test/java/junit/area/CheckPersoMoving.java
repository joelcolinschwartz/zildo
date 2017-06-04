package junit.area;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Assert;
import org.junit.Test;

import tools.EngineUT;
import tools.annotations.SpyHero;
import zildo.monde.sprites.persos.PersoPlayer;
import zildo.monde.sprites.utils.MouvementZildo;
import zildo.server.EngineZildo;

public class CheckPersoMoving extends EngineUT {
/*
	
	 
	*/
	// B11 in Ruben's list
	@Test
	public void fallInWater_igorLily() {
		fall(143, 201, "igorlily");
	}

	@Test
	public void fallInWater_igorVillage() {
		fall(416,308, "igorvillage");
	}

	@Test
	public void fallInWater_sousBois4() {
		fall(912,543, "sousbois4");
	}

	/** Place hero just before a bridge over water, and make him walk toward water. Assert that the right scene is run. **/
	private void fall(int x, int y, String mapName) {
		mapUtils.loadMap("igorlily");
		spawnZildo(143,201);
		waitEndOfScripting();
		simulateDirection(0, 1);
		while (!EngineZildo.scriptManagement.isScripting()) {
			renderFrames(1);
		}
		Assert.assertFalse("Hero should not 'fall' in water, but splash into it !", EngineZildo.scriptManagement.isQuestProcessing("fallPit"));
		Assert.assertTrue(EngineZildo.scriptManagement.isQuestProcessing("dieInWater"));		
	}
	@Test
	public void fallInWater2() {
		mapUtils.loadMap("igorlily");
		PersoPlayer zildo = spawnZildo(143,260);
		waitEndOfScripting();
		zildo.walkTile(false);
		Assert.assertFalse("Hero is still on the bridge ! He shouldn't have dived !", EngineZildo.scriptManagement.isQuestProcessing("dieInWater"));
		Assert.assertFalse(EngineZildo.scriptManagement.isScripting());
	}
	
	/** To ensure no regression has been caused with 'ponton' feature, check that regular case is still ok.
	 * When hero falls into water, jumping from a hill.
	 */
	@Test @SpyHero
	public void fallInRegularWater() {
		mapUtils.loadMap("igorvillage");
		PersoPlayer zildo = spawnZildo(502, 285);
		waitEndOfScripting();
		simulateDirection(0,1);
		renderFrames(20);
		// Check that hero is jumping
		Assert.assertEquals(MouvementZildo.SAUTE, zildo.getMouvement());
		// Wait for his jump to be over
		while (zildo.getMouvement() == MouvementZildo.SAUTE) {
			renderFrames(1);
		}
		// Check that the diving method has been called
		verify(zildo, times(1)).diveAndWound();
		// Check that according script has been launched
		Assert.assertTrue(EngineZildo.scriptManagement.isQuestProcessing("dieInWater"));
	}
	
	@Test
	public void fallInLava1() {
		mapUtils.loadMap("voleursg5");
		waitEndOfScripting();
		// Spawn hero in middle of lava
		PersoPlayer zildo = spawnZildo(218, 133);
		simulateDirection(1,0);
		renderFrames(2);
		checkScriptRunning("dieInPit");
	}
	
	@Test
	public void fallInLava2() {
		mapUtils.loadMap("cavef6");
		waitEndOfScripting();
		// Spawn hero in middle of lava
		PersoPlayer zildo = spawnZildo(87, 215);
		simulateDirection(1,0);
		renderFrames(2);
		checkScriptRunning("dieInPit");
	}
}
