package Messages;

import java.awt.Point;

public class PositionMessage extends GameMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -933285055107934662L;
	private Point position;
	
	public PositionMessage(Point p) {
		this.position = p;
	}
	
	public Point getPosition() {
		return position;
	}
}
