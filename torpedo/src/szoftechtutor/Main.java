/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package szoftechtutor;

/**
 * Az alkalmaz�s ind�t�sa
 */
public class Main {

	public static void main(String[] args) {
		Control c = new Control();
		GUI g = new GUI(c);
		c.gui = g;
	}
}
