/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package szoftechtutor;

/**
 * Hálózati kapcsolatért létrehozása, bontása.
 */
abstract class Network {

	protected Control ctrl;

	/**
	 * @param c	A hálózat inicializálásáért felelõs osztály.
	 */
	Network(Control c) {
		ctrl = c;
	}

	/**
	 * Csatlakozás egy másik félhez.
	 * @param ip	Csatlakozandó IP cím.
	 */
	abstract void connect(String ip);

	/**
	 * A kapcsolat bontása.
	 */
	abstract void disconnect();

}

