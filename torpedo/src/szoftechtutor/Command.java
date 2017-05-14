package szoftechtutor;

import java.awt.Point;
import java.io.Serializable;

import szoftechtutor.Control.NetworkType;

public class Command implements Serializable {
	public Point position;
	public CommandType commandType;
	public NetworkType commandOrigin;
	public boolean ready = false;
	
	// Mi történt
	public enum CommandType {
		Shot,
		PlacedShip,
		Ready,
		Reset
	}
	
	// Ki küldi
	public enum CommandOrigin {
		Server,
		Client
	}
}
