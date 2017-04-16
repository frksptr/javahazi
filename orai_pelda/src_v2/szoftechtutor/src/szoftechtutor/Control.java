/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package szoftechtutor;

import java.awt.Point;

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
		net.send(p);
	}

	void clickReceived(Point p) {
		if (gui == null)
			return;
		gui.shootPos(p);
		// TODO: visszak�ldeni hogy milyen haj� (gui.getShotType valami)
	}
	
	// be�ll�tani hogy hol mit l�tt�nk ki az ellenf�l t�rfel�n
//	void typeReceived(pos, type) {
//		gui.setEnemyShotType(pos, type);
//	}
	// elk�ldnei hogy az ellenf�l n�lunk mit l�tt ki
//	void sendShipType(pos, tpye) {
//		net.sendType(type)
//	}
	
}
