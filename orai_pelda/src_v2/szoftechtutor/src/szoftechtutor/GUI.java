/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package szoftechtutor;

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

import szoftechtutor.Command.CommandType;

/**
 *
 * @author Predi
 */
public class GUI extends JFrame implements IGameState {

	private static final long serialVersionUID = 1L;
	private Control ctrl;
	private boolean running = false;
    private Board enemyBoard, playerBoard;
    
    private boolean placement = true; // rakunk-e, vagy lövünk 

    private int shipsToPlace = 5;

    private boolean enemyTurn = false;

    private Random random = new Random();

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
					// TODO: megfleelõen menjen :D
					@Override
					public void actionPerformed(ActionEvent e) {
						JButton button = (JButton) e.getSource();
						if (placement) {
							
						}
						else {
							Point pos = enemyBoard.getPosition(button);
							Command c = new Command();
							c.positionShot = pos;
							c.CommandType = CommandType.Shot;
							ctrl.onCommand(c);
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

		menuItem = new JMenuItem("Exit");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menuBar.add(menuItem);
		
		menuItem = new JMenuItem("SwitchMode");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				placement = !placement;
			}
		});
		menuBar.add(menuItem);

		setJMenuBar(menuBar);

		JPanel inputPanel = new JPanel();
		
		inputPanel.setBounds(30, 30, 200, 200);
		inputPanel.setBorder(BorderFactory.createTitledBorder("Input"));
		inputPanel.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				// System.out.println("X:" + e.getX() + " Y:" + e.getY());
				ctrl.sendClick(new Point(e.getX(), e.getY()));
			}
		});
		
		
		add(playerBoard);
		
		add(enemyBoard);
		
		
		//add(inputPanel);

//		drawPanel = new DrawPanel();
//		drawPanel.setBounds(230, 30, 200, 200);
//		drawPanel.setBorder(BorderFactory.createTitledBorder("Draw"));
//		add(drawPanel);
		
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
		// gsbõl kiolvasnimi változott és bejelölni a pályákon
		
	}
}
