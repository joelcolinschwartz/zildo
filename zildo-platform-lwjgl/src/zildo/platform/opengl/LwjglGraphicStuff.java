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

package zildo.platform.opengl;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import zildo.fwk.gfx.GraphicStuff;
import zildo.fwk.opengl.compatibility.VBO;
import zildo.monde.util.Vector4f;
import zildo.platform.opengl.compatibility.FBOHardware;
import zildo.platform.opengl.compatibility.FBOSoftware;
import zildo.platform.opengl.compatibility.VBOHardware;
import zildo.platform.opengl.compatibility.VBOSoftware;

public class LwjglGraphicStuff extends GraphicStuff {


    public LwjglGraphicStuff() {
        if (isFBOSupported()) {
            fbo = new FBOHardware();
        } else {
            fbo = new FBOSoftware();
        }
        
        vbo = createVBO();
    }
    
    @Override
	public VBO createVBO() {
    	VBO tempVbo;
        if (isVBOSupported()) {	// Don't use VBO now, it's slower than nothing !
        	tempVbo = new VBOHardware();
        } else {
        	tempVbo = new VBOSoftware();
        }
        return tempVbo;
    }
    /**
     * VBO support.
     * @return TRUE if the current hardware supports VBO.
     */
    protected boolean isVBOSupported() {
        return false; //(GLContext.getCapabilities().GL_ARB_vertex_buffer_object);
    }

    // ///////////////////////////////////////////////////////////////////////////////////////////////////////
    // FBO utils
    // ///////////////////////////////////////////////////////////////////////////////////////////////////////
    // Frame Buffer Object : provide an offscreen render, which can be used further as a texture.
    // ///////////////////////////////////////////////////////////////////////////////////////////////////////
    protected boolean isFBOSupported() {
        return GLContext.getCapabilities().GL_EXT_framebuffer_object;
    }

    // //////////////////////////////////////////////
    // Texture utils
    // //////////////////////////////////////////////
    // color: TRUE=color texture / FALSE=depth texture
    @Override
	public int generateTexture(int sizeX, int sizeY) {
        int textureId = GLUtils.generateTexture(sizeX, sizeY);

        logger.info("Created texture " + textureId);
        return textureId;
    }

    @Override
	public void cleanTexture(int id) {
        GLUtils.cleanTexture(id);
        logger.info("Deleted texture " + id);
    }
	
	static FloatBuffer floatBuffer = null;

	/**
	 * Get the current color set with glColor4f
	 * @param p_info
	 * @param p_size
	 * @return float[]
	 */
	@Override
	public float[] getFloat(int p_info,int p_size) {
		if (floatBuffer == null) {
			floatBuffer=BufferUtils.createFloatBuffer(16);
		}
		GL11.glGetFloat(p_info, floatBuffer);
		
		float[] temp=new float[p_size];
		floatBuffer.get(temp);
		floatBuffer.position(0);
		return temp;
	}
	
	
	/**
	 * Set the current color from a float array.
	 * @param p_color
	 */
	@Override
	public void setCurrentColor(float[] p_color) {
		GL11.glColor4f(p_color[0], p_color[1], p_color[2], p_color[3]);		
	}

    public Vector4f createColor64(float r, float g, float b) {
        return new Vector4f(r / 63.0f, g / 63.0f, b / 63.0f, 1.0f);
    }
}
