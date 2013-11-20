/**
 * The Land of Alembrum
 * Copyright (C) 2006-2013 Evariste Boussaton
 * 
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

package zildo.monde.quest.actions;

import java.util.ArrayList;
import java.util.List;

import zildo.fwk.script.model.StringList;
import zildo.monde.dialog.ActionDialog;
import zildo.monde.items.Item;
import zildo.monde.items.ItemKind;
import zildo.monde.items.SellingItem;
import zildo.monde.sprites.persos.Perso;
import zildo.monde.sprites.persos.PersoZildo;
import zildo.server.EngineZildo;
import zildo.server.state.ClientState;

/**
 * Action launched in order to make Zildo buy items with a given character.
 * @author Tchegito
 *
 */
public class BuyingAction extends ActionDialog {

	PersoZildo zildo;
	Perso seller;
	StringList<SellingItem> sellingItems;
	
	/**
	 * @param p_text
	 */
	public BuyingAction(String p_text) {
		super(p_text);
	}

	public BuyingAction(PersoZildo p_zildo, Perso p_seller, String sellDescription) {
		super(null);
		zildo = p_zildo;
		seller = p_seller;
		String itemsAsString = EngineZildo.scriptManagement.getVarValue(sellDescription);
		sellingItems = SellingItem.fromString(itemsAsString); 
	}
	
	@Override
	public void launchAction(ClientState p_clientState) {
		
		zildo.setDialoguingWith(seller);
		seller.setDialoguingWith(zildo);
		
		List<Item> items=new ArrayList<Item>();
		for (int i=0;i<sellingItems.size();i++) {
			SellingItem sItem = sellingItems.get(i);
			items.add(sItem.item);
		}
		zildo.lookItems(items, 0, seller, true);
		
		p_clientState.dialogState.dialoguing=true;
	}

}
