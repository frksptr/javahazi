package szoftechtutor;

import java.awt.Point;
import java.io.Serializable;

import szoftechtutor.Control.NetworkType;

public class Command implements Serializable {
	public Point position;
	public CommandType commandType;
	public NetworkType commandOrigin;
	public boolean ready = false;
	
	// Mi t�rt�nt
	public enum CommandType {
		Shot,
		PlacedShip,
		Ready,
		Reset
	}
	
	// Ki k�ldi
	public enum CommandOrigin {
		Server,
		Client
	}
}
