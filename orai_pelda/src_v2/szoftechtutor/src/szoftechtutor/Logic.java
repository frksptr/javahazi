package szoftechtutor;

import java.awt.Point;

import szoftechtutor.Command.CommandType;

public class Logic implements ICommand {

	private GameState gameState = new GameState();
	public GUI gui;
	@Override
	public void onCommand(Command c) {
		if (c.CommandType == CommandType.Shot){
			doShotStuff(c.positionShot);
		}
	}

	private void doShotStuff(Point position) {
		GameState gs = gameState;
		/* megn�zni, hogy ahova l�ttek ott mivan
		 * ennek megfelel�en megv�ltoztatni az elt�rolt dolgokat
		 * (haj�t kil�ni, jelezni hogy miafaszvan
		 * egysz�val friss�teni a K�T p�ly�t (a gamestateben van 
		 * let�rolva mindk�t mez�)
		 */
		gui.onNewGameState(gs);
	}

}
