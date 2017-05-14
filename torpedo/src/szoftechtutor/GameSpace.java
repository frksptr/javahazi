package szoftechtutor;

import java.io.Serializable;

public class GameSpace implements Serializable {
	public CellType[][] ownTable = new CellType[10][10];
	public CellType[][] enemyTable = new CellType[10][10];
	public int ownShip = 10;
	public int enemyShip = 10;

	public GameSpace() {
		/* el�sz�r nem tudjuk, hogy az ellenf�l t�rfel�n milyen
		 * mez�k vannak
		 */
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				enemyTable[i][j] = CellType.Unknown;
			}
		}
		
		/*
		 * Saj�t mezeinken el�sz�r v�z van
		 */
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				ownTable[i][j] = CellType.Water;
			}
		}
	}

}
