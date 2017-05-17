/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package szoftechtutor;

/**
 * Az alkalmazas inditasa
 */
public class Main {

	public static void main(String[] args) {
		Control c = new Control();
		GUI g = new GUI(c);
		c.gui = g;
	}
}
