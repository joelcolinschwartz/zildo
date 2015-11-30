/**
 * The Land of Alembrum
 * Copyright (C) 2006-2013 Evariste Boussaton
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

package junit.perso;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import zildo.Zildo;
import zildo.client.Client;
import zildo.client.ClientEngineZildo;
import zildo.client.MapDisplay;
import zildo.client.SpriteDisplay;
import zildo.client.gui.GUIDisplay;
import zildo.client.gui.ScreenConstant;
import zildo.client.sound.SoundPlay;
import zildo.fwk.FilterCommand;
import zildo.fwk.ZUtils;
import zildo.fwk.bank.TileBank;
import zildo.fwk.db.Identified;
import zildo.fwk.gfx.Ortho;
import zildo.fwk.gfx.engine.SpriteEngine;
import zildo.fwk.gfx.engine.TileEngine;
import zildo.fwk.gfx.filter.CircleFilter;
import zildo.fwk.gfx.filter.CloudFilter;
import zildo.fwk.gfx.filter.ScreenFilter;
import zildo.fwk.input.CommonKeyboardHandler;
import zildo.fwk.input.KeyboardHandler;
import zildo.fwk.input.KeyboardHandler.Keys;
import zildo.fwk.input.KeyboardInstant;
import zildo.fwk.opengl.OpenGLGestion;
import zildo.monde.Game;
import zildo.monde.quest.actions.ScriptAction;
import zildo.monde.sprites.SpriteEntity;
import zildo.monde.sprites.desc.PersoDescription;
import zildo.monde.sprites.desc.ZildoOutfit;
import zildo.monde.sprites.elements.Element;
import zildo.monde.sprites.persos.Perso;
import zildo.monde.sprites.persos.PersoNJ;
import zildo.monde.sprites.persos.PersoPlayer;
import zildo.monde.sprites.persos.ia.PathFinder;
import zildo.monde.util.Angle;
import zildo.monde.util.Point;
import zildo.monde.util.Pointf;
import zildo.monde.util.Vector2f;
import zildo.server.EngineZildo;
import zildo.server.MapManagement;
import zildo.server.state.ClientState;

/**
 * @author Tchegito
 *
 */
public class EngineUT {

	protected EngineZildo engine;
	protected ClientEngineZildo clientEngine;
	
	protected ClientState clientState;
	protected MapUtils mapUtils;	// To easily manipulate the map
	
	protected KeyboardInstant instant;
	static KeyboardHandler fakedKbHandler;	// Will be reused all along
	
	Thread freezeMonitor;

	int nFrame = 0;
	
	protected Perso spawnTypicalPerso(String name, int x, int y) {
		return spawnPerso(PersoDescription.BANDIT_CHAPEAU, name, x, y);
	}
	
	protected Perso spawnPerso(PersoDescription desc, String name, int x, int y) {
		Perso perso = new PersoNJ();
		perso.x = x;
		perso.y = y;
		perso.setDesc(desc);
		perso.setAngle(Angle.NORD);
		perso.setName(name);
		EngineZildo.spriteManagement.spawnPerso(perso);
		return perso;
	}
	
	protected PersoPlayer spawnZildo(int x, int y) {
		PersoPlayer perso = spy(new PersoPlayer(x, y, ZildoOutfit.Zildo));
		// As we spy the object with Mockito, pathFinder's reference becomes wrong. So, we recreate it
		perso.setPathFinder(new PathFinder(perso));
		
		perso.x = x;
		perso.y = y;
		perso.setDesc(PersoDescription.ZILDO);
		perso.setAngle(Angle.NORD);
		perso.setName("zildo");
		EngineZildo.spriteManagement.spawnPerso(perso);
		
		clientState.zildoId = perso.getId();
		clientState.zildo = perso;
		clientState.keys = instant;	// Simulate keypressed if we have a hero
		
		return perso;
	}
	
	/**
	 * Check that given character is, or is not, at a specific location.<br/>
	 * The 'isAt' boolean determines if he should, or not be there.
	 * @param perso
	 * @param target
	 * @param isAt TRUE=he should be at / FALSE=he shouldn't be at
	 */
	protected void assertLocation(Perso perso, Point target, boolean isAt) {
		Assert.assertTrue(perso.getTarget() == null);
		assertLocation((Element) perso, target, isAt);
	}
	
	/** Check an element location if it is/isn't at a given location, with 0.5 tolerance **/
	protected void assertLocation(Element elem, Point target, boolean isAt) {
		String entityType = elem.getEntityType().toString();
		String name = entityType + (elem.getName() != null ? (" " + elem.getName()) : "");
		String endMessage = target+" but is at ("+elem.x+","+elem.y+")";
		String message;
		if (isAt) {
			message = name+" should have been at " + endMessage;
			Assert.assertTrue(message, 
					Math.abs(elem.x - target.x) <= 0.5f && Math.abs(elem.y - target.y) <= 0.5f);
		} else {
			message = name+" shouldn't have been at " + endMessage;
			Assert.assertTrue(message, 
					Math.abs(elem.x - target.x) > 0.5f || Math.abs(elem.y - target.y) > 0.5f);
		}
	}
	
	protected void assertNotBlocked(Perso perso) {
		for (Angle a : Angle.values()) {
			Point coord = a.coords;
			Pointf loc = perso.tryMove(perso.x + coord.x, perso.y + coord.y);
			if (loc.x != perso.x || loc.y != perso.y) {
				return;
			}
		}
		Assert.assertTrue("Character is blocked !", false);
	}

	protected void renderFrames(int nbFrame) {
		renderFrames(nbFrame, true);
	}
	
	protected void renderFrames(int nbFrame, boolean debugInfos) {
		for (int i=0;i<nbFrame;i++) {
			if (clientState.zildo != null) {	// Simulate keypressed if we have a hero
				clientState.keys = instant;
			}
			updateGame();
			engine.renderFrame(Collections.singleton(clientState));
			
	        // Dialogs
	        if (ClientEngineZildo.guiDisplay.launchDialog(EngineZildo.dialogManagement.getQueue())) {
	        	EngineZildo.dialogManagement.stopDialog(clientState, false);
	        }
	        
	        clientEngine.renderFrame(false);
			ClientEngineZildo.filterCommand.doFilter();
			if (debugInfos) {
				for (Perso perso : EngineZildo.persoManagement.tab_perso) {
					System.out.println(nFrame+": Perso: "+perso.getName()+" at "+perso.x+","+perso.y);
				}
			}
			nFrame++;
		}		
	}
	
	private void updateGame() {
		engine.renderEvent(clientState.event);
		clientState.event = engine.renderEvent(clientState.event);
		clientState.event = clientEngine.renderEvent(clientState.event);
    	EngineZildo.dialogManagement.resetQueue();
	}
	
	public void initServer(Game game) {
		//game.editing = true;
		engine = new EngineZildo(game);
	}
	
	@Before
	public void setUp() {
		Game game = new Game(null, "hero");
		initServer(game);
		
		// Create standard map
		//EngineZildo.soundManagement.setForceMusic(true);
		// Prepare mock for later
		EngineZildo.mapManagement = spy(new MapManagement());
		
		initCounters();

		// Cheat to have a client
		Client fakeClient = spy(new Client(true));
		ClientEngineZildo.client = fakeClient; 
		// Fake a client state list
        Assert.assertEquals(0, EngineZildo.persoManagement.tab_perso.size());
		clientState = new ClientState(null, 1);
		
		EngineZildo.setClientState(clientState);
		// Tile collision
		for (String bankName : TileEngine.tileBankNames) {
			TileBank motifBank = new TileBank();

			motifBank.charge_motifs(bankName);
		}
		
		// Mock certain screen filters
		@SuppressWarnings("unchecked")
		Class<ScreenFilter>[] filterClasses = new Class[] { CloudFilter.class, CircleFilter.class};
		for (Class<ScreenFilter> clazz : filterClasses) {
			ScreenFilter cloudFilter = (ScreenFilter) mock(clazz);
			Zildo.pdPlugin.filters.put(clazz, cloudFilter);
		}

		// Fake client display
		if (clientEngine == null) {
			clientEngine = new ClientEngineZildo(null, false, fakeClient);
			//ClientEngineZildo.screenConstant = new ScreenConstant(Zildo.viewPortX, Zildo.viewPortY);
			//ClientEngineZildo.spriteDisplay = new SpriteDisplay();
			ClientEngineZildo.soundPlay = mock(SoundPlay.class);
			ClientEngineZildo.filterCommand = new FilterCommand();
			ClientEngineZildo.screenConstant = new ScreenConstant(Zildo.viewPortX, Zildo.viewPortY);
			ClientEngineZildo.openGLGestion = mock(OpenGLGestion.class);
			ClientEngineZildo.spriteEngine = mock(SpriteEngine.class);
			ClientEngineZildo.spriteDisplay = spy(new SpriteDisplay(ClientEngineZildo.spriteEngine));
			ClientEngineZildo.guiDisplay = spy(new GUIDisplay());
			ClientEngineZildo.screenConstant = new ScreenConstant(Zildo.screenX, Zildo.screenY);
			when(ClientEngineZildo.guiDisplay.skipDialog()).thenReturn(true);

			// Load default map and initialize map utils
			EngineZildo.mapManagement.loadMap("preintro", false);
			mapUtils = new MapUtils();

			ClientEngineZildo.mapDisplay = spy(new MapDisplay(mapUtils.area));
			//doNothing().when(ClientEngineZildo.mapDisplay).centerCamera();
			ClientEngineZildo.tileEngine = mock(TileEngine.class);
			ClientEngineZildo.ortho = mock(Ortho.class);
			
		}

		// Tells client that we're done with menu and inside the game
		fakeClient.handleMenu(null);
		/*
		new CloudFilter(null) {
			
			@Override
			public boolean renderFilter() {
				// TODO Auto-generated method stub
				return false;
			}
			@Override
			public void addOffset(int x, int y) {
				offsetU += x;
				offsetV += y;
			}
		});
		*/
		
		// Initialize keyboard to simulate input
		instant = new KeyboardInstant();
		if (fakedKbHandler == null) {
			fakedKbHandler = org.mockito.Mockito.mock(CommonKeyboardHandler.class);
		}
		Zildo.pdPlugin.kbHandler = fakedKbHandler;
		
		// Create a thread wich monitors any freeze
		freezeMonitor = new Thread() {
			boolean done= false;
			int lastOne;
			int cnt=0;
			@Override
			public void run() {
				while (!done) {
					if (lastOne == nFrame) {
						// Still on the same frame ?
						if (++cnt == 5) {
							System.out.println("We got a freeze !");
							// Rude, but no bugs are tolerated in Alembrume !
							System.exit(1);
						}
					} else {
						lastOne = nFrame;
						cnt = 0;
					}
					ZUtils.sleep(500);
				}
			};
		};
		freezeMonitor.start();
	}
	
	public void waitEndOfScripting() {
		waitEndOfScripting(null);
	}
	/** Wait until initialization scripts are over. **/
	public void waitEndOfScripting(ScriptAction action) {
		// Wait end of scripts
		while (EngineZildo.scriptManagement.isScripting()) {
			renderFrames(1);
			if (action != null) {
				action.launchAction(clientState);
			}
		}
	}

	/** Simulates player holding a direction with the d-pad **/
	public void simulateDirection(Vector2f dir) {
		when(fakedKbHandler.getDirection()).thenReturn(dir);
		instant.update();
	}

	public void simulateKeyPressed(Keys key) {
		reset(fakedKbHandler);
		if (key == null) {
			doReturn(false).when(fakedKbHandler).isKeyDown(anyInt());
		} else {
			doReturn(true).when(fakedKbHandler).isKeyDown(key);
		}
		//when(fakedKbHandler.isKeyPressed(key)).thenReturn(true);
		instant.update();
	}
	
	/** Press a key, wait, then hold it, and wait again given time (by frame number) **/
	public void simulatePressButton(Keys key, int time) {
		simulateKeyPressed(key);
		renderFrames(1);
		simulateKeyPressed(null);
		renderFrames(time);		
	}
	
	@SuppressWarnings("deprecation")
	@After
	public void tearDown() {
		// Reset input
		simulateDirection(new Vector2f(0, 0));
		// Deprecated, but we're not concerned by limitations here
		freezeMonitor.stop();
	}
	
	private void initCounters() {
		for (Perso perso : EngineZildo.persoManagement.tab_perso) {
			Identified.remove(SpriteEntity.class, perso.getId());
			for (Element elem : perso.getPersoSprites()) {
				Identified.remove(SpriteEntity.class, elem.getId());
			}
		}
		Identified.resetCounter(SpriteEntity.class);
		Identified.clearAll();
	}
}