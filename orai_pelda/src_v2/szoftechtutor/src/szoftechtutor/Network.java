/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package szoftechtutor;

import java.awt.Point;

import Messages.GameMessage;

/**
 *
 * @author Predi
 */
abstract class Network {

	protected Control ctrl;

	Network(Control c) {
		ctrl = c;
	}

	abstract void connect(String ip);

	abstract void disconnect();

	abstract void send(GameMessage msg);
}
