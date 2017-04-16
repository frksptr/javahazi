package Messages;

import java.awt.Point;

import util.CellType;

public class TypeMessage extends GameMessage {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8342539930805948505L;
	private Point position;
	private CellType type;
	
	public TypeMessage(Point p, CellType t) {
		position = p;
		type = t;
	}
	
	public Point getPosition() {
		return position;
	}
	public CellType getType() {
		return type;
	}
}
