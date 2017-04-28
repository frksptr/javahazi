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
		/* megnézni, hogy ahova lõttek ott mivan
		 * ennek megfelelõen megváltoztatni az eltárolt dolgokat
		 * (hajót kilõni, jelezni hogy miafaszvan
		 * egyszóval frissíteni a KÉT pályát (a gamestateben van 
		 * letárolva mindkét mezõ)
		 */
		gui.onNewGameState(gs);
	}

}
