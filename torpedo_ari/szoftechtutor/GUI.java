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

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;


/**
 *
 * @author Predi
 */
public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private Control ctrl;
	private DrawPanel drawPanel;
	private DrawPanel drawPanel2;

	GUI(Control c) {
		super("SzoftechTutor");
		ctrl = c;
		setSize(500, 350);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);

		JMenuBar menuBar = new JMenuBar();

//		JMenu menu = new JMenu("Start");

		JMenuItem menuItem;
//		menuItem.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				ctrl.startClient();
//			}
//		});
//		menu.add(menuItem);
//
//		menuItem = new JMenuItem("Server");
//		menuItem.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				ctrl.startServer();
//			}
//		});
//		menu.add(menuItem);
//
//		menuBar.add(menu);

		menuItem = new JMenuItem("startserver");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ctrl.startServer();
			}
		});
		menuBar.add(menuItem);
		
		menuItem = new JMenuItem("startclient");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ctrl.startClient();
			}
		});
		menuBar.add(menuItem);
		
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

		setJMenuBar(menuBar);

		JPanel inputPanel = new JPanel();
		inputPanel.setBounds(30, 30, 200, 200);
		inputPanel.setBorder(BorderFactory.createTitledBorder(""));
		inputPanel.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				// System.out.println("X:" + e.getX() + " Y:" + e.getY());
				ctrl.sendClick(new Point(e.getX(), e.getY()));
			}
		});
		add(inputPanel);
		drawPanel = new DrawPanel();
		drawPanel.setBounds(240, 30, 200, 200);
		drawPanel.setBorder(BorderFactory.createTitledBorder(""));
		add(drawPanel);

		setVisible(true);
		
		drawPanel2 = new DrawPanel();
		drawPanel2.setBounds(30, 30, 200, 200);
		drawPanel2.setBorder(BorderFactory.createTitledBorder(""));
		add(drawPanel2);

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
			
			for (int i = 0; i <= 200; i = i + 20){
				g.drawLine(i, 0, i, 200);
			}
			for (int i = 0; i <= 200; i = i + 20){
				g.drawLine(0, i, 200, i);
			}
			for (Point p : points) {
				//g.fill3DRect(p.x-p.x%20, p.y-p.y%20, 20, 20, false);
				g.setColor(Color.RED);

				g.drawOval(p.x-p.x%20+1, p.y-p.y%20+1, 18, 18);
			}
		}
	}
}
