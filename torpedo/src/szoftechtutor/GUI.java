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
 * A játék GUI felépítéséért felelős osztály.
 */
public class GUI extends JFrame implements IGameState {

	private static final long serialVersionUID = 1L;
	private Control ctrl;
	/**
	 * A parancsok feldolgozásáért felelős interface.
	 */
	public ICommand commandProcessor; 
	
    /**
     * Az alkalmazás szemszögéből a játékos és ellenfelének játéktere. 
     */
    private Board playerBoard, enemyBoard;
    
    /**
     * A lerakható és kilőtt hajók, illetve a játék állapotáról való visszajelzést tartalmazó elemek.
     */
    private TextBox shipInformation, statusBar;
    
    /**
     * A játékos befejezte-e a hajói lerakását, készen áll-e a játék megkezdésére. 
     */
    private boolean ready = false;

    /**
     * A játékos jön-e vagy az ellenfel. 
     */
    private boolean enemyTurn = false;

    /**
     * A játék aktuális állapota
     */
    private GameState gameState = new GameState();
    
    private JMenuItem menuItemReady;
    
    private String serverIP = null;
    private String currentIP = null;

	/**
	 * A GUI felépítése.
	 * @param c A hálózati információkat tartalmazó osztály.
	 */
	GUI(Control c) {
		super("SzoftechTutor");
		ctrl = c;
		setSize(700, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		setName("SzoftechTutor");
				
		JMenuBar menuBar = new JMenuBar();
		
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
									setStatusBarText("Az ellenfél van soron!");
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
							System.out.print("Elobb pakoljátok le a hajóitokat!\n");
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
							if(currentIP == null) setStatusBarText("Csatlakozz az ellenfélhez a játék elkezdéséhez!");
							else System.out.print("Gáz van\n");
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
				serverIP = JOptionPane.showInputDialog("Mi az ellenfeled IPv4 címe?");
				ctrl.startClient(serverIP); 
			}
		});
		menu.add(menuItem);

		menuItem = new JMenuItem("Server");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentIP = ctrl.startServer();
				JOptionPane.showMessageDialog(null,"Az IPv4 címed: "+currentIP);
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
					setStatusBarText("Csatlakozz az ellenfélhez a játék elkezdéséhez!");
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
		
		JMenuItem Turns = new JMenuItem("        ");
		if(!enemyTurn && gameState.gamePhase == GamePhase.ShootingShips){
			Turns.setText("Te jössz!");
			Turns.setBackground(Color.GREEN);
			Turns.updateUI();
			Turns.repaint();
		}
		else if(enemyTurn && gameState.gamePhase == GamePhase.ShootingShips){
			Turns.setText("Várj!");
			Turns.setBackground(Color.RED);
			Turns.repaint();
			Turns.updateUI();
		}
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
	 * A játék állapotát kiíró mező változtatása.
	 * @param string	Az új állapotról informáló szöveg.
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
				ownGameSpace.enemyText_f = false;
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
				ownGameSpace.enemyText_f = false;
			}
		}
		
		menuItemReady.setBackground(ready ? Color.GREEN : Color.RED);
		
		if (gs.gamePhase == GamePhase.ShootingShips) {
			shipInformation.setText(shipInformation.textCreator(true,(gameState.gamePhase == GamePhase.PlacingShips),ctrl.networkType,gameState));
			statusBar.setText(statusBar.textCreator(false,(gameState.gamePhase == GamePhase.PlacingShips),ctrl.networkType, gameState));
		}
			
		playerBoard.redrawFromNewGameState(ownGameSpace.ownTable);
		enemyBoard.redrawFromNewGameState(ownGameSpace.enemyTable);
	}

	@Override
	public void toString(String string) {
		// TODO Auto-generated method stub
		
	}
}
