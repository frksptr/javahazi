package szoftechtutor;

import java.io.Serializable;

public class GameState implements Serializable {
	// ki jön
	// játékterek állapota
	// satöbbi
	
	/* ki jön épp
	 * TODO: ez alapján letiltaná rakást
	 */
	public boolean serversTurn;
	
	// milyen állapot
	public GamePhase gamePhase;
	
	// amit a szerver lát a játékterekbõl
	public GameSpace serverGameSpace;
	
	// amit a kliens lát a játékterekbõl
	public GameSpace clientGameSPace;
	
	public enum GamePhase {
		PlacingShips, HÁBORÚÚÚÚÚÚ
	}
}
