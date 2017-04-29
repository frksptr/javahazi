package szoftechtutor;

import java.io.Serializable;

public class GameState implements Serializable {
	// ki jön
	// játékterek állapota
	// satöbbi
	public boolean serversTurn;
	// amit a szerver lát a játékterekbõl
	public GameSpace serverGameSpace;
	// amit a kliens lát a játékterekbõl
	public GameSpace clientGameSPace;
}
