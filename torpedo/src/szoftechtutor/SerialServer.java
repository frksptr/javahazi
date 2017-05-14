package szoftechtutor;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.InetAddress;


import javax.smartcardio.CommandAPDU;


public class SerialServer extends Network implements IGameState{

	private ServerSocket serverSocket = null;
	private Socket clientSocket = null;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;
	public Logic logic = null; 

	SerialServer(Control c) {
		super(c);
	}

	private class ReceiverThread implements Runnable {

		public void run() {
			try {
				System.out.println("Waiting for Client");
				clientSocket = serverSocket.accept();
				System.out.println("Client connected.");
			} catch (IOException e) {
				System.err.println("Accept failed.");
				disconnect();
				return;
			}

			try {
				out = new ObjectOutputStream(clientSocket.getOutputStream());
				in = new ObjectInputStream(clientSocket.getInputStream());
				out.flush();
			} catch (IOException e) {
				System.err.println("Error while getting streams.");
				disconnect();
				return;
			}

			try {
				while (true) {
					/* a szerver csak parancsot kap ami alapján számol
					 * és visszaküld új gamestatet
					 */
					Command c = (Command) in.readObject();
					logic.onCommand(c);
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				System.err.println("Client disconnected!");
			} finally {
				disconnect();
			}
		}
	}

	@Override
	void connect(String ip) {
		disconnect();
		try {
			serverSocket = new ServerSocket(10007);

			Thread rec = new Thread(new ReceiverThread());
			rec.start();
		} catch (IOException e) {
			System.err.println("Could not listen on port: 10007.");
		}
	}

	void send(GameState gs) {
		if (out == null)
			return;
		System.out.println("\nSending point: " + gs + " to Client");
		try {
			out.reset();
			out.writeObject(gs);
			out.flush();
		} catch (IOException ex) {
			System.err.println("Send error.");
			System.err.println(ex.getMessage());
		}
	}

	@Override
	void disconnect() {
		try {
			if (out != null)
				out.close();
			if (in != null)
				in.close();
			if (clientSocket != null)
				clientSocket.close();
			if (serverSocket != null)
				serverSocket.close();
		} catch (IOException ex) {
			Logger.getLogger(SerialServer.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	@Override
	public void onNewGameState(GameState gs) {
		send(gs);
	}

	@Override
	public void toString(String string) {
		
	}
}
