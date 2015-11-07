package zildo.client.stage;

import static zildo.client.ClientEngineZildo.guiDisplay;

import java.util.List;

import zildo.Zildo;
import zildo.client.gui.GUIDisplay.DialogMode;
import zildo.client.gui.GUISequence;
import zildo.fwk.input.KeyboardHandler;
import zildo.fwk.input.KeyboardHandler.Keys;
import zildo.monde.dialog.HistoryRecord;
import zildo.server.EngineZildo;

public class TexterStage extends GameStage {
	
	String wholeText;
	KeyboardHandler kbHandler;
	int position;
	int texterHeight;	// Denormalisation of GUIDisplay.getTexterHeight()
	
	public TexterStage() {
		List<HistoryRecord> records = EngineZildo.game.getLastDialog();
		wholeText = HistoryRecord.getDisplayString(records);
		kbHandler = Zildo.pdPlugin.kbHandler;
		position = 0;
	}
	
	@Override
	public void launchGame() {

	}
	
	@Override
	public void renderGame() {
		if (!done) {
			guiDisplay.displayTexter(wholeText, position);
			texterHeight = guiDisplay.getTexterHeight();
		}
	}
	
	@Override
	public void endGame() {
		guiDisplay.clearSequences(GUISequence.CREDIT, GUISequence.FRAME_DIALOG);
		guiDisplay.setToDisplay_dialogMode(DialogMode.CLASSIC);
	}
	
	@Override
	public void updateGame() {
		// If user presses one of the 'quit' key, then leave
    	boolean askQuit = kbHandler.isKeyPressed(Keys.ESCAPE);
    	askQuit |= kbHandler.isKeyPressed(Keys.COMPASS);
    	askQuit |= kbHandler.isKeyPressed(Keys.TOUCH_BACK);
    	if (askQuit) {
    		endGame();
    		done = true;
    	}
    	
    	// Move the texter
    	if (kbHandler.isKeyDown(Keys.DOWN)) {
    		position = Math.min(texterHeight, position + 1);
    	} else if (kbHandler.isKeyDown(Keys.UP)) {
    		position = Math.max(0,  position - 1);
    	}

	}
}
