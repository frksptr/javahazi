package szoftechtutor;

import java.awt.Point;
import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;

import Messages.GameMessage;
import Messages.PositionMessage;
import Messages.ReadyMessage;
import Messages.TypeMessage;
import util.CellType;

public class SerialClient extends Network {

	private Socket socket = null;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;

	SerialClient(Control c) {
		super(c);
	}

	private class ReceiverThread implements Runnable {

		public void run() {
			System.out.println("Waiting for points...");
			try {
				while (true) {
					GameMessage msg = (GameMessage) in.readObject();
					if (msg instanceof PositionMessage){
						Point received = ((PositionMessage) msg).getPosition();
						ctrl.clickReceived(received);
					}
					else if(msg instanceof ReadyMessage) {
						ctrl.enemyReady();
					}
					else if(msg instanceof TypeMessage) {
						Point p = ((TypeMessage)msg).getPosition();
						CellType t = ((TypeMessage)msg).getType();
						ctrl.typeReceived(p,t);
					}
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

	@Override
	void send(GameMessage msg) {
		if (out == null)
			return;
//		System.out.println("Sending point: " + ns + " to Server");
		try {
			out.writeObject(msg);
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
}
