package szoftechtutor;

import java.awt.Point;
import java.io.Serializable;

public class Command implements Serializable {
	public Point position;
	public CommandType commandType;
	public CommandOrigin commandOrigin;
	
	// Mi történt
	public enum CommandType {
		Shot,
		PlacedShip,
		Nemtommi,
		Ready
	}
	
	// Ki küldi
	public enum CommandOrigin {
		Server,
		Client
	}
}
