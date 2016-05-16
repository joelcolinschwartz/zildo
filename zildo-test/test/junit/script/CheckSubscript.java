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

package junit.script;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import zildo.fwk.script.context.SceneContext;
import zildo.fwk.script.xml.ScriptReader;
import zildo.monde.sprites.SpriteEntity;
import zildo.monde.sprites.desc.ElementDescription;
import zildo.server.EngineZildo;

/**
 * @author Tchegito
 *
 */
public class CheckSubscript extends EngineScriptUT {

	/** Check that each subscript is fully executed before returning to caller **/
	@Test
	public void oneSubCall() {
		scriptMgmt.getAdventure().merge(ScriptReader.loadScript("junit/script/subscript"));

		waitEndOfScripting();
		scriptMgmt.execute("caller", true);
		waitEndOfScripting();
		
		Assert.assertEquals("3.0", scriptMgmt.getVariables().get("state"));
	}
	
	@Test
	public void loop() {
		scriptMgmt.getAdventure().merge(ScriptReader.loadScript("junit/script/loops"));
		
		waitEndOfScripting();
		int nbSprites = countSprites();
		scriptMgmt.execute("handMadeFor", true);
		waitEndOfScripting();

		// Check that "spawn" has been called 16 times
		Assert.assertEquals(16,  countSprites() - nbSprites);
	}

	@Test
	public void checkFor() {
		checkForScript("builtInFor");
	}

	@Test
	public void checkNestedFor() {
		checkForScript("forInActions");
		// Make sure following actions after 'for' have been executed
		Assert.assertEquals("1.0", EngineZildo.scriptManagement.getVarValue("goodToGo"));
	}

	// Try to find a bug occuring with turret boss, when 'for' loops variable exceeds its limit. Consequence was
	// turret always shooting bullets on hero, without waiting between a burst.
	@Test
	public void checkForInLoop() {
		debugInfosPersos = false;
		
		scriptMgmt.getAdventure().merge(ScriptReader.loadScript("junit/script/loops"));
		
		waitEndOfScripting();
		scriptMgmt.execute("forInLoop", true, new SceneContext(), null);
		Map<String, String> vars = scriptMgmt.getVariables();
		int startLength = vars.size();
		boolean waitForResetI = false;
		int nbCycle = 0;
		while (true) {
			renderFrames(1);

			vars = scriptMgmt.getVariables();
			Assert.assertTrue(vars.size() <= startLength + 2);
			String s = vars.get("loc:0");
			if (s != null) {
				int i = (int) Float.parseFloat(s);
				if (waitForResetI) {
					if (i == 0) {
						System.out.println("Cycle over: "+nbCycle);
						waitForResetI = false;
						nbCycle++;
					}
				}
			}
			String forOver = scriptMgmt.getVarValue("forOver");
			if ("1.0".equals(forOver)) {
				waitForResetI = true;
			}
		}
	}
	private void checkForScript(String scriptName) {
		scriptMgmt.getAdventure().merge(ScriptReader.loadScript("junit/script/loops"));
		
		waitEndOfScripting();
		int nbSprites = countSprites();
		scriptMgmt.execute(scriptName, true, new SceneContext(), null);
		waitEndOfScripting();

		// Check that "spawn" has been called 16 times
		Assert.assertEquals(16,  countSprites() - nbSprites);
		
		// Check that spawned entities are at the right place, depending on local variable
		int firstY = 100;
		for (SpriteEntity entity : EngineZildo.spriteManagement.getSpriteEntities(null)) {
			if (entity.getDesc() == ElementDescription.BUSHES) {
				Assert.assertEquals(firstY, (int) entity.y);
				firstY += 16;
			}
		}
	}

	private int countSprites() {
		return EngineZildo.spriteManagement.getSpriteEntities(null).size();
	}
}
