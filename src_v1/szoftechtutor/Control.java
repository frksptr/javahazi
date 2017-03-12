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
	}

	void startClient() {
	}

	void sendClick(Point p) {
		gui.addPoint(p); // for drawing locally
	}

	void clickReceived(Point p) {
	}
}
