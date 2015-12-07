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

package zeditor.tools.sprites;

import java.util.Arrays;

import zeditor.tools.tiles.GraphChange;
import zildo.monde.util.Zone;

/**
 * @author Tchegito
 *
 */
public class Pnj3 extends SpriteBanque {

	public Pnj3() {
		zones=new Zone[] {
				
		    // Falcor
		    new Zone(162, 235, 16, 21), new Zone(147, 235, 15, 22),
		    new Zone(178, 235, 13, 24), new Zone(192, 235, 14, 25),
		    new Zone(222, 235, 16, 22), new Zone(206, 235, 16, 23),
		    new Zone(238, 235, 14, 14),
		    // Bramble
		    new Zone(253, 237, 16, 16),
            // Flying serpent
		    /*
		    new Zone(122, 313, 27, 25),
		    new Zone(122, 313, 27, 25),
		    new Zone(122, 313, 27, 25),
		    */
            //new Zone(34, 265, 9, 15), new Zone(43, 265, 9, 15), new Zone(52, 265, 9, 14),
		    new Zone(34, 265, 14, 15), new Zone(48, 265, 14, 15), new Zone(62, 265, 14, 15),
            // Fisher
		    new Zone(147, 261, 18, 23), new Zone(165, 261, 16, 24),
		    new Zone(181, 261, 14, 24), new Zone(195, 261, 14, 24),
		    // Fish
		    new Zone(294, 21, 19, 8), new Zone(294, 30, 19, 8),
		    new Zone(294, 39, 19, 10), new Zone(295, 50, 20, 10),
		    // Igor
		    new Zone(211, 259, 16, 26), new Zone(228, 258, 16, 27), new Zone(245, 258, 16, 27),
		    new Zone(263, 258, 12, 27), new Zone(276, 257, 14, 28), new Zone(292, 257, 12, 28),
		    new Zone(270, 230, 16, 26), new Zone(287, 229, 16, 27), new Zone(304, 229, 16, 27),
		    // Louise
		    new Zone(67, 307, 14, 18), new Zone(81, 307, 14, 18),
		    new Zone(95, 306, 12, 19), new Zone(107, 307, 12, 18),
		    new Zone(119, 307, 14, 18), new Zone(133, 307, 14, 18),
		    // Fish under water
		    new Zone(295, 60, 18, 7),
		    // Minsk
		    new Zone(109, 86, 16, 24), new Zone(125, 86, 16, 24),
		    new Zone(143, 86, 14, 24), new Zone(157, 85, 14, 25),
		    // Inventor
		    new Zone(109, 111, 16, 25), new Zone(126, 111, 15, 25),
		    new Zone(141, 111, 15, 25), new Zone(156, 110, 15, 26),
		    // Cook
		    new Zone(113, 326, 16, 25), new Zone(129, 327, 16, 24),
		    new Zone(145, 326, 13, 25), new Zone(158, 327, 14, 24),
		    // Big rat
		    new Zone(0, 351, 15, 17), new Zone(15, 351, 15, 17),
		    new Zone(30, 353, 25, 15), new Zone(55, 353, 27, 15),
		    new Zone(82, 351, 15, 17), new Zone(97, 349, 15, 19),
		    new Zone(112, 352, 25, 16),	// (biting)
		    // Squirrel (princess)
		    new Zone(1, 0, 11, 12), // South static
		    new Zone(15, 0, 13, 13), new Zone(30, 0, 13, 13), new Zone(46, 0, 13, 13),	// South
		    new Zone(2, 15, 12, 12),	// East static 
		    new Zone(15, 15, 14, 12), new Zone(31, 15, 13, 12), new Zone(46, 15, 13, 12),
		    new Zone(1, 28, 11, 12), // North static
		    new Zone(15, 28, 13, 13), new Zone(30, 28, 13, 13), new Zone(46, 28, 13, 13),	// South
		    // Dragon (66)
		    new Zone(0, 0, 51, 93), new Zone(51, 0, 45, 44), new Zone(96, 0, 51, 32),
		    new Zone(147, 0, 45, 33), new Zone(227,0, 11, 30), new Zone(192, 30, 62, 50), new Zone(192, 80, 62, 93),
		    new Zone(0,93, 63, 95), new Zone(71, 80, 50, 96), new Zone(262, 107, 50, 88),
		    // Bitey (76)
		    new Zone(0, 0, 21, 27), new Zone(25, 0, 21, 27), new Zone(50, 0, 21, 27),
		    new Zone(75, 0, 21, 27), new Zone(100, 0, 21, 27), new Zone(125, 0, 21, 27),
		    new Zone(150, 0, 21, 27),
		    // Turtle (83)
		    new Zone(1, 197, 22, 13), new Zone(28, 197, 22, 13),	// Rising
		    new Zone(55, 196, 22, 14), new Zone(82, 195, 22, 15),
		    //new Zone(0, 214, 27, 15), new Zone(27, 214, 27, 15),	// Forward
		    //new Zone(54, 215, 27, 14), new Zone(81, 214, 27, 15),
		    new Zone(1, 212, 27, 16), new Zone(30, 212, 26, 16),
		    new Zone(57, 211, 27, 17), new Zone(86, 212, 28, 16),
		    new Zone(105, 193, 5, 5), new Zone(105, 199, 5, 5), new Zone(105, 205, 5, 5)	// Head popping out
		};
		
		pkmChanges = Arrays.asList(new GraphChange[] { new GraphChange("pnj2", 0, 0), 
				new GraphChange("squirrel", 54, 0),
				new GraphChange("dragonpal", 66,0, true),
				new GraphChange("bitey", 76, 0, true),
				new GraphChange("squirrel", 83, 0),
				new GraphChange("dragonpal", 87, 0, true)
				});
	}
}
