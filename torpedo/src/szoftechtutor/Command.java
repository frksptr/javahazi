package szoftechtutor;

import java.awt.Point;
import java.io.Serializable;

import szoftechtutor.Control.NetworkType;

/**
 * A szerver logikája és a játszó felek közti utasítások leírását tartalmazza.
 */
public class Command implements Serializable {
	private static final long serialVersionUID = 1L;

	
	/**
	 * Amennyiben hajólerakás, vagy lövés történt, annak pozíciója.
	 */
	public Point position;
	
	/**
	 * Az utasítás típusa.
	 */
	public CommandType commandType;
	
	/**
	 * Az utasítás küldõje. 
	 */
	public NetworkType commandOrigin;
	public boolean ready = false;
	

	/**
	 * Az utasítás lehetséges típusai.
	 */
	public enum CommandType {
		Shot,
		PlacedShip,
		Ready,
		Reset
	}
	
}
