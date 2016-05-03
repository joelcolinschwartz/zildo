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

package zeditor.fwk.awt;

import java.awt.BorderLayout;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.util.EventListener;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import org.lwjgl.LWJGLException;

import zildo.Zildo;
import zildo.client.ClientEngineZildo;
import zildo.client.MapDisplay;
import zildo.monde.util.Point;

/**
 * Panel handling map display.<p/>
 * 
 * Provide two scrollbars to navigate through the map. Navigation is done by moving camera in the 
 * {@link MapDisplay} class.
 * 
 * @author tchegito
 *
 */
public class ZildoScrollablePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int viewSizeX=Zildo.screenX;
	public static final int viewSizeY=Zildo.screenY; //+26; // + 32 + 16 - 4 - 2;
	
	ZildoCanvas zildoCanvas;
	MapScrollBar horizontal;
	MapScrollBar vertical;
	
	public ZildoScrollablePanel(String p_defaultMap) throws LWJGLException {
		// Create the panel with two scroll bars, and the canvas
		this.setLayout(new BorderLayout());
		horizontal=new MapScrollBar(JScrollBar.HORIZONTAL);
		vertical=new MapScrollBar(JScrollBar.VERTICAL);
		this.add(horizontal, BorderLayout.SOUTH);
		this.add(vertical, BorderLayout.EAST);
		this.setSize(viewSizeX,viewSizeY);
		zildoCanvas = new ZildoCanvas(this, p_defaultMap);
        zildoCanvas.setSize(viewSizeX,viewSizeY);
		this.add(zildoCanvas);
		
		// Create the mouse listener
		EventListener zildoListener=new ZildoMouseKeyListener(this);
		zildoCanvas.addMouseListener((MouseListener) zildoListener);
		zildoCanvas.addMouseMotionListener((MouseMotionListener) zildoListener);
		zildoCanvas.addKeyListener((KeyListener) zildoListener);
		zildoCanvas.addMouseWheelListener((MouseWheelListener) zildoListener);
	}

	public java.awt.Point getPosition() {
		int x=horizontal.getValue();
		int y=vertical.getValue();
		return new java.awt.Point(x,y);
	}
	
	public void setPosition(java.awt.Point p_point) {
		horizontal.setValue(p_point.x);
		vertical.setValue(p_point.y);
	}

	public java.awt.Point getCameraTranslation() {
		java.awt.Point camera=getPosition();
		camera.x=-16*(camera.x / 16);
		camera.y=-16*(camera.y / 16);
		return camera;
	}
	public ZildoCanvas getZildoCanvas() {
		return zildoCanvas;
	}
	
	public void setZoom(boolean p_zoom) {
		double factor2 = 2f;
		if (!p_zoom) {
			factor2 = 1 / factor2;
		}
		int hv = horizontal.getValue();	// Keep old values
		int vv = vertical.getValue();
		horizontal.setValue(0);	// Identity to avoid getting out of range after changing the extent
		vertical.setValue(0);
		horizontal.getModel().setExtent((int) (horizontal.getModel().getExtent() / factor2));
		vertical.getModel().setExtent((int) (vertical.getModel().getExtent() / factor2));
		// Reset the previous values
		horizontal.setValue(hv);
		vertical.setValue(vv);
	}
	
	public class MapScrollBar extends JScrollBar {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public MapScrollBar(int i) {
			super(i);
			int max;
			int extent;
			if (orientation == JScrollBar.HORIZONTAL) {
				max=64*16;
				extent=viewSizeX;
			} else {
				max=64*16;
				extent=viewSizeY;
			}
			setMinimum(0);
			setMaximum(max);
			BoundedRangeModel m=new DefaultBoundedRangeModel(0, extent, 0, max);
			setModel(m);
			setUnitIncrement(0);
		}
		
		@Override
		public void setValue(int val) {
			int max=getMaximum() - getVisibleAmount();
			if (val > max) {
				val=max;
			}
			if (val < 0) {
				val=0;
			}
			if (val >= 0 && val <= (getMaximum() - getVisibleAmount())) {
				Point p=ClientEngineZildo.mapDisplay.getCamera();
				if (orientation == JScrollBar.HORIZONTAL) {
					p.x=val;
				} else {
					p.y=val;
				}
				ClientEngineZildo.mapDisplay.setCamera(p);
				super.setValue(val);
			}
		}
			
		public void increase() {
			setValue(getValue() + 16);
		}

		public void decrease() {
			setValue(getValue() - 16);
		}
	}
	
	public ZildoCanvas getCanvas() {
		return zildoCanvas;
	}
}
