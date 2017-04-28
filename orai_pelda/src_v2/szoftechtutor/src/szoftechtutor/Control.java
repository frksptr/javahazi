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
class Control {
	
	private Network net = null;
	private SerialServer server = null;
	private SerialClient client = null;
	public GUI gui = null;
	
	/*
	 * Igaz�b�l cska a szerver �s a kliens ind�t�s��rt felel�s,
	 * majd a guinak megadja a megfelel� ICommand interface-t
	 */

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
		server.logic = new Logic();
		gui.commandProcessor = server.logic;
	}

	void startClient() {
		if (net != null)
			net.disconnect();
		net = new SerialClient(this);
		net.connect("localhost");
		client = (SerialClient) net;
		gui.commandProcessor = client;
	}
	

	
}
