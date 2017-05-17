package szoftechtutor;

import java.io.Serializable;
import szoftechtutor.Ship;

/**
 * A jatekter allapotat leiro osztaly.
 */
public class GameSpace implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * A jatekos sajat jatektere.
	 */
	public CellType[][] ownTable = new CellType[10][10];
	
	/**
	 * A jatekos informacioi az ellenfele jatektererol.
	 */
	public CellType[][] enemyTable = new CellType[10][10];
	
	/**
	 *  A jatekos sajat cellahoz tartozo hajok.
	 */
	public Ship[][] ownCellsToShip = new Ship[10][10];
	/**
	 * A jatekos ellenfele cellaihoz tartozo hajok.
	 */
	public Ship[][] enemyCellsToShip = new Ship[10][10];
	
	/**
	 * Sajat allapotot leiro uzenet.
	 */
	String ownText = null;
	
	/**
	 * Az ellenfel allapotat leiro uzenet. 
	 */
	String enemyText = null;
	
	/**
	 * ownText megjelenitendo-e.
	 */
	boolean ownText_f = false;
	/**
	 * enemyText megjelenitendo-e.
	 */
	boolean enemyText_f = false;
	
	
	/**
	 * Jatekos hajoira vonatkozo informaciok.
	 */
	public ShipFlags ownShips = new ShipFlags();
	/**
	 * Az ellenfel hajoira vonatkozo informaciok.
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
	 * A jatekter initcializacioja. Az ellenfel mezoirol alapesetben nem rendelkezunk informacioval, a jatekos mezei pedig vizkent szereplnek.
	 */
	public GameSpace() {
		/* eloszor nem tudjuk, hogy az ellenfel terfelen milyen
		 * mezok vannak
		 */
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				enemyTable[i][j] = CellType.Unknown;
			}
		}
		
		/*
		 * Sajat mezeinken eloszor viz van
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