/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package szoftechtutor;

/**
 * Halozati kapcsolat letrehozasa, bontasa.
 */
abstract class Network {

	protected Control ctrl;

	/**
	 * @param c	A halozat inicializalasaert felelos osztaly.
	 */
	Network(Control c) {
		ctrl = c;
	}

	/**
	 * Csatlakozas egy masik felhez.
	 * @param ip	Csatlakozando IP cim.
	 */
	abstract void connect(String ip);

	/**
	 * A kapcsolat bontasa.
	 */
	abstract void disconnect();

}

