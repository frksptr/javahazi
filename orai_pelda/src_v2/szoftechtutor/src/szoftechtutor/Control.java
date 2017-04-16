/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package szoftechtutor;

import java.awt.Point;

import Messages.PositionMessage;
import util.CellType;

/**
 *
 * @author Predi
 */
class Control {

	private GUI gui;
	private Network net = null;

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
	}

	void startClient() {
		if (net != null)
			net.disconnect();
		net = new SerialClient(this);
		net.connect("localhost");
	}

	void sendClick(Point p) {
		// gui.addPoint(p); //for drawing locally
		if (net == null)
			return;
		net.send(new PositionMessage(p));
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
	
}
