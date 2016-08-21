

# Introduction #

Before talking about dialogs in Zildo, we'll start by a brief topo on UI texts. Then, we'll approach the ways dialogs are stored, displayed and articulated inside the game.

# UI Text #

Dialog are a subset of all User Interface text existing in the project. Let's present all the UI text in Zildo.

They are divided into 3 sections : menu, game, and credits. Their meaning is pretty clear, but in details :
  * [menu](http://code.google.com/p/zildo/source/browse/trunk/zildo/src/zildo/resource/bundle/menu.properties) : all text labels found on the menu side : main menu, in-game menu, all messages about internet connection or whatever
  * [game](http://code.google.com/p/zildo/source/browse/trunk/zildo/src/zildo/resource/bundle/game.properties) : all messages displayed inside the game, including dialogs, multiplayer messages, items name and automatic text for the first time Zildo picks an item
  * [credits](http://code.google.com/p/zildo/source/browse/trunk/zildo/src/zildo/resource/bundle/credits.properties) : text displayed in the credits. There's only one multi-line value

## i18n ##

All UI texts are internationalized with keys. For now (09/2012), we have 2 languages : french and english (default one).

# Dialogs, at last #

## Characters ##

Each characters in the game (called Perso in the code) have a set of parametered sentences, 10 maximum, and a script expression.

A sentence is represented by a key whose value is in properties file. (game**.properties)**

### Script expression ###
The script expression indicates what the character will say, according to the adventure state, or other elements, like money in the Zildo's pocket ...

Example of script expressions:
<pre>
chateau_ask:7,enlevement:4,roxy:8,0<br>
</pre>

It means that this character will say the 7th sentence if the quest "chateau\_ask" is one. Otherwise, the 4th if quest "enlevement" is done. Or 8th if "roxy" is done, or 0 in any other case.

There's some extra possibilities to trigger some sentences, depending on various parameters:
  * **money** : followed by a money amount. Triggered if hero has more.
  * **moon** : same with moonstone pieces carried by hero
  * **item** : followed by an item name, in hero's possession to trigger
  * **init** : triggered first time hero meet character since he entered the room
  * **M#** : followed by a map name. Triggered when hero's on this map
  * **P#** : followed by a character name. Triggered if this character is present
  
## Sentence ##

Sentences are the values stored in properties file. There is some predefined special signs to customize dialog process.

### # (sharp) ###

At the end of a sentence, indicate the next sentence to be pronounced by the character.

Example : "Hi Zildo !#2" move the cursor to the sentence number 2 (0 is the first one).

### @ (at) ###

Still at the end of a sentence, it indicates that the next sentence is pronounced automatically. It avoids player to talk again to the same character.

### Game Parameters ###

When using {0} in a sentence, you ask for the player name. It's the only game-related parameter for now.

### Platform-dependent parameter ###

To introduce specific things between platforms, there is 3 different keywords to reference keypads :
  * %key.action% : the action key
  * %key.inventory% : key to get into player's inventory
  * %key.weapon% : key to hit with the current weapon/item

Then, the properties file references translation for each of this key, prefixed by the platform identifier.

Example : "Android.key.action=le cadran supérieur droit de l'écran" for french
