/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package szoftechtutor;

/**
 * Házati kapcsolat lérehozása, bontása.
 */
abstract class Network {

	protected Control ctrl;

	/**
	 * @param c	A hálózat inicializálásáért felelős osztály.
	 */
	Network(Control c) {
		ctrl = c;
	}

	/**
	 * Csatlakoz�s egy m�sik f�lhez.
	 * @param ip	Csatlakozand� IP c�m.
	 */
	abstract void connect(String ip);

	/**
	 * A kapcsolat bont�sa.
	 */
	abstract void disconnect();

}

