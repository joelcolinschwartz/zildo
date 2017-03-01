/**
 * The Land of Alembrum
 * Copyright (C) 2006-2016 Evariste Boussaton
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

package zildo.monde.sprites.persos.boss;

import zildo.monde.Bezier3;
import zildo.monde.sprites.Reverse;
import zildo.monde.sprites.desc.PersoDescription;
import zildo.monde.sprites.desc.SpriteAnimation;
import zildo.monde.sprites.elements.Element;
import zildo.monde.sprites.elements.ElementProjectile;
import zildo.monde.sprites.persos.Perso;
import zildo.monde.sprites.persos.PersoNJ;
import zildo.monde.sprites.utils.CompositeElement;
import zildo.monde.sprites.utils.MouvementPerso;
import zildo.monde.util.Pointf;
import zildo.monde.util.Vector2f;
import zildo.server.EngineZildo;

/**
 * @author Tchegito
 *
 */
public class PersoDragon extends PersoNJ {

	CompositeElement neck;
	
	double gamma;
	
	int cnt = 1;
	
	// 4, 5, 6, 7 : wing
	int[] seq = {3, 3, 2, 2, 1, 0, 4, 5, 6, 7, 4, 5, 6, 7};
	
	// 7 :arm
	// Interval during dragon wait to look for Zildo again
	final static int FOCUS_TIME = 15;
	// Circle radius around dragon's head can move
	final static int HEAD_ELONGATION = 30;
	
	ElementProjectile fireBalls;
	boolean shapeInitialized = false;
	
	boolean spittingFire = true;
	
	Perso focusedEnemy;
	
	public PersoDragon(int x, int y) {
		//this.x = x;
		//this.y = y;
		pv = 5;
		setInfo(PersoInfo.ENEMY);
		
		desc = PersoDescription.DRAGON;
		neck = new CompositeElement(this);
	}
	
	@Override
	public void animate(int compteur_animation) {
		if (!shapeInitialized) {
			neck.lineShape(seq.length);
			shapeInitialized = true;
			// Make head and upper wing parts higher
			/*
			neck.elems.get(6).floor++;
			neck.elems.get(7).floor++;
			neck.elems.get(8).floor++;
			neck.elems.get(11).floor++;
			neck.elems.get(12).floor++;
			*/
			// Name each part (to be able to use them in scripts)
			int nbSprite = 0;
			/*
			for (Element element : neck.elems) {
				element.setName("s" + (nbSprite++) );
			}
			*/
		}

		super.animate(compteur_animation);
		
		int nth=0;
		double beta = gamma;
		double iota = 0;
		float xx=x, yy=y, zz=z; 
		
		
		Pointf neckPoint = new Pointf((float) (x + 10*Math.cos(gamma/5)), 
										(float) ( 60 + 4*Math.sin(gamma*2)) );
		Pointf headPoint = new Pointf((float) (x + 0*10*Math.sin(gamma)),
									(float) (neckPoint.y + 5*0 + 0*5*Math.cos(gamma)) );
		
		// 1) Detect Zildo every FOCUS_TIME
		Perso zildo = null;
		if (cnt % FOCUS_TIME == 0) {
			zildo = EngineZildo.persoManagement.lookForOne(this, 10, PersoInfo.ZILDO, false);
			if (zildo != null) {
				Vector2f dist = new Vector2f(headPoint, new Pointf(zildo.x, zildo.y));
				float norme = dist.norm();
				float ratio = HEAD_ELONGATION / norme;
				headPoint.add(new Vector2f(2, 0)); //dist.mul(ratio));
				//System.out.println(headPoint);
			}
			focusedEnemy = zildo;
		}
		
		Bezier3 bz = new Bezier3(new Pointf(x, 0), neckPoint, headPoint);
		
		vz += az;
		z += vz;

		
		//System.out.println("az="+az+" vz="+vz+"z="+z);
		
		int addX =0;
		if (getQuel_deplacement() == MouvementPerso.RETRACTED) {
			addX += 20;
		}
		for (int i=0;i<neck.elems.size()-1;i++) {
			Element e = neck.elems.get(i+1);
			e.setAddSpr(seq[i]);
			if (i >= 6) {	// Wings
				e.x = neck.elems.get(3).x - 40;
				e.y = neck.elems.get(3).y+10-120;// + 100;
				e.z = neck.elems.get(3).z;// + 30; //60;// + 100; // - 40;
				int factor = 1;
				if (i == 10 || i == 11 || i == 12 || i == 13) {	// Reversed wings
					e.x = e.x + 80;
					e.reverse = Reverse.HORIZONTAL;
					factor = -1;
				}
				if (i == 6 || i == 10) {
					e.x += 9;
					if (i == 10) e.x -= 19;
					e.x += addX * factor;
				} else if (i==7 || i == 11) {
					e.y += 50;
					e.x -= 7 * factor;
					e.x += addX * factor;
				} else if (i == 8 || i == 12) {
					e.y += 50+29;
					e.x -= 14 * factor;
					e.x += addX * factor;
				} else if (i==9 || i == 13) {
					e.y += 50 + 29 + 60;
					// Shift Y and Z so that dragon's arm is displayed before head
					e.y -= 30;
					e.z -= 30;
					e.x += addX * factor;
				}
			} else {
				Pointf interpolated = bz.interpol(i / 5f);
				e.x = interpolated.x;
				e.z = interpolated.y + z;
				//e.x = xx + (float) (3 * Math.cos(beta * 0.7) + 12 * Math.sin(iota));
				//e.z = zz + 20 - (float) (2 * Math.sin(beta) + 2 * Math.cos(iota));
				if (i == 5) {	// Head
					/*
					e.z -= 30;
					e.x -= 10;
					*/
					//e.setForeground(true);
					// Mesure distance with center
					float shiftHead = neck.elems.get(2).x - e.x;
					if (focusedEnemy != null) {
						shiftHead = focusedEnemy.x - e.x;
					}
					//System.out.println(shiftHead);
					if (shiftHead < -12) {	// Head looking left
						e.setAddSpr(0);
						e.reverse = Reverse.NOTHING;
					} else if (shiftHead > 12) {	// Head looking right
						e.setAddSpr(0);
						e.reverse = Reverse.HORIZONTAL; 
							//neckPoint.x < headPoint.x ? Reverse.HORIZONTAL : Reverse.NOTHING;
					} else  {
						e.setAddSpr(9);
						yy+=10;
					}
					if (quel_deplacement == MouvementPerso.SPITFIRE) {
						if (e.getAddSpr() == 0) {
							e.setAddSpr(8);
						} else if (e.getAddSpr() == 9) {
							e.setAddSpr(10);
						} else {
							e.setAddSpr(8);
						}
					}
				}
				e.y = yy;
				//e.z = zz + 6;
			}
			xx = e.x;
			yy = e.y;
			zz = e.z;
			beta += 0.01;
			iota += 0.001;
			nth++;
		}
		neck.elems.get(1).visible = false;
		//refElement.setAddSpr(1);
		visible = false;
		
		spittingFire = (cnt % 150) < 20;
		
		if (cnt++ % 150 == 0) {
			Element h = neck.elems.get(6);
			Element elem = EngineZildo.spriteManagement.spawnSpriteGeneric(SpriteAnimation.SEWER_SMOKE,
					(int) h.x, (int) h.y,
					0, 2, this, null);
			elem.z = h.z + 10;
			elem.setForeground(true);
		}
		gamma += 0.08;
	}
	
	@Override
	public void beingWounded(float cx, float cy, Perso p_shooter, int p_damage) {
		// For now, no collision can hit the dragon
		// super.beingWounded(cx, cy, p_shooter, p_damage);
	}
}
