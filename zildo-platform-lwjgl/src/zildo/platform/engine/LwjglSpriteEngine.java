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

package zildo.platform.engine;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import zildo.client.ClientEngineZildo;
import zildo.fwk.gfx.EngineFX;
import zildo.fwk.gfx.engine.SpriteEngine;
import zildo.fwk.gfx.engine.TextureEngine;
import zildo.monde.util.Vector3f;
import zildo.monde.util.Vector4f;

// SpriteEngine.cpp: implementation of the SpriteEngine class.
//
//////////////////////////////////////////////////////////////////////





public class LwjglSpriteEngine extends SpriteEngine {
    
	public LwjglSpriteEngine(TextureEngine texEngine) {
		this.textureEngine = texEngine;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////
	// render
	///////////////////////////////////////////////////////////////////////////////////////
	// Draw every sprite's primitives
	///////////////////////////////////////////////////////////////////////////////////////
	// IN: true=render BACKground
	//	   false=render FOREground
	///////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void render(boolean backGround) {
	
		// Display every sprites
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		float[] color=textureEngine.graphicStuff.getFloat(GL11.GL_CURRENT_COLOR, 4);

		Vector3f ambient=ClientEngineZildo.ortho.getAmbientColor();
		if (ambient != null) {
			color[0]=ambient.x;
			color[1]=ambient.y;
			color[2]=ambient.z;
		}
		// Respect order from bankOrder
		boolean endSequence=false;
		int posBankOrder=0;
	
		// Retrieve the sprite's order
		int[][] bankOrder = ClientEngineZildo.spriteDisplay.getBankOrder();
		
		int phase=(backGround)?0:1;
		while (!endSequence) {
			int numBank=bankOrder[phase][posBankOrder*4];
			if (numBank == -1) {
				endSequence=true;
			} else {
				// Render the n sprites from this bank
				int nbQuads=bankOrder[phase][posBankOrder*4 + 1];
				int iCurrentFX=bankOrder[phase][posBankOrder*4 + 2];
				int alpha=bankOrder[phase][posBankOrder*4 + 3];
				EngineFX currentFX=EngineFX.values()[iCurrentFX];
				int texId=textureEngine.getNthTexture(numBank);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);

				// Select the right pixel shader (if needed)
                if (pixelShaderSupported) {
                	switch (currentFX) {
                	case NO_EFFECT:
						ARBShaderObjects.glUseProgramObjectARB(0);
						break;
                	case PERSO_HURT:
						// A sprite has been hurt
						ARBShaderObjects.glUseProgramObjectARB(ClientEngineZildo.pixelShaders.getPixelShader(1));
						ClientEngineZildo.pixelShaders.setParameter(1, "randomColor", new Vector4f((float) Math.random(), (float) Math.random(), (float) Math.random(), 1));
						break;
					default:
						if (currentFX.needPixelShader()) {
							// This is a color replacement, so get the right ones
							Vector4f[] tabColors=ClientEngineZildo.pixelShaders.getConstantsForSpecialEffect(currentFX);
		
							// And enable the 'color replacement' pixel shader
							ARBShaderObjects.glUseProgramObjectARB(ClientEngineZildo.pixelShaders.getPixelShader(0));
							ClientEngineZildo.pixelShaders.setParameter(0, "Color1", tabColors[2]);
							ClientEngineZildo.pixelShaders.setParameter(0, "Color2", tabColors[3]);
							ClientEngineZildo.pixelShaders.setParameter(0, "Color3", tabColors[0].scale(color[0]));
							ClientEngineZildo.pixelShaders.setParameter(0, "Color4", tabColors[1].scale(color[0]));
						} else {
							ARBShaderObjects.glUseProgramObjectARB(0);
						}
                	}
                }
                switch (currentFX) {
	                case SHINY:
	                    GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE); // _MINUS_SRC_ALPHA);
	                    GL11.glColor4f(1, (float) Math.random(), 0, (float) Math.random());
	                    break;
	                case QUAD:
	                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	                    GL11.glColor4f(0.5f + 0.5f * (float) Math.random(), 0.5f * (float) Math.random(), 0, 1);
	                    break;
	                case FOCUSED:
	                	GL11.glColor3f(1.0f, 1.0f, 1.0f);
	                	break;
	                default:
	                	color[3]=alpha / 255.0f;
	                	textureEngine.graphicStuff.setCurrentColor(color);
	                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                }
				meshSprites[numBank].render(nbQuads);
				posBankOrder++;
			}
		}

		// Deactivate pixel shader
		if (pixelShaderSupported) {
			ARBShaderObjects.glUseProgramObjectARB(0);
		}
		GL11.glDisable(GL11.GL_BLEND);
	}

}