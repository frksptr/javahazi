package szoftechtutor;

import java.io.Serializable;

public class GameState implements Serializable {
	// ki j�n
	// j�t�kterek �llapota
	// sat�bbi
	
	/* ki j�n �pp
	 * TODO: ez alapj�n letiltan� rak�st
	 */
	public boolean serversTurn;
	
	// milyen �llapot
	public GamePhase gamePhase;
	
	// amit a szerver l�t a j�t�kterekb�l
	public GameSpace serverGameSpace;
	
	// amit a kliens l�t a j�t�kterekb�l
	public GameSpace clientGameSPace;
	
	public enum GamePhase {
		PlacingShips, H�BOR������
	}
}
