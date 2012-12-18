/**
 * Legend of Zildo
 * Copyright (C) 2006-2012 Evariste Boussaton
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

package zildo.monde.sprites.desc;

import zildo.fwk.bank.SpriteBank;
import zildo.monde.sprites.Reverse;
import zildo.monde.sprites.Rotation;
import zildo.monde.sprites.utils.Sprite;
import zildo.monde.util.Angle;
import zildo.monde.util.Point;

/**
 * @author Tchegito
 *
 */
public class ZildoSprSequence {

	
	// Sword attack sequence
	// UP
	Sprite[] seqUp = new Sprite[]{spr(3, Rotation.COUNTERCLOCKWISE, Reverse.HORIZONTAL),
								  spr(1, Rotation.COUNTERCLOCKWISE, null),
								  spr(0, Rotation.CLOCKWISE, null),
								  spr(3, null, Reverse.VERTICAL),
								  spr(2, Rotation.CLOCKWISE, null),
								  spr(1, Rotation.UPSIDEDOWN, null)
								  }; 
	Sprite[] seqRight = new Sprite[]{spr(3, Rotation.UPSIDEDOWN, null),
			  spr(1, null, Reverse.VERTICAL),
			  spr(0, null, Reverse.HORIZONTAL),
			  spr(3, Rotation.COUNTERCLOCKWISE, null),
			  spr(2, null, Reverse.HORIZONTAL),
			  spr(1, Rotation.COUNTERCLOCKWISE, Reverse.VERTICAL)
			  }; 
	
	Sprite[] seqLeft = null;
	
	Sprite[] seqDown = new Sprite[]{spr(0, null, null),
			  spr(2, null, null),
			  spr(3, null, null),
			  spr(0, Rotation.COUNTERCLOCKWISE, null),
			  spr(2, Rotation.COUNTERCLOCKWISE, null),
			  spr(1, null, null)
			  }; 
	
	Point[] seqUpPts = new Point[]{new Point(8, 9), new Point(8, 17), new Point(-3, 22),
			new Point(-6, 18), new Point(-12, 12), new Point(-15, 8)};
	
	Point[] seqRightPts = new Point[]{new Point(11, -1), new Point(14, -2), new Point(19, 10), 
			new Point(16, 13), new Point(14, 14), new Point(9, 19)};
	
	//Point[] seqDownPts = new Point[]{new Point(-5, 11), new Point(-6, 10), new Point(6, 19),
		//	new Point(9, 19), new Point(10, 15), new Point(16, 13)};
	
	//Point[] seqDownPts = new Point[]{new Point(-9, 11), new Point(-8, 10), new Point(4, 2),
	//		new Point(9, 0), new Point(8, 7), new Point(14, 9)};
	Point[] seqDownPts = new Point[]{new Point(-9, 5), new Point(-8, -4), new Point(4, -10),
			new Point(6, -12), new Point(8, -7), new Point(14, -5)};
	Point[] seqLeftPts;
	
	public Sprite getSpr(Angle angle, int pos) {
		if (seqLeft == null) {
			seqLeft = new Sprite[6];
			int i=0;
			for (Sprite spr : seqRight) {
				seqLeft[i++] = new Sprite(spr.nSpr, spr.nBank, spr.reverse.flipHorizontal(), spr.rotate);
			}
		}
		switch (angle) {
		case NORD:
			return seqUp[pos];
		case SUD:
			return seqDown[pos];
		case OUEST:
			return seqRight[pos];
		case EST:
			default:
			return seqLeft[pos];
		}
	}
	
	public Point getOffset(Angle angle, int pos) {
		if (seqLeftPts == null) {
			seqLeftPts = new Point[6];
			int i=0;
			for (Point p : seqRightPts) {
				seqLeftPts[i++] = new Point(p.x, p.y);
			}
		}
		switch (angle) {
		case NORD:
			return seqUpPts[pos];
		case SUD:
			return seqDownPts[pos];
		case OUEST:
			return seqRightPts[pos];
		case EST:
			default:
			return seqLeftPts[pos];
		}
	}
	private Sprite spr(int sword, Rotation rot, Reverse rev) {
		return new Sprite(ZildoDescription.SWORD0.getNSpr() + sword, 
				SpriteBank.BANK_ZILDO, rev, rot);
	}
}
