package szoftechtutor;

/**
 * Parancsra reag�l� interface.
 */
public interface ICommand {
	/**
	 * A kapott parancsra val� reakci�.
	 * @param c	Kapott parancs.
	 */
	public void onCommand(Command c);
}

