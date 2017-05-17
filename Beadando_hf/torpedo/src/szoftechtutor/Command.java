package szoftechtutor;

import java.awt.Point;
import java.io.Serializable;

import szoftechtutor.Control.NetworkType;

/**
 * A szerver logikaja es a jatszo felek kozti utasitasok leirasat tartalmazza.
 */
public class Command implements Serializable {
	private static final long serialVersionUID = 1L;

	
	/**
	 * Amennyiben hajolerakas, vagy loves tortent, annak pozicioja.
	 */
	public Point position;
	
	/**
	 * Az utasitas tipusa.
	 */
	public CommandType commandType;
	
	/**
	 * Az utasitas kuldoje. 
	 */
	public NetworkType commandOrigin;
	public boolean ready = false;
	

	/**
	 * Az utasitas lehetseges tipusai.
	 */
	public enum CommandType {
		Shot,
		PlacedShip,
		Ready,
		Reset
	}
	
}
