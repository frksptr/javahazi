package szoftechtutor;

import java.awt.Point;
import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;

import util.CellType;

public class SerialClient extends Network implements ICommand {

	private Socket socket = null;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;
	private GUI gui = ctrl.gui;

	SerialClient(Control c) {
		super(c);
	}

	private class ReceiverThread implements Runnable {
		public void run() {
			System.out.println("Waiting for points...");
			try {
				while (true) {
					/*
					 * a kliens csak gamestatet kap, majd ha valami történik
					 * azt command formájában továbbküldi a szerónak
					 * ami kiszámolja hogy mi is történt és visszaküld
					 * egy új gamestatet
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
			System.err.println("Don't know about host");
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection. ");
			JOptionPane.showMessageDialog(null, "Cannot connect to server!");
		}
	}

	void send(Command c) {
		if (out == null)
			return;
//		System.out.println("Sending point: " + ns + " to Server");
		try {
			out.writeObject(c);
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
			if (socket != null)
				socket.close();
		} catch (IOException ex) {
			System.err.println("Error while closing conn.");
		}
	}

	@Override
	public void onCommand(Command c) {
		// TODO Auto-generated method stub
		// valami történt a guiban, ahonnan ezt meghívták
		// eküldeni a szevernek aki kiszámolja miafaszvan és
		// visszaküld egy új gamestatet
		send(c);
	}
}
