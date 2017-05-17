package szoftechtutor;

/**
 * A j�t�k �j �llapot�ra reag�l� interface.
 */
public interface IGameState {
	/**
	 * A j�t�k �j �llapot�ra adand� reakci�.
	 * @param gs	A j�t�k �j �llapota.
	 */
	public void onNewGameState(GameState gs);
	void toString(String string);
}
