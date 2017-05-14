package szoftechtutor;

import java.awt.Button;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import java.awt.Font;

public class Board extends JPanel {

	private boolean enemy = false;
	public int ships = 35;
	public int shipToPlace = 15;
<<<<<<< HEAD
	public int shootedShip = 35;
	public Ship[][] cellsIsShipArray = new Ship[10][10];
=======
	public int shotShip = 35;
>>>>>>> c1791d0d6d22cd82d5b2deb9f7819d90b269794f
	private JButton[][] buttonGrid = new JButton[11][11];
	private Color waterColor = Color.CYAN;
	private Color shipColor = Color.BLACK;
	private Color shotShipColor = Color.RED;
	private Color shotWaterColor = Color.blue;
	private Color unknownColor = Color.LIGHT_GRAY;
			
	public Board(int posx, int posy, int width, int height, boolean enemy, ActionListener handler) {

		this.setBounds(posx, posy, width, height);
		this.enemy = enemy;

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		c.ipadx = 0;
		c.ipady = 0;

		setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		
		char a = 'A';

		for (int col = 1; col < 12; col++) {
			for (int row = 1; row < 12; row++) {
				JButton button = new JButton();
				if((col > 10) || (row > 10)){
					button.setEnabled(false);
					button.setPreferredSize(new java.awt.Dimension(height / 11, width / 11));
					if(col > 10 && row <= 10){
						button.setText(""+a);
						a +=1;
					}
					if(row > 10 && col <= 10){
						button.setText(""+col);
					}
					
					
					button.setFont(new Font("Arial", Font.PLAIN, 12));
					button.setBorder(null);
				}
				else{
					button.setBackground(waterColor);
					button.setCursor(new Cursor(Cursor.HAND_CURSOR));
					button.setPreferredSize(new java.awt.Dimension(height / 11, width / 11));
					button.addActionListener(handler);
					//button.setBorder(new LineBorder(Color.BLACK));
				}
				c.gridx = col;
				c.gridy = row;

				this.add(button, c);
				buttonGrid[row-1][col-1] = button;
			}
		}

	}
	
	public Point getPosition(JButton button) {
		int x = 0;
		int y = 0;
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				if (buttonGrid[row][col] == button) {
					x = row;
					y = col;
				}
			}
		}
		return new Point(x,y);
	}
	
	public JButton getButton(Point p) {
		return buttonGrid[p.x][p.y];
		
	}
		
	public void redrawFromNewGameState(CellType[][] cellTypes) {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				switch (cellTypes[i][j]){
					case Ship:
						buttonGrid[i][j].setBackground(shipColor);
						break;
					case ShipShot:
						buttonGrid[i][j].setBackground(shotShipColor);
						break;
					case Water:
						buttonGrid[i][j].setBackground(waterColor);
						break;
					case WaterShot:
						buttonGrid[i][j].setBackground(shotWaterColor);
						break;
					case Unknown:
						buttonGrid[i][j].setBackground(unknownColor);
						break;
				}
			}
		}
	}
}