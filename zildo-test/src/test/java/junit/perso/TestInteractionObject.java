package junit.perso;

import java.util.List;
import java.util.function.Predicate;

import org.junit.Assert;
import org.junit.Test;

import tools.EngineUT;
import zildo.monde.dialog.WaitingDialog;
import zildo.monde.items.Item;
import zildo.monde.items.ItemKind;
import zildo.monde.sprites.Reverse;
import zildo.monde.sprites.Rotation;
import zildo.monde.sprites.SpriteEntity;
import zildo.monde.sprites.desc.ElementDescription;
import zildo.monde.sprites.desc.SpriteDescription;
import zildo.monde.sprites.elements.Element;
import zildo.monde.sprites.elements.ElementGoodies;
import zildo.monde.sprites.persos.PersoPlayer;
import zildo.monde.util.Point;
import zildo.monde.util.Vector2f;
import zildo.server.EngineZildo;

public class TestInteractionObject extends EngineUT {

	PersoPlayer zildo;
	
	/** Check when hero takes items that:
	 * -automatic dialog are well triggered
	 * -item are added into his inventory (if possible) **/
	@Test
	public void automaticBehavior() {
		zildo = spawnZildo(160, 100);
		// Very important: wait, because automatic behavior doesn't run without
		waitEndOfScripting();
		for (ItemKind kind : ItemKind.values()) {
			
			SpriteDescription desc = kind.representation;
			System.out.println(kind);
			
			/*
			boolean before = !(kind.isWeapon() || desc == ElementDescription.FLASK_RED || desc == ElementDescription.NECKLACE
					|| desc == ElementDescription.FLUT || desc != ZildoDescription.SHIELD_DOWN);
					*/
			pick(desc);
			// If automatic sentece is planned, check it
			if (kind.getFoundSentence("") != null) {
				Assert.assertNotNull(dialog(kind));
			}
			//Assert.assertTrue(kind+" should have return "+before+" !", before == pick);
			// Wait for end of dialog
			waitEndOfScriptingPassingDialog();
			if (kind.canBeInInventory()) {
				Assert.assertTrue(kind+" should have been in hero's inventory ! But he only has "+zildo.getInventory(), zildo.hasItem(kind));
			}
			// Remove item in hero's arms
			zildo.setEn_bras(null);
			EngineZildo.dialogManagement.resetQueue();
		}
	}
	
	/** Hero picks an item and return FALSE if he can get it into his inventory **/
	private boolean pick(SpriteDescription desc) {
		ElementGoodies goodie = new ElementGoodies();
		goodie.setDesc(desc);
		return zildo.pickGoodies(goodie, 0);
	}
	
	/** Get the pronounced sentence. Parameter is just for displaying error. **/
	private String dialog(ItemKind kind) {
		List<WaitingDialog> wds = EngineZildo.dialogManagement.getQueue();
		Assert.assertTrue("No automatic dialog was found for "+kind, wds.size() > 0);
		Assert.assertEquals("Only one sentence should have been launched !", 1, wds.size());
		String sentence = wds.get(0).sentence;
		System.out.println(sentence);
		return sentence; 
	}
	
	@Test
	public void plantDynamite() {
		waitEndOfScripting();
		zildo = spawnZildo(160, 100);

		// Hero east of the boulder
		plantAndCheck(new Point(160, 100), boulder -> boulder.vx > 0, "East");
		// Hero west of the boulder
		plantAndCheck(new Point(190, 100), boulder -> boulder.vx < 0, "West");
		// Hero south of the boulder
		plantAndCheck(new Point(160, 110), boulder -> boulder.vy < 0, "South");
		// Hero north of the boulder
		plantAndCheck(new Point(160, 90), boulder -> boulder.vy > 0, "North");
	}
	
	private void plantAndCheck(Point heroPos, Predicate<Element> predicate, String message) {
		// Spawn hero and a boulder
		Element boulder = EngineZildo.spriteManagement.spawnElement(ElementDescription.STONE_HEAVY, 170, 100, 0, Reverse.NOTHING, Rotation.NOTHING);
		// Blow the wall
		zildo.setPos(new Vector2f(heroPos.x, heroPos.y));
		zildo.setWeapon(new Item(ItemKind.DYNAMITE));
		zildo.setCountBomb(4);
		zildo.attack();
		Assert.assertEquals(3,  zildo.getCountBomb());	// Be sure dynamite is planted
		Element dynamite = findByDesc(ElementDescription.DYNAMITE);
		Assert.assertNotNull(dynamite);
		//.forEach(k -> System.out.println(k));;

		// Wait for dynamite to explode
		while (true) {
			renderFrames(1);
			if (findByDesc(ElementDescription.DYNAMITE) == null) {
				break;
			}
		}
		// Check that boulder is projected to the right side
		Assert.assertTrue(boulder.flying);
		Assert.assertTrue("Boulder should have been projected on the "+message, predicate.test(boulder));
	}
	
	/** Find an element from his description. Returns the first one found, or NULL if there isn't any of this kind. **/
	private Element findByDesc(ElementDescription desc) {
		for (SpriteEntity e : EngineZildo.spriteManagement.getSpriteEntities(null)) {
			if (e.getDesc() == desc) {
				return (Element) e;
			}
		}
		return null;
		
	}
}
