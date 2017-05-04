package szoftechtutor;

import java.awt.Point;

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

	@Override
	public void onCommand(Command c) {
		System.out.println("Command '" + c.commandType + "arrived from " + c.commandOrigin);
		if (c.commandType == CommandType.Shot){
			doShotStuff(c.position, c.commandOrigin);
		} if (c.commandType == CommandType.PlacedShip) {
			doPlaceShipStuff(c.position, c.commandOrigin);
		}
		// TODO: ready ellen�rz�s
	}

	private void doShotStuff(Point position, CommandOrigin origin) {
		/*
		 * A logic a szerveren van
		 * 
		 * ha a kliens l�tt, akkor a szerver saj�t t�bl�j�ban megn�zz�k mit
		 * tal�lt el �s �t�ll�tjuk
		 */
		if (origin == CommandOrigin.Client) {
			CellType newType = checkShot(gameState.serverGameSpace.ownTable, position);
			gameState.clientGameSpace.enemyTable[position.x][position.y] = newType;
			gameState.serverGameSpace.ownTable[position.x][position.y] = newType;
		}
		/*
		 * ha a szerver l�tt, akkor a kliens saj�t t�bl�j�ban �ll�tjuk a cell�t
		 */
		else {
			CellType newType = checkShot(gameState.clientGameSpace.ownTable, position);
			gameState.clientGameSpace.ownTable[position.x][position.y] = newType;
			gameState.serverGameSpace.enemyTable[position.x][position.y] = newType;
		}
		
		gui.onNewGameState(gameState);
		server.onNewGameState(gameState);
		// TODO: kliensnek visszak�ldeni
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
		 * Rak� j�t�kosnak megfelel� helyen friss�tj�k az �rt�ket
		 */
		if (commandOrigin == CommandOrigin.Client) {
			// TODO: ellen�rizni, hogy lehet-e oda rakni
			gameState.clientGameSpace.ownTable[position.x][position.y] = CellType.Ship;
		}
		if (commandOrigin == CommandOrigin.Server) {
			
			gameState.serverGameSpace.ownTable[position.x][position.y] = CellType.Ship;
			// TODO: ehelyett megn�zni, hogy lehet-e
			// TODO: ha haj�hoz tartozik akkor ahhoz hozz�adni
		}
	}

}
