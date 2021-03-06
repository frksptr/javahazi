/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package szoftechtutor;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import javax.swing.SwingUtilities;

import szoftechtutor.TextBox;

import szoftechtutor.Command.CommandType;
import szoftechtutor.Control.NetworkType;
import szoftechtutor.GameState.GamePhase;

/**
 * A jatek GUI felepiteseert felelos osztaly.
 */
public class GUI extends JFrame implements IGameState {

	private static final long serialVersionUID = 1L;
	private Control ctrl;
	/**
	 * A parancsok feldolgozasaert felelos interface.
	 */
	public ICommand commandProcessor; 
	
    /**
     * Az alkalmazas szemszogebol a jatekos es ellenfelenek jatektere. 
     */
    private Board playerBoard, enemyBoard;
    
    /**
     * A lerakhato es kilott hajok, illetve a jatek allapotarol valo visszajelzest tartalmazo elemek.
     */
    private TextBox shipInformation, statusBar;
    
    /**
     * A jatekos befejezte-e a hajoi lerakasat, keszen all-e a jatek megkezdesere. 
     */
    private boolean ready = false;

    /**
     * A jatekos jon-e vagy az ellenfel. 
     */
    private boolean enemyTurn = false;

    /**
     * A jatek aktualis allapota
     */
    private GameState gameState = new GameState();
    
    private JMenuItem menuItemReady;
	private JMenuItem Turns = new JMenuItem("        ");
    
    private String serverIP = null;
    private String currentIP = null;

	/**
	 * A GUI felepitese.
	 * @param c A halozati informaciokat tartalmazo osztaly.
	 */
	GUI(Control c) {
		super("SzoftechTutor");
		ctrl = c;
		setSize(700, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		setName("SzoftechTutor");
				
		final JMenuBar menuBar = new JMenuBar();
		
		enemyBoard = new Board(
				260,
				30,
				220,
				220,
				new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						try{
							if (gameState.gamePhase == GamePhase.ShootingShips) {
								JButton button = (JButton) e.getSource();
								if (enemyTurn) {
									return;
								}
								Point pos = enemyBoard.getPosition(button);
								Command c = new Command();
								c.position = pos;
								c.commandType = CommandType.Shot;
								c.commandOrigin = ctrl.networkType;
								commandProcessor.onCommand(c);
								shipInformation.setText(shipInformation.textCreator(true,(gameState.gamePhase == GamePhase.PlacingShips),ctrl.networkType,gameState));
								statusBar.setText(statusBar.textCreator(false,(gameState.gamePhase == GamePhase.PlacingShips),ctrl.networkType,gameState));
							}
						}
						catch(Exception ex){
							System.out.print("Elobb pakoljatok le a hajoitokat!\n");
						}
					}
				});
		
		playerBoard = new Board(
				30,
				30,
				220,
				220,
				new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						try{
							if (gameState.gamePhase == GamePhase.PlacingShips) {
								Object asd = e.getSource();
								Command c = new Command();
								c.position = playerBoard.getPosition((JButton)asd);
								c.commandType = CommandType.PlacedShip;
								c.commandOrigin = ctrl.networkType;
								System.out.print("\n" + c.commandOrigin + " sending " + c.commandType + " command...\n");
								commandProcessor.onCommand(c);
							} 
							shipInformation.setText(shipInformation.textCreator(true,(gameState.gamePhase == GamePhase.PlacingShips),ctrl.networkType,gameState));
							statusBar.setText(statusBar.textCreator(false,(gameState.gamePhase == GamePhase.PlacingShips),ctrl.networkType,gameState));
						}
						catch(Exception ex){
							if(currentIP == null) setStatusBarText("Csatlakozz az ellenfelhez a jatek elkezdesehez!");
							else System.out.print("Gaz van\n");
						}
					}
				});

		shipInformation = new TextBox(500,30,160,200);
		statusBar = new TextBox(30,260,630,50);
		JMenu menu = new JMenu("Start");

		JMenuItem menuItem = new JMenuItem("Client");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				serverIP = JOptionPane.showInputDialog("Mi az ellenfeled IPv4 cime?");
				ctrl.startClient(serverIP); 
			}
		});
		menu.add(menuItem);

		menuItem = new JMenuItem("Server");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentIP = ctrl.startServer();
				JOptionPane.showMessageDialog(null,"Az IPv4 cimed: "+currentIP);
			}
		});
		menu.add(menuItem);
		menuBar.add(menu);
		
		menuItemReady = new JMenuItem("Ready!");
		menuItemReady.setBackground(Color.RED);
		
		menuItemReady.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(ctrl.networkType == NetworkType.Server){
					if (gameState.serverGameSpace.ownShips.shipElements == 0){
						if (gameState.gamePhase == GamePhase.PlacingShips) {								
							Command c = new Command();
							c.commandOrigin = ctrl.networkType;
							c.commandType = CommandType.Ready;
							c.ready = !ready;
							commandProcessor.onCommand(c);
						}
					}
				}
				if(ctrl.networkType == NetworkType.Client){
					if (gameState.clientGameSpace.ownShips.shipElements == 0){
						if (gameState.gamePhase == GamePhase.PlacingShips) {								
							Command c = new Command();
							c.commandOrigin = ctrl.networkType;
							c.commandType = CommandType.Ready;
							c.ready = !ready;
							commandProcessor.onCommand(c);
						}
					}
				}
			}
		});
		menuBar.add(menuItemReady);

		menuItem = new JMenuItem("Reset");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					if(ctrl.networkType == NetworkType.Server)
						ctrl.startServer();
					if(ctrl.networkType == NetworkType.Client)
						ctrl.startClient(serverIP);
					shipInformation.setText(shipInformation.textCreator(true,(gameState.gamePhase == GamePhase.PlacingShips),ctrl.networkType,gameState));
					statusBar.setText(statusBar.textCreator(false,(gameState.gamePhase == GamePhase.PlacingShips),ctrl.networkType,gameState));
					gameState = new GameState();
					Command c = new Command();
					c.commandType = CommandType.Reset;
					commandProcessor.onCommand(c);
				}
				catch(Exception ex){
					setStatusBarText("Csatlakozz az ellenfelhez a jatek elkezdesehez!");
				}
			}
		});
		menuBar.add(menuItem);
		
		menuItem = new JMenuItem("Exit");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menuBar.add(menuItem);
		
		menuBar.add(Turns);
				
		setJMenuBar(menuBar);
		shipInformation.setText(shipInformation.textCreator(true,(gameState.gamePhase == GamePhase.PlacingShips),ctrl.networkType, gameState));
		statusBar.setText(statusBar.textCreator(false,(gameState.gamePhase == GamePhase.PlacingShips),ctrl.networkType, gameState));
				
		add(playerBoard);
		add(enemyBoard);
		add(statusBar);
		add(shipInformation);
		setVisible(true);
	}
	
	/**
	 * A jatek allapotat kiiro mezo valtoztatasa.
	 * @param string	Az uj allapotrol informalo szoveg.
	 */
	public void setStatusBarText(String string) {
		System.out.print(string + "\n");
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				statusBar.setText(string);
			}
			
		});
		
	}

	/* (non-Javadoc)
	 * @see szoftechtutor.IGameState#onNewGameState(szoftechtutor.GameState)
	 */
	@Override
	public void onNewGameState(GameState gs) {
		System.out.print("Drawing new gamestate\n");
		gameState = gs;
		GameSpace clientGameSpace = gs.clientGameSpace;
		GameSpace serverGameSpace = gs.serverGameSpace;
			
		GameSpace ownGameSpace;
		if (ctrl.networkType == NetworkType.Client) {
			ownGameSpace = clientGameSpace;
			enemyTurn = gs.serversTurn;
			ready = gs.clientReady;
			if(ownGameSpace.ownText_f){
				setStatusBarText(ownGameSpace.ownText);
				ownGameSpace.ownText_f = false;
			}
			if(ownGameSpace.enemyText_f){
				setStatusBarText(ownGameSpace.enemyText);
			}
		} else {
			ownGameSpace = serverGameSpace;
			enemyTurn = !gs.serversTurn;
			ready = gs.serverReady;
			if (ownGameSpace.ownText_f) {
				setStatusBarText(ownGameSpace.ownText);
				ownGameSpace.ownText_f = false;
			}
			if (ownGameSpace.enemyText_f) {
				setStatusBarText(ownGameSpace.enemyText);
			}
		}
		
		menuItemReady.setBackground(ready ? Color.GREEN : Color.RED);
		
		shipInformation.setText(shipInformation.textCreator(true,(gameState.gamePhase == GamePhase.PlacingShips),ctrl.networkType,gameState));
		
		if(gameState.gamePhase == GamePhase.ShootingShips){
			statusBar.setText(statusBar.textCreator(false,(gameState.gamePhase == GamePhase.PlacingShips),ctrl.networkType, gameState));
		}
	
		if(!enemyTurn && gameState.gamePhase == GamePhase.ShootingShips){
			Turns.setText("Te jossz!");
			Turns.setBackground(Color.GREEN);
		}
		else if(enemyTurn && gameState.gamePhase == GamePhase.ShootingShips){
			Turns.setText("Varj!");
			Turns.setBackground(Color.RED);
		}
			
		playerBoard.redrawFromNewGameState(ownGameSpace.ownTable);
		enemyBoard.redrawFromNewGameState(ownGameSpace.enemyTable);
	}

	@Override
	public void toString(String string) {
		// TODO Auto-generated method stub
		
	}
}
