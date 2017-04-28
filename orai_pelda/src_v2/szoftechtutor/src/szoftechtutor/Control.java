/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package szoftechtutor;

import java.awt.Point;

import szoftechtutor.Command.CommandType;
import util.CellType;

/**
 *
 * @author Predi
 */
class Control implements ICommand {

	public GUI gui;
	private Network net = null;
	private GameState gameState = new GameState();
	private SerialServer server = null;
	private SerialClient client = null;

	Control() {
	}

	void setGUI(GUI g) {
		gui = g;
	}

	void startServer() {
		if (net != null)
			net.disconnect();
		net = new SerialServer(this);
		net.connect("localhost");
		server = (SerialServer) net;
	}

	void startClient() {
		if (net != null)
			net.disconnect();
		net = new SerialClient(this);
		net.connect("localhost");
		client = (SerialClient) net;
	}

	void sendClick(Point p) {
		// gui.addPoint(p); //for drawing locally
		if (net == null)
			return;

	}



	void clickReceived(Point p) {
		if (gui == null)
			return;
		gui.shootPos(p);
		// TODO: visszaküldeni hogy milyen hajó (gui.getShotType valami)
	}
	
	void enemyReady() {
		// set ready flag
	}
	
	// beállítani hogy hol mit lõttünk ki az ellenfél térfelén
	void typeReceived(Point p, CellType t) {
//		gui.setEnemyShotType(pos, type);
	}
	// elküldnei hogy az ellenfél nálunk mit lõtt ki
	void sendShipType(Point p, CellType t) {
//		net.sendType(type)

	}
	
	@Override
	public void onCommand(Command c) {
		if (c.CommandType == CommandType.Shot){
			doShotStuff(c.positionShot);
		}
	}

	private void doShotStuff(Point position) {
		GameState gs = gameState;
		/* megnézni, hogy ahova lõttek ott mivan
		 * ennek megfelelõen megváltoztatni az eltárolt dolgokat
		 * (hajót kilõni, jelezni hogy miafaszvan
		 * egyszóval frissíteni a KÉT pályát (a gamestateben van 
		 * letárolva mindkét mezõ)
		 */
		gui.onNewGameState(gs);
	}
	
}
