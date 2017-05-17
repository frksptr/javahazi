package szoftechtutor;

import java.io.Serializable;
import szoftechtutor.Ship;

/**
 * A j�t�kt�r �llapot�t le�r� oszt�ly.
 */
public class GameSpace implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * A j�t�kos saj�t j�t�ktere.
	 */
	public CellType[][] ownTable = new CellType[10][10];
	
	/**
	 * A j�t�kos inform�ci�i az ellenfele j�t�kter�r�l.
	 */
	public CellType[][] enemyTable = new CellType[10][10];
	
	/**
	 *  A j�t�kos saj�t cell�hoz tartoz� haj�k.
	 */
	public Ship[][] ownCellsToShip = new Ship[10][10];
	/**
	 * A j�t�kos ellenfele cell�ihoz tartoz� haj�k.
	 */
	public Ship[][] enemyCellsToShip = new Ship[10][10];
	
	/**
	 * Saját állapotot leíró üzenet.
	 */
	String ownText = null;
	
	/**
	 * Az ellenf�l �llapot�t le�r� �zenet. 
	 */
	String enemyText = null;
	
	/**
	 * ownText megjelen�tend�-e.
	 */
	boolean ownText_f = false;
	/**
	 * enemyText megjelen�tend�-e.
	 */
	boolean enemyText_f = false;
	
	
	/**
	 * J�t�kos haj�ira vonatkoz� inform�ci�k.
	 */
	public ShipFlags ownShips = new ShipFlags();
	/**
	 * Az ellenf�l haj�ira vonatkoz� inform�ci�k.
	 */
	public ShipFlags enemyShips = new ShipFlags();
	public int allShips = 15;
	
	public class ShipFlags implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public int id=0;
		public int shipElements = 35;
		public int shotShipElements = 35;
		public int[] placedShips = {0,5,4,3,2,1};  // index szerint: 1 elemu->5 db, stb
		public int[] shotShips = {0,0,0,0,0,0};
	}
	
	/**
	 * A j�t�kt�r initcializ�ci�ja. Az ellenf�l mez�ir�l alapesetben nem rendelkez�nk inform�ci�val, a j�t�kos mezei pedig v�zk�nt szereplnek.
	 */
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
		for (int col = 0; col < 10; col++) {
			for (int row = 0; row < 10; row++) {
				ownCellsToShip[col][row] = new Ship();
				enemyCellsToShip[col][row] = new Ship();
			}
		}
	}
}