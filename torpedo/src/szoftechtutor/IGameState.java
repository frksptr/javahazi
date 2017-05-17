package szoftechtutor;

/**
 * A játék új állapotára reagáló interface.
 */
public interface IGameState {
	/**
	 * A játék új állapotára adandó reakció.
	 * @param gs	A játék új állapota.
	 */
	public void onNewGameState(GameState gs);
	void toString(String string);
}
