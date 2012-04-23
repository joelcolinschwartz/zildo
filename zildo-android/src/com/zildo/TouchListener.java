/**
 * Legend of Zildo
 * Copyright (C) 2006-2012 Evariste Boussaton
 * Based on original Zelda : link to the past (C) Nintendo 1992
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

package com.zildo;

import java.util.ArrayList;
import java.util.List;

import zildo.Zildo;
import zildo.client.Client;
import zildo.client.ClientEngineZildo;
import zildo.fwk.ui.ItemMenu;
import zildo.fwk.ui.Menu;
import zildo.monde.util.Point;
import zildo.platform.input.AndroidKeyboardHandler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * @author Tchegito
 *
 */
public class TouchListener implements OnTouchListener {

	private Client client;
	
	public TouchListener(Client client) {
		this.client = client;
		touchedPoints = new ArrayList<Point>();

	}

	/**
	 * Share touched points with the "keyboard" handler, so as to detect which button is pressed.
	 */
	public void init() {
		AndroidKeyboardHandler kbHandler = (AndroidKeyboardHandler) Zildo.pdPlugin.kbHandler;
		kbHandler.setTouchedPoints(touchedPoints);
	}
	
	ItemMenu item;
	
	final List<Point> touchedPoints;
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		int x = (int) (event.getX() * event.getXPrecision());
		int y = (int) (event.getY() * event.getYPrecision());
		//int a = MotionEvent.ACTION_DOWN;
		Log.d("touch", "pos = "+x+", "+y+" action = "+event.getAction());
		
		Menu menu = client.getCurrentMenu();
		if (menu != null) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				item = ClientEngineZildo.guiDisplay.getItemOnLocation(x, y);
				if (item != null) {
					Log.d("touch", "item "+item.getText());
					menu.activateItem(item);
				}
			}
		} else {
			// No menu ==> player is in game
			switch (event.getAction()) {
			case MotionEvent.ACTION_UP:
				touchedPoints.add(new Point(x,y));
				Log.d("touch", "add points");
				break;
			}
		}
		return true;
	}
	
	public ItemMenu popItem() {
		ItemMenu i = item;
		item = null;
		return i;
	}
	
}