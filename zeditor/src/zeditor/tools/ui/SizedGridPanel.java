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

package zeditor.tools.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

import zeditor.core.tiles.SpriteSet;

/**
 * Subclasses JPanel to obtain a sized grid one, where cells can have a different size between each other.
 * <p/>
 * Default is : 1/3 for left cell, and 2/3 for right.
 * @author Tchegito
 *
 */
@SuppressWarnings("serial")
public class SizedGridPanel extends JPanel {

	/**
	 * @param p_numLines number of rows
	 * @param p_vGap space between each row
	 */
	public SizedGridPanel(int p_numLines, int p_vGap) {
		GridLayout l = new GridLayout(p_numLines, 1);
		l.setVgap(p_vGap);
		setLayout(l);
	}
	
	public void addComp(Component p_compLeft, Component p_compRight) {
		Dimension d = p_compRight.getPreferredSize();

		JPanel currentPanel = new JPanel();
		BorderLayout layout = new BorderLayout();
		layout.setHgap(10);
		currentPanel.setLayout(layout);
		p_compRight.setPreferredSize(new Dimension(2 * SpriteSet.width / 3,
				d.height));

		currentPanel.add(p_compLeft, BorderLayout.WEST);
		currentPanel.add(p_compRight, BorderLayout.EAST);

		add(currentPanel);
	}
}