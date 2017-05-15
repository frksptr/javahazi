package szoftechtutor;

import java.net.InetAddress;
import java.net.UnknownHostException;
/**
 *
 */
class Control {
	private Network net = null;
	private SerialServer server = null;
	private SerialClient client = null;
	public GUI gui = null;
	// éppen szerver, vagy kliens üzemmódban megyünk-e
	public NetworkType networkType = null;
	/*
	 * Igazából cska a szerver és a kliens indításáért felelõs,
	 * majd a guinak megadja a megfelelõ ICommand interface-t
	 */

	Control() {
	}

	void setGUI(GUI g) {
		gui = g;
	}

	public String startServer() {
		String currentIp = null;
		if (net != null)
			net.disconnect();
		try {
		    InetAddress iAddress = InetAddress.getLocalHost();  
			currentIp = iAddress.getHostAddress();
			net = new SerialServer(this);
			net.connect(currentIp+":10007");
			server = (SerialServer) net;
			server.logic = new Logic(server, gui);
			gui.commandProcessor = server.logic;
			networkType = NetworkType.Server;
		    System.out.println("Current IP Address : " +currentIp);
			} catch (UnknownHostException e) {
				System.out.print("Nem tud csatlakozni");
			}
		return currentIp;
	}

	void startClient(String ip) {
		if (net != null)
			net.disconnect();
		net = new SerialClient(this);
		net.connect(ip);
		client = (SerialClient) net;
		gui.commandProcessor = client;
		client.setGui(gui);
		networkType = NetworkType.Client;
		
	}
	
	public enum NetworkType {
		Client, 
		Server
	}
}
