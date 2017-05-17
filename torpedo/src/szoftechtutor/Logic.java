package szoftechtutor;

import java.awt.Point;
import java.util.stream.IntStream;

import szoftechtutor.Command.CommandType;
import szoftechtutor.Control.NetworkType;
import szoftechtutor.GameState.GamePhase;

/**
 * A j�t�k logik�j�t kezel� oszt�ly.
 */
public class Logic implements ICommand {
	
	/**
	 * A j�t�k aktu�lis �llapota.
	 */
	private GameState gameState = new GameState();
	/**
	 * Az alkalmaz�s GUI-ja.
	 */
	public IGameState gui;
	
	/**
	 * A szervert megval�s�t� oszt�ly. 
	 */
	public SerialServer server;
	
	public Logic(SerialServer server, GUI gui) {
		this.gui = gui;
		this.server = server;
	}

	/* (non-Javadoc)
	 * @see szoftechtutor.ICommand#onCommand(szoftechtutor.Command)
	 */
	@Override
	public void onCommand(Command c) {
		System.out.println("Command '" + c.commandType + "arrived from " + c.commandOrigin);
		if (c.commandType == CommandType.Shot){
			gameState.serversTurn = !gameState.serversTurn;
			doShotStuff(c.position, c.commandOrigin);
		} else if (c.commandType == CommandType.PlacedShip) {
			doPlaceShipStuff(c.position, c.commandOrigin);
		} else if (c.commandType == CommandType.Ready) {
			doReadyStuff(c.ready, c.commandOrigin);
		} else if (c.commandType == CommandType.Reset) {
			doResetStuff();
		}
	}
	
	/**
	 * A j�t�k alap�llapotba helyez�se.
	 */
	private void doResetStuff(){
		gameState = new GameState();
		gui.onNewGameState(gameState);		
		server.onNewGameState(gameState);
	}
	
	/**
	 * A j�t�kosok k�szenl�ti �llapot�nak kezel�se.
	 * @param ready K�szen �ll-e a j�t�kos.
	 * @param commandOrigin Mely j�t�kos adta a k�szenl�ti �zenetet.
	 */
	private void doReadyStuff(boolean ready, NetworkType commandOrigin) {
		if (commandOrigin == NetworkType.Client) {
			gameState.clientReady = ready;
		} else {
			gameState.serverReady = ready;
		}
		
		if (gameState.clientReady && gameState.serverReady) {
			if (gameState.gamePhase == GamePhase.PlacingShips) {
				gameState.gamePhase = GamePhase.ShootingShips;
			}
		}
		
		gui.onNewGameState(gameState);
		server.onNewGameState(gameState);
		
	}

	/**
	 * A t�bl�n val� l�v�s kezel�se.
	 * @param position	A l�v�s poz�ci�ja.
	 * @param origin	Mely j�t�kos l�tt.
	 */
	private void doShotStuff(Point position, NetworkType origin) {
		/*
		 * A logic a szerveren van
		 * ha a kliens l�tt, akkor a szerver saj�t t�bl�j�ban megn�zz�k mit
		 * tal�lt el �s �t�ll�tjuk
		 */
		gameState.serverGameSpace.ownText_f = false;
		gameState.clientGameSpace.ownText_f = false;
		
		if (origin == NetworkType.Client && gameState.clientGameSpace.enemyTable[position.x][position.y] == CellType.Unknown) {
			CellType newType = checkShot(gameState.serverGameSpace.ownTable, position);
			System.out.println("\nCelltype " + newType + "\n");
			if(newType == null)
				return;
			else
			gameState.clientGameSpace.enemyTable[position.x][position.y] = newType;
			gameState.serverGameSpace.ownTable[position.x][position.y] = newType;
			int kilott = 0;
			if(newType == CellType.ShipShot){
				gameState.serverGameSpace.ownCellsToShip[position.x][position.y].scuttled=true;
				gameState.serverGameSpace.ownShips.shotShipElements--;
				for (int col = 0; col < 10; col++) {
					for (int row = 0; row < 10; row++) {
						if(gameState.serverGameSpace.ownCellsToShip[position.x][position.y].id == gameState.serverGameSpace.ownCellsToShip[col][row].id){
							if(gameState.serverGameSpace.ownCellsToShip[col][row].scuttled==true){
								kilott++;
								if(kilott==gameState.serverGameSpace.ownCellsToShip[position.x][position.y].size){
									gameState.serverGameSpace.ownShips.shotShips[kilott]++;
								}
							}
						}
					}
				}
			}
			
			if(IntStream.of(gameState.serverGameSpace.ownShips.shotShips).sum() == gameState.serverGameSpace.allShips){
				gameState.clientGameSpace.ownText_f = true;
				gameState.clientGameSpace.ownText = "Gratul�lok! Nyert�l! �j j�t�k ind�t�s�hoz nyomd meg a Reset gombot.";
				gameState.serverGameSpace.enemyText_f = true;
				gameState.serverGameSpace.enemyText = "Sajn�lom! Vesztett�l! �j j�t�k ind�t�s�hoz nyomd meg a Reset gombot.";
			}
		}
		/*
		 * ha a szerver l�tt, akkor a kliens saj�t t�bl�j�ban �ll�tjuk a cell�t
		 */
		else if (origin == NetworkType.Server && gameState.serverGameSpace.enemyTable[position.x][position.y] == CellType.Unknown)  {
			CellType newType = checkShot(gameState.clientGameSpace.ownTable, position);
			if(newType  == null)
				return;
			else
			gameState.clientGameSpace.ownTable[position.x][position.y] = newType;
			gameState.serverGameSpace.enemyTable[position.x][position.y] = newType;
			int kilott = 0;
			if(newType == CellType.ShipShot){
				gameState.clientGameSpace.ownCellsToShip[position.x][position.y].scuttled=true;
				gameState.clientGameSpace.ownShips.shotShipElements--;
				for (int col = 0; col < 10; col++) {
					for (int row = 0; row < 10; row++) {
						if(gameState.clientGameSpace.ownCellsToShip[position.x][position.y].id == gameState.clientGameSpace.ownCellsToShip[col][row].id){
							if(gameState.clientGameSpace.ownCellsToShip[col][row].scuttled==true){
								kilott++;
								if(kilott==gameState.clientGameSpace.ownCellsToShip[position.x][position.y].size){
									gameState.clientGameSpace.ownShips.shotShips[kilott]++;
								}
							}
						}
					}
				}
			}
			if(IntStream.of(gameState.clientGameSpace.ownShips.shotShips).sum() == gameState.clientGameSpace.allShips){
				gameState.serverGameSpace.ownText_f = true;
				gameState.serverGameSpace.ownText = "Gratul�lok! Nyert�l! �j j�t�k ind�t�s�hoz nyomd meg a Reset gombot.";
				gameState.clientGameSpace.enemyText_f = true;
				gameState.clientGameSpace.enemyText = "Sajn�lom! Vesztett�l! �j j�t�k ind�t�s�hoz nyomd meg a Reset gombot.";
			}
		}
		
		gui.onNewGameState(gameState);		
		server.onNewGameState(gameState);
		gameState.serverGameSpace.ownText_f = false;
		gameState.clientGameSpace.ownText_f = false;
		gameState.serverGameSpace.enemyText_f = false;
		gameState.clientGameSpace.enemyText_f = false;
	}
	
	/**
	 * A l�tt poz�ci�n leellen�rzi a l�tt cella �llapot�t, azt megv�ltoztatja a l�v�s hat�s�ra.
	 * @param cells	A l�v�shez tartoz� j�t�kt�r cell�i.
	 * @param pos	A l�v�s poz�ci�ja.
	 * @return	A l�tt cella �j t�pusa.
	 */
	private CellType checkShot(CellType[][] cells, Point pos) {
		CellType shotCell = cells[pos.x][pos.y];
		switch (shotCell) {
			case Ship:
				return CellType.ShipShot;
			case Water:
				return CellType.WaterShot;
		default:
			break;
		}
		return null;
	}
	
	/**
	 * Haj�k lerak�s�t kezel� f�ggv�ny.
	 * @param position	Hova rakunk haj�t.
	 * @param commandOrigin	Mely j�t�kos rakta a haj�t.
	 */
	private void doPlaceShipStuff(Point position, NetworkType commandOrigin) {

		gameState.serverGameSpace.ownText_f = false;
		gameState.clientGameSpace.ownText_f = false;
				
		/*
		 * Client Logic
		 */
		Point[] szomszed = new Point[5];
		for(int i=0; i<5;i++){
			szomszed[i] = new Point();
		}
		
		Point[] atlo = new Point[4];
		for(int i=0; i<4;i++){
			atlo[i] = new Point();
		}
		boolean atlo_f = false;
		int foglalt=0;
		int[] maximumShips = {0,5,4,3,2,1}; 
		int e_0i = 0, e_0j=0, e_10i=1, e_10j=1;

		if (commandOrigin == NetworkType.Client) {
			if(position.x==9) e_10i=0;
			if(position.y==9) e_10j=0;
			for(int i=-1+e_0i; i<=e_10i;i++){
				for(int j=-1+e_0j; j<=e_10j;j++){
					try{
						if(gameState.clientGameSpace.ownTable[position.x+i][position.y+j] == CellType.Ship){
							foglalt++;
							if(foglalt==1) szomszed[0].setLocation((position.x+i), (position.y+j));
							if(foglalt==2) szomszed[1].setLocation(position.x+i, position.y+j);
							if(i==1 && j==-1){ atlo[0].setLocation(position.x+i, position.y+j); atlo_f = true;}
							if(i==1 && j==1){ atlo[1].setLocation(position.x+i, position.y+j); atlo_f = true;}
							if(i==-1 && j==1){ atlo[2].setLocation(position.x+i, position.y+j); atlo_f = true;}
							if(i==-1 && j==-1){ atlo[3].setLocation(position.x+i, position.y+j); atlo_f = true;}
						}
					}
					catch(Exception ex){
						if(i==-1 && position.x==0) e_0i = 1;
						if(j==-1 && position.y==0) e_0j = 1;
					}
				}
			}
			switch(foglalt){
				case 0:
					if(gameState.clientGameSpace.ownShips.placedShips[1]>0)
					{
							gameState.clientGameSpace.ownCellsToShip[position.x][position.y].id = 1 + gameState.clientGameSpace.ownShips.id;
							gameState.clientGameSpace.ownCellsToShip[position.x][position.y].elements_no++;
							gameState.clientGameSpace.ownCellsToShip[position.x][position.y].size++;
							gameState.clientGameSpace.ownShips.placedShips[1]--;
							gameState.clientGameSpace.ownShips.id++;
							gameState.clientGameSpace.ownTable[position.x][position.y] = CellType.Ship;
							gameState.clientGameSpace.ownShips.shipElements--;
					}
					else {
						gameState.clientGameSpace.ownText_f = true;
						gameState.clientGameSpace.ownText = "1 elem� haj�b�l nem rakhatsz m�r le t�bbet";
					}
					break;
				case 1:
					if(gameState.clientGameSpace.ownTable[position.x][position.y] == CellType.Ship){
						gameState.clientGameSpace.ownTable[position.x][position.y] = CellType.Water;
						gameState.clientGameSpace.ownShips.shipElements++;
						gameState.clientGameSpace.ownCellsToShip[position.x][position.y].elements_no--;
						if(gameState.clientGameSpace.ownCellsToShip[position.x][position.y].elements_no==0){
							gameState.clientGameSpace.ownShips.placedShips[1]++;
						}
					}
					else if(atlo_f==true){
								atlo_f = false;
								gameState.clientGameSpace.ownText_f = true;
								gameState.clientGameSpace.ownText = "�tl�san nem �p�tkezhetsz, a haj�k sarkai nem �rhetnek �ssze!";
					}
					else{			
							int tempInt = gameState.clientGameSpace.ownCellsToShip[szomszed[0].x][szomszed[0].y].size;
							if(tempInt<5 && gameState.clientGameSpace.ownShips.placedShips[tempInt+1] > 0){
								gameState.clientGameSpace.ownTable[position.x][position.y] = CellType.Ship;
								gameState.clientGameSpace.ownShips.shipElements--;
								gameState.clientGameSpace.ownCellsToShip[position.x][position.y].id = gameState.clientGameSpace.ownCellsToShip[szomszed[0].x][szomszed[0].y].id;
								gameState.clientGameSpace.ownCellsToShip[position.x][position.y].elements_no = gameState.clientGameSpace.ownCellsToShip[szomszed[0].x][szomszed[0].y].elements_no;
								gameState.clientGameSpace.ownCellsToShip[position.x][position.y].size = gameState.clientGameSpace.ownCellsToShip[szomszed[0].x][szomszed[0].y].size;
								for (int col = 0; col < 10; col++) {
									for (int row = 0; row < 10; row++) {
										if(gameState.clientGameSpace.ownCellsToShip[col][row].id == gameState.clientGameSpace.ownCellsToShip[position.x][position.y].id){
											gameState.clientGameSpace.ownCellsToShip[col][row].elements_no++;
											gameState.clientGameSpace.ownCellsToShip[col][row].size++;
										}
									}
								}
								gameState.clientGameSpace.ownShips.placedShips[tempInt+1]--;
								if(maximumShips[tempInt]>gameState.clientGameSpace.ownShips.placedShips[tempInt]){
										gameState.clientGameSpace.ownShips.placedShips[tempInt]++;
								}
							}
							else{
								gameState.clientGameSpace.ownText_f = true;
								gameState.clientGameSpace.ownText = String.format("%d elem� haj�b�l nem rakhatsz m�r le t�bbet",(tempInt<5 ? (tempInt+1) : tempInt));
							}
						}
					
					break;
				case 2:
					int tempInt = gameState.clientGameSpace.ownCellsToShip[szomszed[0].x][szomszed[0].y].size;
					if(gameState.clientGameSpace.ownTable[position.x][position.y] == CellType.Ship){
						gameState.clientGameSpace.ownTable[position.x][position.y] = CellType.Water;
						gameState.clientGameSpace.ownShips.shipElements++;
						for (int col = 0; col < 10; col++) {
							for (int row = 0; row < 10; row++) {
								if(gameState.clientGameSpace.ownCellsToShip[col][row].id == gameState.clientGameSpace.ownCellsToShip[position.x][position.y].id){
									gameState.clientGameSpace.ownCellsToShip[col][row].elements_no--;
									gameState.clientGameSpace.ownCellsToShip[col][row].size--;
								}
							}
						}
						gameState.clientGameSpace.ownShips.placedShips[tempInt]++;
						if(0<gameState.clientGameSpace.ownShips.placedShips[tempInt]){
								gameState.clientGameSpace.ownShips.placedShips[tempInt-1]--;
						}
					}
					else{
						double dist_1 = szomszed[0].distance(szomszed[1].x,szomszed[1].y);
						double dist_2 = position.distance(szomszed[0].x, szomszed[0].y);
						double dist_3 = position.distance(szomszed[1].x, szomszed[1].y);
						if(dist_1<2.0 && (dist_2<2.0 || dist_3<2.0)){
							if(gameState.clientGameSpace.ownShips.placedShips[tempInt+1] > 0){
								gameState.clientGameSpace.ownTable[position.x][position.y] = CellType.Ship;
								gameState.clientGameSpace.ownShips.shipElements--;
								gameState.clientGameSpace.ownCellsToShip[position.x][position.y].id = gameState.clientGameSpace.ownCellsToShip[szomszed[0].x][szomszed[0].y].id;
								gameState.clientGameSpace.ownCellsToShip[position.x][position.y].elements_no = gameState.clientGameSpace.ownCellsToShip[szomszed[0].x][szomszed[0].y].elements_no;
								gameState.clientGameSpace.ownCellsToShip[position.x][position.y].size = gameState.clientGameSpace.ownCellsToShip[szomszed[0].x][szomszed[0].y].size;
								for (int col = 0; col < 10; col++) {
									for (int row = 0; row < 10; row++) {
										if(gameState.clientGameSpace.ownCellsToShip[col][row].id == gameState.clientGameSpace.ownCellsToShip[position.x][position.y].id){
											gameState.clientGameSpace.ownCellsToShip[col][row].elements_no++;
											gameState.clientGameSpace.ownCellsToShip[col][row].size++;
										}
									}
								}
								gameState.clientGameSpace.ownShips.placedShips[tempInt+1]--;
								if(maximumShips[tempInt]>gameState.clientGameSpace.ownShips.placedShips[tempInt]){
										gameState.clientGameSpace.ownShips.placedShips[tempInt]++;
								}
							}
							else{
								gameState.clientGameSpace.ownText_f = true;
								gameState.clientGameSpace.ownText = String.format("%d elem� haj�b�l nem rakhatsz m�r le t�bbet",(tempInt<5 ? (tempInt+1) : tempInt));
							}		
						}
						else{
							gameState.clientGameSpace.ownText_f = true;
							gameState.clientGameSpace.ownText = "K�t haj�t nem k�thetsz �ssze!";
						}
					}
					break;
				default: 
					gameState.clientGameSpace.ownText_f = true;
					gameState.clientGameSpace.ownText = "Oda nem rakhatsz!";
					break;
			}
			for (int col = 0; col < 10; col++) {
				for (int row = 0; row < 10; row++) {
					if(gameState.clientGameSpace.ownTable[col][row] == CellType.Water && gameState.clientGameSpace.ownCellsToShip[col][row].id>0){
						gameState.clientGameSpace.ownCellsToShip[col][row] = new Ship();
					}
				}
			}
		}
		
		/*
		 * Server Logic
		 */
		e_0j=0; e_0i=0; e_10i=1; e_10j=1;

		for(int i=0; i<5;i++){
			szomszed[i] = new Point();
		}
		
		for(int i=0; i<4;i++){
			atlo[i] = new Point();
		}
		atlo_f = false;
		foglalt=0;
		if (commandOrigin == NetworkType.Server) {
			if(position.x==9) e_10i=0;
			if(position.y==9) e_10j=0;
			for(int i=-1+e_0i; i<=e_10i;i++){
				for(int j=-1+e_0j; j<=e_10j;j++){
					try{
						if(gameState.serverGameSpace.ownTable[position.x+i][position.y+j] == CellType.Ship){
							foglalt++;
							if(foglalt==1) szomszed[0].setLocation((position.x+i), (position.y+j));
							if(foglalt==2) szomszed[1].setLocation(position.x+i, position.y+j);
							if(i==1 && j==-1){ atlo[0].setLocation(position.x+i, position.y+j); atlo_f = true;}
							if(i==1 && j==1){ atlo[1].setLocation(position.x+i, position.y+j); atlo_f = true;}
							if(i==-1 && j==1){ atlo[2].setLocation(position.x+i, position.y+j); atlo_f = true;}
							if(i==-1 && j==-1){ atlo[3].setLocation(position.x+i, position.y+j); atlo_f = true;}
						}
					}
					catch(Exception ex){
						if(i==-1 && position.x==0) e_0i = 1;
						if(j==-1 && position.y==0) e_0j = 1;
					}
				}
			}
			switch(foglalt){
				case 0:
					if(gameState.serverGameSpace.ownShips.placedShips[1]>0)
					{
							gameState.serverGameSpace.ownCellsToShip[position.x][position.y].id = 1 + gameState.serverGameSpace.ownShips.id;
							gameState.serverGameSpace.ownCellsToShip[position.x][position.y].elements_no++;
							gameState.serverGameSpace.ownCellsToShip[position.x][position.y].size++;
							gameState.serverGameSpace.ownShips.placedShips[1]--;
							gameState.serverGameSpace.ownShips.id++;
							gameState.serverGameSpace.ownTable[position.x][position.y] = CellType.Ship;
							gameState.serverGameSpace.ownShips.shipElements--;
					}
					else {
						gameState.serverGameSpace.ownText_f = true;
						gameState.serverGameSpace.ownText = "Nem rakhatsz le t�bb 1 elem� haj�t!";
					}
					break;
				case 1:
					if(gameState.serverGameSpace.ownTable[position.x][position.y] == CellType.Ship){
						gameState.serverGameSpace.ownTable[position.x][position.y] = CellType.Water;
						gameState.serverGameSpace.ownShips.shipElements++;
						gameState.serverGameSpace.ownCellsToShip[position.x][position.y].elements_no--;
						if(gameState.serverGameSpace.ownCellsToShip[position.x][position.y].elements_no==0){
							gameState.serverGameSpace.ownShips.placedShips[1]++;
						}
					}
					else if(atlo_f==true){
								atlo_f = false;
								gameState.serverGameSpace.ownText_f = true;
								gameState.serverGameSpace.ownText = "�tl�san nem �p�tkezhetsz, a haj�k sarkai nem �rhetnek �ssze!";
					}
					else{			
							int tempInt = gameState.serverGameSpace.ownCellsToShip[szomszed[0].x][szomszed[0].y].size;
							if(tempInt<5 && gameState.serverGameSpace.ownShips.placedShips[tempInt+1] > 0){
								gameState.serverGameSpace.ownTable[position.x][position.y] = CellType.Ship;
								gameState.serverGameSpace.ownShips.shipElements--;
								gameState.serverGameSpace.ownCellsToShip[position.x][position.y].id = gameState.serverGameSpace.ownCellsToShip[szomszed[0].x][szomszed[0].y].id;
								gameState.serverGameSpace.ownCellsToShip[position.x][position.y].elements_no = gameState.serverGameSpace.ownCellsToShip[szomszed[0].x][szomszed[0].y].elements_no;
								gameState.serverGameSpace.ownCellsToShip[position.x][position.y].size = gameState.serverGameSpace.ownCellsToShip[szomszed[0].x][szomszed[0].y].size;
								for (int col = 0; col < 10; col++) {
									for (int row = 0; row < 10; row++) {
										if(gameState.serverGameSpace.ownCellsToShip[col][row].id == gameState.serverGameSpace.ownCellsToShip[position.x][position.y].id){
											gameState.serverGameSpace.ownCellsToShip[col][row].elements_no++;
											gameState.serverGameSpace.ownCellsToShip[col][row].size++;
										}
									}
								}
								gameState.serverGameSpace.ownShips.placedShips[tempInt+1]--;
								if(maximumShips[tempInt]>gameState.serverGameSpace.ownShips.placedShips[tempInt]){
										gameState.serverGameSpace.ownShips.placedShips[tempInt]++;
								}
							}
							else{
								gameState.serverGameSpace.ownText_f = true;
								gameState.serverGameSpace.ownText = String.format("%d elem� haj�b�l nem rakhatsz m�r le t�bbet",(tempInt<5 ? (tempInt+1) : tempInt));
							}
						}
					
					break;
				case 2:
					int tempInt = gameState.serverGameSpace.ownCellsToShip[szomszed[0].x][szomszed[0].y].size;
					if(gameState.serverGameSpace.ownTable[position.x][position.y] == CellType.Ship){
						gameState.serverGameSpace.ownTable[position.x][position.y] = CellType.Water;
						gameState.serverGameSpace.ownShips.shipElements++;
						for (int col = 0; col < 10; col++) {
							for (int row = 0; row < 10; row++) {
								if(gameState.serverGameSpace.ownCellsToShip[col][row].id == gameState.serverGameSpace.ownCellsToShip[position.x][position.y].id){
									gameState.serverGameSpace.ownCellsToShip[col][row].elements_no--;
									gameState.serverGameSpace.ownCellsToShip[col][row].size--;
								}
							}
						}
						gameState.serverGameSpace.ownShips.placedShips[tempInt]++;
						if(0<gameState.serverGameSpace.ownShips.placedShips[tempInt]){
								gameState.serverGameSpace.ownShips.placedShips[tempInt-1]--;
						}
					}
					else{
						double dist_1 = szomszed[0].distance(szomszed[1].x,szomszed[1].y);
						double dist_2 = position.distance(szomszed[0].x, szomszed[0].y);
						double dist_3 = position.distance(szomszed[1].x, szomszed[1].y);
						if(dist_1<2.0 && (dist_2<2.0 || dist_3<2.0)){
							if(gameState.serverGameSpace.ownShips.placedShips[tempInt+1] > 0){
								gameState.serverGameSpace.ownTable[position.x][position.y] = CellType.Ship;
								gameState.serverGameSpace.ownShips.shipElements--;
								gameState.serverGameSpace.ownCellsToShip[position.x][position.y].id = gameState.serverGameSpace.ownCellsToShip[szomszed[0].x][szomszed[0].y].id;
								gameState.serverGameSpace.ownCellsToShip[position.x][position.y].elements_no = gameState.serverGameSpace.ownCellsToShip[szomszed[0].x][szomszed[0].y].elements_no;
								gameState.serverGameSpace.ownCellsToShip[position.x][position.y].size = gameState.serverGameSpace.ownCellsToShip[szomszed[0].x][szomszed[0].y].size;
								for (int col = 0; col < 10; col++) {
									for (int row = 0; row < 10; row++) {
										if(gameState.serverGameSpace.ownCellsToShip[col][row].id == gameState.serverGameSpace.ownCellsToShip[position.x][position.y].id){
											gameState.serverGameSpace.ownCellsToShip[col][row].elements_no++;
											gameState.serverGameSpace.ownCellsToShip[col][row].size++;
										}
									}
								}
								gameState.serverGameSpace.ownShips.placedShips[tempInt+1]--;
								if(maximumShips[tempInt]>gameState.serverGameSpace.ownShips.placedShips[tempInt]){
										gameState.serverGameSpace.ownShips.placedShips[tempInt]++;
								}
							}
							else{
								gameState.serverGameSpace.ownText_f = true;
								gameState.serverGameSpace.ownText = String.format("%d elem� haj�b�l nem rakhatsz m�r le t�bbet",(tempInt<5 ? (tempInt+1) : tempInt));
							}		
						}
						else{
							gameState.serverGameSpace.ownText_f = true;
							gameState.serverGameSpace.ownText = "K�t haj�t nem k�thetsz �ssze!";
						}
					}
					break;
				default: 
					gameState.serverGameSpace.ownText_f = true;
					gameState.serverGameSpace.ownText = "Oda nem rakhatsz!";
					break;
			}
			for (int col = 0; col < 10; col++) {
				for (int row = 0; row < 10; row++) {
					if(gameState.serverGameSpace.ownTable[col][row] == CellType.Water && gameState.serverGameSpace.ownCellsToShip[col][row].id>0){
						gameState.serverGameSpace.ownCellsToShip[col][row] = new Ship();
					}
				}
			}
		}	
		gui.onNewGameState(gameState);
		server.send(gameState);
		gameState.serverGameSpace.ownText_f = false;
		gameState.clientGameSpace.ownText_f = false;
	}
}
