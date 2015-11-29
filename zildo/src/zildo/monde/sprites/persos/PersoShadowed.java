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

package zildo.monde.sprites.persos;

import zildo.monde.sprites.desc.ElementDescription;
import zildo.monde.sprites.elements.Element;

/**
 * @author Tchegito
 * 
 */
public class PersoShadowed extends PersoNJ {

	int addY;

	public PersoShadowed() {
		super();
		// TODO: we just have to set here visibility of shadow to 'false' when night has come
		shadow = new Element();
		shadow.setSprModel(ElementDescription.SHADOW);
		addPersoSprites(shadow);
	}

	public PersoShadowed(ElementDescription p_shadowType, int p_addY) {
		this();
		shadow.setSprModel(p_shadowType);
		addY = p_addY;
	}

	@Override
	public void finaliseComportement(int compteur_animation) {
		// Move character's shadow
		if (shadow != null) {
			shadow.setX(x);
			shadow.setY(y - 1);
			shadow.setZ(-7 + addY);
			// TODO:Check this ! Removed when adding fish behavior (no shadow in the water, naturally)
			//ombre.setVisible(z >= 0);
		}
		super.finaliseComportement(compteur_animation);
	}
	
	public Element getShadow() {
		return shadow;
	}
}
