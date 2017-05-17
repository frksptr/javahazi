package szoftechtutor;

import java.io.Serializable;

/**
 * A jatek aktualis allapotat leiro osztaly.
 */
public class GameState implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Eppen a szerver jon-e, vagy a kliens.
	 */
	public boolean serversTurn = true;
	
	/**
	 * A jatek melyik allapotban tart.
	 */
	public GamePhase gamePhase = GamePhase.PlacingShips;
	
	
	/**
	 * A szerver altal ismert jatekter.
	 */
	public GameSpace serverGameSpace = new GameSpace();
	

	/**
	 * A kliens altal ismert jatekter. 
	 */
	public GameSpace clientGameSpace = new GameSpace();
	
	/**
	 * A szerver befejezte-e a hajok lerakasat, keszen all-e a jatek megkezdesere.
	 */
	public boolean serverReady;
	
	/**
	 * A klien befejezte-e a hajok lerakasat, keszen all-e a jatek megkezdesere. 
	 */
	public boolean clientReady;
	
	/**
	 * A jatek lehetseges allapotai.
	 */
	public enum GamePhase {
		PlacingShips, ShootingShips
	}
}
