package szoftechtutor;

import java.io.Serializable;

public class GameState implements Serializable {
	// ki j�n
	// j�t�kterek �llapota
	// sat�bbi
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* ki j�n �pp
	 * TODO: ez alapj�n letiltan� rak�st
	 */
	public boolean serversTurn = true;
	
	// milyen �llapot
	public GamePhase gamePhase;
	
	// amit a szerver l�t a j�t�kterekb�l
	public GameSpace serverGameSpace = new GameSpace();
	
	// amit a kliens l�t a j�t�kterekb�l
	public GameSpace clientGameSpace = new GameSpace();
	
	public enum GamePhase {
		PlacingShips, ShootingShips
	}
}
