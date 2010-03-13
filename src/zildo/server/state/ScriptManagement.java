package zildo.server.state;

import zildo.fwk.script.command.ScriptExecutor;
import zildo.fwk.script.xml.AdventureElement;
import zildo.fwk.script.xml.QuestElement;
import zildo.fwk.script.xml.SceneElement;
import zildo.fwk.script.xml.ScriptReader;
import zildo.fwk.script.xml.TriggerElement;
import zildo.monde.quest.MapReplacement;
import zildo.monde.quest.QuestEvent;

/**
 * Delegate class which deals with script.<p/>
 * 
 * It provides two functions:<ul>
 * <li>render script (via {@link ScriptExecutor})</li>
 * <li>trigger an event</li>
 * </ul>
 * @author Tchegito
 *
 */
public class ScriptManagement {

    ScriptExecutor scriptExecutor;
    private AdventureElement adventure=null;
    MapReplacement replaces;
    
    public ScriptManagement() {
        // Load adventure
        adventure=(AdventureElement) ScriptReader.loadScript("quests.xml");

        scriptExecutor=new ScriptExecutor();
        
        replaces=new MapReplacement();
    }
    
    public void render() {
    	scriptExecutor.render();
    }
    
    public boolean isScripting() {
    	return scriptExecutor.isScripting();
    }
    
    public void userEndAction() {
    	scriptExecutor.userEndAction();
    }
    
    /**
     * Execute the given named script, if it exists.
     * @param p_name
     */
    public void execute(String p_name) {
    	SceneElement scene=adventure.getSceneNamed(p_name);
    	if (scene != null) {
    		scriptExecutor.execute(scene);
    	}
    }
    
    /**
     * Entry point for all identifiable action, which could target a trigger.<p/>
     * Trigger could be any kind of QuestEvent.
     * @param p_triggerElement element created by a static method from TriggerElement
     */
    public void trigger(TriggerElement p_triggerElement) {
    	// 1: check the existing triggers to potentially enable them
    	for (QuestElement quest : adventure.getQuests()) {
    		if (!quest.done) {
    			// For each quest undone yet :
    			for (TriggerElement trig : quest.getTriggers()) {
    				if (!trig.done && trig.match(p_triggerElement)) {
    					trig.done=true;
    				}
    			}
    		}
    	}
    	// 2: recheck all triggers to potentially accomplish a quest
    	for (QuestElement quest : adventure.getQuests()) {
    		if (!quest.done) {
    			// For each quest undone yet :
    			boolean achieved=true;
    			for (TriggerElement trig : quest.getTriggers()) {
    				achieved&=trig.done;
    			}
    			if (achieved) {
    				accomplishQuest(quest);
    			} else if (quest.isTriggersBoth()) {
    				// All trigger are not activated at the same time ==> then we reset them to 'undone'
    				for (TriggerElement trig : quest.getTriggers()) {
    					trig.done=false;
    				}
    			} else {
    				// Reset only the 'location' trigger to 'undone' (because they have to be immediate)
    				for (TriggerElement trig : quest.getTriggers()) {
    					if (QuestEvent.LOCATION == trig.kind) {
    						trig.done=false;
    					}
    				}    				
    			}
    		}
    	}
    }


    /**
     * Update quest status, and launch the associated actions.
     * @param p_questName
     */
    public void accomplishQuest(String p_questName) {
    	for (QuestElement quest : adventure.getQuests()) {
    		if (quest.name.equals(p_questName)) {
    			accomplishQuest(quest);
    		}
    	}
    }
    
    /**
     * Update quest status, and launch the associated actions.
     * @param p_quest
     */
    private void accomplishQuest(QuestElement p_quest) {
    	p_quest.done=true;
    	// Target potentials triggers
    	TriggerElement trig=TriggerElement.createQuestDoneTrigger(p_quest.name);
    	trigger(trig);
		// Execute the corresponding actions
		// Create a SceneElement from the given actions
		SceneElement scene=SceneElement.createScene(p_quest.getActions());
		// And execute this list
		scriptExecutor.execute(scene);
    }
    
    public String getReplacedMapName(String p_mapName) {
        String name = replaces.get(p_mapName);
        if (name == null) {
            return p_mapName;
        } else {
            return name;
        }
    }
    
    public void addReplacedMapName(String p_ancient, String p_new) {
    	replaces.put(p_ancient, p_new);
    }
}