package szoftechtutor;

import java.io.Serializable;

public class GameState implements Serializable {
	// ki j�n
	// j�t�kterek �llapota
	// sat�bbi
	public boolean serversTurn;
	// amit a szerver l�t a j�t�kterekb�l
	public GameSpace serverGameSpace;
	// amit a kliens l�t a j�t�kterekb�l
	public GameSpace clientGameSPace;
}
