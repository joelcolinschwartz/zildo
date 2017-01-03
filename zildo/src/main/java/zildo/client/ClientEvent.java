/**
 * The Land of Alembrum
 * Copyright (C) 2006-2016 Evariste Boussaton
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

package zildo.client;

import zildo.fwk.gfx.filter.FilterEffect;
import zildo.monde.map.ChainingPoint;
import zildo.monde.util.Angle;

public class ClientEvent {

	public ClientEventNature nature;
	public Angle angle;	// Only used with the CHANGINGMAP_SCROLL_START nature, now.
	public ChainingPoint chPoint;
	public boolean mapChange;
	public FilterEffect effect;
	public int wait;
	public boolean script;
	
	
	public ClientEvent(ClientEventNature p_nature) {
		nature=p_nature;
		angle=null;
		wait=0;
		mapChange=false;
		effect=FilterEffect.BLEND;
		script=false;
	}
	
	public ClientEvent(ClientEventNature p_nature, FilterEffect p_effect) {
		this(p_nature);
		effect=p_effect;
	}
	
}
