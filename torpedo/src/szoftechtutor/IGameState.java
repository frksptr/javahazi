package szoftechtutor;

public interface IGameState {
	public void onNewGameState(GameState gs);
	public void setStatusBarText(String string);
	void toString(String string);
}
