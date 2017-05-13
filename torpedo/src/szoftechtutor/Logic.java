package szoftechtutor;

import java.awt.Point;

import javax.swing.JTextArea;

import sun.security.jgss.GSSCaller;
import szoftechtutor.Command.CommandOrigin;
import szoftechtutor.Command.CommandType;

public class Logic implements ICommand {

	private GameState gameState = new GameState();
	public IGameState gui;
	public SerialServer server;
	public Logic(SerialServer server, GUI gui) {
		this.gui = gui;
		this.server = server;
	}
	public int ship=10;

	@Override
	public void onCommand(Command c) {
		System.out.println("Command '" + c.commandType + "arrived from " + c.commandOrigin);
		if (c.commandType == CommandType.Shot){
			gameState.serversTurn = !gameState.serversTurn;
			//checkShot(gameState.serverGameSpace.ownTable, c.position);
			doShotStuff(c.position, c.commandOrigin);
		} if (c.commandType == CommandType.PlacedShip) {
			doPlaceShipStuff(c.position, c.commandOrigin);
		}
		// TODO: ready ellenõrzés
	}

	private void doShotStuff(Point position, CommandOrigin origin) {
		/*
		 * A logic a szerveren van
		 * 
		 * ha a kliens lõtt, akkor a szerver saját táblájában megnézzük mit
		 * talált el és átállítjuk
		 */
		if (origin == CommandOrigin.Client) {
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
		GameState gs = new GameState();
		gs.clientGameSpace = gameState.clientGameSpace;
		gs.gamePhase = gameState.gamePhase;
		gs.serverGameSpace = gameState.serverGameSpace;
		gs.serversTurn = gameState.serversTurn;
		
		server.onNewGameState(gs);
		server.send(gs);
		// TODO: kliensnek visszaküldeni
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
	
	private void doPlaceShipStuff(Point position, CommandOrigin commandOrigin) {
		/*
		 * Rakó játékosnak megfelelõ helyen frissítjük az értéket
		 */
		boolean free = true;
		int e_0i = 0, e_0j=0, e_10i=1, e_10j=1;
		// TODO: ha hajóhoz tartozik akkor ahhoz hozzáadni
		if (commandOrigin == CommandOrigin.Client) {
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
			if(free && ship>=0){
				gameState.clientGameSpace.ownTable[position.x][position.y] = CellType.Ship;
				ship--;
			}
			else{
				gameState.clientGameSpace.ownTable[position.x][position.y] = CellType.Water;
				gui.toString("Oda nem rakhatsz!");
				free = true;
			}
		}
		e_0j=0; e_0i=0; e_10i=1; e_10j=1;
		if (commandOrigin == CommandOrigin.Server) {
			if(position.x==9) e_10i=0;
			if(position.y==9) e_10j=0;
			for(int i=-1+e_0i; i<=e_10i;i++){
				for(int j=-1+e_0j; j<=e_10j;j++){
					try{
						if(gameState.serverGameSpace.ownTable[position.x+i][position.y+j] == CellType.Ship){
							free = false;
						}
					}
					catch(Exception ex){
						if(i==-1 && position.x==0) e_0i = 1;
						if(j==-1 && position.y==0) e_0j = 1;
					}
				}
			}
			if(free && gameState.serverGameSpace.onwShip>=0){
				gameState.serverGameSpace.ownTable[position.x][position.y] = CellType.Ship;
				gameState.serverGameSpace.onwShip--;
			}
			else{
				gameState.serverGameSpace.ownTable[position.x][position.y] = CellType.Water;
				gui.toString("Oda nem rakhatsz!");
				free = true;
			}
		}
		gui.onNewGameState(gameState);
		server.send(gameState);
	}

}
