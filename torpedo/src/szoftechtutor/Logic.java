package szoftechtutor;

import java.awt.Point;

import javax.swing.JTextArea;

import sun.security.jgss.GSSCaller;
import szoftechtutor.Command.CommandOrigin;
import szoftechtutor.Command.CommandType;
import szoftechtutor.Control.NetworkType;
import szoftechtutor.GameState.GamePhase;

public class Logic implements ICommand {
	
	private GameState gameState = new GameState();
	public IGameState gui;
	public SerialServer server;
	public Logic(SerialServer server, GUI gui) {
		this.gui = gui;
		this.server = server;
	}

	@Override
	public void onCommand(Command c) {
		System.out.println("Command '" + c.commandType + "arrived from " + c.commandOrigin);
		if (c.commandType == CommandType.Shot){
			gameState.serversTurn = !gameState.serversTurn;
			//checkShot(gameState.serverGameSpace.ownTable, c.position);
			doShotStuff(c.position, c.commandOrigin);
		} else if (c.commandType == CommandType.PlacedShip) {
			doPlaceShipStuff(c.position, c.commandOrigin);
		} else if (c.commandType == CommandType.Ready) {
			doReadyStuff(c.ready, c.commandOrigin);
		} else if (c.commandType == CommandType.Ready) {
			doResetStuff();
		}
	}
	
	private void doResetStuff(){
		gameState = new GameState();
		gui.onNewGameState(gameState);		
		server.onNewGameState(gameState);
	}
	
	private void doReadyStuff(boolean ready, NetworkType commandOrigin) {
		if (commandOrigin == NetworkType.Client) {
			gameState.clientReady = ready;
		} else {
			gameState.serverReady = ready;
		}
		
		if (gameState.clientReady & gameState.serverReady) {
			if (gameState.gamePhase == GamePhase.PlacingShips) {
				gameState.gamePhase = GamePhase.ShootingShips;
			}
		}
		
		gui.onNewGameState(gameState);
		server.onNewGameState(gameState);
		
	}

	private void doShotStuff(Point position, NetworkType origin) {
		/*
		 * A logic a szerveren van
		 * 
		 * ha a kliens lõtt, akkor a szerver saját táblájában megnézzük mit
		 * talált el és átállítjuk
		 */
		if (origin == NetworkType.Client) {
			CellType newType = checkShot(gameState.serverGameSpace.ownTable, position);
			System.out.println("\nCelltype " + newType + "\n");
			if(newType == null)
				return; //Védelem a dupla kattintáshoz. Nem a legjobb de mûködik
			else
			gameState.clientGameSpace.enemyTable[position.x][position.y] = newType;
			gameState.serverGameSpace.ownTable[position.x][position.y] = newType;
		}
		/*
		 * ha a szerver lõtt, akkor a kliens saját táblájában állítjuk a cellát
		 */
		else {
			CellType newType = checkShot(gameState.clientGameSpace.ownTable, position);
			if(newType  == null)
				return; //Védelem a dupla kattintáshoz. Nem a legjobb de mûködik
			else
			gameState.clientGameSpace.ownTable[position.x][position.y] = newType;
			gameState.serverGameSpace.enemyTable[position.x][position.y] = newType;
		}
		
		gui.onNewGameState(gameState);		
		server.onNewGameState(gameState);
	}
	
	private CellType checkShot(CellType[][] cells, Point pos) {
		CellType shotCell = cells[pos.x][pos.y];
		switch (shotCell) {
			case Ship:
				return CellType.ShipShot;
			case Water:
				return CellType.WaterShot;
		}
		return null;
	}
	
	private void doPlaceShipStuff(Point position, NetworkType commandOrigin) {
		/*
		 * Rakó játékosnak megfelelõ helyen frissítjük az értéket
		 */
		boolean free = true;
		boolean hajo = false;
				
		gameState.serverGameSpace.ownText_f = false;
		gameState.clientGameSpace.ownText_f = false;
		
		int e_0i = 0, e_0j=0, e_10i=1, e_10j=1;
		// TODO: ha hajóhoz tartozik akkor ahhoz hozzáadni
		if (commandOrigin == NetworkType.Client) {
			if(position.x==9) e_10i=0;
			if(position.y==9) e_10j=0;
			for(int i=-1+e_0i; i<=e_10i; i++){
				for(int j=-1+e_0j; j<=e_10j; j++){
					try{
						if(gameState.clientGameSpace.ownTable[position.x+i][position.y+j] == CellType.Ship){
							free = false;
						}
					}
					catch(Exception ex){
						if(i==-1 && position.x==0) e_0i = 1;
						if(j==-1 && position.y==0) e_0j = 1;
					}
				}
			}
			if(free && gameState.clientGameSpace.ownShip>0){
				gameState.clientGameSpace.ownTable[position.x][position.y] = CellType.Ship;
				gameState.clientGameSpace.ownShip--;
				}
			else{
				if(gameState.clientGameSpace.ownTable[position.x][position.y] == CellType.Ship){
					gameState.clientGameSpace.ownTable[position.x][position.y] = CellType.Water;
					gameState.clientGameSpace.ownShip++;
				}
				else {
					gameState.clientGameSpace.ownTable[position.x][position.y] = CellType.Water;
					gameState.clientGameSpace.ownText_f = true;
					gameState.clientGameSpace.ownText = "Oda nem rakhatsz!";
				}
				free = true;
			}
		}
		
		/*
		 * Server Logic
		 */
		e_0j=0; e_0i=0; e_10i=1; e_10j=1;
		class Szomszed{
			int x=0,y=0;
			void set(int xx, int yy)
			{
				x = xx;
				y = yy;
			}
		};
		Szomszed[] szomszed = new Szomszed[2];
		szomszed[0] = new Szomszed();
		szomszed[1] = new Szomszed();
		int foglalt=0;
		int[] maximumShips = {0,5,4,3,2,1}; 
		if (commandOrigin == NetworkType.Server) {
			if(position.x==9) e_10i=0;
			if(position.y==9) e_10j=0;
			for(int i=-1+e_0i; i<=e_10i;i++){
				for(int j=-1+e_0j; j<=e_10j;j++){
					try{
						if(gameState.serverGameSpace.ownTable[position.x+i][position.y+j] == CellType.Ship){
							foglalt++;
							if(foglalt==1) szomszed[0].set((position.x+i), (position.y+j));
							if(foglalt==2) szomszed[1].set(position.x+i, position.y+j);
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
							gameState.serverGameSpace.ownCellsIsShip[position.x][position.y].id = 1 + gameState.serverGameSpace.ownShips.id;
							gameState.serverGameSpace.ownCellsIsShip[position.x][position.y].elements_no++;
							gameState.serverGameSpace.ownCellsIsShip[position.x][position.y].size++;
							gameState.serverGameSpace.ownShips.placedShips[1]--;
							gameState.serverGameSpace.ownShips.id++;
							gameState.serverGameSpace.ownTable[position.x][position.y] = CellType.Ship;
							gameState.serverGameSpace.ownShip--;
					}
					else {
						gameState.serverGameSpace.ownText_f = true;
						gameState.serverGameSpace.ownText = "Nem rakhatsz le több 1 elemû hajót!";
					}
					break;
				case 1:
					if(gameState.serverGameSpace.ownTable[position.x][position.y] == CellType.Ship){
						gameState.serverGameSpace.ownTable[position.x][position.y] = CellType.Water;
						gameState.serverGameSpace.ownShip++;
						gameState.serverGameSpace.ownCellsIsShip[position.x][position.y].elements_no--;
						if(gameState.serverGameSpace.ownCellsIsShip[position.x][position.y].elements_no==0){
							gameState.serverGameSpace.ownShips.placedShips[1]++;
						}
					}
					else
					{
						int tempInt = gameState.serverGameSpace.ownCellsIsShip[szomszed[0].x][szomszed[0].y].size;
						if(gameState.serverGameSpace.ownShips.placedShips[tempInt+1] > 0){
							gameState.serverGameSpace.ownTable[position.x][position.y] = CellType.Ship;
							gameState.serverGameSpace.ownCellsIsShip[position.x][position.y].id = gameState.serverGameSpace.ownCellsIsShip[szomszed[0].x][szomszed[0].y].id;
							gameState.serverGameSpace.ownCellsIsShip[position.x][position.y].elements_no = gameState.serverGameSpace.ownCellsIsShip[szomszed[0].x][szomszed[0].y].elements_no;
							gameState.serverGameSpace.ownCellsIsShip[position.x][position.y].size = gameState.serverGameSpace.ownCellsIsShip[szomszed[0].x][szomszed[0].y].size;
							for (int col = 0; col < 10; col++) {
								for (int row = 0; row < 10; row++) {
									if(gameState.serverGameSpace.ownCellsIsShip[col][row].id == gameState.serverGameSpace.ownCellsIsShip[position.x][position.y].id){
										gameState.serverGameSpace.ownCellsIsShip[col][row].elements_no++;
										gameState.serverGameSpace.ownCellsIsShip[col][row].size++;
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
							gameState.serverGameSpace.ownText = printf("%d elemû hajóból nem rakhatsz már le többet",tempInt+1);
						}
					}
					break;
				case 2:
					gameState.serverGameSpace.ownText_f = true;
					gameState.serverGameSpace.ownText = "Két hajót nem köthetsz össze!";
					break;
				default: 
					gameState.serverGameSpace.ownText_f = true;
					gameState.serverGameSpace.ownText = "Oda nem rakhatsz!";
					break;
			}
		}
		
		gui.onNewGameState(gameState);
		server.send(gameState);
		gameState.serverGameSpace.ownText_f = false;
		gameState.clientGameSpace.ownText_f = false;
	}

	private String printf(String string, int tempInt) {
		// TODO Auto-generated method stub
		return null;
	}

}
