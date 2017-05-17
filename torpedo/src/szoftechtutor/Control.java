package szoftechtutor;

import java.net.InetAddress;
import java.net.UnknownHostException;
/**
 * A h�l�zati kapcsolat fel�p�t�s��rt �s a rendszer inicializ�l�s��rt felel�s oszt�ly.
 */
class Control {
	private Network net = null;
	private SerialServer server = null;
	private SerialClient client = null;
	public GUI gui = null;

	/**
	 * Elt�rolja, hogy az aktu�lis alkalmaz�s szerver, vagy kliens �zemm�dban �zemel.
	 */
	public NetworkType networkType = null;

	Control() {
	}

	/**
	 * Az alkalmaz�s inicializ�l�sa szerver �zemm�dban.
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
	 * Az alkalmaz�s inicializ�l�sa kliens �zemm�dban.
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
	 * Az alkalmaz�s �zemm�dj�nak (szerver, vagy kliens) t�pusa.
	 */
	public enum NetworkType {
		Client, 
		Server
	}
}
