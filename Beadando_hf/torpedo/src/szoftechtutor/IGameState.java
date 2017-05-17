package szoftechtutor;

/**
 * A jatek uj allapotara reagalo interface.
 */
public interface IGameState {
	/**
	 * A jatek uj allapotara adando reakcio.
	 * @param gs	A jatek uj allapota.
	 */
	public void onNewGameState(GameState gs);
	void toString(String string);
}
