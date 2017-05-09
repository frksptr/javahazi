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

import szoftechtutor.Command.CommandOrigin;
import szoftechtutor.Command.CommandType;
import szoftechtutor.Control.NetworkType;

/**
 *
 * @author Predi
 */
public class GUI extends JFrame implements IGameState {

	private static final long serialVersionUID = 1L;
	private Control ctrl;
	/*commandProessor:
	 * Ez vagy a szerveroldali Logic, vagy a kliensoldali Client
	 * A Controlban dĹ‘l el, hogy mivan
	 */
	public ICommand commandProcessor; 
	
    private Board enemyBoard, playerBoard;
    
    // ezalatt tipikusan olyanok amik valszeg majd ĂˇtkerĂĽlnek 
    // GameStatebe
    private boolean placement = true; // rakunk-e, vagy lĂ¶vĂĽnk 
    private boolean running = false;
    private int shipsToPlace = 5;

    private boolean enemyTurn = false;

    private Random random = new Random();
    
    private GameState gameState = new GameState();
    
    private JMenuItem menuItemReady;
    
    private String status_text;
    private String status_text_1;
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
		JTextArea textArea = new JTextArea();
		JTextArea statusBar = new JTextArea();
		
		enemyBoard = new Board(
				260,
				30,
				220,
				220,
				true, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
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
						if (placement) {
							Object asd = e.getSource();
							/*
							 * TODO: felvenni GameState/GameSpace-be
							 */
							Command c = new Command();
							c.position = playerBoard.getPosition((JButton)asd);
							c.commandType = CommandType.PlacedShip;

							c.commandOrigin = getCommandOriginFromNetworkType(ctrl.networkType);
							System.out.print(c.commandOrigin + "sending " + c.commandType + " command...");
							commandProcessor.onCommand(c);
							
							/* TODO
							 *  ezt lehet, hogy itt nem is kĂ©ne hanem inkĂˇbb majd
							 *  ha visszakĂĽldi a gamestatet a logic/client  
							 */
							playerBoard.placeShip(null, (JButton)asd);
						} else {
							
						}
					}
				});

		textArea.setSize(160, 200);
		textArea.setLocation(500, 30);
		textArea.setBorder(BorderFactory.createLineBorder(Color.black));
		textArea.setEditable(false);	
		
		statusBar.setSize(630, 50);
		statusBar.setLocation(30, 260);
		statusBar.setBorder(BorderFactory.createLineBorder(Color.black));
		statusBar.setEditable(false);
		
		JMenu menu = new JMenu("Start");

		JMenuItem menuItem = new JMenuItem("Client");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				serverIP = JOptionPane.showInputDialog("What is the Server's IP Address?");
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
				if(placement) item.setBackground(Color.GREEN);
				else item.setBackground(Color.RED);
				placement = !placement;
				
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

		add(playerBoard);
		
		add(enemyBoard);
		
	    if(!placement) {
	        status_text = String.format("MĂ©g lepakolhatĂł hajĂłid:\n"
	        		+ "â–� %d darab\n"
	        		+ "â–�â–� %d darab\n"
	        		+ "â–�â–�â–� %d darab\n"
	        		+ "â–�â–�â–�â–� %d darab\n"
	        		+ "â–�â–�â–�â–�â–� %d darab\n"
	        		,2,3,5,1,5);
	    }
	    else {
	        status_text = String.format("Shooted ship number:\n"
	        		+ "â–� %d darab\n"
	        		+ "â–�â–� %d darab\n"
	        		+ "â–�â–�â–� %d darab\n"
	        		+ "â–�â–�â–�â–� %d darab\n"
	        		+ "â–�â–�â–�â–�â–� %d darab\n",2,3,5,1,5);
	        		status_text_1 = String.format(
	        		"EllenfĂ©l elsĂĽllyesztendĹ‘ hajĂłi:\n"
	        		+ "  â–� %d  -  "
	        		+ "â–�â–� %d  -  " 
	        		+ "â–�â–�â–� %d  -  "
	        		+ "â–�â–�â–�â–� %d  -  "
	        		+ "â–�â–�â–�â–�â–� %d"
	        		,1,1,1,1,1);   	
	    }
		
		textArea.append(status_text);
		statusBar.append(status_text_1);
		add(textArea);
		add(statusBar);
		setVisible(true);
	}
    
	private String sprintf(String string, boolean placement2) {
		// TODO Auto-generated method stub
		return null;
	}

	public void shootPos(Point p){
		playerBoard.checkShoot(p);
		//ctrl.sendClick(p);
		//playerBoard.placeShip(null, playerBoard.getButton(p));
	}

	@Override
	public void onNewGameState(GameState gs) {
		System.out.print("Drawing nem gamestate");
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
