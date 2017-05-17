package szoftechtutor;

import java.net.InetAddress;
import java.net.UnknownHostException;
/**
 * A h�l�zati kapcsolat felépítéséért és a rendszer inicializálásáért felelős osztáSly.
 */
class Control {
	private Network net = null;
	private SerialServer server = null;
	private SerialClient client = null;
	public GUI gui = null;

	/**
	 * Eltárolja, hogy az aktuális alkalmazás szerver, vagy kliens üzemmódban üzemel.
	 */
	public NetworkType networkType = null;

	Control() {
	}

	/**
	 * Az alkalmazás inicializálása szerver üzemmódban.
	 * @return A szerver IP c�me.
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
	 * Az alkalmazás inicializálása kliens üzemmódban.
	 * @param ip A kliens IP címe.
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
	 * Az alkalmazás üzemmódjának (szerver, vagy kliens) típusa.
	 */
	public enum NetworkType {
		Client, 
		Server
	}
}
