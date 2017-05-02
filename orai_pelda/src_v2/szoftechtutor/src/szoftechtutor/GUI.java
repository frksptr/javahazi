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
    
    

	GUI(Control c) {
		super("SzoftechTutor");
		ctrl = c;
		setSize(500, 350);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);

		JMenuBar menuBar = new JMenuBar();
		
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
							enemyBoard.showShot(button);
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
		
		JMenuItem menuItemReady = new JMenuItem("Ready!");
		menuItemReady.setBackground(Color.RED);
		
		menuItemReady.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JMenuItem item = (JMenuItem) e.getSource();
				if(placement) item.setBackground(Color.GREEN);
				else item.setBackground(Color.RED);
				placement = !placement;
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
			
		setVisible(true);
	}

	public void shootPos(Point p){
		playerBoard.checkShoot(p);
		//ctrl.sendClick(p);
		//playerBoard.placeShip(null, playerBoard.getButton(p));
	}

	@Override
	public void onNewGameState(GameState gs) {
		// TODO dolgok történnek
		// gsbõl kiolvasni mi változott és bejelölni a pályákon
		System.out.print("Drawing nem gamestate");
		GameSpace clientGameSpace = gs.clientGameSpace;
		GameSpace serverGameSpace = gs.serverGameSpace;
		
		
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
