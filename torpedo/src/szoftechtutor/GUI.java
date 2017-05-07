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
import javax.swing.JPanel;
import javax.swing.text.Position;
import javax.swing.JTextArea;

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
	 * A Controlban dõl el, hogy mivan
	 */
	public ICommand commandProcessor; 
	
    private Board enemyBoard, playerBoard;
    
    // ezalatt tipikusan olyanok amik valszeg majd átkerülnek 
    // GameStatebe
    private boolean placement = true; // rakunk-e, vagy lövünk 
    private boolean running = false;
    private int shipsToPlace = 5;

    private boolean enemyTurn = false;

    private Random random = new Random();
    
    private GameState gameState = new GameState();
    
    private JMenuItem menuItemReady;
    
    private String status_text;

	GUI(Control c) {
		super("SzoftechTutor");
		ctrl = c;
		setSize(700, 350);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		JTextArea textArea = new JTextArea();
		
		enemyBoard = new Board(
				260,
				30,
				200,
				200,
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
				200,
				200,
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
							 *  ezt lehet, hogy itt nem is kéne hanem inkább majd
							 *  ha visszaküldi a gamestatet a logic/client  
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
		
		JMenu menu = new JMenu("Start");

		JMenuItem menuItem = new JMenuItem("Client");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ctrl.startClient();
			}
		});
		menu.add(menuItem);

		menuItem = new JMenuItem("Server");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ctrl.startServer();
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
					ctrl.startClient();
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
		
	    if(placement) {
	        status_text = String.format("Még lepakolható hajóid:\n"
	        		+ "1 elemû: %d darab\n"
	        		+ "2 elemû: %d darab\n"
	        		+ "3 elemû: %d darab\n"
	        		+ "4 elemû: %d darab\n"
	        		+ "5 elemû: %d darab\n"
	        		,2,3,5,1,5);
	    }
	    else {
	        status_text = String.format("Kilõtt hajóid száma:\n"
	        		+ "1 elemû: %d darab\n"
	        		+ "2 elemû: %d darab\n"
	        		+ "3 elemû: %d darab\n"
	        		+ "4 elemû: %d darab\n"
	        		+ "5 elemû: %d darab\n"
	        		+ "\n"
	        		+ "Ellenfél elsüllyesztendõ hajói:\n"
	        		+ "1 elemû: %d darab\n"
	        		+ "2 elemû: %d darab\n"
	        		+ "3 elemû: %d darab\n"
	        		+ "4 elemû: %d darab\n"
	        		+ "5 elemû: %d darab\n"
	        		,2,3,5,1,5,1,1,1,1,1);   	
	    }
		
		textArea.append(status_text);
		add(textArea);
		
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
