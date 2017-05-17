package szoftechtutor;

import java.awt.Point;
import java.io.Serializable;

import szoftechtutor.Control.NetworkType;

/**
 * A szerver logik�ja �s a j�tsz� felek k�zti utas�t�sok le�r�s�t tartalmazza.
 */
public class Command implements Serializable {
	private static final long serialVersionUID = 1L;

	
	/**
	 * Amennyiben haj�lerak�s, vagy l�v�s t�rt�nt, annak poz�ci�ja.
	 */
	public Point position;
	
	/**
	 * Az utas�t�s t�pusa.
	 */
	public CommandType commandType;
	
	/**
	 * Az utas�t�s k�ld�je. 
	 */
	public NetworkType commandOrigin;
	public boolean ready = false;
	

	/**
	 * Az utas�t�s lehets�ges t�pusai.
	 */
	public enum CommandType {
		Shot,
		PlacedShip,
		Ready,
		Reset
	}
	
}
