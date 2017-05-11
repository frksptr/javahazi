/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package szoftechtutor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.Position;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.sun.org.apache.xerces.internal.impl.XMLScanner.NameType;

import szoftechtutor.TextBox;
import szoftechtutor.Command.CommandOrigin;
import szoftechtutor.Command.CommandType;
import szoftechtutor.Control.NetworkType;

/**
 *
 */
public class GUI extends JFrame implements IGameState {

	private static final long serialVersionUID = 1L;
	private Control ctrl;
	/*commandProessor:
	 * Ez vagy a szerveroldali Logic, vagy a kliensoldali Client
	 * A Controlban dől el, hogy mivan
	 */
	public ICommand commandProcessor; 
	
    private Board enemyBoard, playerBoard;
    private TextBox textArea, statusBar;
    
    // ezalatt tipikusan olyanok amik valszeg majd átkerülnek 
    // GameStatebe
    private boolean placement = true; // rakunk-e, vagy lövünk 
    private boolean running = false;

    private boolean enemyTurn = false;

    private Random random = new Random();
    
    private GameState gameState = new GameState();
    
    private JMenuItem menuItemReady;
    
    private String serverIP;
    private String currentIP;

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
				true, new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						textArea.setText(textArea.textCreator(true,placement, playerBoard, playerBoard.shipToPlace, playerBoard.shootedShip));
						statusBar.setText(statusBar.textCreator(false,placement, enemyBoard, enemyBoard.shipToPlace, enemyBoard.shootedShip));
						JButton button = (JButton) e.getSource();
						if (placement) {
							
						}
						else {
							Point pos = enemyBoard.getPosition(button);
							Command c = new Command();
							c.position = pos;
							c.commandType = CommandType.Shot;
							c.commandOrigin = getCommandOriginFromNetworkType(ctrl.networkType);
							commandProcessor.onCommand(c);
						}
					}
				});
		
		playerBoard = new Board(
				30,
				30,
				220,
				220,
				true, new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						textArea.setText(textArea.textCreator(true,placement, playerBoard, playerBoard.shipToPlace, playerBoard.shootedShip));
						statusBar.setText(statusBar.textCreator(false,placement, enemyBoard, enemyBoard.shipToPlace, enemyBoard.shootedShip));
						if (placement) {
							Object asd = e.getSource();
							/*
							 * TODO: felvenni GameState/GameSpace-be
							 */
							Command c = new Command();
							c.position = playerBoard.getPosition((JButton)asd);
							c.commandType = CommandType.PlacedShip;
							c.commandOrigin = getCommandOriginFromNetworkType(ctrl.networkType);
							System.out.print("\n" + c.commandOrigin + " sending " + c.commandType + " command...");
							commandProcessor.onCommand(c);
						} else {
							
						}
					}
				});

		textArea = new TextBox(500,30,160,200);
		statusBar = new TextBox(30,260,630,50);
		JMenu menu = new JMenu("Start");

		JMenuItem menuItem = new JMenuItem("Client");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				serverIP = JOptionPane.showInputDialog("What is the Server's IP Address?","192.168.56.1");
				ctrl.startClient(serverIP); 
			}
		});
		menu.add(menuItem);

		menuItem = new JMenuItem("Server");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentIP = ctrl.startServer();
				JOptionPane.showMessageDialog(null,"Your IP Address is: "+currentIP);
			}
		});
		menu.add(menuItem);
		menuBar.add(menu);
		
		menuItemReady = new JMenuItem("Ready!");
		menuItemReady.setBackground(Color.RED);
		
		menuItemReady.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JMenuItem item = (JMenuItem) e.getSource();
				if(playerBoard.ships == 0){
					if(placement) item.setBackground(Color.GREEN);
					else item.setBackground(Color.RED);
					placement = !placement;
					textArea.setText(textArea.textCreator(true,placement, playerBoard, playerBoard.shipToPlace, playerBoard.shootedShip));
					statusBar.setText(statusBar.textCreator(false,placement, enemyBoard, enemyBoard.shipToPlace, enemyBoard.shootedShip));
				}
				else item.setBackground(Color.RED);
				
				//TODO: ez itt
//				Command c = new Command();
//				c.position = playerBoard.getPosition((JButton)asd);
//				c.commandType = CommandType.PlacedShip;
//
//				c.commandOrigin = getCommandOriginFromNetworkType(ctrl.networkType);
//				System.out.print(c.commandOrigin + "sending " + c.commandType + " command...");
//				commandProcessor.onCommand(c);
			}
		});
		menuBar.add(menuItemReady);

		menuItem = new JMenuItem("Reset");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(ctrl.networkType == NetworkType.Server)
					ctrl.startServer();
				if(ctrl.networkType == NetworkType.Client)
					ctrl.startClient(serverIP);
				placement = true;
				menuItemReady.setBackground(Color.RED);
				textArea.setText(textArea.textCreator(true,placement, playerBoard, playerBoard.shipToPlace, playerBoard.shootedShip));
				statusBar.setText(statusBar.textCreator(false,placement, enemyBoard, enemyBoard.shipToPlace, enemyBoard.shootedShip));				
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
				
		setJMenuBar(menuBar);

		textArea.setText(textArea.textCreator(true,placement, playerBoard, playerBoard.shipToPlace, playerBoard.shootedShip));
		statusBar.setText(statusBar.textCreator(false,placement, enemyBoard, enemyBoard.shipToPlace, enemyBoard.shootedShip));
		
		
		add(playerBoard);
		add(enemyBoard);
		add(statusBar);
		add(textArea);
		setVisible(true);
	}
    
	public void toString(String string) {
		statusBar.setText(string);
		add(statusBar);
	}

	public void shootPos(Point p){
		playerBoard.checkShoot(p);
		//ctrl.sendClick(p);
		//playerBoard.placeShip(null, playerBoard.getButton(p));
	}

	@Override
	public void onNewGameState(GameState gs) {
		System.out.print("Drawing new gamestate\n");
		GameSpace clientGameSpace = gs.clientGameSpace;
		GameSpace serverGameSpace = gs.serverGameSpace;
		
		GameSpace ownGameSpace;
		if (ctrl.networkType == NetworkType.Client) {
			ownGameSpace = clientGameSpace;
		} else {
			ownGameSpace = serverGameSpace;
		}
		playerBoard.redrawFromNewGameState(ownGameSpace.ownTable);
		enemyBoard.redrawFromNewGameState(ownGameSpace.enemyTable);
	}
	
	private CommandOrigin getCommandOriginFromNetworkType(NetworkType networkType) {
		CommandOrigin commandOrigin = null;
		if (ctrl.networkType == NetworkType.Client) {
			commandOrigin = CommandOrigin.Client;
		} else {
			commandOrigin = CommandOrigin.Server;
		}
		return commandOrigin; 
	}
}
