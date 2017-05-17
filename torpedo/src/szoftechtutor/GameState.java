package szoftechtutor;

import java.io.Serializable;

/**
 * A játék aktuális állapotát leíró osztály.
 */
public class GameState implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Éppen a szerver jön-e, vagy a kliens.
	 */
	public boolean serversTurn = true;
	
	/**
	 * A játék melyik állapotban tart.
	 */
	public GamePhase gamePhase = GamePhase.PlacingShips;
	
	
	/**
	 * A szerver által ismert játéktér.
	 */
	public GameSpace serverGameSpace = new GameSpace();
	

	/**
	 * A kliens által ismert játéktér.
	 */
	public GameSpace clientGameSpace = new GameSpace();
	
	/**
	 * A szerver befejezte-e a hajók lerakását, készen áll-e a játék megkezdésére.
	 */
	public boolean serverReady;
	
	/**
	 * A klien befejezte-e a hajók lerakását, készen áll-e a játék megkezdésére.
	 */
	public boolean clientReady;
	
	/**
	 * A j�t�k lehetséges állapotai.
	 */
	public enum GamePhase {
		PlacingShips, ShootingShips
	}
}
