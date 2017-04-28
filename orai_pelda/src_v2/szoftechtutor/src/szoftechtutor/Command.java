package szoftechtutor;

import java.awt.Point;
import java.io.Serializable;

public class Command implements Serializable {
	public Point positionShot;
	public CommandType CommandType;
	
	public enum CommandType {
		Shot,
		Nemtommi
	}
}
