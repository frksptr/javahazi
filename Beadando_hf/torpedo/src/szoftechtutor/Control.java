package szoftechtutor;

import java.net.InetAddress;
import java.net.UnknownHostException;
/**
 * A halozati kapcsolat felepiteseert es a rendszer inicializalasaert felelos osztaly.
 */
class Control {
	private Network net = null;
	private SerialServer server = null;
	private SerialClient client = null;
	public GUI gui = null;

	/**
	 * Eltarolja, hogy az aktualis alkalmazas szerver, vagy kliens uzemmodban uzemel.
	 */
	public NetworkType networkType = null;

	Control() {
	}

	/**
	 * Az alkalmazas inicializalasa szerver uzemmodban.
	 * @return A szerver IP cime.
	 */
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
				System.out.print("Nem tud csatlakozni\n");
			}
		return currentIp;
	}

	/**
	 * Az alkalmazas inicializalasa kliens uzemmodban.
	 */
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
	
	/**
	 * Az alkalmazas uzemmodjanak (szerver, vagy kliens) tipusa.
	 */
	public enum NetworkType {
		Client, 
		Server
	}
}
