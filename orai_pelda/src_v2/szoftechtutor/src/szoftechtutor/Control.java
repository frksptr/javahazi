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
		// TODO: visszak�ldeni hogy milyen haj� (gui.getShotType valami)
	}
	
	void enemyReady() {
		// set ready flag
	}
	
	// be�ll�tani hogy hol mit l�tt�nk ki az ellenf�l t�rfel�n
	void typeReceived(Point p, CellType t) {
//		gui.setEnemyShotType(pos, type);
	}
	// elk�ldnei hogy az ellenf�l n�lunk mit l�tt ki
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
		/* megn�zni, hogy ahova l�ttek ott mivan
		 * ennek megfelel�en megv�ltoztatni az elt�rolt dolgokat
		 * (haj�t kil�ni, jelezni hogy miafaszvan
		 * egysz�val friss�teni a K�T p�ly�t (a gamestateben van 
		 * let�rolva mindk�t mez�)
		 */
		gui.onNewGameState(gs);
	}
	
}
