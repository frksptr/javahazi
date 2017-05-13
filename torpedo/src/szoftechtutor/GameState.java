package szoftechtutor;

import java.io.Serializable;

public class GameState implements Serializable {
	// ki jön
	// játékterek állapota
	// satöbbi
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* ki jön épp
	 * TODO: ez alapján letiltaná rakást
	 */
	public boolean serversTurn = true;
	
	// milyen állapot
	public GamePhase gamePhase;
	
	// amit a szerver lát a játékterekbõl
	public GameSpace serverGameSpace = new GameSpace();
	
	// amit a kliens lát a játékterekbõl
	public GameSpace clientGameSpace = new GameSpace();
	
	public enum GamePhase {
		PlacingShips, ShootingShips
	}
}
