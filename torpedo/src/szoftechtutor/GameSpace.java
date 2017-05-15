package szoftechtutor;

import java.io.Serializable;
import szoftechtutor.Ship;

public class GameSpace implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public CellType[][] ownTable = new CellType[10][10];
	public CellType[][] enemyTable = new CellType[10][10];
	public Ship[][] ownCellsIsShip = new Ship[10][10];
	public Ship[][] enemyCellsIsShip = new Ship[10][10];
	String ownText = null;
	String enemyText = null;
	boolean ownText_f = false;
	boolean enemyText_f = false;
	public ShipFlags ownShips = new ShipFlags();
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
	
	public GameSpace() {
		/* elõször nem tudjuk, hogy az ellenfél térfelén milyen
		 * mezõk vannak
		 */
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				enemyTable[i][j] = CellType.Unknown;
			}
		}
		
		/*
		 * Saját mezeinken elõször víz van
		 */
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				ownTable[i][j] = CellType.Water;
			}
		}
		for (int col = 0; col < 10; col++) {
			for (int row = 0; row < 10; row++) {
				ownCellsIsShip[col][row] = new Ship();
				enemyCellsIsShip[col][row] = new Ship();
			}
		}
	}
}