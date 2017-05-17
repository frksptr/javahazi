package szoftechtutor;

/**
 * Parancsra reagalo interface.
 */
public interface ICommand {
	/**
	 * A kapott parancsra valo reakcio.
	 * @param c	Kapott parancs.
	 */
	public void onCommand(Command c);
}

