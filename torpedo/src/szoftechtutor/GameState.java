package szoftechtutor;

import java.io.Serializable;

/**
 * A j�t�k aktu�lis �llapot�t le�r� oszt�ly.
 */
public class GameState implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * �ppen a szerver j�n-e, vagy a kliens.
	 */
	public boolean serversTurn = true;
	
	/**
	 * A j�t�k melyik �llapotban tart.
	 */
	public GamePhase gamePhase = GamePhase.PlacingShips;
	
	
	/**
	 * A szerver �ltal ismert j�t�kt�r.
	 */
	public GameSpace serverGameSpace = new GameSpace();
	

	/**
	 * A kliens �ltal ismert j�t�kt�r. 
	 */
	public GameSpace clientGameSpace = new GameSpace();
	
	/**
	 * A szerver befejezte-e a haj�k lerak�s�t, k�szen �ll-e a j�t�k megkezd�s�re.
	 */
	public boolean serverReady;
	
	/**
	 * A klien befejezte-e a haj�k lerak�s�t, k�szen �ll-e a j�t�k megkezd�s�re. 
	 */
	public boolean clientReady;
	
	/**
	 * A j�t�k lehets�ges �llapotai.
	 */
	public enum GamePhase {
		PlacingShips, ShootingShips
	}
}
