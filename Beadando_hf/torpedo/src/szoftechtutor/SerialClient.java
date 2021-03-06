package szoftechtutor;

import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;


/**
 * A kliens halozati kommunikaciojat irja le.
 */
public class SerialClient extends Network implements ICommand {

	private Socket socket = null;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;
	public GUI gui = null;

	SerialClient(Control c) {
		super(c);
	}

	/**
	 * Adatok fogadasa a masik feltol.
	 */
	private class ReceiverThread implements Runnable {
		public void run() {
			System.out.println("Waiting for points...");
			try {
				while (true) {
					/*
					 * a kliens csak gamestatet kap, majd ha valami tortenik
					 * azt command formajaban tovabbkuldi a szeronak
					 * ami kiszamolja hogy mi is tortent es visszakuld
					 * egy uj gamestatet
					 */
					GameState gs = (GameState) in.readObject();
					gui.onNewGameState(gs);
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				System.err.println("Server disconnected!");
			} finally {
				disconnect();
			}
		}
	}

	/* (non-Javadoc)
	 * @see szoftechtutor.Network#connect(java.lang.String)
	 */
	@Override
	void connect(String ip) {
		disconnect();
		try {
			socket = new Socket(ip, 10007);

			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			out.flush();

			Thread rec = new Thread(new ReceiverThread());
			rec.start();
		} catch (UnknownHostException e) {
			System.err.println("Dons't know about host");
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection. ");
			JOptionPane.showMessageDialog(null, "Cannot connect to server!");
		}
	}

	/**
	 * Parancs kuldese a szervernek.
	 * @param c	A parancs tartalmat leiro osztaly.
	 */
	void send(Command c) {
		if (out == null)
			return;
		System.out.println("Sending command: " + c + " to Server");
		try {
			out.writeObject(c);
			out.flush();
		} catch (IOException ex) {
			System.err.println("Send error.");
			System.err.println(ex.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see szoftechtutor.Network#disconnect()
	 */
	@Override
	void disconnect() {
		try {
			if (out != null)
				out.close();
			if (in != null)
				in.close();
			if (socket != null)
				socket.close();
		} catch (IOException ex) {
			System.err.println("Error while closing conn.");
		}
	}

	@Override
	public void onCommand(Command c) {
		send(c);
	}

	public void setGui(GUI gui) {
		this.gui = gui;
		this.ctrl.gui = gui;
	}
}
