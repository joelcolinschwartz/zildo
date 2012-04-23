package zildo.client.gui;

import zildo.resource.Constantes;

/**
 * Determine fixed size for various thing (frame, font size) depending on the screen resolution.<p>
 * We assume that viewPortX and viewPortY are defined from Zildo class.
 * @author tchegito
 *
 */
public class ScreenConstant {

	final int SORTY_MAX;
	final int SORTY_REALMAX;
	final int TEXTER_COORDINATE_X;
	final int TEXTER_COORDINATE_Y;
	final int TEXTER_SIZEX;
	final int TEXTER_MENU_SIZEY;
	final int TEXTER_SIZESPACE;
	final int TEXTER_NUMLINE = Constantes.TEXTER_NUMLINE;	// Unchanged for now
	final int TEXTER_SIZELINE = Constantes.TEXTER_SIZELINE;	// Idem
	
	public ScreenConstant(int screenX, int screenY) {
		SORTY_MAX = Constantes.SORTY_MAX + screenY + 40;
		SORTY_REALMAX = Constantes.SORTY_REALMAX + screenY + 40 + 80;
		
		float ratioX = screenX / 320;
		float ratioY = screenY / 240;
		
		// Texter
		TEXTER_COORDINATE_X= (int) (Constantes.TEXTER_COORDINATE_X * ratioX); 
		TEXTER_COORDINATE_Y= (int) (Constantes.TEXTER_COORDINATE_Y * ratioY); 
		TEXTER_SIZEX= (int) (Constantes.TEXTER_SIZEX * ratioX); 
		TEXTER_MENU_SIZEY= (int) (Constantes.TEXTER_MENU_SIZEY * ratioY); 
		TEXTER_SIZESPACE= (int) (Constantes.TEXTER_SIZESPACE * ratioX); 		
	}
}