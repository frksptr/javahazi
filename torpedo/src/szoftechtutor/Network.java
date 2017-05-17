/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package szoftechtutor;

/**
 * H�l�zati kapcsolat�rt l�trehoz�sa, bont�sa.
 */
abstract class Network {

	protected Control ctrl;

	/**
	 * @param c	A h�l�zat inicializ�l�s��rt felel�s oszt�ly.
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

