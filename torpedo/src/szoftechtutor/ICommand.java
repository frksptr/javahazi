package szoftechtutor;

/**
 * Parancsra reagáló interface.
 */
public interface ICommand {
	/**
	 * A kapott parancsra való reakció.
	 * @param c	Kapott parancs.
	 */
	public void onCommand(Command c);
}

