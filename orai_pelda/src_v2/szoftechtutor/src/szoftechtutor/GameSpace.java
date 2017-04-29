package szoftechtutor;

import java.io.Serializable;

public class GameSpace implements Serializable {
	public CellType[][] ownTable;
	public CellType[][] enemyTable;
	
	public GameSpace() {
		/* el�sz�r nem tudjuk, hogy az ellenf�l t�rfel�n milyen
		 * mez�k vannak
		 */
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; i++) {
				enemyTable[i][j] = CellType.Unknown;
			}
		}
	}

}
