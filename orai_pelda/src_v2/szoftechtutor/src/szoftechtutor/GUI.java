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

/**
 *
 * @author Predi
 */
public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private Control ctrl;
	private DrawPanel drawPanel;
    private boolean running = false;
    private Board enemyBoard, playerBoard;
    
    private boolean placement = true; // rakunk-e, vagy l�v�nk 

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
					// TODO: megfleel�en menjen :D
					@Override
					public void actionPerformed(ActionEvent e) {
						JButton button = (JButton) e.getSource();
						if (placement) {
							
						}
						else {
							Point pos = enemyBoard.getPosition(button);
							ctrl.sendClick(pos);
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

		menuItem = new JMenuItem("Clear");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				drawPanel.points.clear();
				drawPanel.repaint();
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

	void addPoint(Point p) {
		drawPanel.points.add(p);
		drawPanel.repaint();
	}

	private class DrawPanel extends JPanel {

		private static final long serialVersionUID = 1L;
		private ArrayList<Point> points = new ArrayList<Point>();

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			for (Point p : points) {
				g.drawOval(p.x, p.y, 10, 10);
			}
		}
	}
	
	public void shootPos(Point p){
		playerBoard.checkShoot(p);
		//ctrl.sendClick(p);
		//playerBoard.placeShip(null, playerBoard.getButton(p));
	}
}
