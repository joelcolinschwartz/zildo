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

package zildo.fwk.opengl.compatibility;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import zildo.fwk.ZUtils;

/**
 * Simple class being a composition of all buffers attached to a mesh. We provide here :<ul>
 * <li>vertice buffer (default shared)</li>
 * <li>texture buffer (default shared)</li>
 * <li>indice buffer (never shared)</li>
 * </ul>
 * Here, <i>sharing</i> means that a second call to this class' constructor will reuse the last
 * buffer of same kind.<br/>
 * <b>Important:</b> texture buffer can be reallocated by calling {@link #resetTextureBuffer()} if
 * you don't want to have two meshes sharing the same buffer.
 * @author Tchegito
 *
 */
public class VBOBuffers {
	
	public int vertexBufferId;
	public int textureBufferId;
	public int indiceBufferId;

    public ShortBuffer vertices;
    public FloatBuffer textures;

    public VBOBuffers(int p_numPoints, boolean p_forTiles) {
        // Allocate buffers
    	// 3 coordinates (x,y) x 3 dots in a triangle
        vertices = ZUtils.createShortBuffer(2 * 3 * (p_numPoints / 2));
        // 2 coordinates (u,v)   x 3 dots in a triangle
        textures = ZUtils.createFloatBuffer(2 * 3 * (p_numPoints / 2));    	
    }

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("Vertices : [");
		for (int i=0;i<vertices.limit();i++) {
			sb.append(vertices.get(i)+", ");
		}
		sb.append("]\nTextures : [");
		for (int i=0;i<textures.limit();i++) {
			sb.append(textures.get(i)+", ");
		}
		sb.append("]");
		return sb.toString();
	}
}
